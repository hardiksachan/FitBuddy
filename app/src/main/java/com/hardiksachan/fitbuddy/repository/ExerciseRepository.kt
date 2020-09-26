package com.hardiksachan.fitbuddy.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.hardiksachan.fitbuddy.database.ExerciseDatabase
import com.hardiksachan.fitbuddy.database.asDomainModel
import com.hardiksachan.fitbuddy.domain.Exercise
import com.hardiksachan.fitbuddy.network.WgerApi
import com.hardiksachan.fitbuddy.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class ExerciseRepository(private val database: ExerciseDatabase) {

    val exercises: LiveData<List<Exercise>> =
        Transformations.map(database.exerciseDao.getExercises()) {
            it.asDomainModel()
        }

    suspend fun refreshExercises() {
        withContext(Dispatchers.IO) {
            try {
                Timber.i("Started to look for exercises")
                val exerciseResponse = WgerApi.retrofitService.getExercises(2, 2).await()
                Timber.i("${exerciseResponse.results!!.size} exercises fetched}")
                database.exerciseDao.insertAll(*exerciseResponse.results!!.asDatabaseModel())
                Timber.i("Saved exercised to db")
            } catch (t: Throwable) {
                Timber.e(t.message.toString())
            }

        }
    }
}