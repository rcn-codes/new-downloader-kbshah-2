package com.example.newdownloader26.data.local

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsPreferences(context: Context) {
    private val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private val _autoDetectLinkEnabled = MutableStateFlow(prefs.getBoolean(KEY_AUTO_DETECT, true))
    val autoDetectLinkFlow: StateFlow<Boolean> = _autoDetectLinkEnabled.asStateFlow()

    private val _disclaimerAccepted =
        MutableStateFlow(prefs.getBoolean(KEY_DISCLAIMER_ACCEPTED, false))
    val disclaimerAcceptedFlow: StateFlow<Boolean> = _disclaimerAccepted.asStateFlow()

    fun getAutoDetectLinkEnabled(): Boolean = _autoDetectLinkEnabled.value

    fun setAutoDetectLinkEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_AUTO_DETECT, enabled).apply()
        _autoDetectLinkEnabled.value = enabled
    }

    fun isDisclaimerAccepted(): Boolean = _disclaimerAccepted.value

    fun setDisclaimerAccepted(accepted: Boolean) {
        prefs.edit().putBoolean(KEY_DISCLAIMER_ACCEPTED, accepted).apply()
        _disclaimerAccepted.value = accepted
    }

    private companion object {
        const val PREF_NAME = "app_settings"
        const val KEY_AUTO_DETECT = "auto_detect_link"
        const val KEY_DISCLAIMER_ACCEPTED = "disclaimer_accepted"
    }
}
