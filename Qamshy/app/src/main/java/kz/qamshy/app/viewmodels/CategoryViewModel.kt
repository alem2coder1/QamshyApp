package kz.qamshy.app.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kz.qamshy.app.common.ApiService
import kz.qamshy.app.common.JsonHelper
import kz.qamshy.app.models.OrderUiState
import kz.qamshy.app.models.site.ArticleListModel
import kz.qamshy.app.models.site.CategoryGroup
import kz.qamshy.app.models.site.CategoryModel
import kz.sira.app.viewmodels.QarBaseViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent.inject

class CategoryViewModel(
    private val apiService: ApiService
):QarBaseViewModel() {

    private val _categoryUiState = MutableStateFlow<OrderUiState<CategoryModel>>(OrderUiState.Loading)
    val categoryUiState: StateFlow<OrderUiState<CategoryModel>> = _categoryUiState.asStateFlow()

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
        lectureData()
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
        _categoryUiState.value = OrderUiState.Success(CategoryModel())
        lectureData {
            _isRefreshing.value = false
        }
        loadDataTea{
            _isRefreshing.value = false
        }
    }

    fun loadMoreData() {
        if (_isRefreshing.value || isLoadingMore) return
        isLoadingMore = true
        currentPageOffsetAll += 1
        lectureData {
            isLoadingMore = false
        }
    }
    fun loadDataTea(onComplete: () -> Unit = {}) {
        _categoryUiState.value = OrderUiState.Loading
        viewModelScope.launch {
            val result = apiService.queryAsync(
                url    = "getcategoryList",
                method = "GET"
            )
            result.fold(
                onSuccess = { ajaxMsg ->
                    if (ajaxMsg.status.equals("success", ignoreCase = true)) {
                        try {
                            val categoryGroups = JsonHelper.convertAnyToObject<List<CategoryGroup>>(ajaxMsg.data)
                            val newModel = if (categoryGroups != null) {
                                CategoryModel(categories = categoryGroups)
                            } else {
                                CategoryModel()
                            }

                            _categoryUiState.value = OrderUiState.Success(newModel)
                        } catch (e: Exception) {
                            _categoryUiState.value = OrderUiState.Error("${e.message}")
                        }
                    } else {
                        _categoryUiState.value = OrderUiState.Success(CategoryModel())
                    }
                    onComplete()
                },
                onFailure = { ex ->
                    _categoryUiState.value = OrderUiState.Error("${ex.message}")
                    onComplete()
                }
            )
        }
    }


    private val _articleUiState = MutableStateFlow<OrderUiState<ArticleListModel>>(OrderUiState.Loading)
    val articleUiState: StateFlow<OrderUiState<ArticleListModel>> = _articleUiState.asStateFlow()
    fun lectureData(onComplete: () -> Unit = {}) {
        if (currentPageOffsetAll == 1) {
            _articleUiState.value = OrderUiState.Loading
        }
        viewModelScope.launch {
            val result = apiService.queryAsync("articlelist?categoryId=${_selectedTeacherId.value}&page=${currentPageOffsetAll}", "GET")
            result.fold(
                onSuccess = { ajaxMsg ->
                    if (ajaxMsg.status.equals("success", ignoreCase = true)) {
                        val response = JsonHelper.convertAnyToObject<ArticleListModel>(ajaxMsg.data)
                        val newOrders = response?.articleList.orEmpty()
                        hasMore = newOrders.size >= PAGE_SIZE

                        val currentList = when (val state = _articleUiState.value) {
                            is OrderUiState.Success -> state.data.articleList
                            else -> emptyList()
                        }

                        val combined = if (currentPageOffsetAll == 1) newOrders
                        else currentList + newOrders

                        _articleUiState.value = OrderUiState.Success(
                            ArticleListModel(
                                keyword = response?.keyword.orEmpty(),
                                articleList = combined
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
    private val _scrollPosition = MutableStateFlow(0 to 0)
    val scrollPosition = _scrollPosition.asStateFlow()

    fun saveScrollPosition(index: Int, offset: Int) {
        _scrollPosition.value = index to offset
    }
}