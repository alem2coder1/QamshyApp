package kz.sira.app.ui.components.sound

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

fun cancelPrayerAlarms(context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val prayerKeys = listOf("ls_Fajr", "ls_Dhuhr", "ls_Asr", "ls_Maghrib", "ls_Isha")

    prayerKeys.forEach { key ->
        val intent = Intent(context, AzanForegroundService::class.java)
        val pendingIntent = PendingIntent.getService(
            context, key.hashCode(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }
}

