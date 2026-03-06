package com.yeasinx.oshudhkhan.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class FrequencyType {
    DAILY,
    EVERY_N_DAYS
}

@Entity(tableName = "medicines")
data class Medicine(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val dosage: String,
    val frequencyType: FrequencyType,
    val timesPerDay: Int = 1,
    val intervalDays: Int = 1,
    val reminderTimes: String,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)