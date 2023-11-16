package com.droidcon.videomate.ui

import java.util.concurrent.TimeUnit

fun Number.toFormattedDuration(timeUnit: TimeUnit = TimeUnit.SECONDS): String {
    val thisLong = toLong()
    return "%02d:%02d".format(
        timeUnit.toMinutes(thisLong),
        timeUnit.toSeconds(thisLong) % 60
    )
}