package com.hardiksachan.fitbuddy.mainactivityfragments.exerciseselector

import android.app.Application
import androidx.lifecycle.*
import com.hardiksachan.fitbuddy.database.getDatabase
import com.hardiksachan.fitbuddy.repository.ExerciseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ExerciseSelectorViewModel(application: Application) : AndroidViewModel(application) {
    private val _status = MutableLiveData<String>()
    val status : LiveData<String>
    get() = _status

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = getDatabase(application)
    private val exerciseRepository = ExerciseRepository(database)

    init{
        viewModelScope.launch {
            exerciseRepository.refreshExercises()
        }
    }

    val exercise = exerciseRepository.exercises

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ExerciseSelectorViewModel::class.java)) {
                return ExerciseSelectorViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
