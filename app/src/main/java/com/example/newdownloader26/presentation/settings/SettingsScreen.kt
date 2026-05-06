package com.example.newdownloader26.presentation.settings

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.newdownloader26.R
import com.example.newdownloader26.presentation.components.CommonSurface
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import network.chaintech.sdpcomposemultiplatform.sdp

@Composable
fun SettingsScreen(
    autoDetectEnabled: Boolean,
    onAutoDetectChange: (Boolean) -> Unit,
    onChangeLanguage: () -> Unit,
    onOpenProUpgrade: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showHowToDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 12.sdp)
            .padding(bottom = 18.sdp)
    ) {
        Spacer(modifier = Modifier.height(5.sdp))
        PremiumPromoCard(onTryPro = onOpenProUpgrade)
        Spacer(modifier = Modifier.height(10.sdp))

        SettingsNavRow(
            iconRes = R.drawable.ic_how_to_download,
            label = stringResource(R.string.settings_how_to_download),
            onClick = { showHowToDialog = true }
        )
        Spacer(modifier = Modifier.height(12.dp))
        SettingsToggleRow(
            iconRes = R.drawable.ic_auto_detect,
            label = stringResource(R.string.settings_auto_detect_link),
            checked = autoDetectEnabled,
            onCheckedChange = onAutoDetectChange
        )
        Spacer(modifier = Modifier.height(12.dp))
        SettingsNavRow(
            iconRes = R.drawable.ic_language,
            label = stringResource(R.string.settings_change_language),
            onClick = onChangeLanguage
        )
        Spacer(modifier = Modifier.height(12.dp))
        SettingsNavRow(
            iconRes = R.drawable.ic_privacy_policy,
            label = stringResource(R.string.settings_privacy_policy),
            onClick = {
                context.openExternalUrl(context.getString(R.string.settings_privacy_policy_url))
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        SettingsNavRow(
            iconRes = R.drawable.ic_rate_us,
            label = stringResource(R.string.settings_rate_us),
            onClick = { context.openPlayStoreForRating() }
        )
        Spacer(modifier = Modifier.height(12.dp))
        SettingsNavRow(
            iconRes = R.drawable.ic_more_apps,
            label = stringResource(R.string.settings_more_apps),
            onClick = {
                context.openExternalUrl(context.getString(R.string.settings_more_apps_url))
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        SettingsNavRow(
            iconRes = R.drawable.ic_share_app,
            label = stringResource(R.string.settings_share_app),
            onClick = { context.shareApp() }
        )
    }

    if (showHowToDialog) {
        AlertDialog(
            onDismissRequest = { showHowToDialog = false },
            title = {
                Text(
                    text = stringResource(R.string.settings_how_to_download),
                    fontWeight = FontWeight.SemiBold
                )
            },
            text = {
                Text(text = stringResource(R.string.settings_how_to_message))
            },
            confirmButton = {
                TextButton(onClick = { showHowToDialog = false }) {
                    Text(stringResource(R.string.common_ok))
                }
            }
        )
    }
}

@Composable
private fun PremiumPromoCard(
    onTryPro: () -> Unit,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.diamond)
    )
    CommonSurface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFFF0F0F0),
        shadowElevation = 1,
        enabled = false
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.sdp, end = 15.sdp, bottom = 10.sdp, top = 5.sdp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(50.sdp),
                contentAlignment = Alignment.Center
            ) {
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier.size(48.sdp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.settings_get_premium),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D1D1D)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.settings_premium_description),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF787F82),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(22.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(26.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFFF7A2F),
                                Color(0xFFFFC93F)
                            )
                        )
                    )
                    .clickable(onClick = onTryPro),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.settings_try_pro_now),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun SettingsNavRow(
    iconRes: Int,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CommonSurface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFFF0F0F0),
        shadowElevation = 1,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = Color.Unspecified
            )
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1D1D1D),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun SettingsToggleRow(
    iconRes: Int,
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFFF0F0F0),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = Color.Unspecified
            )
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1D1D1D),
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF2196F3),
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFFB0BEC5),
                    uncheckedBorderColor = Color.Transparent
                )
            )
        }
    }
}

private fun Context.openExternalUrl(url: String) {
    runCatching {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}

private fun Context.openPlayStoreForRating() {
    val pkg = packageName
    val marketUri = Uri.parse("market://details?id=$pkg")
    try {
        startActivity(Intent(Intent.ACTION_VIEW, marketUri))
    } catch (_: ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$pkg")
            )
        )
    }
}

private fun Context.shareApp() {
    val message = getString(
        R.string.settings_share_app_message,
        getString(R.string.downloader_title),
        packageName
    )
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    val chooser = Intent.createChooser(intent, getString(R.string.share_chooser_title))
    runCatching { startActivity(chooser) }
}
