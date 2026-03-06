package com.yeasinx.oshudhkhan.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.ContextualFlowRow
import androidx.core.app.NotificationCompat
import com.yeasinx.oshudhkhan.MainActivity

object NotificationHelper {
    private const val CHANNEL_ID = "medicine_remainder_channel"
    private const val CHANNEL_NAME = "Medicine Remainders"

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Reminds you to take your medicine"
            enableVibration(true)
        }
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    fun showMedicineNotification(
        context: Context,
        notificationId: Int,
        medicineName: String,
        dosage: String
    ) {
        val tapIntent = Intent(context, MainActivity::class.java).apply {
            flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            tapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("\uD83D\uDC8A Time to take $medicineName")
            .setContentText("Dosage: $dosage")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationId, notification)

    }
}