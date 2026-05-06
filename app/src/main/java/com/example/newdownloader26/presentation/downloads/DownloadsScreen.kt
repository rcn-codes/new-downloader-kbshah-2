package com.example.newdownloader26.presentation.downloads

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.StatFs
import android.util.Size
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.newdownloader26.R
import com.example.newdownloader26.domain.model.DownloadedVideo
import com.example.newdownloader26.domain.model.SourcePlatform
import com.example.newdownloader26.presentation.components.CommonSurface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import network.chaintech.sdpcomposemultiplatform.sdp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DownloadsScreen(
    state: DownloadsState,
    onIntent: (DownloadsIntent) -> Unit,
    onOpenVideo: (DownloadedVideo) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) { onIntent(DownloadsIntent.OnLoad) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FB))
            .padding(bottom = 7.sdp, start = 12.sdp, end = 12.sdp)
    ) {

        Text(
            text = "Downloads",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(14.dp))
        PlatformFilterRow(
            selected = state.selectedFilter,
            onSelect = { onIntent(DownloadsIntent.OnFilterSelected(it)) }
        )
        Spacer(modifier = Modifier.height(16.dp))

        when {
            state.isLoading -> Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            state.visibleItems.isEmpty() -> Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                EmptyDownloadsCard()
            }

            else -> LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.visibleItems, key = { it.id }) { item ->
                    DownloadedVideoCard(
                        item = item,
                        onClick = { onOpenVideo(item) },
                        onShareClick = { shareVideo(context, item) },
                        onDeleteClick = { onIntent(DownloadsIntent.OnDeleteClicked(item)) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        AvailableStorageCard()
    }
}

@Composable
private fun PlatformFilterRow(
    selected: SourcePlatform,
    onSelect: (SourcePlatform) -> Unit
) {
    val chips = listOf(
        SourcePlatform.ALL,
        SourcePlatform.TIKTOK,
        SourcePlatform.FACEBOOK,
        SourcePlatform.INSTAGRAM,
        SourcePlatform.X,
        SourcePlatform.LINKEDIN
    )

    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(chips) { platform ->
            val selectedChip = selected == platform
            val width = when (platform) {
                SourcePlatform.ALL -> 84.dp
                SourcePlatform.TIKTOK -> 96.dp
                SourcePlatform.FACEBOOK -> 96.dp
                SourcePlatform.INSTAGRAM -> 96.dp
                SourcePlatform.X -> 78.dp
                SourcePlatform.LINKEDIN -> 98.dp
                else -> 90.dp
            }
            CommonSurface(
                modifier = Modifier
                    .width(width)
                    .height(44.dp),
                shape = RoundedCornerShape(26.dp),
                color = if (selectedChip) Color(0xFFFF2D2E) else Color(0xFFF7F7F7),
                shadowElevation = 0,
                onClick = { onSelect(platform) }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            width = if (selectedChip) 0.dp else 1.dp,
                            color = Color(0xFFE8E8E8),
                            shape = RoundedCornerShape(26.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = platformLabel(platform),
                        color = if (selectedChip) Color.White else Color(0xFF828282),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (selectedChip) FontWeight.Medium else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
private fun DownloadedVideoCard(
    item: DownloadedVideo,
    onClick: () -> Unit,
    onShareClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val cardShape = RoundedCornerShape(20.dp)
    val meta = buildString {
        append(formatFileSize(item.sizeBytes))
        append(" · ")
        append(SimpleDateFormat("MMM dd", Locale.getDefault()).format(Date(item.createdAtMillis)))
    }

    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(93.dp)
            .clip(cardShape)
            .border(1.dp, Color(0xFFEAEAEA), cardShape),
        shape = cardShape,
        color = Color(0xFFF7F7F7),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 0.dp, end = 12.dp, top = 0.dp, bottom = 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(width = 76.dp, height = 93.dp)

                    .background(Color(0xFFD8D8D8)),

                contentAlignment = Alignment.Center
            ) {
                VideoThumbnail(
                    item = item,
                    modifier = Modifier
                        .fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White.copy(alpha = 0.50f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_play),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.fileName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFF171717)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = platformLabel(item.sourcePlatform),
                        style = MaterialTheme.typography.labelSmall,
                        color = platformColor(item.sourcePlatform)
                    )
                    Text(
                        text = "  $meta",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF8A8A8A)
                    )
                }
            }
            Icon(
                painter = painterResource(R.drawable.ic_share),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(20.dp)
                    .clickable(onClick = onShareClick)
            )
            Spacer(modifier = Modifier.width(18.dp))
            Icon(
                painter = painterResource(R.drawable.ic_delete),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(20.dp)
                    .clickable(onClick = onDeleteClick)
            )
        }
    }
}

