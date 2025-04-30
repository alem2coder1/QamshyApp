package kz.qamshy.app.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kz.qamshy.app.common.ApiService
import kz.qamshy.app.common.JsonHelper
import kz.qamshy.app.common.SearchHistoryManager
import kz.qamshy.app.models.OrderUiState
import kz.qamshy.app.models.site.ArticleListModel
import kz.qamshy.app.models.site.CategoryGroup
import kz.qamshy.app.models.site.CategoryModel
import kz.qamshy.app.models.site.TagItemModel
import kz.sira.app.viewmodels.QarBaseViewModel

class SearchViewModel(
    private val apiService: ApiService
) : QarBaseViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()
    fun updateSearch(text: String) {
        val trimmedText = text.trim()
        _searchText.value = trimmedText
    }

    private val searchHistoryManager = SearchHistoryManager.getInstance()
    val searchHistory = searchHistoryManager.searchHistory
    fun addSearchQuery(query: String) {
        viewModelScope.launch {
            searchHistoryManager.addSearchQuery(query)
        }
    }
    fun removeSearchQuery(query: String) {
        viewModelScope.launch {
            searchHistoryManager.removeSearchQuery(query)
        }
    }
    fun clearSearchHistory() {
        viewModelScope.launch {
            searchHistoryManager.clearSearchHistory()
        }
    }
    private val _tagList =
        MutableStateFlow<List<TagItemModel>>(emptyList())
    val tagList = _tagList.asStateFlow()
    fun loadDataTag(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            val result = apiService.queryAsync(
                url    = "gettaglist",
                method = "GET"
            )
            result.fold(
                onSuccess = { ajaxMsg ->
                    if (ajaxMsg.status.equals("success", ignoreCase = true)) {
                        try {
                            val categoryGroups = JsonHelper.convertAnyToObject<List<TagItemModel>>(ajaxMsg.data)
                            if (categoryGroups != null) {
                                _tagList.value = categoryGroups
                            } else {
                                _tagList.value = emptyList()
                            }
                        } catch (e: Exception) {
                            _tagList.value = emptyList()
                        }
                    } else {
                        _tagList.value = emptyList()
                    }
                    onComplete()
                },
                onFailure = { ex ->
                    _tagList.value = emptyList()
                    onComplete()
                }
            )
        }
    }


    private var currentPageOffsetAll = 1
    var isLoadingMore = false
        private set
    private val PAGE_SIZE = 10
    var hasMore by mutableStateOf(true)
    fun canLoadMore(): Boolean = !isLoadingMore && hasMore

    private val _selectedTeacherId = MutableStateFlow(0)
    val selectedTeacherId: StateFlow<Int> = _selectedTeacherId.asStateFlow()
    fun toggleTeacherSelection(id: Int) {
        _selectedTeacherId.value = if (_selectedTeacherId.value == id) 0 else id
        performSearch()
    }
    private val _selectedTitle = MutableStateFlow("")
    val selectedTitle: StateFlow<String> = _selectedTitle.asStateFlow()
    fun toggleTitleSelection(title: String) {
        _selectedTitle.value = title
    }

    fun refreshData() {
        if (_isRefreshing.value) return
        _isRefreshing.value = true
        hasMore = true
        currentPageOffsetAll = 1
        performSearch {
            _isRefreshing.value = false
        }
        performSearch{
            _isRefreshing.value = false
        }
    }

    fun loadMoreData() {
        if (_isRefreshing.value || isLoadingMore) return
        isLoadingMore = true
        currentPageOffsetAll += 1
        performSearch {
            isLoadingMore = false
        }
    }
    private val _articleUiState = MutableStateFlow<OrderUiState<ArticleListModel>>(OrderUiState.Loading)
    val articleUiState: StateFlow<OrderUiState<ArticleListModel>> = _articleUiState.asStateFlow()
    fun performSearch(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            val result = apiService.queryAsync("articlelist?keyword=${_searchText.value}", "GET")
            result.fold(
                onSuccess = { ajaxMsg ->
                    if (ajaxMsg.status.equals("success", ignoreCase = true)) {
                        val response = JsonHelper.convertAnyToObject<ArticleListModel>(ajaxMsg.data)
                        val newOrders = response?.articleList.orEmpty()
                        _articleUiState.value = OrderUiState.Success(
                            ArticleListModel(
                                keyword = response?.keyword.orEmpty(),
                                articleList = newOrders
                            )
                        )
                    } else {
                        _articleUiState.value = OrderUiState.Success(ArticleListModel())
                    }
                    onComplete()
                },
                onFailure = {
                    _articleUiState.value = OrderUiState.Error("")
                    onComplete()
                }
            )
        }
    }
}