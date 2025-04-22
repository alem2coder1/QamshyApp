package kz.qamshy.app.common

import android.content.Context
import android.content.SyncStats
import android.view.LayoutInflater
import android.widget.Toast

object ToastHelper {
    fun showMessage(context: Context, status: String, message: String) {

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
