package kz.qamshy.app.common

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object JsonHelper {
    val gson: Gson = Gson()

    fun <T> serializeObject(obj: T): String {
        return gson.toJson(obj)
    }

    inline fun <reified T> deserializeObject(json: String): T? {
        return try {
            gson.fromJson(json, object : TypeToken<T>() {}.type)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    inline fun <reified T> convertAnyToObject(anyObject: Any?): T? {
        if (anyObject == null) return null
        val json = gson.toJson(anyObject)
        return deserializeObject(json)
    }
}