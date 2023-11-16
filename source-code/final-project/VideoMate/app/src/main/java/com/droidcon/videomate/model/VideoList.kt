package com.droidcon.videomate.model

object VideoList : List<Video> by arrayListOf(
    Video(
        id = 1,
        title = "Big Buck Bunny",
        source = "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        thumb = "https://storage.googleapis.com/gtv-videos-bucket/sample/images/BigBuckBunny.jpg",
        durationSeconds = 596,
        format = "mp4",
    ),
    Video(
        id = 2,
        title = "Elephants Dream",
        source = "https://storage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
        thumb = "https://storage.googleapis.com/gtv-videos-bucket/sample/images/ElephantsDream.jpg",
        durationSeconds = 653,
        format = "mp4",
    ),
    Video(
        id = 3,
        title = "By Yaroslav Shuraev - 1",
        source = "pexels_yaroslav_shuraev_4434286_1080x1920_30fps",
        thumb = "",
        durationSeconds = 18,
        format = "mp4",
    ),
    Video(
        id = 4,
        title = "For Bigger Blazes",
        source = "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
        thumb = "https://storage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerBlazes.jpg",
        durationSeconds = 15,
        format = "mp4",
    ),
    Video(
        id = 5,
        title = "By Yaroslav Shuraev - 2",
        source = "pexels_yaroslav_shuraev_4434150_1080x1920_30fps",
        thumb = "",
        durationSeconds = 9,
        format = "mp4",

    ),
    Video(
        id = 6,
        title = "Error test",
        source = "invalid",
        thumb = "",
        durationSeconds = 0,
        format = "error",
    ),
    Video(
        id = 7,
        title = "For Bigger Escapes",
        source = "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
        thumb = "https://storage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerEscapes.jpg",
        durationSeconds = 15,
        format = "mp4",
    ),
    Video(
        id = 8,
        title = "Sintel",
        source = "https://storage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
        thumb = "https://storage.googleapis.com/gtv-videos-bucket/sample/images/Sintel.jpg",
        durationSeconds = 887,
        format = "mp4",
    ),
)