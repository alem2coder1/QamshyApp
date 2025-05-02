package kz.sira.app.ui.components.sound

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kz.qamshy.app.R
import kz.qamshy.app.common.Translator.T
import kz.qamshy.app.models.LanguageModel


class AzanReceiver : BroadcastReceiver() {

    companion object {
        private var currentMediaPlayer: MediaPlayer? = null
    }
    val currentLanguage = LanguageModel(
        flagUrl = "https://sira.kz/images/flag/kz.svg",
        fullName = "Қазақша",
        shortName = "Қаз",
        languageCulture = "kz",
        isDefault = true,
        uniqueSeoCode = "kk"
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "STOP_AZAN") {
            currentMediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
                it.release()
            }
            currentMediaPlayer = null
            NotificationManagerCompat.from(context).cancel(1001)
            return
        }

        if (!PrayerPreferences.isNotificationEnabled(context)) return
        val prayerName = intent.getStringExtra("prayer_name") ?: return

        playPrayerNotification(context, prayerName)

        if (PrayerPreferences.isVibrationEnabled(context)) {
            vibrate(context)
        }
        showAzanNotification(context, prayerName,currentLanguage)
    }

    private fun playPrayerNotification(context: Context, prayerName: String) {
        val soundResId = getSoundResId(context, prayerName)

        val player = MediaPlayer.create(context, soundResId).apply {
            start()
            setOnCompletionListener { release() }
        }
        currentMediaPlayer = player
    }

    private fun getSoundResId(context: Context, prayerName: String): Int {
        return if (PrayerPreferences.useUnifiedSound(context)) {
            PrayerPreferences.getUnifiedSound(context)
        } else {
            PrayerPreferences.getPrayerSound(context, prayerName)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun vibrate(context: Context, duration: Long = 1000) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
    }


    private fun showAzanNotification(context: Context, prayerName: String,currentLanguage:LanguageModel) {
        createNotificationChannel(context)
        val notificationManager = NotificationManagerCompat.from(context)
        if (!notificationManager.areNotificationsEnabled()) return

        val stopIntent = Intent(context, AzanReceiver::class.java).apply {
            action = "STOP_AZAN"
        }
        val stopPendingIntent = PendingIntent.getBroadcast(
            context,
            1002,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, "azan_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(T(prayerName,currentLanguage))
            .setContentText(T("ls_Aibr",currentLanguage))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(false)
            .setOngoing(true)
            .addAction(
                R.drawable.ic_launcher_foreground,
                T("ls_Stop",currentLanguage),
                stopPendingIntent
            )
            .build()

        try {
            notificationManager.notify(1001, notification)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "azan_channel",
                "Azan Notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.setSound(null, null)
            context.getSystemService(NotificationManager::class.java)
                ?.createNotificationChannel(channel)
        }
    }
}
