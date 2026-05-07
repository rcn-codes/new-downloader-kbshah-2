package com.example.newdownloader26.presentation.downloader

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newdownloader26.R
import com.example.newdownloader26.presentation.components.CommonSurface
import com.example.newdownloader26.presentation.components.InvalidLinkDialog
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.serialization.Serializable
import androidx.annotation.StringRes
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp

@Serializable
enum class DownloadPlatform(
    @StringRes val titleRes: Int,
    @StringRes val subtitleRes: Int,
    val iconRes: Int
) {
    TIKTOK(R.string.topbar_tiktok_downloader, R.string.downloader_video_and_music, R.drawable.ic_tiktok),
    INSTAGRAM(R.string.topbar_instagram_downloader, R.string.downloader_reels_and_posts, R.drawable.ic_intagram),
    FACEBOOK(R.string.topbar_facebook_downloader, R.string.downloader_video_and_stories, R.drawable.ic_facebook),
    LINKEDIN(R.string.topbar_linkedin_downloader, R.string.downloader_posts_and_clips, R.drawable.ic_linkedin),
    X(R.string.topbar_x_downloader, R.string.downloader_tweets_and_video, R.drawable.ic_axe)
}

@Composable
fun DownloaderScreen(
    state: DownloaderState,
    effect: SharedFlow<DownloaderEffect>,
    onIntent: (DownloaderIntent) -> Unit,
    onPlatformSelected: (DownloadPlatform) -> Unit,
    onViewDownloads: () -> Unit = {},
    onResumeAutoPaste: (() -> Unit)? = null,
    title: String = stringResource(R.string.downloader_title),
    subtitle: String = stringResource(R.string.downloader_subtitle),
    showHeader: Boolean = true,
    showDirectLinks: Boolean = true,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val clipboardEmptyText = stringResource(R.string.downloader_clipboard_empty)
    val onDownloadClick = rememberDownloadActionWithPermissions {
        onIntent(DownloaderIntent.OnDownloadClicked)
    }

    LaunchedEffect(effect) {
        effect.collect { uiEffect ->
            when (uiEffect) {
                is DownloaderEffect.ShowToast -> {
                    Toast.makeText(context, uiEffect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    onResumeAutoPaste?.let { paste ->
        DownloaderAutoPasteLifecycleEffect(onAutoPaste = paste)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FB))
            .padding(start = 12.sdp, end = 12.sdp)
            .verticalScroll(rememberScrollState())
    ) {

        if (showHeader) {
            Text(
                title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(5.sdp))
            Text(
                subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF787F82)
            )
            Spacer(modifier = Modifier.height(13.sdp))
        }
        CommonSurface(enabled = false, shape = RoundedCornerShape(13.sdp)) {
            OutlinedTextField(
                value = state.url,
                onValueChange = { onIntent(DownloaderIntent.OnUrlChanged(it)) },
                placeholder = {
                    Text(
                        stringResource(R.string.downloader_paste_placeholder),
                        color = Color(0xFFB3B3B3),
                        fontSize = 12.ssp,
                        maxLines = 1
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.sdp),
                shape = RoundedCornerShape(13.sdp),
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_link),
                        contentDescription = stringResource(R.string.common_link),
                        tint = Color.Unspecified,
                        modifier = Modifier.size(15.sdp)

                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                trailingIcon = {
                    Box(
                        modifier = Modifier
                            .padding(end = 10.sdp)
                            .clip(RoundedCornerShape(99.sdp))
                            .background(Color(0x1400B306))
                            .padding(horizontal = 2.sdp, vertical = 2.sdp)
                            .clickable {
                                val text = clipboardManager.getText()?.text?.toString()?.trim().orEmpty()
                                if (text.isBlank()) {
                                    Toast.makeText(
                                        context,
                                        clipboardEmptyText,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    onIntent(DownloaderIntent.OnUrlChanged(text))
                                }
                            }
                            .padding(horizontal = 8.sdp, vertical = 3.sdp)
                    ) {
                        Text(
                            stringResource(R.string.downloader_paste),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFF00B306)
                        )
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(11.sdp))
        Button(
            onClick = onDownloadClick,
            enabled = !state.isLoading,
            shape = RoundedCornerShape(13.sdp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF22B29)),
            modifier = Modifier
                .fillMaxWidth()
                .height(43.sdp)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.sdp),
                    strokeWidth = 2.sdp,
                    color = Color.White
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_download),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(13.sdp)
                )
                Spacer(modifier = Modifier.size(7.sdp))
                Text(
                    stringResource(R.string.downloader_download_video),
                    color = Color(0xFFFAF8FF),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        if (showDirectLinks) {
            Spacer(modifier = Modifier.height(13.sdp))
            Text(
                stringResource(R.string.downloader_direct_links),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.sdp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.sdp)) {
                TiktokDirectLinkCard(
                    title = stringResource(R.string.platform_tiktok),
                    subtitle = stringResource(R.string.downloader_video_and_music),
                    modifier = Modifier
                        .weight(1f)
                        .height(140.sdp),
                    iconPainter = R.drawable.ic_tiktok,
                    onClick = { onPlatformSelected(DownloadPlatform.TIKTOK) }
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.sdp)
                ) {
                    PlatformDirectLinkRowCard(
                        stringResource(R.string.downloader_fb_saver),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.sdp),
                        iconPainter = R.drawable.ic_facebook,
                        onClick = { onPlatformSelected(DownloadPlatform.FACEBOOK) })
                    PlatformDirectLinkRowCard(
                        stringResource(R.string.downloader_ig_saver),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.sdp),
                        iconPainter = R.drawable.ic_intagram,
                        onClick = { onPlatformSelected(DownloadPlatform.INSTAGRAM) })
                }
            }

            Spacer(modifier = Modifier.height(10.sdp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.sdp)) {
                PlatformDirectLinkRowCard(
                    stringResource(R.string.downloader_linkedin_saver),
                    modifier = Modifier
                        .weight(1f)
                        .height(64.sdp),
                    iconPainter = R.drawable.ic_linkedin,
                    onClick = { onPlatformSelected(DownloadPlatform.LINKEDIN) })
                PlatformDirectLinkRowCard(
                    stringResource(R.string.downloader_axe),
                    modifier = Modifier
                        .weight(1f)
                        .height(64.sdp),
                    iconPainter = R.drawable.ic_axe,
                    onClick = { onPlatformSelected(DownloadPlatform.X) })
            }
        }



        Spacer(modifier = Modifier.height(11.sdp))
    }

    if (state.showDownloadDialog) {
        DownloadProgressDialog(
            state = state,
            onDismiss = { onIntent(DownloaderIntent.OnDismissDownloadDialog) },
            onViewDownloads = onViewDownloads
        )
    }

    if (state.showInvalidLinkDialog) {
        InvalidLinkDialog(
            onClose = { onIntent(DownloaderIntent.OnDismissInvalidLinkDialog) }
        )
    }
}

@Composable
private fun TiktokDirectLinkCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    iconPainter: Int,
    onClick: () -> Unit
) {
    CommonSurface(modifier = modifier, onClick = onClick)
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.sdp),
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(iconPainter),
                contentDescription = title,
                modifier = Modifier.size(35.sdp),
            )

            Spacer(modifier = Modifier.weight(1f))
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.sdp))
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color(0xFF787F82))
        }
    }
}

@Composable
private fun PlatformDirectLinkRowCard(
    title: String,
    modifier: Modifier = Modifier,
    iconPainter: Int,
    onClick: () -> Unit
) {
    CommonSurface(modifier = modifier, onClick = { onClick.invoke() })
    {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.sdp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.sdp)
        ) {

            Image(
                painter = painterResource(iconPainter),
                contentDescription = title,
                modifier = Modifier.size(30.sdp)
            )

            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
