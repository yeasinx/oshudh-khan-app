package com.yeasinx.oshudhkhan.data

import kotlinx.coroutines.flow.Flow

class MedicineRepository(private val dao: MedicineDao) {
    val allMedicine: Flow<List<Medicine>> = dao.getAllMedicines()

    suspend fun insert(medicine: Medicine): Long = dao.insertMedicine(medicine)

    suspend fun update(medicine: Medicine) = dao.updateMedicine(medicine)

    suspend fun delete(medicine: Medicine) = dao.deleteMedicine(medicine)

    suspend fun getActiveMedicines(): List<Medicine> = dao.getActiveMedicines()

}