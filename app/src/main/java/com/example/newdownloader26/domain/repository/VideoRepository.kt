package com.example.newdownloader26.domain.repository

import com.example.newdownloader26.domain.model.DownloadRequest
import com.example.newdownloader26.domain.model.DownloadProgress
import com.example.newdownloader26.domain.model.DownloadResult

interface VideoRepository {
    suspend fun downloadVideo(
        request: DownloadRequest,
        onProgress: (DownloadProgress) -> Unit
    ): DownloadResult
}
