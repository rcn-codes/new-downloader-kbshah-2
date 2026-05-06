package com.example.newdownloader26.domain.model

data class DownloadRequest(
    val url: String
)

data class DownloadResult(
    val success: Boolean,
    val message: String,
    val savedPath: String?
)

enum class DownloadPhase {
    PREPARING,
    DOWNLOADING,
    SAVING,
    COMPLETED,
    FAILED
}

data class DownloadProgress(
    val phase: DownloadPhase,
    val downloadedBytes: Long = 0L,
    val totalBytes: Long = -1L,
    val message: String = ""
)

