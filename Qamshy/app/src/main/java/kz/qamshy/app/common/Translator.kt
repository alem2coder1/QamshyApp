package kz.qamshy.app.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.qamshy.app.models.LanguageModel
import kz.qamshy.app.ui.QamshyApp
import java.util.concurrent.ConcurrentHashMap

object Translator {

    private var langDictionary: MutableMap<String, String>? = null

    fun loadLanguagePack(language: LanguageModel, callback: (Boolean) -> Unit) {
        val currentLanguage = QamshyApp.currentLanguage.value
        if (language.languageCulture != currentLanguage.languageCulture) {
            langDictionary = null
        }
        if (langDictionary != null) {
            callback(true)
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            val jsonLanguagePack = withContext(Dispatchers.IO) {
                LanguagePackHelper.getLanguagePackJsonString(language.languageCulture, QamshyApp.appContext)
            }

            if (jsonLanguagePack.isNotEmpty()) {
                try {
                    langDictionary = JsonHelper.deserializeObject<ConcurrentHashMap<String, String>>(jsonLanguagePack)
                } catch (e: Exception) {
                    e.printStackTrace()
                    callback(false)
                    return@launch
                }
            }

            callback(true)
        }
    }

    fun T(localString: String, language: LanguageModel? = null): String {
        language?.let {
            loadLanguagePack(it) { }
        }

        return langDictionary?.get(localString) ?: localString
    }
}