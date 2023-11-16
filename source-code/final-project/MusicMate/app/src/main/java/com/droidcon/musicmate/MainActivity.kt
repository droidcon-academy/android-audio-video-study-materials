package com.droidcon.musicmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import com.droidcon.musicmate.ui.PlayerScreen
import com.droidcon.musicmate.ui.theme.MusicMateTheme


class MainActivity : ComponentActivity() {

    private val model by viewModels<PlayerScreenViewModel> {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicMateTheme {
                Surface {
                    val playerScreenState by model.playerScreenState.collectAsState()
                    PlayerScreen(playerScreenState)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        model.onStart()
    }

    override fun onStop() {
        super.onStop()
        model.onStop()
    }

}