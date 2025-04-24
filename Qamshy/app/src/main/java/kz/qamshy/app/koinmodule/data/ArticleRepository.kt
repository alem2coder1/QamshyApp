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
    private val dataStore: DataStore<Preferences> // 使用DataStore来存储数据
) : ArticleRepository {

    companion object {
        private const val INDEX_DATA_KEY = "index_data_cache"
        private val indexDataKey = stringPreferencesKey(INDEX_DATA_KEY)
    }

    // 检查网络是否可用
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }

    // 获取首页数据，考虑缓存
    override suspend fun getIndex(forceRefresh: Boolean): Result<IndexViewModel> {
        return if (isNetworkAvailable() && forceRefresh) {
            // 强制刷新或有网络时从网络获取
            fetchIndexFromNetwork().also { result ->
                result.onSuccess { data ->
                    saveIndexToCache(data)
                }
            }
        } else if (isNetworkAvailable()) {
            // 有网络但不是强制刷新，先尝试从缓存获取，如果没有再从网络获取
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
            // 无网络时从缓存获取
            getIndexFromCache()
        }
    }

    // 从缓存获取首页数据
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

    // 保存首页数据到缓存
    // 更正后的保存首页数据到缓存方法
    private suspend fun saveIndexToCache(data: IndexViewModel) {
        try {
            val dataJson = JsonHelper.serializeObject(data)
            dataStore.edit { preferences ->
                preferences[indexDataKey] = dataJson
            }
        } catch (e: Exception) {
            // 记录错误但不抛出，缓存失败不应影响主流程
            Log.e("ArticleRepository", "Failed to save data to cache", e)
        }
    }

    // 从网络获取首页数据
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

    // 提供Flow版本的数据获取
    override fun getIndexAsFlow(): Flow<Result<IndexViewModel>> = flow {
        emit(Result.success(IndexViewModel())) // 先发送空数据，避免UI等待

        // 尝试从缓存获取
        val cachedResult = getIndexFromCache()
        cachedResult.onSuccess { emit(Result.success(it)) }

        // 然后从网络获取最新数据
        if (isNetworkAvailable()) {
            emit(fetchIndexFromNetwork())
        }
    }
}