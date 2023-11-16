package com.droidcon.musicmate

import android.app.PendingIntent
import android.content.Intent
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.util.EventLogger
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

class PlaybackService : MediaSessionService() {

    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()

        val audioAttrs = AudioAttributes.Builder()
            // What is this sound for? The usage helps the system to better route
            // the audio (e.g. send it to earbuds or speaker) and manage volume
            // controls. Pressing volume up/down hardware buttons will change
            // only the media volume.
            .setUsage(C.USAGE_MEDIA)
            // What will be played? This information can be used by the system
            // to better configure the audio playback.
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()

        val player = ExoPlayer.Builder(this)
            .setAudioAttributes(audioAttrs, true)
            // Setting handleAudioBecomingNoisy to true makes the player pause
            // the playback when, for example, the earbuds are unplugged instead
            // of keep playing the audio through the speakers.
            .setHandleAudioBecomingNoisy(true)
            // Holds a Wifi and Power Lock to make sure the device stays on and
            // connected even after long periods with the screen off. When the screen
            // is off, the system may turn the wifi off and slow down the CPU
            // to save power.
            .setWakeMode(C.WAKE_MODE_NETWORK)
            .build()

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )
        mediaSession = MediaSession.Builder(this, player)
            // Sets the activity that will be launched when tapping on the media notification.
            .setSessionActivity(pendingIntent)
            .build()
    }


    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }

}