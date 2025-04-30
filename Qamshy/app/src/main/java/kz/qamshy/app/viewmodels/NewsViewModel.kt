package kz.qamshy.app.viewmodels

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
import kz.qamshy.app.models.OrderUiState
import kz.qamshy.app.models.site.ArticleListModel
import kz.qamshy.app.models.site.ArticleModel
import kz.qamshy.app.models.site.CategoryModel
import kz.qamshy.app.ui.activities.DescriptionActivity
import kz.sira.app.viewmodels.QarBaseViewModel

class NewsViewModel(private val apiService: ApiService):QarBaseViewModel() {
    private val _articleUiState = MutableStateFlow<OrderUiState<List<ArticleModel>>>(OrderUiState.Loading)
    val articleUiState: StateFlow<OrderUiState<List<ArticleModel>>> = _articleUiState.asStateFlow()

    private val _selectedTeacherId = MutableStateFlow(0)
    val selectedTeacherId: StateFlow<Int> = _selectedTeacherId.asStateFlow()
    fun toggleTeacherSelection(id: Int) {
        _selectedTeacherId.value = if (_selectedTeacherId.value == id) 0 else id
        lectureData()
    }

    fun lectureData(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            val result = apiService.queryAsync("GetLatestArticleList?isTop=${_isTop.value}", "GET")
            result.fold(
                onSuccess = { ajaxMsg ->
                    if (ajaxMsg.status.equals("success", ignoreCase = true)) {
                        val response = JsonHelper.convertAnyToObject<List<ArticleModel>>(ajaxMsg.data)
                        if (response != null) {
                            _articleUiState.value = OrderUiState.Success(response)
                        }else{
                            _articleUiState.value = OrderUiState.Success(emptyList())
                        }


                    } else {
                        _articleUiState.value = OrderUiState.Success(emptyList())
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

    private val _isTop = MutableStateFlow(false)
    val isTop: StateFlow<Boolean> = _isTop.asStateFlow()
    fun updateIsTop(isTop:Boolean){
        _isTop.value = isTop
        lectureData()
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

}