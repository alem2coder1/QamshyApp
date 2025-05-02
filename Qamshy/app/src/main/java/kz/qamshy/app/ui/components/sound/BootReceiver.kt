package kz.sira.app.ui.components.sound

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.qamshy.app.viewmodels.RepositoryCityModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val Context.dataStore by preferencesDataStore("city_cache_store")

class BootReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED && !isEmulator()) {
            CoroutineScope(Dispatchers.IO).launch {
                val repository = RepositoryCityModel(context.dataStore)
                val savedCity = repository.loadCity()
                savedCity?.prayerTimes?.firstOrNull {
                    it.date == LocalDate.now().format(DateTimeFormatter.ISO_DATE)
                }?.let { todayPrayer ->
                    schedulePrayerAlarms(context, todayPrayer)
                }
            }
        }
    }

    private fun isEmulator(): Boolean =
        Build.FINGERPRINT.startsWith("generic") ||
                Build.FINGERPRINT.lowercase().contains("emulator") ||
                Build.MODEL.lowercase().contains("emulator") ||
                Build.MODEL.contains("Android SDK built for x86") ||
                Build.HARDWARE.contains("goldfish") ||
                Build.HARDWARE.contains("ranchu") ||
                Build.HARDWARE.contains("qcom") ||
                Build.PRODUCT.contains("sdk") ||
                Build.PRODUCT.contains("emulator") ||
                Build.PRODUCT.contains("simulator") ||
                Build.MANUFACTURER.contains("Genymotion") ||
                Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic") ||
                "google_sdk" == Build.PRODUCT
}