package com.hardiksachan.fitbuddy.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.hardiksachan.fitbuddy.database.*
import com.hardiksachan.fitbuddy.domain.*
import com.hardiksachan.fitbuddy.network.WgerApi
import com.hardiksachan.fitbuddy.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class FitBuddyRepository(private val applicationContext: Context) {

    private val database = getDatabase(applicationContext)

//    private val _repositoryUpdated = MutableLiveData<Boolean>()
//    val repositoryUpdated: LiveData<Boolean>
//        get() = _repositoryUpdated

//    fun onRepositoryUpdatedHandled(){
//        _repositoryUpdated.value = false
//    }

    fun getExercises(
        searchQuery: String? = "%",
        categories: List<Int>? = null,
        equipments: List<Int>? = null,
        primaryMuscles: List<Int>? = null,
        secondaryMuscles: List<Int>? = null
    ): LiveData<List<Exercise>>? {

        val searchQuery = searchQuery ?: "%"
        val categories: List<Int> = if (categories == null || categories.isEmpty()) {
            listOf(-255)
        } else {
            categories
        }
        val equipments: List<Int> = if (equipments == null || equipments.isEmpty()) {
            listOf(-255)
        } else {
            equipments
        }
        val primaryMuscles: List<Int> =
            if (primaryMuscles == null || primaryMuscles.isEmpty()) {
                listOf(-255)
            } else {
                primaryMuscles
            }
        val secondaryMuscles: List<Int> =
            if (secondaryMuscles == null || secondaryMuscles.isEmpty()) {
                listOf(-255)
            } else {
                secondaryMuscles
            }
        Timber.d("Categories: $categories")
        Timber.d("Equipments: $equipments")
        Timber.d("Primary Muscles: $primaryMuscles")
        Timber.d("Secondary Muscles: $secondaryMuscles")
        return Transformations.map(
            database.exerciseDao.getExercises(
                categories = categories,
                equipments = equipments,
                primaryMuscles = primaryMuscles,
                secondaryMuscles = secondaryMuscles,
                searchText = searchQuery
            )
        ) {
            it.asDomainModel(this)

        }

    }

    fun getExerciseDayByDay(day: Int): LiveData<List<ExerciseDay>> {
        return Transformations.map(database.exerciseDao.getExerciseByDay(day)) {
            it.map { exerciseDay ->
                exerciseDay.asDomainModel(this)
            }
        }
    }

    fun getExerciseIdByDay(day: Int): LiveData<List<Int>> {
        return database.exerciseDao.getExerciseIdListOfDay(day)
    }

    val exerciseCategories: LiveData<List<ExerciseCategory>> =
        Transformations.map(database.exerciseDao.getExerciseCategories()) {
            it.asDomainModel()
        }

    val equipments: LiveData<List<Equipment>> =
        Transformations.map(database.exerciseDao.getEquipments()) {
            it.asDomainModel()
        }

    val muscles: LiveData<List<Muscle>> =
        Transformations.map(database.exerciseDao.getMuscles()) {
            it.asDomainModel()
        }

    val user: LiveData<User> =
        Transformations.map(database.exerciseDao.getUser()) {
            it.asDomainModel()
        }

    val weightList: LiveData<List<Weight>> =
        Transformations.map(database.exerciseDao.getWeightList()) {
            it.asDomainModel()
        }

    val heightList: LiveData<List<Height>> =
        Transformations.map(database.exerciseDao.getHeightList()) {
            it.asDomainModel()
        }

    val currentWeight = database.exerciseDao.getCurrentWeight()
    val currentHeight = database.exerciseDao.getCurrentHeight()

    val allSleepNights = database.exerciseDao.getAllNights()

    fun getExerciseCategoryFromId(id: Int) = database.exerciseDao.getExerciseCategoryNameFromId(id)

    fun getExerciseFromId(id: Int) = database.exerciseDao.getExerciseFromId(id)

    fun getEquipmentFromId(id: Int) = database.exerciseDao.getEquipmentNameFromId(id)

    fun deleteAllEquipmentsOfExercise(exerciseId: Int) =
        database.exerciseDao.clearEquipmentsForExercise(exerciseId)

    fun getEquipmentsFromExercise(exerciseId: Int) =
        database.exerciseDao.getEquipmentsForExercise(exerciseId)

    fun getPrimaryMuscleFromExercise(exerciseId: Int) =
        database.exerciseDao.getMusclesForExercise(exerciseId, false)

    fun getSecondaryMuscleFromExercise(exerciseId: Int) =
        database.exerciseDao.getMusclesForExercise(exerciseId, true)

    fun deleteAllMusclesOfExercise(exerciseId: Int) =
        database.exerciseDao.clearMusclesForExercise(exerciseId)

    suspend fun insertUser(user: User) {
        withContext(Dispatchers.IO) {
            database.exerciseDao.clearAllUsers()
            database.exerciseDao.insertUser(user.asDatabaseModel())
        }
    }

    suspend fun insertHeights(h: List<Height>) {
        withContext(Dispatchers.IO) {
            database.exerciseDao.insertHeight(*h.asDatabaseModel().toTypedArray())
        }
    }


    suspend fun insertWeights(w: List<Weight>) {
        withContext(Dispatchers.IO) {
            database.exerciseDao.insertWeight(*w.asDatabaseModel().toTypedArray())
        }
    }

    suspend fun insertExerciseDayByDay(day: Int, exerciseId: Int, sets: Int, reps: Int) {
        withContext(Dispatchers.IO) {
            database.exerciseDao.insertExerciseDayByDay(
                DatabaseExerciseByDay(
                    day = day,
                    exerciseId = exerciseId,
                    sets = sets,
                    reps = reps
                )
            )
        }
    }

    suspend fun deleteExerciseDay(id: Int) {
        withContext(Dispatchers.IO) {
            database.exerciseDao.deleteExerciseDaybyId(id)
        }
    }

    suspend fun updateExerciseDay(exerciseDay: ExerciseDay) {
        withContext(Dispatchers.IO) {
            database.exerciseDao.updateExerciseByDat(
                DatabaseExerciseByDay(
                    id = exerciseDay.id,
                    exerciseId = exerciseDay.exercise.value!!.id,
                    sets = exerciseDay.sets,
                    reps = exerciseDay.reps,
                    day = exerciseDay.day
                )
            )
        }
    }

    suspend fun insertSleepNight(night: SleepNight) {
        withContext(Dispatchers.IO) {
            database.exerciseDao.insertSleepNight(night)
        }
    }

    suspend fun updateSleepNight(night: SleepNight) {
        withContext(Dispatchers.IO) {
            database.exerciseDao.updateSleepNight(night)
        }
    }

    suspend fun getSleepNight(key: Long) {
        withContext(Dispatchers.IO) {
            database.exerciseDao.getSleepNight(key)
        }
    }

    suspend fun getTonight(): SleepNight? {
        return withContext(Dispatchers.IO) {
            database.exerciseDao.getTonight()
        }
    }

    suspend fun clearsleepNight() {
        withContext(Dispatchers.IO) {
            database.exerciseDao.clearSleepNights()
        }
    }


    suspend fun saveExerciseEquipment(equipment: List<Int>?, id: Int?) {
        val equipment = if (equipment == null || equipment.isEmpty()) {
            listOf<Int>(-1)
        } else {
            equipment
        }
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

    suspend fun saveExerciseMuscle(muscle: List<Int>?, id: Int?, isSecondary: Boolean) {
        val muscle = if (muscle == null || muscle.isEmpty()) {
            Timber.e("Saving 1 Exercise With No Muscle")
            listOf<Int>(-1)
        } else {
            muscle
        }
        id ?: return

        muscle.map {
            withContext(Dispatchers.IO) {
                database.exerciseDao.insertMuscleForExercise(
                    DatabaseExerciseMuscle(
                        muscleId = it,
                        exerciseId = id,
                        isSecondary = isSecondary
                    )
                )
            }
        }

    }

    suspend fun refreshExercises(offset: Int = 0, limit: Int = 1000) {
        withContext(Dispatchers.IO) {
            try {
                Timber.i("Started to look for exercises")
                val exerciseResponse = WgerApi.retrofitService.getExercises(
                    language = 2,
                    status = 2,
                    limit = limit,
                    offset = offset
                ).await()
                Timber.i("${exerciseResponse.results!!.size} exercises fetched")
                database.exerciseDao.insertAllExercises(
                    *exerciseResponse.results!!.asDatabaseModel(
                        this@FitBuddyRepository
                    )
                )
                if (exerciseResponse.next != null) {
                    refreshExercises(offset = offset + limit, limit = limit)
                }
                Timber.i("Saved exercises to db")

//                masterExerciseCategories =
//                    database.exerciseDao.getExerciseCategoriesIds() as MutableList<Int>

                Timber.i("Saving Equipments and muscles to db")
                exerciseResponse.results!!.forEach {
                    val repository = this@FitBuddyRepository
                    repository.deleteAllEquipmentsOfExercise(
                        it.id ?: throw Exception("Exercise must have Id")
                    )
                    repository.saveExerciseEquipment(it.equipment, it.id)

                    repository.deleteAllMusclesOfExercise(
                        it.id ?: throw Exception("Exercise must have Id")
                    )
                    repository.saveExerciseMuscle(it.muscles, it.id, false)
                    repository.saveExerciseMuscle(it.musclesSecondary, it.id, true)
                }
//                masterExerciseEquipments =
//                    database.exerciseDao.getEquipmentIds() as MutableList<Int>
//                masterExerciseMuscles = database.exerciseDao.getMuscleIds() as MutableList<Int>
                Timber.i("Saved Equipments and muscles to db")
//                _repositoryUpdated.postValue(true)
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
                database.exerciseDao.insertAllExerciseCategories(
                    DatabaseExerciseCategory(
                        id = -1,
                        name = "None"
                    )
                )
//                _repositoryUpdated.postValue(true)
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
                database.exerciseDao.insertAllEquipments(DatabaseEquipment(id = -1, name = "None"))
//                _repositoryUpdated.postValue(true)
            } catch (t: Throwable) {
                Timber.e(t.message.toString())
            }
        }
    }

    suspend fun refreshMuscles() {
        withContext(Dispatchers.IO) {
            try {
                Timber.i("Started to look for muscles")
                val exerciseResponse = WgerApi.retrofitService.getMuscles().await()
                Timber.i("${exerciseResponse.results!!.size} muscles fetched")
                database.exerciseDao.insertAllMuscles(*exerciseResponse.results!!.asDatabaseModel())
                Timber.i("Saved muscles to db")
                database.exerciseDao.insertAllMuscles(DatabaseMuscle(id = -1, name = "None"))
//                _repositoryUpdated.postValue(true)
            } catch (t: Throwable) {
                Timber.e(t.message.toString())
            }
        }
    }
}
