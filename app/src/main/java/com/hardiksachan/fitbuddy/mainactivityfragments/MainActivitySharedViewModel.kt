package com.hardiksachan.fitbuddy.mainactivityfragments

import android.app.Application
import androidx.lifecycle.*
import com.hardiksachan.fitbuddy.mainactivityfragments.exerciseselector.ExerciseSelectorViewModel
import com.hardiksachan.fitbuddy.repository.FitBuddyRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivitySharedViewModel(application: Application) : AndroidViewModel(application){

    private val _eventUpdateExerciseLiveDataObserver = MutableLiveData<Boolean>()
    val eventUpdateExerciseLiveDataObserver: LiveData<Boolean>
        get() = _eventUpdateExerciseLiveDataObserver

    private val fitBuddyRepository = FitBuddyRepository(application)

    var categoryFilterList: MutableList<Int> = mutableListOf()
    var equipmentFilterList: MutableList<Int> = mutableListOf()

    var exercises = fitBuddyRepository.getExercises()
    val exerciseCategories = fitBuddyRepository.exerciseCategories
    val equipments = fitBuddyRepository.equipments

    init {
        viewModelScope.launch {
            fitBuddyRepository.refreshEquipments()
            fitBuddyRepository.refreshExerciseCategories()
            fitBuddyRepository.refreshExercises()
        }
        _eventUpdateExerciseLiveDataObserver.value = true
    }

    fun onCategoryFilterChanged(categoryId: Int, checked: Boolean) {
        if (checked) {
            categoryFilterList.add(categoryId)
        } else {
            categoryFilterList.remove(categoryId)
        }

        updateExercises()

    }

    private fun updateExercises() {
        exercises = if (categoryFilterList.size == 0 && equipmentFilterList.size == 0) {
            fitBuddyRepository.getExercises()
        } else if (categoryFilterList.size != 0 && equipmentFilterList.size == 0) {
            fitBuddyRepository.getExercises(categories = categoryFilterList as List<Int>)
        } else if (categoryFilterList.size == 0 && equipmentFilterList.size != 0){
            fitBuddyRepository.getExercises(equipments = equipmentFilterList as List<Int>)
        } else {
            fitBuddyRepository.getExercises(categories = categoryFilterList, equipments = equipmentFilterList as List<Int>)
        }
        _eventUpdateExerciseLiveDataObserver.value = true
    }

    fun onEquipmentFilterChanged(equipmentId: Int, checked: Boolean) {
        if (checked) {
            equipmentFilterList.add(equipmentId)
        } else {
            equipmentFilterList.remove(equipmentId)
        }

        Timber.i("Equipment filter changed: $equipmentId was checked to $checked")
        Timber.i("new Equipment Filter List: $equipmentFilterList")

        updateExercises()

    }

    fun eventUpdateExerciseLiveDataObserverDone() {
        _eventUpdateExerciseLiveDataObserver.value = false
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainActivitySharedViewModel::class.java)) {
                return MainActivitySharedViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}