package com.droidcon.videomate.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.droidcon.videomate.R
import com.droidcon.videomate.model.Video
import com.droidcon.videomate.model.VideoList
import com.droidcon.videomate.ui.theme.VideoMateTheme

@Composable
fun VideoListItem(video: Video, modifier: Modifier = Modifier) {
    VideoListItem(
        thumb = video.thumb,
        title = video.title,
        durationSeconds = video.durationSeconds,
        format = video.format,
        modifier = modifier,
        localFile = !video.source.startsWith("http")
    )
}

@Composable
fun VideoListItem(
    title: String,
    thumb: String,
    durationSeconds: Int,
    format: String,
    modifier: Modifier = Modifier,
    localFile: Boolean,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp),
    ) {
        Box(
            modifier = Modifier
                .height(80.dp)
                .aspectRatio(16 / 9f),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = thumb,
                contentDescription = "Video image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
            if (thumb.isBlank()) {
                Text(
                    text = ".$format",
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .wrapContentSize()
                        .alpha(0.5f)
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_duration),
                    contentDescription = "",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = durationSeconds.toFormattedDuration(),
                    fontSize = 12.sp,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                val iconRes = when (localFile) {
                    true -> R.drawable.icon_download
                    else -> R.drawable.icon_cloud
                }
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = "",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = ".$format",
                    fontSize = 12.sp,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview
@Composable
fun VideoListItemPreview() {
    VideoMateTheme {
        VideoListItem(VideoList[0])
    }
}