package kz.qamshy.app.viewmodels

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kz.qamshy.app.common.ApiService
import kz.qamshy.app.common.JsonHelper
import kz.qamshy.app.models.OrderUiState
import kz.qamshy.app.models.site.ArticleModel
import kz.qamshy.app.models.site.CurrencyModel
import kz.sira.app.viewmodels.QarBaseViewModel

class CurrencyViewModel(
    private val apiService: ApiService
):QarBaseViewModel() {
    private val _currencyList =
        MutableStateFlow<List<CurrencyModel>>(emptyList())
    val currencyList: StateFlow<List<CurrencyModel>> = _currencyList.asStateFlow()
    fun lectureData(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            val result = apiService.queryAsync("getcurrencylist", "GET")
            result.fold(
                onSuccess = { ajaxMsg ->
                    if (ajaxMsg.status.equals("success", ignoreCase = true)) {
                        val response = JsonHelper.convertAnyToObject<List<CurrencyModel>>(ajaxMsg.data)
                        if (response != null) {
                            _currencyList.value = response
                        }else{
                            _currencyList.value = emptyList()
                        }


                    } else {
                        _currencyList.value = emptyList()
                    }
                    onComplete()
                },
                onFailure = {
                    _currencyList.value = emptyList()
                    onComplete()
                }
            )
        }
    }

}