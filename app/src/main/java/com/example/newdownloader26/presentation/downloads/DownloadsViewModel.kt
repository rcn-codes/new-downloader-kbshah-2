package com.example.newdownloader26.presentation.downloads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newdownloader26.data.local.DeviceVideoStore
import com.example.newdownloader26.domain.model.DownloadedVideo
import com.example.newdownloader26.domain.model.SourcePlatform
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DownloadsViewModel(
    private val deviceVideoStore: DeviceVideoStore
) : ViewModel() {

    private val _state = MutableStateFlow(DownloadsState())
    val state: StateFlow<DownloadsState> = _state.asStateFlow()

    fun onIntent(intent: DownloadsIntent) {
        when (intent) {
            DownloadsIntent.OnLoad -> load()
            is DownloadsIntent.OnFilterSelected -> {
                _state.update {
                    val filtered = filterItems(it.allItems, intent.platform)
                    it.copy(selectedFilter = intent.platform, visibleItems = filtered)
                }
            }
            is DownloadsIntent.OnDeleteClicked -> Unit
            is DownloadsIntent.OnDeleteConfirmed -> deleteVideo(intent.item)
            DownloadsIntent.OnDeletedDialogDismissed -> _state.update { it.copy(showDeletedSuccess = false) }
        }
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val items = withContext(Dispatchers.IO) {
                deviceVideoStore.listVideos()
            }
            val filter = _state.value.selectedFilter
            _state.update {
                it.copy(
                    isLoading = false,
                    allItems = items,
                    visibleItems = filterItems(items, filter)
                )
            }
        }
    }

    private fun deleteVideo(item: DownloadedVideo) {
        val uri = item.playUri ?: return
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                deviceVideoStore.deleteByUri(uri)
            }
            _state.update { it.copy(showDeletedSuccess = true) }
            load()
        }
    }

    private fun filterItems(
        items: List<DownloadedVideo>,
        selected: SourcePlatform
    ): List<DownloadedVideo> {
        return when (selected) {
            SourcePlatform.ALL -> items
            else -> items.filter { it.sourcePlatform == selected }
        }
    }
}
