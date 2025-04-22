package kz.qamshy.app.common

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class LatestLocationHelper(private val context: Context) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private var locationCallback: LocationCallback? = null

    @SuppressLint("MissingPermission")
    fun startLocationUpdates(onLocationResult: (Location) -> Unit) {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            10_000L
        )
            .setMinUpdateIntervalMillis(5_000L)
            .setMinUpdateDistanceMeters(10000f)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    onLocationResult(location)
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback!!,
            Looper.getMainLooper()
        )
    }

    fun stopLocationUpdates() {
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
        }
    }

    @SuppressLint("MissingPermission")
    fun getSingleAccurateLocation(onLocationResult: (Location?) -> Unit) {
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                onLocationResult(location)
            }
            .addOnFailureListener {
                onLocationResult(null)
            }
    }
}