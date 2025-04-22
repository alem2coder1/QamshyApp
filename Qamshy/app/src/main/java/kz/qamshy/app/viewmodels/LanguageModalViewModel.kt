package kz.sira.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.qamshy.app.common.APIHelper
import kz.qamshy.app.common.JsonHelper
import kz.qamshy.app.models.LanguageModel
import kz.qamshy.app.ui.QamshyApp

class LanguageModalViewModel : QarBaseViewModel() {
    private val _languageList = MutableLiveData<List<LanguageModel>>()
    val languageList: LiveData<List<LanguageModel>> get() = _languageList

    init {
        loadData()
    }
//    android:networkSecurityConfig="@xml/network_security_config"
//    android:usesCleartextTraffic="true"
    private fun loadData() {
        viewModelScope.launch {
            val result = APIHelper.queryAsync("language/list", "GET")
            result.fold(
                onSuccess = { ajaxMsg ->
                    if (ajaxMsg.status.equals("success", ignoreCase = true)) {
                        val languages: List<LanguageModel>? = JsonHelper.convertAnyToObject(ajaxMsg.data)
                        _languageList.postValue(languages ?: emptyList())
                        for (lan in languages ?: emptyList()) {
                            if (lan.languageCulture == QamshyApp.currentLanguage.value.languageCulture) {
                                QamshyApp.updateLanguage(lan)
                            }
                        }

                    } else {
                        _languageList.postValue(emptyList())
                    }
                },
                onFailure = { exception ->
                    _languageList.postValue(emptyList())
                }
            )
        }
    }
}
