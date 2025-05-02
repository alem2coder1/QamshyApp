package kz.qamshy.app.viewmodels

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kz.qamshy.app.models.CityModel

class RepositoryCityModel(private val dataStore: DataStore<Preferences>) {

    private val CITY_MODEL_KEY = stringPreferencesKey("cached_city_model")

    private val _cityModel = MutableStateFlow(
        CityModel(0, "", 0.0, 0.0, 0.0, emptyList())
    )
    val cityModel: StateFlow<CityModel> = _cityModel

    suspend fun saveCity(city: CityModel) {
        val json = Json.encodeToString(city)
        dataStore.edit { preferences ->
            preferences[CITY_MODEL_KEY] = json
            _cityModel.value = city
        }
    }


    suspend fun loadCity(): CityModel? {
        val cityJson = dataStore.data.map { preferences ->
            preferences[CITY_MODEL_KEY]
        }.firstOrNull()
        return cityJson?.let { json ->
            Json.decodeFromString<CityModel>(json).also { city ->
                _cityModel.value = city
            }
        }
    }

    suspend fun clearCity() {
        dataStore.edit { preferences ->
            preferences.remove(CITY_MODEL_KEY)
        }
        _cityModel.value = CityModel(0, "", 0.0, 0.0, 0.0, emptyList())
    }
}