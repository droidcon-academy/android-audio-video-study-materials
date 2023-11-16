@file:OptIn(ExperimentalMaterial3Api::class)

package com.droidcon.videomate.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.droidcon.videomate.R
import com.droidcon.videomate.ui.theme.VideoMateTheme
import java.util.concurrent.TimeUnit


@Composable
fun PlayerControls(
    isPlaying: Boolean,
    isLoading: Boolean,
    currentSpeed: Int,
    currentPosition: Long,
    bufferedPosition: Long,
    durationSeconds: Int?,
    controlsListener: ControlsListener,
    modifier: Modifier = Modifier,
) {
    PlayerControls(
        isPlaying = isPlaying,
        isLoading = isLoading,
        currentSpeed = currentSpeed,
        currentPosition = currentPosition,
        bufferedPosition = bufferedPosition,
        durationSeconds = durationSeconds,
        onPlayPause = controlsListener::onPlayPause,
        onRewind = controlsListener::onRewind,
        onForward = controlsListener::onForward,
        onSkipNext = controlsListener::onSkipNext,
        onSkipPrevious = controlsListener::onSkipPrevious,
        onSeek = controlsListener::onSeek,
        onSpeedChange = controlsListener::onSpeedChange,
        modifier = modifier,
    )
}

@Composable
fun PlayerControls(
    isPlaying: Boolean,
    isLoading: Boolean,
    currentSpeed: Int,
    currentPosition: Long,
    bufferedPosition: Long,
    durationSeconds: Int?,
    modifier: Modifier = Modifier,
    onPlayPause: () -> Unit = {},
    onSkipPrevious: () -> Unit = {},
    onSkipNext: () -> Unit = {},
    onSeek: (seconds: Float) -> Unit = {},
    onForward: () -> Unit = {},
    onRewind: () -> Unit = {},
    onSpeedChange: () -> Unit = {},
) {
    val verticalGradient = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to Color.Black.copy(alpha = 0.6f),
            0.5f to Color.Black.copy(alpha = 0.3f),
            1f to Color.Black.copy(alpha = 0.6f),
        )
    )
    ConstraintLayout(
        modifier = modifier.background(verticalGradient)
    ) {
        val (midControls, slider, startText, endText, speedIcon) = createRefs()
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.constrainAs(midControls) {
                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_skip_previous),
                contentDescription = stringResource(
                    id = R.string.skip_previous_action_text
                ),
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onSkipPrevious),
                tint = Color.White,
            )
            Icon(
                painter = painterResource(id = R.drawable.icon_rewind),
                contentDescription = stringResource(
                    id = R.string.rewind_action_text
                ),
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onRewind),
                tint = Color.White,
            )
            Crossfade(targetState = isLoading, label = "PlayPauseButton") { isLoading ->
                if (!isPlaying && isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(60.dp)
                            .padding(8.dp), color = Color.White
                    )
                } else {
                    val (playPauseIconId, playPauseContentDescription) = when (isPlaying) {
                        true -> R.drawable.icon_pause to R.string.pause_action_text
                        else -> R.drawable.icon_play to R.string.play_action_text
                    }
                    Icon(
                        painter = painterResource(id = playPauseIconId),
                        contentDescription = stringResource(id = playPauseContentDescription),
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .clickable(onClick = onPlayPause),
                        tint = Color.White,
                    )
                }
            }
            Icon(
                painter = painterResource(id = R.drawable.icon_forward),
                contentDescription = stringResource(
                    id = R.string.forward_action_text
                ),
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onForward),
                tint = Color.White,
            )
            Icon(
                painter = painterResource(id = R.drawable.icon_skip_next),
                contentDescription = stringResource(
                    id = R.string.skip_next_action_text
                ),
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onSkipNext),
                tint = Color.White,
            )
        }

        Text(
            text = currentPosition.toFormattedDuration(TimeUnit.MILLISECONDS),
            textAlign = TextAlign.Start,
            fontSize = 12.sp,
            modifier = Modifier
                .constrainAs(startText) {
                    width = Dimension.value(40.dp)
                    height = Dimension.wrapContent
                    start.linkTo(parent.start, margin = 8.dp)
                    end.linkTo(slider.start)
                    top.linkTo(slider.top)
                    bottom.linkTo(slider.bottom)
                },
        )
        Text(
            text = (durationSeconds ?: 0).toFormattedDuration(),
            textAlign = TextAlign.End,
            fontSize = 12.sp,
            modifier = Modifier
                .constrainAs(endText) {
                    width = Dimension.value(40.dp)
                    height = Dimension.wrapContent
                    start.linkTo(slider.end)
                    end.linkTo(parent.end, margin = 8.dp)
                    top.linkTo(slider.top)
                    bottom.linkTo(slider.bottom)
                },
        )
        Row(
            modifier = Modifier
                .constrainAs(speedIcon) {
                    end.linkTo(parent.end, margin = 8.dp)
                    top.linkTo(parent.top, margin = 8.dp)
                }
                .clip(CircleShape)
                .clickable(onClick = onSpeedChange),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("${currentSpeed}x")
            Icon(
                painter = painterResource(id = R.drawable.icon_speed),
                contentDescription = stringResource(
                    id = R.string.playback_speed_action
                ),
                modifier = Modifier.size(30.dp),
                tint = Color.White,
            )
        }
        PlayerSlider(
            modifier = Modifier.constrainAs(slider) {
                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
                linkTo(midControls.bottom, parent.bottom, bias = 1.0f)
                start.linkTo(startText.end, margin = 8.dp)
                end.linkTo(endText.start, margin = 8.dp)
            },
            currentProgress = currentPosition,
            bufferedProgress = bufferedPosition,
            durationSeconds = durationSeconds?.toLong() ?: 0L,
            onSeek = onSeek,
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerSlider(
    currentProgress: Long,
    bufferedProgress: Long,
    durationSeconds: Long,
    modifier: Modifier = Modifier,
    onSeek: (seconds: Float) -> Unit,
) {
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
        modifier = modifier,
        value = sliderValue,
        interactionSource = interactionSource,
        valueRange = 0f..durationSeconds.toFloat(),
        onValueChange = { sliderState = it },
        onValueChangeFinished = { onSeek(sliderState) },
        colors = SliderDefaults.colors(),
        track = {
            Box {
                val bufferedSeconds = (TimeUnit.MILLISECONDS.toSeconds(bufferedProgress).toFloat()) / durationSeconds
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
        },
    )
}

@Preview(widthDp = 360, heightDp = 211)
@Composable
fun PlayerControlsPreview() {
    VideoMateTheme {
        Surface {
            PlayerControls(
                isPlaying = false,
                isLoading = false,
                currentSpeed = 1,
                currentPosition = 5_000L,
                bufferedPosition = 50_000L,
                durationSeconds = 50,
                modifier = Modifier,
                onPlayPause = {},
                onRewind = {},
                onForward = {},
                onSkipNext = {},
                onSkipPrevious = {},
                onSeek = {},
                onSpeedChange = {},
            )
        }
    }
}