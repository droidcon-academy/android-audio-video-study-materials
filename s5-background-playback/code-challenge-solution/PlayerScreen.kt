package com.droidcon.musicmate.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.droidcon.musicmate.PlayerScreenState
import com.droidcon.musicmate.R
import com.droidcon.musicmate.model.Song
import com.droidcon.musicmate.model.SongList
import com.droidcon.musicmate.ui.theme.MusicMateTheme


@Composable
fun PlayerScreen(playerScreenState: PlayerScreenState, modifier: Modifier = Modifier) {
    PlayerScreen(
        modifier = modifier,
        isPlaying = playerScreenState.isPlaying,
        isBuffering = playerScreenState.isBuffering,
        playlist = playerScreenState.playlist,
        repeatMode = playerScreenState.repeatMode,
        shuffleModeEnabled = playerScreenState.shuffleModeEnabled,
        currentProgress = playerScreenState.currentPosition,
        bufferedProgress = playerScreenState.bufferedPosition,
        nowPlaying = playerScreenState.currentSong,
        error = playerScreenState.error,
        onPlayPause = playerScreenState.onPlayPause,
        onSkipPrevious = playerScreenState.onSkipPrevious,
        onSkipNext = playerScreenState.onSkipNext,
        onSeek = playerScreenState.onSeek,
        onSongClick = playerScreenState.onSongClick,
        onRepeat = playerScreenState.onRepeat,
        onShuffle = playerScreenState.onShuffle,
    )
}

@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier,
    isPlaying: Boolean = false,
    isBuffering: Boolean = false,
    shuffleModeEnabled: Boolean = false,
    repeatMode: Int = 0,
    currentProgress: Long = 0L,
    bufferedProgress: Long = 0L,
    nowPlaying: Song? = null,
    playlist: List<Song> = emptyList(),
    error: Exception? = null,
    onPlayPause: () -> Unit = {},
    onSkipPrevious: () -> Unit = {},
    onSkipNext: () -> Unit = {},
    onSeek: (seconds: Float) -> Unit = {},
    onSongClick: (Song) -> Unit = {},
    onRepeat: () -> Unit = {},
    onShuffle: () -> Unit = {},
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (header, playlistRef, controls) = createRefs()
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 32.sp,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .padding(8.dp)
                .constrainAs(header) {
                    width = Dimension.fillToConstraints
                    height = Dimension.wrapContent
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
        )
        Playlist(
            playlist = playlist,
            onSongClick = onSongClick,
            modifier = Modifier.constrainAs(playlistRef) {
                height = Dimension.fillToConstraints
                start.linkTo(parent.start)
                top.linkTo(header.bottom)
                end.linkTo(parent.end)
                bottom.linkTo(controls.top)
            }
        )
        PlaybackControls(
            song = nowPlaying,
            isPlaying = isPlaying,
            isBuffering = isBuffering,
            repeatMode = repeatMode,
            shuffleModeEnabled = shuffleModeEnabled,
            currentProgress = currentProgress,
            bufferedProgress = bufferedProgress,
            error = error,
            onPlayPause = onPlayPause,
            onSkipPrevious = onSkipPrevious,
            onSkipNext = onSkipNext,
            onSeek = onSeek,
            onRepeat = onRepeat,
            onShuffle = onShuffle,
            modifier = Modifier.constrainAs(controls) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MusicMateTheme {
        PlayerScreen(playlist = SongList, nowPlaying = SongList[0])
    }
}
