package com.example.newdownloader26.presentation.downloader

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.newdownloader26.R
import com.example.newdownloader26.data.local.AutoPasteManager
import com.example.newdownloader26.data.local.SettingsPreferences
import com.example.newdownloader26.domain.model.DownloadPhase
import com.example.newdownloader26.domain.model.DownloadProgress
import com.example.newdownloader26.domain.model.DownloadRequest
import com.example.newdownloader26.domain.usecase.DownloadVideoUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DownloaderViewModel(
    application: Application,
    private val downloadVideoUseCase: DownloadVideoUseCase,
    private val autoPasteManager: AutoPasteManager,
    private val settingsPreferences: SettingsPreferences
) : AndroidViewModel(application) {

    val autoDetectLinkEnabled: StateFlow<Boolean> = settingsPreferences.autoDetectLinkFlow

    private var lastClipboardProcessed: String? = null

    private val _state = MutableStateFlow(DownloaderState())
    val state: StateFlow<DownloaderState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<DownloaderEffect>()
    val effect: SharedFlow<DownloaderEffect> = _effect.asSharedFlow()

    fun tryAutoPasteFromClipboard(onlyPlatform: DownloadPlatform? = null) {
        if (!autoPasteManager.isAutoPasteEnabled) return
        val raw = autoPasteManager.readPrimaryClipPlainText() ?: return
        if (raw == lastClipboardProcessed) return
        val url = AutoPasteManager.extractFirstHttpUrl(raw)
        if (url == null) {
            lastClipboardProcessed = raw
            return
        }
        if (onlyPlatform != null && !AutoPasteManager.isUrlForPlatform(url, onlyPlatform)) {
            lastClipboardProcessed = raw
            return
        }
        val currentUrl = _state.value.url.trim()
        lastClipboardProcessed = raw
        if (currentUrl == url) return
        _state.update { it.copy(url = url, isError = false, statusMessage = "") }
    }

    fun onIntent(intent: DownloaderIntent) {
        when (intent) {
            is DownloaderIntent.OnUrlChanged -> {
                if (intent.url.isBlank()) lastClipboardProcessed = null
                _state.update { it.copy(url = intent.url, isError = false, statusMessage = "") }
            }
            DownloaderIntent.OnDownloadClicked -> download()
            DownloaderIntent.OnDismissDownloadDialog -> {
                _state.update { it.copy(showDownloadDialog = false) }
            }
        }
    }

    private fun download() {
        val current = _state.value
        if (current.url.isBlank()) {
            _state.update {
                it.copy(
                    isError = true,
                    statusMessage = "",
                    statusMessageRes = R.string.downloader_enter_video_url
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    isError = false,
                    showDownloadDialog = true,
                    downloadPhase = DownloadPhase.PREPARING,
                    progressPercent = null,
                    savedPath = null,
                    statusMessage = "",
                    statusMessageRes = R.string.downloader_preparing_server
                )
            }

            val result = downloadVideoUseCase(DownloadRequest(url = current.url.trim())) { progress ->
                updateProgress(progress)
            }

            result.fold(
                onSuccess = { response ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isError = !response.success,
                            showDownloadDialog = true,
                            downloadPhase = if (response.success) DownloadPhase.COMPLETED else DownloadPhase.FAILED,
                            progressPercent = if (response.success) 100 else it.progressPercent,
                            statusMessage = response.message,
                            statusMessageRes = null,
                            savedPath = response.savedPath
                        )
                    }
                    _effect.emit(DownloaderEffect.ShowToast(response.message))
                },
                onFailure = { throwable ->
                    val fallback = R.string.error_something_went_wrong
                    val message = throwable.message
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isError = true,
                            showDownloadDialog = true,
                            downloadPhase = DownloadPhase.FAILED,
                            statusMessage = message.orEmpty(),
                            statusMessageRes = if (message.isNullOrBlank()) fallback else null
                        )
                    }
                    if (!message.isNullOrBlank()) {
                        _effect.emit(DownloaderEffect.ShowToast(message))
                    }
                }
            )
        }
    }

    private fun updateProgress(progress: DownloadProgress) {
        val percent = if (progress.totalBytes > 0) {
            ((progress.downloadedBytes * 100) / progress.totalBytes).toInt().coerceIn(0, 100)
        } else {
            null
        }
        _state.update {
            it.copy(
                downloadPhase = progress.phase,
                progressPercent = percent,
                statusMessage = progress.message,
                statusMessageRes = null
            )
        }
    }
}
