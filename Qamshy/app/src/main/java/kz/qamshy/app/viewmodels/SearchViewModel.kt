package kz.qamshy.app.viewmodels

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kz.qamshy.app.common.ApiService
import kz.qamshy.app.common.JsonHelper
import kz.qamshy.app.common.SearchHistoryManager
import kz.qamshy.app.models.OrderUiState
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
}