package com.example.newdownloader26.presentation.downloader

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.newdownloader26.R
import com.example.newdownloader26.data.local.AutoPasteManager
import com.example.newdownloader26.presentation.components.CommonSurface
import kotlinx.coroutines.flow.SharedFlow
import org.koin.androidx.compose.koinViewModel

@Composable
fun TikTokPlatformScreen() {
    val viewModel: DownloaderViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    PlatformDownloaderContent(
        platform = DownloadPlatform.TIKTOK,
        headline = stringResource(R.string.platform_tt_headline),
        subhead = stringResource(R.string.platform_tt_subhead),
        platformIconRes = R.drawable.ic_tiktok,
        buttonGradient = Brush.horizontalGradient(
            listOf(Color(0xFF2B2B2B), Color(0xFF0A0A0A))
        ),
        state = state,
        effect = viewModel.effect,
        onIntent = viewModel::onIntent,
        onResumeAutoPaste = { viewModel.tryAutoPasteFromClipboard(onlyPlatform = DownloadPlatform.TIKTOK) }
    )
}

@Composable
fun InstagramPlatformScreen() {
    val viewModel: DownloaderViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    PlatformDownloaderContent(
        platform = DownloadPlatform.INSTAGRAM,
        headline = stringResource(R.string.platform_ig_headline),
        subhead = stringResource(R.string.platform_ig_subhead),
        platformIconRes = R.drawable.ic_intagram,
        buttonGradient = Brush.horizontalGradient(
            listOf(Color(0xFFF09433),Color(0xFFE6683C), Color(0xFFDC2743), Color(0xFFCC2366), Color(0xFFBC1888))
        ),
        state = state,
        effect = viewModel.effect,
        onIntent = viewModel::onIntent,
        onResumeAutoPaste = { viewModel.tryAutoPasteFromClipboard(onlyPlatform = DownloadPlatform.INSTAGRAM) }
    )
}

@Composable
fun FacebookPlatformScreen() {
    val viewModel: DownloaderViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    PlatformDownloaderContent(
        platform = DownloadPlatform.FACEBOOK,
        headline = stringResource(R.string.platform_fb_headline),
        subhead = stringResource(R.string.platform_fb_subhead),
        platformIconRes = R.drawable.ic_facebook,
        buttonGradient = Brush.horizontalGradient(
            listOf(Color(0xFF5B9BFF), Color(0xFF0D4BC4))
        ),
        state = state,
        effect = viewModel.effect,
        onIntent = viewModel::onIntent,
        onResumeAutoPaste = { viewModel.tryAutoPasteFromClipboard(onlyPlatform = DownloadPlatform.FACEBOOK) }
    )
}

@Composable
fun LinkedInPlatformScreen() {
    val viewModel: DownloaderViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    PlatformDownloaderContent(
        platform = DownloadPlatform.LINKEDIN,
        headline = stringResource(R.string.platform_li_headline),
        subhead = stringResource(R.string.platform_li_subhead),
        platformIconRes = R.drawable.ic_linkedin,
        buttonGradient = Brush.horizontalGradient(
            listOf(Color(0xFF0A66C2), Color(0xFF004B83))
        ),
        state = state,
        effect = viewModel.effect,
        onIntent = viewModel::onIntent,
        onResumeAutoPaste = { viewModel.tryAutoPasteFromClipboard(onlyPlatform = DownloadPlatform.LINKEDIN) }
    )
}

@Composable
fun XPlatformScreen() {
    val viewModel: DownloaderViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    PlatformDownloaderContent(
        platform = DownloadPlatform.X,
        headline = stringResource(R.string.platform_x_headline),
        subhead = stringResource(R.string.platform_x_subhead),
        platformIconRes = R.drawable.ic_axe,
        buttonGradient = Brush.horizontalGradient(
            listOf(Color(0xFF2F2F2F), Color(0xFF0F0F0F))
        ),
        state = state,
        effect = viewModel.effect,
        onIntent = viewModel::onIntent,
        onResumeAutoPaste = { viewModel.tryAutoPasteFromClipboard(onlyPlatform = DownloadPlatform.X) }
    )
}

