package kz.qamshy.app.models

import kotlinx.serialization.Serializable


@Serializable
data class CityResponse(
    val city: CityModel
)
@Serializable
data class CityModel(
    val id: Int,
    val title :String,
    val lat:Double,
    val lng:Double,
    val distance:Double,
    val prayerTimes:List<PrayerTime>
)
@Serializable
data class PrayerTime(
    val imsak:String,
    val fajr:String,
    val sunrise:String,
    val dhuhr:String,
    val asr:String,
    val sunset:String,
    val maghrib:String,
    val isha:String,
    val midnight:String,
    val date:String,
)