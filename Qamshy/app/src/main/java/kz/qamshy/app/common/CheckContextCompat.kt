package kz.qamshy.app.common

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.ContactsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat

@Composable
fun CheckAndRequestPermission(
    context:Context,
    permission: String,
    onPermissionResult: (Boolean) -> Unit,

) {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        onPermissionResult(isGranted)
    }

    if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
        onPermissionResult(true)
    } else {
        permissionLauncher.launch(android.Manifest.permission.READ_CONTACTS)
    }
}

fun openContacts(context: Context) {
    val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
    context.startActivity(intent)
}