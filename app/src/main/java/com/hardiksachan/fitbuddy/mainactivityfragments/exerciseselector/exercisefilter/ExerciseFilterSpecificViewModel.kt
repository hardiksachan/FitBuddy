package com.hardiksachan.fitbuddy.mainactivityfragments.exerciseselector.exercisefilter

import android.app.Application
import androidx.lifecycle.*
import timber.log.Timber


class ExerciseFilterSpecificViewModel(
    application: Application
) : AndroidViewModel(application) {

    class Factory(
        private val application: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ExerciseFilterSpecificViewModel::class.java)) {
                return ExerciseFilterSpecificViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}