package com.droidcon.videomate.model

data class Video(
    val id: Int,
    val title: String,
    val source: String,
    val thumb: String,
    val durationSeconds: Int,
    val format: String,
)