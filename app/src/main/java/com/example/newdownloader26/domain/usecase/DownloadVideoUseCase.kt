package com.example.newdownloader26.domain.usecase

import com.example.newdownloader26.domain.model.DownloadRequest
import com.example.newdownloader26.domain.model.DownloadProgress
import com.example.newdownloader26.domain.model.DownloadResult
import com.example.newdownloader26.domain.repository.VideoRepository

class DownloadVideoUseCase(
    private val repository: VideoRepository
) {
    suspend operator fun invoke(
        request: DownloadRequest,
        onProgress: (DownloadProgress) -> Unit
    ): Result<DownloadResult> {
        return runCatching { repository.downloadVideo(request, onProgress) }
    }
}
