package com.hardiksachan.fitbuddy.exerciseselectionfragmentsactivity.exerciseselector

import android.app.Application
import androidx.lifecycle.*
import com.hardiksachan.fitbuddy.domain.Exercise

class ExerciseSelectorViewModel(application: Application) : AndroidViewModel(application) {

    private val _navigateToFilter = MutableLiveData<Boolean>()
    val navigateToFilter: LiveData<Boolean>
        get() = _navigateToFilter

    private val _navigateToDetail = MutableLiveData<Boolean>()
    val navigateToDetail: LiveData<Boolean>
        get() = _navigateToDetail


    fun onFilterFabClicked() {
        _navigateToFilter.value = true
    }

    fun onNavigateToFilterDone() {
        _navigateToFilter.value = false
    }

    fun navigateToDetail(exercise: Exercise) {
        _navigateToDetail.value = true
    }

    fun onNavigateToDetailDone() {
        _navigateToDetail.value = false
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