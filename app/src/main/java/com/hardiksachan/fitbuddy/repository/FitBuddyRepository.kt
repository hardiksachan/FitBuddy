package com.hardiksachan.fitbuddy.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hardiksachan.fitbuddy.database.asDomainModel
import com.hardiksachan.fitbuddy.database.getDatabase
import com.hardiksachan.fitbuddy.domain.Exercise
import com.hardiksachan.fitbuddy.domain.ExerciseCategory
import com.hardiksachan.fitbuddy.network.WgerApi
import com.hardiksachan.fitbuddy.network.asDatabaseModel
import kotlinx.coroutines.*
import timber.log.Timber

class FitBuddyRepository(private val applicationContext: Context) {

    private val database = getDatabase(applicationContext)

    fun getExercises(categories: List<Int>? = null): LiveData<List<Exercise>>? {
        val _exercises = if (categories == null) {
            Transformations.map(database.exerciseDao.getExercises()) {
                it.asDomainModel()
            }
        } else {
            Transformations.map(database.exerciseDao.getExercises(categories)) {
                it.asDomainModel()
            }
        }
        return _exercises
    }

    val exerciseCategories: LiveData<List<ExerciseCategory>> =
        Transformations.map(database.exerciseDao.getExerciseCategories()) {
            it.asDomainModel()
        }

    fun getExerciseCategoryFromId(id: Int) = database.exerciseDao.getExerciseCategoryNameFromId(id)


    suspend fun refreshExercises() {
        withContext(Dispatchers.IO) {
            try {
                Timber.i("Started to look for exercises")
                val exerciseResponse = WgerApi.retrofitService.getExercises(2, 2).await()
                Timber.i("${exerciseResponse.results!!.size} exercises fetched}")
                database.exerciseDao.insertAllExercises(*exerciseResponse.results!!.asDatabaseModel())
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
                Timber.i("${exerciseResponse.results!!.size} exercise categories fetched}")
                database.exerciseDao.insertAllExerciseCategories(*exerciseResponse.results!!.asDatabaseModel())
                Timber.i("Saved exercise categories to db")
            } catch (t: Throwable) {
                Timber.e(t.message.toString())
            }
        }
    }
}