package kz.qamshy.app.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class TakeFullResPhotoContract : ActivityResultContract<Void?, Uri?>() {

    private var contentUri: Uri? = null

    override fun createIntent(context: Context, input: Void?): Intent {
        val photoFile = createTempImageFile(context)
        contentUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            photoFile
        )
        return Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(android.provider.MediaStore.EXTRA_OUTPUT, contentUri)
            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return if (resultCode == Activity.RESULT_OK) contentUri else null
    }

    private fun createTempImageFile(context: Context): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }
}