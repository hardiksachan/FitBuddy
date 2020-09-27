package com.hardiksachan.fitbuddy.mainactivityfragments.exerciseselector.exercisefilter

import android.app.Application
import androidx.lifecycle.*

class ExerciseFilterViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val _navigateToSelector = MutableLiveData<Boolean>()
    val navigateToSelector: LiveData<Boolean>
        get() = _navigateToSelector

    fun onDoneFabClicked() {
        _navigateToSelector.value = true
    }

    fun onNavigateToSelectorDone() {
        _navigateToSelector.value = false
    }


    class Factory(
        private val application: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ExerciseFilterViewModel::class.java)) {
                return ExerciseFilterViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}