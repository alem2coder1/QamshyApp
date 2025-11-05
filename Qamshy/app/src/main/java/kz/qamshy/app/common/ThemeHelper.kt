package kz.qamshy.app.common


import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import kotlin.collections.getOrNull

class ThemeHelper(private val context: Context) {

    companion object {
        private const val PREF_NAME = "theme_prefs"
        private const val KEY_THEME_MODE = "key_theme_mode"
    }

    fun loadThemeMode(): ThemeMode {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val modeOrdinal = prefs.getInt(KEY_THEME_MODE, ThemeMode.FOLLOW_SYSTEM.ordinal)
        return ThemeMode.values().getOrNull(modeOrdinal) ?: ThemeMode.FOLLOW_SYSTEM
    }

    fun saveThemeMode(mode: ThemeMode) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putInt(KEY_THEME_MODE, mode.ordinal)
            .apply()
    }
    fun applyThemeMode(mode: ThemeMode) {
        val nightMode = when (mode) {
            ThemeMode.FOLLOW_SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }

    fun isDarkModeEnabled(): Boolean {
        val currentNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }
}