package com.example.newdownloader26.presentation.downloader

import com.example.newdownloader26.domain.model.DownloadPhase

sealed interface DownloaderIntent {
    data class OnUrlChanged(val url: String) : DownloaderIntent
    data object OnDownloadClicked : DownloaderIntent
    data object OnDismissDownloadDialog : DownloaderIntent
    data object OnDismissInvalidLinkDialog : DownloaderIntent
}

data class DownloaderState(
    val url: String = "",
    val isLoading: Boolean = false,
    val showDownloadDialog: Boolean = false,
    val showInvalidLinkDialog: Boolean = false,
    val downloadPhase: DownloadPhase? = null,
    val progressPercent: Int? = null,
    val savedPath: String? = null,
    val statusMessage: String = "",
    val statusMessageRes: Int? = null,
    val isError: Boolean = false
)

sealed interface DownloaderEffect {
    data class ShowToast(val message: String) : DownloaderEffect
}
