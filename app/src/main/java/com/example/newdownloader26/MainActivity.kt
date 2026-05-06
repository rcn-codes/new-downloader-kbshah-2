package com.example.newdownloader26

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.newdownloader26.core.localization.AppLocale
import com.example.newdownloader26.presentation.navigation.AppNavigation
import com.example.newdownloader26.ui.theme.Newdownloader26Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppLocale.getSavedLanguageTag(this)?.let { AppLocale.applyLanguageTag(this, it) }
        enableEdgeToEdge()
        setContent {
            Newdownloader26Theme {
                AppNavigation()
            }
        }
    }
}