@Composable
private fun EmptyDownloadsCard() {
    CommonSurface(shape = RoundedCornerShape(18.dp), color = Color.White, enabled = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_downloaded),
                contentDescription = null,
                modifier = Modifier.size(34.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "No downloads yet",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun VideoThumbnail(
    item: DownloadedVideo,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val bitmap by produceState<Bitmap?>(initialValue = null, key1 = item.playUri, key2 = item.savedPath) {
        value = withContext(Dispatchers.IO) {
            loadVideoThumbnail(context, item)
        }
    }

    if (bitmap != null) {
        Image(
            bitmap = bitmap!!.asImageBitmap(),
            contentDescription = null,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}

private fun loadVideoThumbnail(context: Context, item: DownloadedVideo): Bitmap? {
    val uri = item.playUri?.let { Uri.parse(it) }

    if (uri != null) {
        runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                return context.contentResolver.loadThumbnail(uri, Size(220, 220), null)
            }
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(context, uri)
            val frame = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            retriever.release()
            return frame
        }
    }

    if (item.savedPath.startsWith("/")) {
        runCatching {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(item.savedPath)
            val frame = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            retriever.release()
            return frame
        }
    }

    return null
}

@Composable
private fun AvailableStorageCard() {
    val context = LocalContext.current
    val storage = rememberStorageInfo(context)

    CommonSurface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF1F2F3),
        enabled = false
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFF4A4B)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_available_storage),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(14.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Available Storage",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF171717)
                )
                Text(
                    text = "${storage.usedGb} GB of ${storage.totalGb} GB used",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF9A9A9A)
                )
            }
            Box(
                modifier = Modifier
                    .width(92.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color(0xFFDDE3E8))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(storage.usedPercent)
                        .height(6.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(Color(0xFFFF4344))
                )
            }
        }
    }
}

@Composable
private fun rememberStorageInfo(context: Context): StorageInfo {
    val info by produceState(initialValue = StorageInfo("0.0", "0.0", 0f), key1 = context) {
        value = withContext(Dispatchers.IO) {
            val path = context.getExternalFilesDir(null)?.absolutePath ?: context.filesDir.absolutePath
            val statFs = StatFs(path)
            val totalBytes = statFs.totalBytes
            val availableBytes = statFs.availableBytes
            val usedBytes = (totalBytes - availableBytes).coerceAtLeast(0L)
            val usedPercent = if (totalBytes <= 0L) 0f else (usedBytes.toFloat() / totalBytes.toFloat()).coerceIn(0f, 1f)
            StorageInfo(
                usedGb = formatGb(usedBytes),
                totalGb = formatGb(totalBytes),
                usedPercent = usedPercent
            )
        }
    }
    return info
}

private fun formatGb(bytes: Long): String {
    val gb = bytes / (1024.0 * 1024.0 * 1024.0)
    return String.format(Locale.getDefault(), "%.1f", gb)
}

private data class StorageInfo(
    val usedGb: String,
    val totalGb: String,
    val usedPercent: Float
)

private fun shareVideo(context: android.content.Context, item: DownloadedVideo) {
    val uriText = item.playUri
    if (uriText.isNullOrBlank()) {
        Toast.makeText(context, "Unable to share this video", Toast.LENGTH_SHORT).show()
        return
    }
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "video/*"
        putExtra(Intent.EXTRA_STREAM, Uri.parse(uriText))
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    try {
        context.startActivity(Intent.createChooser(shareIntent, "Share video"))
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(context, "No app found to share", Toast.LENGTH_SHORT).show()
    }
}

private fun formatFileSize(sizeBytes: Long?): String {
    val bytes = sizeBytes ?: return "Unknown size"
    val mb = bytes / (1024.0 * 1024.0)
    return String.format(Locale.getDefault(), "%.1f MB", mb)
}

private fun platformLabel(platform: SourcePlatform): String = when (platform) {
    SourcePlatform.ALL -> "All"
    SourcePlatform.TIKTOK -> "TikTok"
    SourcePlatform.INSTAGRAM -> "Instagram"
    SourcePlatform.FACEBOOK -> "Facebook"
    SourcePlatform.LINKEDIN -> "LinkedIn"
    SourcePlatform.X -> "X"
    SourcePlatform.UNKNOWN -> "Other"
}

private fun platformColor(platform: SourcePlatform): Color = when (platform) {
    SourcePlatform.FACEBOOK -> Color(0xFF4A6CFF)
    SourcePlatform.INSTAGRAM -> Color(0xFFFF3D6B)
    SourcePlatform.LINKEDIN -> Color(0xFF2F7BCC)
    SourcePlatform.TIKTOK -> Color(0xFF5E5E5E)
    SourcePlatform.X -> Color(0xFF505050)
    else -> Color(0xFF7D7D7D)
}
