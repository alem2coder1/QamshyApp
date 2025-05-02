package kz.sira.app.ui.components.sound

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import kz.qamshy.app.common.ToastHelper
import kz.qamshy.app.models.PrayerTime
import kz.qamshy.app.ui.QamshyApp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs

fun schedulePrayerAlarms(context: Context, prayerTimes: PrayerTime) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
            data = Uri.parse("package:${context.packageName}")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
        ToastHelper.showMessage(context, "error", "Permission not granted, unable to set precise alarm")
        return
    }

    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val prayers = mapOf(
        "ls_Fajr" to prayerTimes.fajr,
        "ls_Dhuhr" to prayerTimes.dhuhr,
        "ls_Asr" to prayerTimes.asr,
        "ls_Maghrib" to prayerTimes.maghrib,
        "ls_Isha" to prayerTimes.isha
    )

    val currentLanguage = QamshyApp.currentLanguage.value.languageCulture

    prayers.forEach { (name, timeStr) ->

        if (!PrayerPreferences.isPrayerEnabled(context, name)) {
            ToastHelper.showMessage(context, "error", "jump over $name Because notifications are not enabled")
            return@forEach
        }

        val prayerName = when (currentLanguage) {
            "kz" -> when (name) {
                "ls_Fajr" -> "Таң"
                "ls_Dhuhr" -> "Түс"
                "ls_Asr" -> "Кеш"
                "ls_Maghrib" -> "Батыс"
                "ls_Isha" -> "Құптан"
                else -> name
            }
            "ru" -> when (name) {
                "ls_Fajr" -> "Фаджр"
                "ls_Dhuhr" -> "Зухр"
                "ls_Asr" -> "Аср"
                "ls_Maghrib" -> "Магриб"
                "ls_Isha" -> "Иша"
                else -> name
            }
            else -> when (name) {
                "ls_Fajr" -> "Fajr"
                "ls_Dhuhr" -> "Dhuhr"
                "ls_Asr" -> "Asr"
                "ls_Maghrib" -> "Maghrib"
                "ls_Isha" -> "Isha"
                else -> name
            }
        }

        val contentText = when (currentLanguage) {
            "kz" -> when(name){
                "ls_Fajr" -> "Қараңғыда мешітке барғандар – қиямет күні нұрмен жарқырайды."
                "ls_Dhuhr" -> "Қарбаластың арасында Раббыңды ұмытпа."
                "ls_Asr" -> "Екінтіні тәрк еткеннің амалы жойылады."
                "ls_Maghrib" -> "Пайғамбар сүннеті — ақшамды кешіктірме."
                "ls_Isha" -> "Құптанға беріктік – екіжүзділіктен қорғайды."
                else -> name
            }
            "ru" -> when (name) {
                "ls_Fajr" -> "Те, кто ходит в мечеть в темноте, будут сиять светом в Судный день."
                "ls_Dhuhr" -> "Среди суеты не забывай своего Господа."
                "ls_Asr" -> "Дела того, кто пропускает Аср, будут напрасны."
                "ls_Maghrib" -> "Сунна Пророка – не откладывать Магриб."
                "ls_Isha" -> "Соблюдение Иша защищает от лицемерия."
                else -> name
            }
            else -> when (name) {
                "ls_Fajr" -> "Those who go to the mosque in darkness will shine with light on Judgment Day."
                "ls_Dhuhr" -> "Don't forget your Lord amidst the busyness."
                "ls_Asr" -> "Whoever misses Asr prayer, their deeds will be lost."
                "ls_Maghrib" -> "The Prophet’s Sunnah is not to delay Maghrib."
                "ls_Isha" -> "Firmness in Isha protects from hypocrisy."
                else -> name
            }
        }

        val btnText = when (currentLanguage) {
            "kz" -> "Тоқтату"
            "ru" -> "Остановить"
            else -> "stop"
        }

        val prayerTime = LocalTime.parse(timeStr, formatter)
        var prayerDateTime = ZonedDateTime.now(ZoneId.systemDefault())
            .withHour(prayerTime.hour)
            .withMinute(prayerTime.minute)
            .withSecond(0)
            .withNano(0)

        if (prayerDateTime.isBefore(ZonedDateTime.now())) {
            prayerDateTime = prayerDateTime.plusDays(1)
        }

        val intent = Intent(context, AzanForegroundService::class.java).apply {
            putExtra("prayer_key", name)
            putExtra("prayer_name", prayerName)
            putExtra("content_text", contentText)
            putExtra("btn_text", btnText)
        }

        val pendingIntent = PendingIntent.getService(
            context,
            abs(name.hashCode()),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            prayerDateTime.toInstant().toEpochMilli(),
            pendingIntent
        )
    }
}