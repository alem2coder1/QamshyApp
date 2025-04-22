package kz.qamshy.app.common

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


data class UserLocation(var longitude: Double = 0.0, var latitude: Double = 0.0)

object LocationManager {
    private val _userLocation = MutableStateFlow(UserLocation())
    val userLocation = _userLocation.asStateFlow()
    fun updateLocation(longitude: Double, latitude: Double,context: Context) {
        _userLocation.value = UserLocation(longitude, latitude)
    }
}