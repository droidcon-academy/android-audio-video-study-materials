package com.droidcon.musicmate

import androidx.compose.runtime.Immutable
import com.droidcon.musicmate.model.Song

@Immutable
data class PlayerScreenState(
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val currentPosition: Long = 0L,
    val bufferedPosition: Long = 0L,
    val playlist: List<Song> = emptyList(),
    val currentSong: Song? = null,
    val repeatMode: Int = 0,
    val shuffleModeEnabled: Boolean = false,
    val onPlayPause: () -> Unit = {},
    val onSkipPrevious: () -> Unit = {},
    val onSkipNext: () -> Unit = {},
    val onSeek: (Float) -> Unit = {},
    val onSongClick: (Song) -> Unit = {},
    val onShuffle: () -> Unit = {},
    val onRepeat: () -> Unit = {},
    val error: Exception? = null,
)