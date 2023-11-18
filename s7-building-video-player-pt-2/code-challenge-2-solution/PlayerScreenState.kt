package com.droidcon.videomate

import androidx.compose.runtime.Immutable
import com.droidcon.videomate.model.Video
import com.droidcon.videomate.ui.ControlsListener

@Immutable
data class PlayerScreenState(
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val currentPosition: Long = 0L,
    val bufferedPosition: Long = 0L,
    val currentVideo: Video? = null,
    val currentSpeed: Int = 1,
    val playlist: List<Video> = emptyList(),
    val controlsListener: ControlsListener = object : ControlsListener {},
    val videoAspectRatio: Float = 16 / 9f,
    val error: Exception? = null,
)
