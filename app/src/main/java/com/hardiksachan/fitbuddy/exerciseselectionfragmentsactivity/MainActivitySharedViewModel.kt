package com.hardiksachan.fitbuddy.exerciseselectionfragmentsactivity

import android.app.Application
import androidx.lifecycle.*
import com.hardiksachan.fitbuddy.domain.Exercise
import com.hardiksachan.fitbuddy.exerciseselectionfragmentsactivity.exerciseselector.exercisefilter.FilterByWhat
import com.hardiksachan.fitbuddy.repository.FitBuddyRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivitySharedViewModel(application: Application) : AndroidViewModel(application) {

    private val _eventUpdateExerciseLiveDataObserver = MutableLiveData<Boolean>()
    val eventUpdateExerciseLiveDataObserver: LiveData<Boolean>
        get() = _eventUpdateExerciseLiveDataObserver

    private val fitBuddyRepository = FitBuddyRepository(application)

    private var exerciseToDisplayOnDetail: Exercise? = null

    var categoryFilterList: MutableList<Int> = mutableListOf()
    var equipmentFilterList: MutableList<Int> = mutableListOf()
    var primaryMuscleFilterList: MutableList<Int> = mutableListOf()
    var secondaryMuscleFilterList: MutableList<Int> = mutableListOf()
    var searchQuery: String? = "%"

    var exercises = fitBuddyRepository.getExercises()
    val exerciseCategories = fitBuddyRepository.exerciseCategories
    val equipments = fitBuddyRepository.equipments
    val muscles = fitBuddyRepository.muscles

//    val repositoryUpdated = fitBuddyRepository.repositoryUpdated
//
//    fun repositoryUpdateHandled() = fitBuddyRepository.onRepositoryUpdatedHandled()

    init {
        viewModelScope.launch {
            fitBuddyRepository.refreshMuscles()
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

    fun onprimaryMuscleFilterChanged(muscleId: Int, checked: Boolean) {
        if (checked) {
            primaryMuscleFilterList.add(muscleId)
        } else {
            primaryMuscleFilterList.remove(muscleId)
        }

        updateExercises()

    }

    fun onSecndaryMuscleFilterChanged(muscleId: Int, checked: Boolean) {
        if (checked) {
            secondaryMuscleFilterList.add(muscleId)
        } else {
            secondaryMuscleFilterList.remove(muscleId)
        }

        updateExercises()

    }

    fun updateExercises() {

        exercises = fitBuddyRepository.getExercises(
            categories = categoryFilterList as List<Int>,
            equipments = equipmentFilterList as List<Int>,
            primaryMuscles = primaryMuscleFilterList as List<Int>,
            secondaryMuscles = secondaryMuscleFilterList as List<Int>,
            searchQuery = searchQuery
        )

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

    fun getActiveFilters(filterByWhat: FilterByWhat?): Int {
        return if (filterByWhat == null) {
            (equipmentFilterList.size +
                    categoryFilterList.size + primaryMuscleFilterList.size + secondaryMuscleFilterList.size)
        } else {
            when (filterByWhat) {
                FilterByWhat.Equipment -> equipmentFilterList.size
                FilterByWhat.Category -> categoryFilterList.size
                FilterByWhat.PrimaryMuscle -> primaryMuscleFilterList.size
                FilterByWhat.SecondaryMuscle -> secondaryMuscleFilterList.size
            }
        }
    }

    fun clearFilters(filterByWhat: FilterByWhat?) {
        if (filterByWhat == null) {
            equipmentFilterList.clear()
            categoryFilterList.clear()
            primaryMuscleFilterList.clear()
            secondaryMuscleFilterList.clear()
        } else {
            when (filterByWhat) {
                FilterByWhat.Equipment -> equipmentFilterList.clear()
                FilterByWhat.Category -> categoryFilterList.clear()
                FilterByWhat.PrimaryMuscle -> primaryMuscleFilterList.clear()
                FilterByWhat.SecondaryMuscle -> secondaryMuscleFilterList.clear()
            }
        }
        updateExercises()
    }

    fun searchFor(newSearchQuery: String?) {
        if (newSearchQuery == null) searchQuery = "%"
        else searchQuery = "%$newSearchQuery%"
        updateExercises()
    }

    fun getExerciseToDisplayOnDetail(): Exercise {
        return exerciseToDisplayOnDetail ?: throw Exception("exercise Must Be selected")
    }

    fun setExerciseToDisplayOnDetail(e: Exercise) {
        exerciseToDisplayOnDetail = e
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