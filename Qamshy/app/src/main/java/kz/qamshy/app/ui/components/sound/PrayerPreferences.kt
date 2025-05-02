package kz.sira.app.ui.components.sound

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import kz.qamshy.app.R

object PrayerPreferences {
    private fun prefs(context: Context) =
        context.getSharedPreferences("prayer_prefs", Context.MODE_PRIVATE)

    fun isNotificationEnabled(context: Context): Boolean {
        val userPreferenceEnabled = prefs(context).getBoolean("notification_enabled", true)
        val systemPermissionGranted =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else true

        return userPreferenceEnabled && systemPermissionGranted
    }

    fun setNotificationEnabled(context: Context, enabled: Boolean) =
        prefs(context).edit().putBoolean("notification_enabled", enabled).apply()

    fun isVibrationEnabled(context: Context): Boolean =
        prefs(context).getBoolean("vibration_enabled", true)

    fun setVibrationEnabled(context: Context, enabled: Boolean) =
        prefs(context).edit().putBoolean("vibration_enabled", enabled).apply()

    fun isPrayerEnabled(context: Context, prayer: String): Boolean =
        prefs(context).getBoolean("${prayer}_enabled", prayer != "ls_Sunrise")

    fun setPrayerEnabled(context: Context, prayer: String, enabled: Boolean) =
        prefs(context).edit().putBoolean("${prayer}_enabled", enabled).apply()

    fun getPrayerSound(context: Context, prayer: String): Int =
        prefs(context).getInt("${prayer}_sound", R.raw.azan)

    fun setPrayerSound(context: Context, prayer: String, soundResId: Int) =
        prefs(context).edit().putInt("${prayer}_sound", soundResId).apply()
    fun useUnifiedSound(context: Context): Boolean =
        prefs(context).getBoolean("use_unified_sound", true)

    fun setUseUnifiedSound(context: Context, unified: Boolean) =
        prefs(context).edit().putBoolean("use_unified_sound", unified).apply()

    fun getUnifiedSound(context: Context): Int =
        prefs(context).getInt("unified_sound", R.raw.azan)

    fun setUnifiedSound(context: Context, soundResId: Int) =
        prefs(context).edit().putInt("unified_sound", soundResId).apply()
    fun isAzanPaused(context: Context): Boolean =
        prefs(context).getBoolean("azan_paused", false)

    fun setAzanPaused(context: Context, paused: Boolean) =
        prefs(context).edit().putBoolean("azan_paused", paused).apply()


}