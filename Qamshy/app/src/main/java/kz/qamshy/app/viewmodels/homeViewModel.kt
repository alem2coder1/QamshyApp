package kz.qamshy.app.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kz.qamshy.app.models.OrderUiState
import kz.qamshy.app.common.JsonHelper
import kz.qamshy.app.common.SearchHistoryManager
import kz.qamshy.app.koinmodule.data.ArticleRepository
import kz.qamshy.app.models.site.IndexViewModel
import kz.sira.app.viewmodels.QarBaseViewModel

class HomeViewModel(
    private val articleRepository: ArticleRepository
) : QarBaseViewModel() {
    private val _articleUiState = MutableStateFlow<OrderUiState<IndexViewModel>>(OrderUiState.Loading)
    val articleUiState: StateFlow<OrderUiState<IndexViewModel>> = _articleUiState.asStateFlow()

    var isLoadingMore = false
        private set
    private var currentPageOffsetAll = 0
    var hasMore by mutableStateOf(true)
    private val PAGE_SIZE = 10
    private val _totalCount1 = MutableStateFlow(0)
    val totalCount1: StateFlow<Int> = _totalCount1

    init {
        loadIndex()
    }

    fun canLoadMore(): Boolean = !isLoadingMore && hasMore

    fun refreshData(isEnabled: Boolean = false) {
        if (_isRefreshing.value) return
        _isRefreshing.value = true
        hasMore = true
        currentPageOffsetAll = 0
        _articleUiState.value = OrderUiState.Success(IndexViewModel())
        loadIndex(forceRefresh = true) {
            _isRefreshing.value = false
        }
    }

    fun loadIndex(forceRefresh: Boolean = false, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            _articleUiState.value = OrderUiState.Loading
            articleRepository.getIndex(forceRefresh).fold(
                onSuccess = { indexViewModel ->
                    hasMore = (indexViewModel.pinnedArticleList.size >= PAGE_SIZE)
                    _articleUiState.value = OrderUiState.Success(indexViewModel)
                    onComplete()
                },
                onFailure = { exception ->
                    articleRepository.getIndexFromCache().fold(
                        onSuccess = { cachedData ->
                            if (cachedData.pinnedArticleList.isNotEmpty() ||
                                cachedData.focusArticleList.isNotEmpty() ||
                                cachedData.blockList.isNotEmpty()) {

                                hasMore = (cachedData.pinnedArticleList.size >= PAGE_SIZE)
                                _articleUiState.value = OrderUiState.Success(cachedData)
                            } else {
                                _articleUiState.value = OrderUiState.Error("No cached data available")
                            }
                            onComplete()
                        },
                        onFailure = {
                            _articleUiState.value = OrderUiState.Error("Load failed: ${exception.message}")
                            onComplete()
                        }
                    )
                }
            )
        }
    }


}