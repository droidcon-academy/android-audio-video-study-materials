package com.droidcon.videomate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import com.droidcon.videomate.ui.PlayerScreen
import com.droidcon.videomate.ui.theme.VideoMateTheme

@UnstableApi
class MainActivity : ComponentActivity() {

    private lateinit var mediaSession: MediaSession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupMediaSession()
        setContent {
            VideoMateTheme {
                PlayerScreen(mediaSession.player, mediaSession.token)
            }
        }
    }

    override fun onDestroy() {
        mediaSession.player.release()
        mediaSession.release()
        super.onDestroy()
    }

    private fun setupMediaSession() {
        val audioAttrs = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
            .build()

        val player = ExoPlayer.Builder(this)
            .setAudioAttributes(audioAttrs, true)
            .setHandleAudioBecomingNoisy(true)
            .setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
            .build()

        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                // TODO: Add/Remove window FLAG_KEEP_SCREEN_ON
            }
        })

        mediaSession = MediaSession.Builder(this, player).build()
    }

}