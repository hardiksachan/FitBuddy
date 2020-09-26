package com.hardiksachan.fitbuddy.mainactivityfragments.exerciseselector

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.hardiksachan.fitbuddy.repository.FitBuddyRepository
import kotlinx.coroutines.launch

class ExerciseSelectorViewModel(application: Application) : AndroidViewModel(application) {

    private val _eventUpdateExerciseLiveDataObserver = MutableLiveData<Boolean>()
    val eventUpdateExerciseLiveDataObserver: LiveData<Boolean>
        get() = _eventUpdateExerciseLiveDataObserver

    private val fitBuddyRepository = FitBuddyRepository(application)

    init {
        viewModelScope.launch {
            fitBuddyRepository.refreshEquipments()
            fitBuddyRepository.refreshExerciseCategories()
            fitBuddyRepository.refreshExercises()
        }
        _eventUpdateExerciseLiveDataObserver.value = true
    }

    var exercises = fitBuddyRepository.getExercises()
    val exerciseCategories = fitBuddyRepository.exerciseCategories
    val equipments = fitBuddyRepository.equipments

    var categoryFilterList: MutableList<Int> = mutableListOf()

    override fun onCleared() {
        super.onCleared()
    }

    fun onCategoryFilterChanged(categoryId: Int, checked: Boolean) {
        if (checked) {
            categoryFilterList.add(categoryId)
        } else {
            categoryFilterList.remove(categoryId)
        }

        exercises = if (categoryFilterList.size == 0) {
            fitBuddyRepository.getExercises()
        } else {
            fitBuddyRepository.getExercises(categories = categoryFilterList as List<Int>)
        }
        _eventUpdateExerciseLiveDataObserver.value = true
    }

    fun eventUpdateExerciseLiveDataObserverDone() {
        _eventUpdateExerciseLiveDataObserver.value = false
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
