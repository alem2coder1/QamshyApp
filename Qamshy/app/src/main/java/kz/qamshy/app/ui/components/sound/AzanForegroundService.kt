package kz.sira.app.ui.components.sound

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.app.Service
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import kz.qamshy.app.R


class AzanForegroundService : Service() {
    companion object {
        private var currentMediaPlayer: MediaPlayer? = null
        const val NOTIFICATION_ID = 1001
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent?.action == "STOP_AZAN") {
            currentMediaPlayer?.let { player ->
                if (player.isPlaying) {
                    player.stop()
                }
                player.release()
            }
            currentMediaPlayer = null
            stopForeground(true)
            stopSelf()
            return START_NOT_STICKY
        }
       val prayerkey = intent?.getStringExtra("prayer_key")
        val prayerName = intent?.getStringExtra("prayer_name")
        val contentText = intent?.getStringExtra("content_text")
        val btnText = intent?.getStringExtra("btn_text")

        if (prayerName.isNullOrEmpty()) {
            stopSelf()
            return START_NOT_STICKY
        }
        if (prayerkey.isNullOrEmpty()) {
            stopSelf()
            return START_NOT_STICKY
        }

        try {
            val notification = buildNotification(prayerName,contentText ?: "",btnText ?: "")
            startForeground(NOTIFICATION_ID, notification)
        } catch (e: Exception) {
            stopSelf()
        }

        playPrayerNotification(prayerkey)

        if (PrayerPreferences.isVibrationEnabled(this)) {
            vibrate()
        }

        return START_NOT_STICKY
    }

    private fun buildNotification(prayerName: String,contentText:String,btnText:String): Notification {
        val stopIntent = Intent(this, AzanForegroundService::class.java).apply {
            action = "STOP_AZAN"
        }
        val stopPendingIntent = PendingIntent.getService(
            this,
            1002,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, "azan_channel")
            .setSmallIcon(R.drawable.logo_dark)
            .setContentTitle(prayerName)
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .addAction(
                R.drawable.logo_dark,
                btnText,
                stopPendingIntent
            )
            .build()
    }

    private fun playPrayerNotification(prayerkey: String) {
        val soundResId = if (PrayerPreferences.useUnifiedSound(this)) {
            PrayerPreferences.getUnifiedSound(this)
        } else {
            PrayerPreferences.getPrayerSound(this, prayerkey)
        }
        currentMediaPlayer?.release()
        currentMediaPlayer = MediaPlayer.create(this, soundResId).apply {
            setOnCompletionListener {
                stopForeground(true)
                stopSelf()
                release()
            }
            start()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun vibrate(duration: Long = 1000) {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "azan_channel",
                "Azan Notification",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setSound(null, null)
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        currentMediaPlayer?.release()
        currentMediaPlayer = null
        super.onDestroy()
    }
}