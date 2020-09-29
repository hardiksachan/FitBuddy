package com.hardiksachan.fitbuddy.exerciseselectionfragmentsactivity.exerciseselector.exercisefilter

import android.app.Application
import androidx.lifecycle.*


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