package kz.qamshy.app.common

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kz.qamshy.app.ui.QamshyApp.Companion.dataStore

class SearchHistoryManager private constructor() {
    private lateinit var appContext: Context
    private lateinit var searchHistoryStore: DataStore<Preferences>


    private val MAX_HISTORY_ITEMS = 4

    private val MIN_QUERY_LENGTH = 3

    private val SEARCH_HISTORY_KEY = stringPreferencesKey("search_history")

    val searchHistory: Flow<List<String>> by lazy {
        searchHistoryStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val historyJson = preferences[SEARCH_HISTORY_KEY] ?: "[]"
                try {
                    Gson().fromJson(historyJson, object : TypeToken<List<String>>() {}.type)
                } catch (e: Exception) {
                    emptyList()
                }
            }
    }
    fun initialize(context: Context) {
        if (!::appContext.isInitialized) {
            appContext = context.applicationContext
            searchHistoryStore = appContext.dataStore
        }
    }

    suspend fun addSearchQuery(query: String) {
        if (query.length <= MIN_QUERY_LENGTH) return

        searchHistoryStore.edit { preferences ->
            val historyJson = preferences[SEARCH_HISTORY_KEY] ?: "[]"
            val history = try {
                Gson().fromJson<List<String>>(historyJson, object : TypeToken<List<String>>() {}.type).toMutableList()
            } catch (e: Exception) {
                mutableListOf()
            }

            history.remove(query)

            history.add(0, query)

            while (history.size > MAX_HISTORY_ITEMS) {
                history.removeAt(history.size - 1)
            }

            preferences[SEARCH_HISTORY_KEY] = Gson().toJson(history)
        }
    }

    suspend fun removeSearchQuery(query: String) {
        searchHistoryStore.edit { preferences ->
            val historyJson = preferences[SEARCH_HISTORY_KEY] ?: "[]"
            val history = try {
                Gson().fromJson<List<String>>(historyJson, object : TypeToken<List<String>>() {}.type).toMutableList()
            } catch (e: Exception) {
                mutableListOf()
            }

            history.remove(query)

            preferences[SEARCH_HISTORY_KEY] = Gson().toJson(history)
        }
    }

    suspend fun clearSearchHistory() {
        searchHistoryStore.edit { preferences ->
            preferences[SEARCH_HISTORY_KEY] = "[]"
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SearchHistoryManager? = null

        fun getInstance(): SearchHistoryManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SearchHistoryManager().also { INSTANCE = it }
            }
        }
    }
}