@Composable
private fun PlatformDownloaderContent(
    platform: DownloadPlatform,
    headline: String,
    subhead: String,
    platformIconRes: Int,
    buttonGradient: Brush,
    state: DownloaderState,
    effect: SharedFlow<DownloaderEffect>,
    onIntent: (DownloaderIntent) -> Unit,
    onResumeAutoPaste: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val clipboardEmptyText = stringResource(R.string.downloader_clipboard_empty)
    val screenBg = Color(0xFFF7F9FB)
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

    DownloaderAutoPasteLifecycleEffect(onAutoPaste = onResumeAutoPaste)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(screenBg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        CommonSurface(
            enabled = false,
            shape = RoundedCornerShape(34.dp),
            color = Color.White,
            shadowElevation = 1,
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF4F6F8)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(platformIconRes),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(48.dp)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = headline,
                    color = Color(0xFF1D1D1D),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 26.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = subhead,
                    color = Color(0xFF787F82),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = state.url,
                        onValueChange = { onIntent(DownloaderIntent.OnUrlChanged(it)) },
                        placeholder = {
                            Text(
                                stringResource(R.string.platform_paste_placeholder),
                                color = Color(0xFFB3B3B3),
                                fontSize = 15.sp
                            )
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),

                        shape = RoundedCornerShape(28.dp),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_link),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color(0xFFE4E7EC),
                            unfocusedIndicatorColor = Color(0xFFE4E7EC)
                        ),
                        trailingIcon = {
                            Box(
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(Color(0x1400B306))
                                    .clickable {
                                        val text =
                                            clipboardManager.getText()?.text?.toString()?.trim().orEmpty()
                                        if (text.isBlank()) {
                                            Toast.makeText(
                                                context,
                                                clipboardEmptyText,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            val url = AutoPasteManager.extractFirstHttpUrl(text)
                                            if (url != null && AutoPasteManager.isUrlForPlatform(url, platform)) {
                                                onIntent(DownloaderIntent.OnUrlChanged(url))
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    context.getString(R.string.downloader_clipboard_wrong_platform),
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    }
                                    .padding(horizontal = 13.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    stringResource(R.string.platform_paste_link),
                                    color = Color(0xFF00B306),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    )

                Spacer(modifier = Modifier.height(20.dp))
                GradientPillButton(
                    onClick = onDownloadClick,
                    isLoading = state.isLoading,
                    gradient = buttonGradient
                )
            }
        }
        Spacer(modifier = Modifier.height(28.dp))
        HowToUseSectionFigma()
        Spacer(modifier = Modifier.height(20.dp))
    }

    if (state.showDownloadDialog) {
        DownloadProgressDialog(
            state = state,
            onDismiss = { onIntent(DownloaderIntent.OnDismissDownloadDialog) }
        )
    }
}

@Composable
private fun HowToUseSectionFigma() {
    Text(
        text = stringResource(R.string.platform_how_to_use),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF1D1D1D),
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(12.dp))
    HowToUseFigmaStep(
        stepNumber = stringResource(R.string.platform_step_1),
        title = stringResource(R.string.platform_copy_video_link),
        description = stringResource(R.string.platform_copy_video_link_desc),
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(12.dp))
    HowToUseFigmaStep(
        stepNumber = stringResource(R.string.platform_step_2),
        title = stringResource(R.string.platform_paste_and_download),
        description = stringResource(R.string.platform_paste_and_download_desc),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun HowToUseFigmaStep(
    stepNumber: String,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    CommonSurface (
        shape = RoundedCornerShape(22.dp),
        modifier = modifier,
        shadowElevation = 0

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(88.dp)
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF22B29)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stepNumber,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1D1D1D),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = description,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = Color(0xFF787F82),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun GradientPillButton(
    onClick: () -> Unit,
    isLoading: Boolean,
    gradient: Brush
) {
    val pillShape = RoundedCornerShape(28.dp)
    Surface(
        onClick = onClick,
        enabled = !isLoading,
        shape = pillShape,
        color = Color.Transparent,
        shadowElevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = gradient),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.dp,
                    color = Color.White
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_download),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.platform_download_hd),
                    color = Color(0xFFFAF8FF),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
