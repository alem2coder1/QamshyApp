package kz.qamshy.app.common

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.util.UUID

object PhotoHelper {
    fun createMultipartFileFromUri(context: Context, imageUri: Uri): MultipartBody.Part? {
        return try {
            val inputStream = context.contentResolver.openInputStream(imageUri) ?: return null
            val mimeType = context.contentResolver.getType(imageUri) ?: "application/octet-stream"
            val requestBody = inputStream.readBytes().toRequestBody(mimeType.toMediaTypeOrNull())
            val fileName = "image_${UUID.randomUUID()}.jpg"
            MultipartBody.Part.createFormData("file", fileName, requestBody)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}