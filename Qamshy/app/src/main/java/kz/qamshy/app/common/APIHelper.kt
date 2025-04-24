
package kz.qamshy.app.common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kz.qamshy.app.models.AjaxMsgModel
import kz.qamshy.app.ui.QamshyApp
import kz.qamshy.app.common.JsonHelper
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException
import java.util.concurrent.TimeUnit

interface ApiService {
    suspend fun queryAsync(
        url: String,
        method: String,
        headers: Map<String, String> = emptyMap(),
        paraDic: Map<String, Any>? = null,
        filePart: MultipartBody.Part? = null,
        requestBody: RequestBody? = null
    ): Result<AjaxMsgModel>
}

class ApiServiceImpl(
    private val baseUrl: String,
    private val defaultLanguageProvider: () -> String
) : ApiService {

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val androidVersion = android.os.Build.VERSION.RELEASE
                val request = chain.request().newBuilder()
                    .addHeader("User-Agent", "QamshyApp/1.0")
                    .addHeader("language", defaultLanguageProvider())
                    .addHeader("X-Client-Platform", "Android-$androidVersion")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    override suspend fun queryAsync(
        url: String,
        method: String,
        headers: Map<String, String>,
        paraDic: Map<String, Any>?,
        filePart: MultipartBody.Part?,
        requestBody: RequestBody?
    ): Result<AjaxMsgModel> {
        return withContext(Dispatchers.IO) {
            try {
                val fullUrl = "$baseUrl$url"
                val request = buildRequest(fullUrl, method, headers, paraDic, filePart, requestBody)
                client.newCall(request).execute().use { response ->
                    val responseBody = response.body?.string()
                    if (response.isSuccessful && responseBody != null) {
                        val result = JsonHelper.deserializeObject<AjaxMsgModel>(responseBody)
                        Result.success(result ?: AjaxMsgModel("Error", "Failed to parse response"))
                    } else {
                        val errorMsg = responseBody ?: "HTTP error ${response.code}"
                        Result.failure(IOException(errorMsg))
                    }
                }
            } catch (ex: Exception) {
                Result.failure(ex)
            }
        }
    }

    private fun buildRequest(
        url: String,
        method: String,
        headers: Map<String, String>,
        paraDic: Map<String, Any>?,
        filePart: MultipartBody.Part? = null,
        requestBody: RequestBody? = null
    ): Request {
        val builder = Request.Builder().url(url)
        headers.forEach { (key, value) ->
            builder.addHeader(key, value)
        }

        when (method.uppercase()) {
            "GET" -> builder.get()
            "POST", "PUT" -> {
                val body = if (requestBody != null) {
                    requestBody
                } else if (filePart != null) {
                    MultipartBody.Builder().setType(MultipartBody.FORM).apply {
                        addPart(filePart)
                        paraDic?.forEach { (key, value) ->
                            addFormDataPart(key, value.toString())
                        }
                    }.build()
                } else {
                    paraDic?.let {
                        val jsonBody = JsonHelper.serializeObject(it)
                        jsonBody.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                    } ?: "".toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                }

                if (method.uppercase() == "POST") {
                    builder.post(body)
                } else {
                    builder.put(body)
                }
            }
            "DELETE" -> {
                val body = paraDic?.let {
                    val jsonBody = JsonHelper.serializeObject(it)
                    jsonBody.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                } ?: "".toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                builder.delete(body)
            }
            else -> throw IllegalArgumentException("Unsupported HTTP method: $method")
        }

        return builder.build()
    }
}
