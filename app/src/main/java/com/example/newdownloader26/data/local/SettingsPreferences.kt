package com.example.newdownloader26.data.local

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsPreferences(context: Context) {
    private val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private val _autoDetectLinkEnabled = MutableStateFlow(prefs.getBoolean(KEY_AUTO_DETECT, true))
    val autoDetectLinkFlow: StateFlow<Boolean> = _autoDetectLinkEnabled.asStateFlow()

    fun getAutoDetectLinkEnabled(): Boolean = _autoDetectLinkEnabled.value

    fun setAutoDetectLinkEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_AUTO_DETECT, enabled).apply()
        _autoDetectLinkEnabled.value = enabled
    }

    private companion object {
        const val PREF_NAME = "app_settings"
        const val KEY_AUTO_DETECT = "auto_detect_link"
    }
}
