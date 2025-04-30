package kz.qamshy.app.koinmodule

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kz.qamshy.app.common.ApiService
import kz.qamshy.app.common.ApiServiceImpl
import kz.qamshy.app.koinmodule.data.AppDatabase
import kz.qamshy.app.koinmodule.data.ArticleRepository
import kz.qamshy.app.koinmodule.data.ArticleRepositoryImpl
import kz.qamshy.app.ui.QamshyApp
import kz.qamshy.app.ui.QamshyApp.Companion.dataStore
import kz.qamshy.app.viewmodels.CategoryViewModel
import kz.qamshy.app.viewmodels.CurrencyViewModel
import kz.qamshy.app.viewmodels.DescriptionViewModel
import kz.qamshy.app.viewmodels.HomeViewModel
import kz.qamshy.app.viewmodels.NewsViewModel
import kz.qamshy.app.viewmodels.NotificationViewModel
import kz.qamshy.app.viewmodels.SearchViewModel
import kz.sira.app.viewmodels.LanguageModalViewModel
import kz.sira.app.viewmodels.QarBaseViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val appModule = module {
    single {
        get<Context>().getSharedPreferences(QamshyApp.PREFS_NAME, Context.MODE_PRIVATE)
    }

    single<DataStore<Preferences>> {
        get<Context>().dataStore
    }
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

val repositoryModule = module {
    single<ArticleRepository> {
        ArticleRepositoryImpl(
            apiService = get(),
            context = androidContext(),
            dataStore = get()
        )
    }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { LanguageModalViewModel(get()) }
    viewModel { NotificationViewModel(get()) }
    viewModel { QarBaseViewModel() }
    viewModel { CategoryViewModel(get()) }
    viewModel{ SearchViewModel(get()) }
    viewModel{NewsViewModel(get())}
    viewModel{ CurrencyViewModel(get()) }
    viewModel{ DescriptionViewModel(get()) }
}