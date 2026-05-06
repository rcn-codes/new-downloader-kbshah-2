package com.example.newdownloader26.presentation.downloads

import com.example.newdownloader26.domain.model.DownloadedVideo
import com.example.newdownloader26.domain.model.SourcePlatform

sealed interface DownloadsIntent {
    data object OnLoad : DownloadsIntent
    data class OnFilterSelected(val platform: SourcePlatform) : DownloadsIntent
    data class OnDeleteClicked(val item: DownloadedVideo) : DownloadsIntent
}

data class DownloadsState(
    val isLoading: Boolean = false,
    val selectedFilter: SourcePlatform = SourcePlatform.ALL,
    val allItems: List<DownloadedVideo> = emptyList(),
    val visibleItems: List<DownloadedVideo> = emptyList()
)
