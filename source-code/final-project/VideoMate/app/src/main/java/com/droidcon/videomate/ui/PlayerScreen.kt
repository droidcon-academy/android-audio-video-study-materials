package com.droidcon.videomate.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.Player
import androidx.media3.session.SessionToken
import com.droidcon.videomate.PlayerScreenState
import com.droidcon.videomate.PlayerScreenViewModel
import com.droidcon.videomate.R
import com.droidcon.videomate.model.VideoList
import com.droidcon.videomate.ui.theme.VideoMateTheme

@Composable
fun PlayerScreen(player: Player, sessionToken: SessionToken, modifier: Modifier = Modifier) {
    val model: PlayerScreenViewModel = viewModel()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = sessionToken, key2 = context, key3 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                player.pause()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        model.onStart(context, sessionToken)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            model.onStop()
        }
    }

    val screenState by model.playerScreenState.collectAsState()
    PlayerScreen(player, screenState, modifier)
}

@Composable
private fun PlayerScreen(player: Player, screenState: PlayerScreenState, modifier: Modifier = Modifier) {
    Surface(modifier = Modifier.fillMaxSize()) {
        if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            VideoPlayer(player, screenState)
        } else {
            Column(modifier = modifier.fillMaxSize()) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    fontSize = 32.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(8.dp),
                )
                VideoPlayer(player = player, screenState = screenState)
                Playlist(
                    videos = screenState.playlist,
                    currentVideo = screenState.currentVideo.takeIf { screenState.isPlaying },
                    onVideoClick = screenState.controlsListener::onVideoClick,
                    modifier = Modifier.fillMaxHeight(),
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    VideoMateTheme {
        PlayerScreen(DummyPlayer(), PlayerScreenState(playlist = VideoList))
    }
}
