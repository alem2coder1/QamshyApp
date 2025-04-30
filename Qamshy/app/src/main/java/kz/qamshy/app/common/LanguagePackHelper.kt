package kz.qamshy.app.common

import android.content.Context
import androidx.preference.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kz.qamshy.app.models.AjaxMsgModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

object LanguagePackHelper {

    private const val BASE_ADDRESS = "https://www.sozdikqor.org"
    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .build()
    }

    suspend fun getLanguagePackJsonString(language: String, context: Context): String {
        val languageLocalKey = "language_pack_new2_$language"
        return withContext(Dispatchers.IO) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            var result = sharedPreferences.getString(languageLocalKey, "")

            if (!result.isNullOrEmpty() && result != "null") {
                return@withContext result
            }

            try {
                val responseString = fetchLanguagePackFromServer(language)
                if (!responseString.isNullOrEmpty()) {
                    val ajaxResult = JsonHelper.deserializeObject<AjaxMsgModel>(responseString)
                    if (ajaxResult?.status == "success") {
                        val maps: Map<String, String>? = JsonHelper.convertAnyToObject(ajaxResult.data)
                        if(maps!=null)
                        {
                            result = JsonHelper.serializeObject(maps)
                            result.let {
                                sharedPreferences.edit().putString(languageLocalKey, it).apply()
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext ""
            }

            return@withContext result ?: ""
        }
    }

    private fun fetchLanguagePackFromServer(language: String): String? {
        val mediaType = "application/json".toMediaType()
        val body = "{\"languageList\": [\"$language\"]}".toRequestBody(mediaType)
        val request = Request.Builder()
            .url("$BASE_ADDRESS/api/v1/packs")
            .post(body)
            .header("Accept", "application/json")
            .build()

        return try {
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    response.body?.string()
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}