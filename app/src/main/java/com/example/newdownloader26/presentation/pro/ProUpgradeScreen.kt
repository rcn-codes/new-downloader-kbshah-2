package com.example.newdownloader26.presentation.pro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newdownloader26.R
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp

enum class ProUpgradePlan {
    Weekly,
    Monthly,
    Lifetime,
}

@Composable
fun ProUpgradeScreen(
    onBack: () -> Unit,
    onBuyNow: (ProUpgradePlan) -> Unit,
    onOpenTerms: () -> Unit,
    onOpenPrivacy: () -> Unit,
    onRestorePurchase: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedPlan by remember { mutableStateOf(ProUpgradePlan.Monthly) }

    val plans = remember {
        listOf(
            ProPlanUi(
                plan = ProUpgradePlan.Weekly,
                titleRes = R.string.pro_plan_weekly,
                priceRes = R.string.pro_price_weekly,
                showOffBadge = false
            ),
            ProPlanUi(
                plan = ProUpgradePlan.Monthly,
                titleRes = R.string.pro_plan_monthly,
                priceRes = R.string.pro_price_monthly,
                showOffBadge = true
            ),
            ProPlanUi(
                plan = ProUpgradePlan.Lifetime,
                titleRes = R.string.pro_plan_lifetime,
                priceRes = R.string.pro_price_lifetime,
                showOffBadge = false
            ),
        )
    }

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(R.drawable.bg_inapp_screen),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
            )



        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            CloseButton(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 7.sdp , end = 8.sdp )
            )

            Spacer(modifier = Modifier.height(185.sdp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                UpgradeTitle()
                Spacer(modifier = Modifier.height(14.sdp))
                FeaturesCard(modifier = Modifier.padding(horizontal = 12.sdp ))
                Spacer(modifier = Modifier.height(15.sdp))

                PlanSelector(
                    plans = plans,
                    selectedPlan = selectedPlan,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.sdp ),
                    onSelect = { selectedPlan = it }
                )

                Spacer(modifier = Modifier.height(22.sdp))

                BuyNowButton(
                    onClick = { onBuyNow(selectedPlan) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.sdp )
                )

                Spacer(modifier = Modifier.height(15.sdp))

                Text(
                    text = stringResource(R.string.pro_legal_disclaimer),
                    modifier = Modifier.padding(horizontal = (12.sdp  + 4.sdp)),
                    color = Color(0xFF323233),
                    fontSize = 9.ssp,
                    lineHeight = 10.ssp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.sdp))

                FooterLinks(
                    onOpenTerms = onOpenTerms,
                    onOpenPrivacy = onOpenPrivacy,
                    onRestorePurchase = onRestorePurchase,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.sdp )
                        .padding(bottom = 24.sdp)
                )
            }
        }
    }



}


private data class ProPlanUi(
    val plan: ProUpgradePlan,
    val titleRes: Int,
    val priceRes: Int,
    val showOffBadge: Boolean,
)

@Composable
private fun CloseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        val closeInteraction = remember { MutableInteractionSource() }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(34.sdp)
                .clip(RoundedCornerShape(12.sdp))
                .background(Color(0x44000000))
                .clickable(
                    interactionSource = closeInteraction,
                    indication = null,
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.common_close),
                tint = Color.White,
                modifier = Modifier.size(18.sdp)
            )
        }
    }
}

@Composable
private fun UpgradeTitle() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.sdp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.pro_upgrade_title_prefix),
            color = Color(0xFF1A1A1A),
            style = MaterialTheme.typography.headlineLarge,

        )
        Spacer(modifier = Modifier.width(5.sdp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.sdp))
                .background(Color(0xFF1A1A1A))
                .padding(horizontal = 8.sdp, vertical = 2.sdp)
        ) {
            Text(
                text = stringResource(R.string.pro_upgrade_badge),
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,

            )
        }
    }
}

