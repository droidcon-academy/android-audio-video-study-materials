package com.droidcon.videomate.ui

import android.content.SharedPreferences
import androidx.compose.runtime.Immutable
import androidx.core.content.edit
import androidx.media3.common.C
import androidx.media3.common.Player
import com.droidcon.videomate.PlayerScreenViewModel.Companion.PLAYBACK_SPEED_PREFERENCE
import com.droidcon.videomate.model.Video
import java.util.concurrent.TimeUnit

interface ControlsListener {
    fun onPlayPause() {}
    fun onSkipPrevious() {}
    fun onSkipNext() {}
    fun onSeek(seconds: Float) {}
    fun onForward() {}
    fun onRewind() {}
    fun onVideoClick(video: Video) {}
    fun onSpeedChange() {}
}

@Immutable
class PlayerControlsListener(
    private val player: Player,
    private val playbackPreferences: SharedPreferences,
) : ControlsListener {
    override fun onPlayPause() {
        if (player.isPlaying) {
            player.pause()
        } else {
            if (player.playbackState == Player.STATE_IDLE) {
                player.prepare()
            }
            player.play()
        }
    }

    override fun onSkipPrevious() {
        if (player.playbackState == Player.STATE_IDLE) {
            player.prepare()
        }
        player.seekToPreviousMediaItem()
    }

    override fun onSkipNext() {
        if (player.playbackState == Player.STATE_IDLE) {
            player.prepare()
        }
        player.seekToNextMediaItem()
    }

    override fun onSeek(seconds: Float) {
        if (player.duration != C.TIME_UNSET) {
            player.seekTo(TimeUnit.SECONDS.toMillis(seconds.toLong()))
        }
    }

    override fun onForward() {
        player.seekForward()
    }

    override fun onRewind() {
        player.seekBack()
    }

    override fun onVideoClick(video: Video) {
        for (index in 0 until player.mediaItemCount) {
            if (player.getMediaItemAt(index).mediaId == video.id.toString()) {
                if (index != player.currentMediaItemIndex) {
                    player.seekTo(index, 0)
                }
            }
        }
        if (player.playbackState == Player.STATE_IDLE) {
            player.prepare()
        }
        player.play()
    }

    override fun onSpeedChange() {
        when (player.playbackParameters.speed) {
            1f -> player.setPlaybackSpeed(2f)
            2f -> player.setPlaybackSpeed(1f)
        }
        playbackPreferences.edit {
            putFloat(PLAYBACK_SPEED_PREFERENCE, player.playbackParameters.speed)
        }
    }

}