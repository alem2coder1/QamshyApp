package kz.qamshy.app.viewmodels

import android.app.Application
import android.content.Context
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
import kz.qamshy.app.ui.activities.DescriptionActivity
import kz.qamshy.app.ui.activities.SearchActivity
import kz.sira.app.viewmodels.QarBaseViewModel

class SearchViewModel(
    private val apiService: ApiService
) : QarBaseViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()
    fun updateSearch(text: String,fromQuery:Boolean = false,context:Context) {
        val trimmedText = text.trim()
        _searchText.value = trimmedText
        if(fromQuery){
            navigateToSearchActivity(context = context,searchText = trimmedText)
        }


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

    private val _articleUiState = MutableStateFlow<OrderUiState<ArticleListModel>>(OrderUiState.Loading)
    val articleUiState: StateFlow<OrderUiState<ArticleListModel>> = _articleUiState.asStateFlow()

    private val _isSearch = MutableStateFlow(false)
    val isSearch: StateFlow<Boolean> = _isSearch.asStateFlow()
    fun toggleSearch() {
        _isSearch.value = !_isSearch.value
    }

    fun performSearch(onComplete: () -> Unit = {},tagId :Int =0) {
        val keyWord = if (tagId == 0) _searchText.value else tagId
        val kerWordText = if(tagId == 0) "keyword" else "tagId"
        viewModelScope.launch {
            val result = apiService.queryAsync("articlelist?${kerWordText}=${keyWord}", "GET")
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
                        _isSearch.value = true
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
    fun navigateToDescActivity(context: Context, id:Int) {
        navigateToActivity(
            context = context,
            targetActivity = DescriptionActivity ::class,
            paraDic = mapOf(
                "id" to id
            )
        )
    }
    fun navigateToSearchActivity(context: Context,tagId:Int = 0,
                                 tagTitle:String = "",
                                 searchText:String = ""
                                 ) {
        navigateToActivity(
            context = context,
            targetActivity = SearchActivity ::class,
            paraDic = mapOf(
                "tagId" to tagId,
                "tagTitle" to tagTitle,
                "searchText" to searchText
            )
        )
    }
    fun onBackButtonPressed(context: Context) {
        navigateToBack(context)
    }
}