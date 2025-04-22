package kz.qamshy.app.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kz.qamshy.app.common.APIHelper
import kz.qamshy.app.models.OrderUiState
import kz.qamshy.app.models.site.ArticleModel
import kz.qamshy.app.common.JsonHelper
import kz.qamshy.app.models.site.IndexViewModel
import kz.sira.app.viewmodels.QarBaseViewModel

class HomeViewModel: QarBaseViewModel() {
    private val _articleUiState = MutableStateFlow<OrderUiState<IndexViewModel>>(OrderUiState.Loading)
    val articleUiState: StateFlow<OrderUiState<IndexViewModel>> = _articleUiState.asStateFlow()
    var isLoadingMore = false
        private set
    private var currentPageOffsetAll = 0
    var hasMore by mutableStateOf(true)
    fun canLoadMore(): Boolean = !isLoadingMore && hasMore
    fun refreshData(isEnabled: Boolean = false) {
        if (_isRefreshing.value) return
        _isRefreshing.value = true
        hasMore = true
        currentPageOffsetAll = 0
        _articleUiState.value = OrderUiState.Success(IndexViewModel())
        loadIndex {
            _isRefreshing.value = false
        }
    }
    private val PAGE_SIZE = 10
    private val _totalCount1 = MutableStateFlow(0)
    val totalCount1: StateFlow<Int> = _totalCount1
    fun loadIndex(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            val result = APIHelper.queryAsync("index", "GET")
            result.fold(
                onSuccess = { ajaxMsg ->
                    if (ajaxMsg.status.equals("success", ignoreCase = true)) {
                        val response = JsonHelper.convertAnyToObject<IndexViewModel>(ajaxMsg.data)
                        val newOrders = response?: IndexViewModel()
                        hasMore = (newOrders.pinnedArticleList.size >= PAGE_SIZE)
                        val currentOrders = when (val state = _articleUiState.value) {
                            is OrderUiState.Success -> state.data
                            else -> IndexViewModel()
                        }
                        _articleUiState.value = OrderUiState.Success(newOrders)
                    } else {
                        _articleUiState.value = OrderUiState.Error("Load failed: Incorrect state")
                    }
                    onComplete()
                },
                onFailure = {
                    _articleUiState.value = OrderUiState.Error("Load failed: Incorrect state")
                    onComplete()
                }
            )
        }
    }

}