package kz.qamshy.app.koinmodule.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kz.qamshy.app.common.ApiService
import kz.qamshy.app.common.JsonHelper
import kz.qamshy.app.models.site.IndexViewModel
import java.io.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first

interface ArticleRepository {
    suspend fun getIndex(forceRefresh: Boolean = false): Result<IndexViewModel>
    suspend fun getIndexFromCache(): Result<IndexViewModel>
    fun getIndexAsFlow(): Flow<Result<IndexViewModel>>
}

class ArticleRepositoryImpl(
    private val apiService: ApiService,
    private val context: Context,
    private val dataStore: DataStore<Preferences>
) : ArticleRepository {

    companion object {
        private const val INDEX_DATA_KEY = "index_data_cache"
        private val indexDataKey = stringPreferencesKey(INDEX_DATA_KEY)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }

    override suspend fun getIndex(forceRefresh: Boolean): Result<IndexViewModel> {
        return if (isNetworkAvailable() && forceRefresh) {
            fetchIndexFromNetwork().also { result ->
                result.onSuccess { data ->
                    saveIndexToCache(data)
                }
            }
        } else if (isNetworkAvailable()) {
            getIndexFromCache().fold(
                onSuccess = { Result.success(it) },
                onFailure = {
                    fetchIndexFromNetwork().also { result ->
                        result.onSuccess { data ->
                            saveIndexToCache(data)
                        }
                    }
                }
            )
        } else {
            getIndexFromCache()
        }
    }

    override suspend fun getIndexFromCache(): Result<IndexViewModel> {
        return try {
            val cachedDataJson = dataStore.data.first()[indexDataKey] ?: ""
            if (cachedDataJson.isNotEmpty()) {
                val cachedData = JsonHelper.deserializeObject<IndexViewModel>(cachedDataJson)
                if (cachedData != null) {
                    Result.success(cachedData)
                } else {
                    Result.failure(IOException("Failed to parse cached data"))
                }
            } else {
                Result.failure(IOException("No cached data found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun saveIndexToCache(data: IndexViewModel) {
        try {
            val dataJson = JsonHelper.serializeObject(data)
            dataStore.edit { preferences ->
                preferences[indexDataKey] = dataJson
            }
        } catch (e: Exception) {
            Log.e("ArticleRepository", "Failed to save data to cache", e)
        }
    }

    private suspend fun fetchIndexFromNetwork(): Result<IndexViewModel> {
        return try {
            val result = apiService.queryAsync("index", "GET")
            result.fold(
                onSuccess = { ajaxMsg ->
                    if (ajaxMsg.status.equals("success", ignoreCase = true)) {
                        val response = JsonHelper.convertAnyToObject<IndexViewModel>(ajaxMsg.data)
                        Result.success(response ?: IndexViewModel())
                    } else {
                        Result.failure(IOException("API error: ${ajaxMsg.message}"))
                    }
                },
                onFailure = {
                    Result.failure(it)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getIndexAsFlow(): Flow<Result<IndexViewModel>> = flow {
        emit(Result.success(IndexViewModel()))

        val cachedResult = getIndexFromCache()
        cachedResult.onSuccess { emit(Result.success(it)) }

        if (isNetworkAvailable()) {
            emit(fetchIndexFromNetwork())
        }
    }
}