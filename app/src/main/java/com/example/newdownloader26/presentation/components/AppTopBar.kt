package com.example.newdownloader26.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.newdownloader26.R
import com.example.newdownloader26.core.ui.singleClick
import network.chaintech.sdpcomposemultiplatform.sdp

@Composable
fun AppTopBar(
    title: String? = null,
    showBack: Boolean = false,
    onBackClick: () -> Unit = {},
    trailingContent: (@Composable () -> Unit)? = null,
    showPremium: Boolean = true,
    onPremiumClick: (() -> Unit)? = null,
    onMenuClick: (() -> Unit)? = null,
    backInRoundedContainer: Boolean = false
) {
    val premiumLabel = stringResource(R.string.common_premium)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF7F9FB))
            .statusBarsPadding()
            .height(56.dp)
            .padding(start = 8.sdp, end = 12.sdp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (showBack) {
            if (backInRoundedContainer) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFE8EDF2))
                        .clickable(onClick = singleClick(onClick = onBackClick)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_backpress),
                        contentDescription = stringResource(R.string.common_back),
                        modifier = Modifier.size(22.dp),
                        tint = Color(0xFF1D1D1D)
                    )
                }
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_backpress),
                    contentDescription = stringResource(R.string.common_back),
                    modifier = Modifier
                        .size(36.dp)
                        .clickable(onClick = singleClick(onClick = onBackClick)),
                    tint = Color(0xFF1D1D1D)
                )
            }
        } else {
            val menuInteraction = remember { MutableInteractionSource() }
            Icon(
                painter = painterResource(R.drawable.ic_menu),
                contentDescription = stringResource(R.string.common_menu),
                modifier = Modifier
                    .size(22.sdp)
                    .then(
                        if (onMenuClick != null) {
                            Modifier.clickable(
                                interactionSource = menuInteraction,
                                indication = null,
                                onClick = singleClick(onClick = onMenuClick)
                            )
                        } else {
                            Modifier
                        }
                    ),
                tint = Color.Unspecified
            )
        }


        if (title != null) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )

        } else {
            Box(modifier = Modifier.weight(1f))
        }

        when {
            trailingContent != null -> trailingContent()
            showPremium -> {
                val composition by rememberLottieComposition(
                    LottieCompositionSpec.RawRes(R.raw.diamond)
                )
                val premiumInteraction = remember { MutableInteractionSource() }
                Box(
                    modifier = Modifier
                        .size(34.sdp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.White)
                        .then(
                            if (onPremiumClick != null) {
                                Modifier.clickable(
                                    interactionSource = premiumInteraction,
                                    indication = null,
                                    onClick = singleClick(onClick = onPremiumClick)
                                )
                            } else {
                                Modifier
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier
                            .size(28.sdp)
                            .semantics { contentDescription = premiumLabel }
                    )
                }
            }
            else -> Box(modifier = Modifier.size(40.dp))
        }
    }
}
