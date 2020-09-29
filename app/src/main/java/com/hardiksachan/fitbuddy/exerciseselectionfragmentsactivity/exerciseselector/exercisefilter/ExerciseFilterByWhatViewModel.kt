package com.hardiksachan.fitbuddy.exerciseselectionfragmentsactivity.exerciseselector.exercisefilter

import android.app.Application
import androidx.lifecycle.*

class ExerciseFilterByWhatViewModel(
    application: Application
) : AndroidViewModel(application) {


    private val _navigateToSpecificFilter = MutableLiveData<FilterByWhat>()
    val navigateToSpecificFilter: LiveData<FilterByWhat>
        get() = _navigateToSpecificFilter

    fun onFilterSpecificTitleClicked(filter: FilterByWhat) {
        _navigateToSpecificFilter.value = filter
    }

    fun navigatedToSpecificFilter() {
        _navigateToSpecificFilter.value = null
    }

    class Factory(
        private val application: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ExerciseFilterByWhatViewModel::class.java)) {
                return ExerciseFilterByWhatViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}