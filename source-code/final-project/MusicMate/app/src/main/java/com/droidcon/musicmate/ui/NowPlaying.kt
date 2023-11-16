package com.droidcon.musicmate.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.droidcon.musicmate.R
import com.droidcon.musicmate.model.Song
import com.droidcon.musicmate.model.SongList
import com.droidcon.musicmate.ui.theme.MusicMateTheme

@Composable
fun NowPlaying(song: Song, error: Exception? = null) {
    NowPlaying(
        image = song.thumb,
        title = song.title,
        artist = song.artist,
        error = error,
    )
}


@Composable
fun NowPlaying(image: String, title: String, artist: String, error: Exception?) {
    Row(verticalAlignment = Alignment.Top) {
        AsyncImage(
            model = image.takeIf { it.isNotBlank() },
            fallback = painterResource(id = R.drawable.icon_music_note),
            placeholder = painterResource(id = R.drawable.icon_music_note),
            contentDescription = stringResource(id = R.string.now_playing_album_cover_text),
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
        ) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_person),
                    contentDescription = "",
                    modifier = Modifier.size(16.dp)
                )
                Text(text = artist, modifier = Modifier.padding(start = 4.dp))
            }
            if (error != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_error),
                        contentDescription = "",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Red
                    )
                    Text(
                        text = stringResource(id = R.string.generic_playback_error_text),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp),
                        color = Color.Red
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NowPlayingPreview() {
    MusicMateTheme {
        NowPlaying(SongList[0])
    }
}