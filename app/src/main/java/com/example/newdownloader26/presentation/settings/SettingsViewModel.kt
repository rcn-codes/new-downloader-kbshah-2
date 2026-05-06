package com.example.newdownloader26.presentation.settings

import androidx.lifecycle.ViewModel
import com.example.newdownloader26.data.local.SettingsPreferences
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(
    private val preferences: SettingsPreferences
) : ViewModel() {

    val autoDetectEnabled: StateFlow<Boolean> = preferences.autoDetectLinkFlow

    fun setAutoDetectEnabled(enabled: Boolean) {
        preferences.setAutoDetectLinkEnabled(enabled)
    }
}
