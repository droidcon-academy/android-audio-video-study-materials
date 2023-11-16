package com.droidcon.musicmate

import android.app.Application
import android.content.ComponentName
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaItem.RequestMetadata
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.Util
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionToken
import com.droidcon.musicmate.model.SongList
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class PlayerScreenViewModel(app: Application) : AndroidViewModel(app) {

    private lateinit var mediaControllerFuture: ListenableFuture<MediaController>

    private val mediaController: MediaController?
        get() = when (mediaControllerFuture.isDone && !mediaControllerFuture.isCancelled) {
            true -> mediaControllerFuture.get()
            else -> null
        }

    private val _playerScreenState = MutableStateFlow(PlayerScreenState())
    val playerScreenState: StateFlow<PlayerScreenState> = _playerScreenState

    private var progressUpdateJob: Job? = null

    private fun updatePlayerScreenState(update: PlayerScreenState.() -> PlayerScreenState) {
        _playerScreenState.value = _playerScreenState.value.update()
    }

    fun onStart() {
        val context: Context = getApplication()
        mediaControllerFuture = MediaController.Builder(
            context,
            SessionToken(context, ComponentName(context, PlaybackService::class.java)),
        ).buildAsync()
        mediaControllerFuture.addListener({ setupPlayer() }, ContextCompat.getMainExecutor(context))
    }

    private fun setupPlayer() {
        val player = mediaController ?: return

        if (player.mediaItemCount == 0) {
            val mediaItems = SongList.map { song ->
                val mediaUri = if (song.source.startsWith("http")) {
                    song.source.toUri()
                } else {
                    val packageName = getApplication<Application>().packageName
                    val resources = getApplication<Application>().resources
                    Uri.Builder()
                        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                        .authority(packageName)
                        .appendPath(
                            resources.getIdentifier(song.source, "raw", packageName).toString()
                        )
                        .build()
                }

                MediaItem.Builder()
                    .setUri(mediaUri)
                    .setMediaId(song.id.toString())
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle(song.title)
                            .setArtist(song.artist)
                            .setArtworkUri(song.thumb.toUri())
                            .build()
                    ).build()
            }
            player.setMediaItems(mediaItems)
        }
        if (player.playbackState == Player.STATE_IDLE) {
            player.prepare()
        }
        player.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                updatePlayerScreenState {
                    copy(currentSong = SongList.find { it.id.toString() == mediaItem?.mediaId })
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                updatePlayerScreenState {
                    copy(isPlaying = isPlaying)
                }
            }

            override fun onIsLoadingChanged(isLoading: Boolean) {
                updatePlayerScreenState {
                    copy(isBuffering = isLoading)
                }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                updatePlayerScreenState {
                    val error = when (playbackState != Player.STATE_IDLE && error != null) {
                        true -> null
                        else -> error
                    }
                    copy(isBuffering = playbackState == Player.STATE_BUFFERING, error = error)
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                updatePlayerScreenState {
                    copy(error = error)
                }
            }

            override fun onRepeatModeChanged(repeatMode: Int) {
                updatePlayerScreenState {
                    copy(repeatMode = repeatMode)
                }
            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                updatePlayerScreenState {
                    copy(shuffleModeEnabled = shuffleModeEnabled)
                }
            }

            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                updatePlayerScreenState {
                    copy(currentPosition = newPosition.positionMs)
                }
            }

        })

        setInitialPlayerScreenState(player)
        setupProgressUpdateJob(player)
    }

    private fun setInitialPlayerScreenState(player: Player) {
        updatePlayerScreenState {
            copy(
                isPlaying = player.isPlaying,
                playlist = SongList,
                currentSong = SongList.find { it.id.toString() == player.currentMediaItem?.mediaId },
                repeatMode = player.repeatMode,
                shuffleModeEnabled = player.shuffleModeEnabled,
                onPlayPause = {
                    if (player.isPlaying) {
                        player.pause()
                    } else {
                        if (player.playbackState == Player.STATE_IDLE) {
                            player.prepare()
                        }
                        player.play()
                    }
                },
                onSkipPrevious = {
                    // Prepare player if recovering from error state.
                    if (player.playbackState == Player.STATE_IDLE) {
                        player.prepare()
                    }
                    player.seekToPreviousMediaItem()
                },
                onSkipNext = {
                    // Prepare player if recovering from error state.
                    if (player.playbackState == Player.STATE_IDLE) {
                        player.prepare()
                    }
                    player.seekToNextMediaItem()
                },
                onSeek = { seconds ->
                    if (player.duration != C.TIME_UNSET) {
                        player.seekTo(TimeUnit.SECONDS.toMillis(seconds.toLong()))
                    }
                },
                onSongClick = { song ->
                    for (index in 0 until player.mediaItemCount) {
                        if (player.getMediaItemAt(index).mediaId == song.id.toString()) {
                            if (index != player.currentMediaItemIndex) {
                                player.seekTo(index, 0)
                                break
                            }
                        }
                    }
                    if (player.playbackState == Player.STATE_IDLE) {
                        player.prepare()
                    }
                    player.play()
                },
                onShuffle = {
                    player.shuffleModeEnabled = !player.shuffleModeEnabled
                },
                onRepeat = {
                    val availableRepeatModesCount = 3
                    player.repeatMode = (player.repeatMode + 2) % availableRepeatModesCount
                }
            )
        }
    }

    private fun setupProgressUpdateJob(player: Player) {
        progressUpdateJob?.cancel()
        progressUpdateJob = viewModelScope.launch {
            while (true) {
                updatePlayerScreenState {
                    copy(
                        currentPosition = player.currentPosition,
                        bufferedPosition = player.bufferedPosition,
                    )
                }
                delay(1000)
            }
        }
    }

    fun onStop() {
        MediaController.releaseFuture(mediaControllerFuture)
    }

}