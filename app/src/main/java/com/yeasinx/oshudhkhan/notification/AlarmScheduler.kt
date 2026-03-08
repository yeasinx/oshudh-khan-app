package com.yeasinx.oshudhkhan.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.yeasinx.oshudhkhan.data.FrequencyType
import com.yeasinx.oshudhkhan.data.Medicine
import java.util.Calendar

object AlarmScheduler {

    fun scheduleMedicineAlarms(context: Context, medicine: Medicine) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val times = medicine.reminderTimes.split(",").map { it.trim() }.filter { it.isNotEmpty() }

        times.forEachIndexed { index, time ->
            val parts = time.split(":")
            if (parts.size < 2) return@forEachIndexed
            val hour = parts[0].toIntOrNull() ?: return@forEachIndexed
            val minute = parts[1].toIntOrNull() ?: return@forEachIndexed

            val requestCode = medicine.id * 100 + index

            val intervalDays = when (medicine.frequencyType) {
                FrequencyType.DAILY -> 1
                FrequencyType.EVERY_N_DAYS -> medicine.intervalDays
            }

            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra(AlarmReceiver.EXTRA_MEDICINE_ID, medicine.id)
                putExtra(AlarmReceiver.EXTRA_MEDICINE_NAME, medicine.name)
                putExtra(AlarmReceiver.EXTRA_MEDICINE_DOSAGE, medicine.dosage)
                putExtra(AlarmReceiver.EXTRA_HOUR, hour)
                putExtra(AlarmReceiver.EXTRA_MINUTE, minute)
                putExtra(AlarmReceiver.EXTRA_INTERVAL_DAYS, intervalDays)
                putExtra(AlarmReceiver.EXTRA_REQUEST_CODE, requestCode)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                if (timeInMillis <= System.currentTimeMillis()) {
                    add(Calendar.DAY_OF_MONTH, 1)
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        }
    }

    fun cancelMedicineAlarms(context: Context, medicine: Medicine) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val times = medicine.reminderTimes.split(",")

        times.forEachIndexed { index, _ ->
            val requestCode = medicine.id * 100 + index
            val intent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
        }
    }
}