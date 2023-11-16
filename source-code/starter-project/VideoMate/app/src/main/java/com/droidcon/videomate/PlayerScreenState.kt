package com.droidcon.videomate

import com.droidcon.videomate.model.Video
import com.droidcon.videomate.ui.ControlsListener

data class PlayerScreenState(
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val currentPosition: Long = 0L,
    val bufferedPosition: Long = 0L,
    val currentVideo: Video? = null,
    val playlist: List<Video> = emptyList(),
    val controlsListener: ControlsListener = object : ControlsListener {},
    val error: Exception? = null,
)