@Composable
private fun FeaturesCard(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.sdp),
        color = Color.White,
        shadowElevation = 4.sdp
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 5.sdp)
                .fillMaxWidth()
                .padding(vertical = 10.sdp, horizontal = 10.sdp),
            verticalArrangement = Arrangement.spacedBy(12.sdp)
        ) {
            FeatureRow(
                icon = R.drawable.ic_unlimited_download,
                iconWidth = 20.sdp,
                iconHeight = 18.sdp,
                label = stringResource(R.string.pro_feature_unlimited)
            )
            FeatureRow(
                icon = R.drawable.ic_2x_faster,
                iconWidth = 17.sdp,
                iconHeight = 17.sdp,
                label = stringResource(R.string.pro_feature_faster)
            )
            FeatureRow(
                icon = R.drawable.ic_ad_free,
                iconWidth = 18.sdp,
                iconHeight = 18.sdp,
                label = stringResource(R.string.pro_feature_ad_free)
            )
        }
    }
}

@Composable
private fun FeatureRow(
    icon: Int,
    iconWidth: Dp,
    iconHeight: Dp,
    label: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.sdp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(width = iconWidth, height = iconHeight),
            tint = Color.Unspecified
        )
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = Color(0xFF1A1A1A),
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.bodyLarge
        )
        Icon(
            painter = painterResource(R.drawable.ic_crown_offer),
            contentDescription = null,
            modifier = Modifier.size(18.sdp),
            tint = Color.Unspecified
        )
    }
}

@Composable
private fun PlanOption(
    title: String,
    price: String,
    selected: Boolean,
    showOffBadge: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bg = if (selected) Color(0xFFE82222) else Color.White
    val fg = if (selected) Color.White else Color(0xFF1A1A2E)
    val crownTint = if (selected) Color.White else Color.Unspecified

    Box(modifier = modifier) {
        Surface(
            onClick = onClick,
            shape = RoundedCornerShape(16.sdp),
            color = bg,
            shadowElevation = if (selected) 1.sdp else 1.sdp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.sdp, horizontal = 9.sdp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(7.sdp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_crown_price),
                    contentDescription = null,
                    modifier = Modifier.size(24.sdp),
                    tint = crownTint
                )
                Text(
                    text = title,
                    color = fg,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
                Text(
                    text = price,
                    color = fg,
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center,

                )
            }
        }
        if (showOffBadge) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 6.sdp, y = (-6).sdp)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFFF6B9D))
                    .padding(horizontal = 8.sdp, vertical = 3.sdp)
            ) {
                Text(
                    text = stringResource(R.string.pro_off_badge),
                    color = Color.White,
                    fontSize = 9.ssp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun PlanSelector(
    plans: List<ProPlanUi>,
    selectedPlan: ProUpgradePlan,

    onSelect: (ProUpgradePlan) -> Unit,
    modifier: Modifier = Modifier,
) {

        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(7.sdp)
        ) {
            plans.forEach { p ->
                PlanOption(
                    title = stringResource(p.titleRes),
                    price = stringResource(p.priceRes),
                    selected = selectedPlan == p.plan,
                    showOffBadge = p.showOffBadge,
                    onClick = { onSelect(p.plan) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
}

@Composable
private fun BuyNowButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(43.sdp)
            .clip(RoundedCornerShape(14.sdp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFFF9C86),
                        Color(0xFFBB7CF2)
                    )
                )
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.pro_buy_now),
            color = Color.White,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun FooterLinks(
    onOpenTerms: () -> Unit,
    onOpenPrivacy: () -> Unit,
    onRestorePurchase: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val linkStyle = MaterialTheme.typography.bodySmall.copy(
        color = Color(0xFF323233),
        fontSize = 10.ssp
    )
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FooterLink(text = stringResource(R.string.pro_terms), style = linkStyle, onClick = onOpenTerms)
        FooterSeparator(style = linkStyle)
        FooterLink(
            text = stringResource(R.string.pro_privacy),
            style = linkStyle,
            onClick = onOpenPrivacy
        )
        FooterSeparator(style = linkStyle)
        FooterLink(
            text = stringResource(R.string.pro_restore),
            style = linkStyle,
            onClick = onRestorePurchase
        )
    }
}

@Composable
private fun FooterLink(
    text: String,
    style: androidx.compose.ui.text.TextStyle,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = style,
        modifier = modifier.clickable(onClick = onClick)
    )
}

@Composable
private fun FooterSeparator(
    style: androidx.compose.ui.text.TextStyle,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(R.string.pro_footer_separator),
        style = style,
        modifier = modifier.padding(horizontal = 5.sdp)
    )
}
