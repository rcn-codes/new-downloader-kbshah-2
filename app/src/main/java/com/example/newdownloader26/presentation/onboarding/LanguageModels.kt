package com.example.newdownloader26.presentation.onboarding

import androidx.annotation.DrawableRes

data class LanguageOption(
    val englishName: String,
    val nativeName: String,
    val languageTag: String,
    @DrawableRes val flagRes: Int
)

