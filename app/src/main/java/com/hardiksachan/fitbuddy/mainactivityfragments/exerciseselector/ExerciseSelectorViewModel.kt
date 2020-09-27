package com.hardiksachan.fitbuddy.mainactivityfragments.exerciseselector

import android.app.Application
import androidx.lifecycle.*

class ExerciseSelectorViewModel(application: Application) : AndroidViewModel(application) {

    private val _navigateToFilter = MutableLiveData<Boolean>()
    val navigateToFilter: LiveData<Boolean>
        get() = _navigateToFilter


    fun onFilterFabClicked() {
        _navigateToFilter.value = true
    }

    fun onNavigateToFilterDone() {
        _navigateToFilter.value = false
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