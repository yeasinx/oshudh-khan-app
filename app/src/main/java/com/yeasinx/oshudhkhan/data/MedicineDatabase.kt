package com.yeasinx.oshudhkhan.data

import android.content.Context
import androidx.room.*

class Converters {
    @TypeConverter
    fun fromFrequencyType(value: FrequencyType): String = value.name

    @TypeConverter
    fun toFrequencyType(value: String): FrequencyType = FrequencyType.valueOf(value)
}

@Database(
    entities = [Medicine::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MedicineDatabase : RoomDatabase() {

    abstract fun medicineDao(): MedicineDao

    companion object {
        @Volatile
        private var INSTANCE: MedicineDatabase? = null

        fun getDatabase(context: Context): MedicineDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MedicineDatabase::class.java,
                    "medicine_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}