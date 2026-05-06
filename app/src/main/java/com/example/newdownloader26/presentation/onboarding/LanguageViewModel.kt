package com.example.newdownloader26.presentation.onboarding

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LanguageViewModel(
    initialTag: String
) : ViewModel() {

    private val _selectedTag = MutableStateFlow(initialTag)
    val selectedTag: StateFlow<String> = _selectedTag.asStateFlow()

    fun selectLanguage(tag: String) {
        if (_selectedTag.value != tag) {
            _selectedTag.value = tag
        }
    }
}
