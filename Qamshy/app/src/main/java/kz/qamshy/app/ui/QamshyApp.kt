package kz.qamshy.app.ui

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kz.qamshy.app.common.JsonHelper
import kz.qamshy.app.models.LanguageModel
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import kz.qamshy.app.common.SearchHistoryManager
import kz.qamshy.app.koinmodule.appModule
import kz.qamshy.app.koinmodule.databaseModule
import kz.qamshy.app.koinmodule.networkModule
import kz.qamshy.app.koinmodule.repositoryModule
import kz.qamshy.app.koinmodule.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import java.util.Locale

class QamshyApp : Application() {
    private val dataStore by preferencesDataStore("city_cache_store")
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")
    companion object {
        const val PREFS_NAME = "QamshyAppPrefs"
        private const val FIRST_LAUNCH_KEY = "IsFirstLaunch"
        private const val CURRENT_LANGUAGE_KEY = "CurrentLanguage"
        private const val QAR_TOKEN_KEY = "QarToken"

        private lateinit var prefs: SharedPreferences
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")
        val defaultLanguage: LanguageModel by lazy {
            val locale = Locale.getDefault()
            when (locale.language) {
                "kk" -> LanguageModel(
                    flagUrl = "https://sira.kz/images/flag/kz.svg",
                    fullName = "Қазақша",
                    shortName = "Қаз",
                    languageCulture = "kz",
                    isDefault = true,
                    uniqueSeoCode = "kk"
                )
                "ru" -> LanguageModel(
                    flagUrl = "https://sira.kz/images/flag/ru.svg",
                    fullName = "Русский",
                    shortName = "Рус",
                    languageCulture = "ru",
                    isDefault = true,
                    uniqueSeoCode = "ru"
                )
//                "zh" -> LanguageModel(
//                    flagUrl = "https://sira.kz/images/flag/cn.svg",
//                    fullName = "简体中文",
//                    shortName = "中",
//                    languageCulture = "zh-CN",
//                    isDefault = true,
//                    uniqueSeoCode = "zh"
//                )
                "en" -> LanguageModel(
                    flagUrl = "https://sira.kz/images/flag/en.svg",
                    fullName = "English",
                    shortName = "En",
                    languageCulture = "en",
                    isDefault = true,
                    uniqueSeoCode = "en"
                )
                else -> LanguageModel(
                    flagUrl = "https://sira.kz/images/flag/en.svg",
                    fullName = "English",
                    shortName = "En",
                    languageCulture = "en",
                    isDefault = true,
                    uniqueSeoCode = "en"
                )
            }
        }
        //        打开app时候把notification等permission都要问
        private val _currentLanguage = MutableStateFlow(defaultLanguage)
        val currentLanguage: StateFlow<LanguageModel> = _currentLanguage.asStateFlow()

        var currentToken: String = ""
            private set
        var currentAndroidToken: String = ""
            private set

        lateinit var appContext: Context
            private set

        lateinit var siteUrl: String
            private set

        var isFirstLaunch: Boolean = false
            private set

        private val _themeType = MutableStateFlow(false)
        val themeType: StateFlow<Boolean> = _themeType.asStateFlow()
        fun updateThemeType(value: String) {
            if(value == "light"){
                _themeType.value = false
            }else{
                _themeType.value = true
            }
        }
        fun updateLanguage(value: LanguageModel) {
            if (_currentLanguage.value != value) {
                _currentLanguage.value = value
                putCurrentLanguagePreference(JsonHelper.serializeObject(value))
                if(_currentLanguage.value.languageCulture == "tote"){
                    updateIsRtl(true)
                }
            }
        }
        fun setCurrentAndroidToken(token: String) {
            currentAndroidToken = token
            prefs.edit().putString("FCM_TOKEN", token).apply()
        }

        private fun putCurrentLanguagePreference(value: String?) {
            prefs.edit().putString(CURRENT_LANGUAGE_KEY, value).apply()
        }

        private inline fun <reified T> getPreference(key: String, defaultValue: T? = null): T? {
            return when (T::class) {
                String::class -> prefs.getString(key, defaultValue as? String) as T?
                Boolean::class -> prefs.getBoolean(key, defaultValue as? Boolean ?: false) as T?
                Int::class -> prefs.getInt(key, defaultValue as? Int ?: -1) as T?
                Long::class -> prefs.getLong(key, defaultValue as? Long ?: -1L) as T?
                Float::class -> prefs.getFloat(key, defaultValue as? Float ?: -1f) as T?
                else -> null
            }
        }

        private val _isRtl = MutableStateFlow(false)
        val isRtl = _isRtl.asStateFlow()
       fun updateIsRtl(isTote:Boolean){
           if(isTote){
               _isRtl.value = true
           }

       }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        siteUrl = "https://qamshy.kz/api/query/"
        initializeLanguage()
        isFirstLaunch = getPreference(FIRST_LAUNCH_KEY, true) ?: true
        if (isFirstLaunch) {
            prefs.edit().putBoolean(FIRST_LAUNCH_KEY, false).apply()
        }
        startKoin{
            androidContext(this@QamshyApp)
            modules(
                appModule,
                viewModelModule,
                networkModule,
                databaseModule,
                repositoryModule

            )
        }
        SearchHistoryManager.getInstance().initialize(this)
        FirebaseApp.initializeApp(this)

//        retrieveFirebaseToken()

    }

    private fun initializeLanguage() {
        val currentLanguageJsonStr = getPreference<String>(CURRENT_LANGUAGE_KEY)
        _currentLanguage.value = if (!currentLanguageJsonStr.isNullOrEmpty()) {
            JsonHelper.deserializeObject<LanguageModel?>(currentLanguageJsonStr) ?: defaultLanguage
        } else {
            defaultLanguage
        }
    }

}

//fun retrieveFirebaseToken() {
//    FirebaseMessaging.getInstance().token
//        .addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val token = task.result
//                QamshyApp.setCurrentAndroidToken(token)
//            } else {
//            }
//        }
//}