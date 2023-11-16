@file:OptIn(ExperimentalMaterial3Api::class)

package com.droidcon.musicmate.ui

import androidx.compose.animation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import com.droidcon.musicmate.R
import com.droidcon.musicmate.model.Song
import com.droidcon.musicmate.model.SongList
import com.droidcon.musicmate.ui.theme.MusicMateTheme
import java.util.concurrent.TimeUnit

@Composable
fun PlaybackControls(
    song: Song?,
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    isBuffering: Boolean = false,
    repeatMode: Int = 0,
    shuffleModeEnabled: Boolean = false,
    currentProgress: Long = 0L,
    bufferedProgress: Long = 0L,
    error: Exception? = null,
    onSeek: (seconds: Float) -> Unit = {},
    onPlayPause: () -> Unit = {},
    onSkipPrevious: () -> Unit = {},
    onSkipNext: () -> Unit = {},
    onRepeat: () -> Unit = {},
    onShuffle: () -> Unit = {},
) {
    Card(
        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Crossfade(targetState = song, label = "NowPlaying") { song ->
                if (song != null) {
                    NowPlaying(song = song, error = error)
                }
            }
            PlayerSlider(
                song = song,
                isBuffering = isBuffering,
                currentProgress = currentProgress,
                bufferedProgress = bufferedProgress,
                onSeek = onSeek,
            )
            Spacer(modifier = Modifier.height(8.dp))
            TransportControlButtons(
                isPlaying = isPlaying,
                repeatMode = repeatMode,
                shuffleModeEnabled = shuffleModeEnabled,
                onPlayPause = onPlayPause,
                onSkipPrevious = onSkipPrevious,
                onSkipNext = onSkipNext,
                onRepeat = onRepeat,
                onShuffle = onShuffle,
            )
        }
    }
}

@Composable
private fun PlayerSlider(
    song: Song?,
    isBuffering: Boolean,
    currentProgress: Long,
    bufferedProgress: Long,
    onSeek: (seconds: Float) -> Unit,
) {
    Column {
        if (isBuffering) {
            CircularProgressIndicator(
                strokeWidth = 2.dp,
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.End)
            )
        } else {
            Spacer(modifier = Modifier.size(16.dp))
        }
        var sliderState by remember {
            mutableStateOf(TimeUnit.MILLISECONDS.toSeconds(currentProgress).toFloat())
        }
        val interactionSource = remember { MutableInteractionSource() }
        val isSeeking by interactionSource.collectIsDraggedAsState()
        val sliderValue = when (isSeeking) {
            true -> sliderState
            else -> TimeUnit.MILLISECONDS.toSeconds(currentProgress).toFloat()
        }
        Slider(
            value = sliderValue,
            interactionSource = interactionSource,
            valueRange = 0f..(song?.durationSeconds?.toFloat() ?: 0f),
            onValueChange = { sliderState = it },
            onValueChangeFinished = { onSeek(sliderState) },
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(),
            track = {
                Box {
                    val bufferedSeconds = (TimeUnit.MILLISECONDS.toSeconds(
                        bufferedProgress
                    ).toFloat()) / (song?.durationSeconds ?: 1)
                    SliderDefaults.Track(
                        colors = SliderDefaults.colors(),
                        enabled = false,
                        sliderState = SliderState(bufferedSeconds),
                    )
                    SliderDefaults.Track(
                        colors = SliderDefaults.colors(inactiveTrackColor = Color.Transparent),
                        enabled = true,
                        sliderState = it,
                    )
                }
            }
        )
        Row {
            Text(
                text = sliderValue.toFormattedDuration(),
                modifier = Modifier.fillMaxWidth(0.5f),
                textAlign = TextAlign.Start,
            )
            Text(
                text = (song?.durationSeconds?.toLong() ?: 0L).toFormattedDuration(),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
            )
        }
    }
}

@Composable
private fun TransportControlButtons(
    isPlaying: Boolean,
    repeatMode: Int,
    shuffleModeEnabled: Boolean,
    onPlayPause: () -> Unit,
    onSkipPrevious: () -> Unit,
    onSkipNext: () -> Unit,
    onRepeat: () -> Unit,
    onShuffle: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onRepeat,
            modifier = Modifier.size(36.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            val repeatIcon = when (repeatMode) {
                Player.REPEAT_MODE_ALL -> R.drawable.icon_repeat
                Player.REPEAT_MODE_ONE -> R.drawable.icon_repeat_one
                else -> R.drawable.icon_repeat_off
            }
            Icon(
                painter = painterResource(id = repeatIcon),
                contentDescription = stringResource(
                    id = R.string.repeat_mode_action_text
                ),
            )
        }
        Button(
            onClick = onSkipPrevious,
            modifier = Modifier.size(48.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_skip_previous),
                contentDescription = stringResource(
                    id = R.string.skip_previous_action_text
                ),
            )
        }
        Button(
            onClick = onPlayPause,
            modifier = Modifier.size(60.dp),
            contentPadding = PaddingValues(8.dp),
        ) {
            Crossfade(targetState = isPlaying, label = "PlayPauseButton") { isPlaying ->
                val (playPauseIconId, playPauseContentDescription) = when (isPlaying) {
                    true -> R.drawable.icon_pause to R.string.pause_action_text
                    else -> R.drawable.icon_play to R.string.play_action_text
                }
                Icon(
                    painter = painterResource(id = playPauseIconId),
                    contentDescription = stringResource(id = playPauseContentDescription),
                    modifier = Modifier.size(36.dp),
                )
            }
        }
        Button(
            onClick = onSkipNext,
            modifier = Modifier.size(48.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_skip_next),
                contentDescription = stringResource(
                    id = R.string.skip_next_action_text
                ),
            )
        }
        Button(
            onClick = onShuffle,
            modifier = Modifier.size(36.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            val shuffleIcon = when (shuffleModeEnabled) {
                true -> R.drawable.icon_shuffle
                else -> R.drawable.icon_shuffle_off
            }
            Icon(
                painter = painterResource(id = shuffleIcon),
                contentDescription = stringResource(
                    id = R.string.shuffle_action_text
                ),
            )
        }
    }
}

@Preview
@Composable
fun PlaybackControlsPreview() {
    MusicMateTheme {
        PlaybackControls(
            song = SongList[0],
            isBuffering = true,
            isPlaying = false,
        )
    }
}