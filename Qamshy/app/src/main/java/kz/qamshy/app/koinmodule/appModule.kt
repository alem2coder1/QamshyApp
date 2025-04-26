package kz.qamshy.app.koinmodule

import android.content.Context
import kz.qamshy.app.common.ApiService
import kz.qamshy.app.common.ApiServiceImpl
import kz.qamshy.app.koinmodule.data.AppDatabase
import kz.qamshy.app.koinmodule.data.ArticleRepository
import kz.qamshy.app.ui.QamshyApp
import kz.qamshy.app.ui.QamshyApp.Companion.dataStore
import kz.qamshy.app.viewmodels.HomeViewModel
import kz.qamshy.app.viewmodels.NotificationViewModel
import kz.sira.app.viewmodels.LanguageModalViewModel
import kz.sira.app.viewmodels.QarBaseViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val appModule = module {
    // 提供SharedPreferences
    single {
        get<Context>().getSharedPreferences(QamshyApp.PREFS_NAME, Context.MODE_PRIVATE)
    }

    // 提供DataStore
    single {
        get<Context>().dataStore
    }

    // 其他通用依赖
}
val networkModule = module {
    single { QamshyApp.siteUrl }


    single<ApiService> {
        ApiServiceImpl(
            baseUrl = get(),
            defaultLanguageProvider = { QamshyApp.currentLanguage.value.languageCulture }
        )
    }

    single {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
}

val databaseModule = module {
    single { AppDatabase.getInstance(get()) }
    single { get<AppDatabase>().articleDao() }
    single { get<AppDatabase>().tagDao() }
    single { get<AppDatabase>().articleTagCrossRefDao() }
    single { get<AppDatabase>().indexDao() }
}

//val repositoryModule = module {
//    single { ArticleRepository(get(), get(), get()) }
//}
val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { LanguageModalViewModel(get()) }
    viewModel{NotificationViewModel(get())}
    viewModel { params -> QarBaseViewModel() }
}