package kz.qamshy.app.viewmodels

import android.content.Context
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kz.qamshy.app.common.ApiService
import kz.qamshy.app.common.JsonHelper
import kz.qamshy.app.models.OrderUiState
import kz.qamshy.app.models.site.ArticleModel
import kz.qamshy.app.models.site.DescriptionModel
import kz.sira.app.viewmodels.QarBaseViewModel

class DescriptionViewModel (
    private val apiService: ApiService
): QarBaseViewModel() {
    private val _descUiState = MutableStateFlow<OrderUiState<DescriptionModel>>(OrderUiState.Loading)
    val descUiState: StateFlow<OrderUiState<DescriptionModel>> = _descUiState.asStateFlow()
    fun descData(onComplete: () -> Unit = {},id:Int) {
        viewModelScope.launch {
            val result = apiService.queryAsync("articleview?articleId=${id}", "GET")
            result.fold(
                onSuccess = { ajaxMsg ->
                    if (ajaxMsg.status.equals("success", ignoreCase = true)) {
                        val response = JsonHelper.convertAnyToObject<DescriptionModel>(ajaxMsg.data)
                        if (response != null) {
                            _descUiState.value = OrderUiState.Success(response)
                        }else{
                            _descUiState.value = OrderUiState.Success(DescriptionModel())
                        }


                    } else {
                        _descUiState.value = OrderUiState.Success(DescriptionModel())
                    }
                    onComplete()
                },
                onFailure = {
                    _descUiState.value = OrderUiState.Error("")
                    onComplete()
                }
            )
        }
    }
    fun onBackButtonPressed(context: Context) {
        navigateToBack(context)
    }
}