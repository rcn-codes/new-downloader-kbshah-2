package com.example.newdownloader26.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.newdownloader26.R
import network.chaintech.sdpcomposemultiplatform.ssp
// Local Urbanist fonts from `res/font`
val Urbanist = FontFamily(
    Font(R.font.urbanist_medium, weight = FontWeight.Normal),
    Font(R.font.urbanist_medium, weight = FontWeight.Medium),
    Font(R.font.urbanist_semi_bold, weight = FontWeight.SemiBold),
    Font(R.font.urbanist_bold, weight = FontWeight.Bold),
)


@Composable
fun appTypography(): Typography = Typography(
    displayLarge = TextStyle(fontFamily = Urbanist, fontWeight = FontWeight.ExtraBold, fontSize = 26.ssp),
    displayMedium = TextStyle(fontFamily = Urbanist, fontWeight = FontWeight.ExtraBold, fontSize = 24.ssp),
    displaySmall = TextStyle(fontFamily = Urbanist, fontWeight = FontWeight.ExtraBold, fontSize = 18.ssp),

    headlineLarge = TextStyle(fontFamily = Urbanist, fontWeight = FontWeight.Bold, fontSize = 20.ssp),
    headlineMedium = TextStyle(fontFamily = Urbanist, fontWeight = FontWeight.Bold, fontSize = 18.ssp),
    headlineSmall = TextStyle(fontFamily = Urbanist, fontWeight = FontWeight.Bold, fontSize = 16.ssp),

    titleLarge = TextStyle(fontFamily = Urbanist, fontWeight = FontWeight.SemiBold, fontSize = 13.ssp),
    titleMedium = TextStyle(fontFamily = Urbanist, fontWeight = FontWeight.SemiBold, fontSize = 11.ssp),
    titleSmall = TextStyle(fontFamily = Urbanist, fontWeight = FontWeight.Bold, fontSize = 11.ssp),

    bodyLarge = TextStyle(fontFamily = Urbanist, fontWeight = FontWeight.Medium, fontSize = 12.ssp),
    bodyMedium = TextStyle(fontFamily = Urbanist, fontWeight = FontWeight.Medium, fontSize = 11.ssp),
    bodySmall = TextStyle(fontFamily = Urbanist, fontWeight = FontWeight.Medium, fontSize = 11.ssp),

    labelLarge = TextStyle(fontFamily = Urbanist, fontWeight = FontWeight.Normal, fontSize = 12.ssp),
    labelMedium = TextStyle(fontFamily = Urbanist, fontWeight = FontWeight.Normal, fontSize = 11.ssp),
    labelSmall = TextStyle(fontFamily = Urbanist, fontWeight = FontWeight.Normal, fontSize = 8.ssp),
)