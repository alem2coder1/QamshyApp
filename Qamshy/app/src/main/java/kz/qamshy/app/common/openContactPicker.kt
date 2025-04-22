package kz.qamshy.app.common

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

fun openContactPicker(
    context: Context,
    contactPickerLauncher: ActivityResultLauncher<Intent>,
    onPermissionDenied: () -> Unit
) {
    val permission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
    if (permission == android.content.pm.PackageManager.PERMISSION_GRANTED) {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        contactPickerLauncher.launch(intent)
    } else {
        onPermissionDenied()
    }
}
fun openAppSettings(context: Context) {
    val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}

fun getPhoneNumberFromContactUri(context: Context, contactUri: Uri): String? {
    val contentResolver = context.contentResolver
    val cursor = contentResolver.query(contactUri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val hasPhoneIndex = it.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
            if (hasPhoneIndex != -1 && it.getInt(hasPhoneIndex) > 0) {
                val contactId = it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                val phoneCursor = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                    arrayOf(contactId),
                    null
                )
                phoneCursor?.use { phones ->
                    if (phones.moveToFirst()) {
                        val numberIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        if (numberIndex != -1) {
                            return phones.getString(numberIndex)
                        }
                    }
                }
            }
        }
    }
    return null
}
