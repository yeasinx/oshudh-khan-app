package com.yeasinx.oshudhkhan.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_MEDICINE_ID = "medicine_id"
        const val EXTRA_MEDICINE_NAME = "medicine_name"
        const val EXTRA_MEDICINE_DOSAGE = "medicine_dosage"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val medicineId = intent.getIntExtra(EXTRA_MEDICINE_ID, -1)
        val medicineName = intent.getStringExtra(EXTRA_MEDICINE_NAME) ?: return
        val medicineDosage = intent.getStringExtra(EXTRA_MEDICINE_DOSAGE) ?: ""

        NotificationHelper.showMedicineNotification(
            context = context,
            notificationId = medicineId,
            medicineName = medicineName,
            dosage = medicineDosage
        )
    }
}