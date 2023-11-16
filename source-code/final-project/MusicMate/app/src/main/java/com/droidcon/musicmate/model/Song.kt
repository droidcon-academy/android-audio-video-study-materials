package com.droidcon.musicmate.model

data class Song(
    val id: Int,
    val title: String,
    val artist: String,
    val source: String,
    val thumb: String,
    val durationSeconds: Int,
)