package com.hardiksachan.fitbuddy.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.hardiksachan.fitbuddy.database.DatabaseExerciseEquipment
import com.hardiksachan.fitbuddy.database.asDomainModel
import com.hardiksachan.fitbuddy.database.getDatabase
import com.hardiksachan.fitbuddy.domain.Equipment
import com.hardiksachan.fitbuddy.domain.Exercise
import com.hardiksachan.fitbuddy.domain.ExerciseCategory
import com.hardiksachan.fitbuddy.network.WgerApi
import com.hardiksachan.fitbuddy.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class FitBuddyRepository(private val applicationContext: Context) {

    private val database = getDatabase(applicationContext)

    fun getExercises(searchQuery: String? = "%" ,categories: List<Int>? = null, equipments: List<Int>? = null): LiveData<List<Exercise>>? {
        val searchQuery = searchQuery ?: "%"
        return if (categories == null && equipments == null) {
            Transformations.map(database.exerciseDao.getExercises(searchQuery)) {
                it.asDomainModel(this)
            }
        } else if (categories != null && equipments == null){
            Transformations.map(database.exerciseDao.getExercises(categories = categories, searchText = searchQuery)) {
                it.asDomainModel(this)
            }
        } else if (categories == null && equipments != null){
            Transformations.map(database.exerciseDao.getExercisesUsingEquipment(equipments = equipments, searchText = searchQuery)) {
                it.asDomainModel(this)
            }
        } else {
            Transformations.map(database.exerciseDao.getExercises(categories = categories!!,equipments = equipments!!, searchText = searchQuery)) {
                it.asDomainModel(this)
            }
        }
    }

    val exerciseCategories: LiveData<List<ExerciseCategory>> =
        Transformations.map(database.exerciseDao.getExerciseCategories()) {
            it.asDomainModel()
        }

    val equipments: LiveData<List<Equipment>> =
        Transformations.map(database.exerciseDao.getEquipments()) {
            it.asDomainModel()
        }

    fun getExerciseCategoryFromId(id: Int) = database.exerciseDao.getExerciseCategoryNameFromId(id)

    fun getEquipmentFromId(id: Int) = database.exerciseDao.getEquipmentNameFromId(id)

    fun deleteAllEquipmentsOfExercise(exerciseId: Int) = database.exerciseDao.clearEquipmentsForExercise(exerciseId)

    fun getEquipmentsFromExercise(exerciseId: Int) =
        database.exerciseDao.getEquipmentsForExercise(exerciseId)

    suspend fun saveExerciseEquipment(equipment: List<Int>?, id: Int?) {
        equipment ?: return
        id ?: return

        equipment.map {
            withContext(Dispatchers.IO) {
                database.exerciseDao.insertEquipmentForExercise(
                    DatabaseExerciseEquipment(
                        equipmentId = it,
                        exerciseId = id
                    )
                )
            }
        }

    }

    suspend fun refreshExercises() {
        withContext(Dispatchers.IO) {
            try {
                Timber.i("Started to look for exercises")
                val exerciseResponse = WgerApi.retrofitService.getExercises(2, 2).await()
                Timber.i("${exerciseResponse.results!!.size} exercises fetched")
                database.exerciseDao.insertAllExercises(
                    *exerciseResponse.results!!.asDatabaseModel(
                        this@FitBuddyRepository
                    )
                )
                Timber.i("Saved exercises to db")
            } catch (t: Throwable) {
                Timber.e(t.message.toString())
            }

        }
    }

    suspend fun refreshExerciseCategories() {
        withContext(Dispatchers.IO) {
            try {
                Timber.i("Started to look for exercise categories")
                val exerciseResponse = WgerApi.retrofitService.getExerciseCategories().await()
                Timber.i("${exerciseResponse.results!!.size} exercise categories fetched")
                database.exerciseDao.insertAllExerciseCategories(*exerciseResponse.results!!.asDatabaseModel())
                Timber.i("Saved exercise categories to db")
            } catch (t: Throwable) {
                Timber.e(t.message.toString())
            }
        }
    }

    suspend fun refreshEquipments() {
        withContext(Dispatchers.IO) {
            try {
                Timber.i("Started to look for equipments")
                val exerciseResponse = WgerApi.retrofitService.getEquipments().await()
                Timber.i("${exerciseResponse.results!!.size} equipments fetched")
                database.exerciseDao.insertAllEquipments(*exerciseResponse.results!!.asDatabaseModel())
                Timber.i("Saved equipment to db")
            } catch (t: Throwable) {
                Timber.e(t.message.toString())
            }
        }
    }
}
