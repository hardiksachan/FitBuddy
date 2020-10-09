package com.hardiksachan.fitbuddy.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hardiksachan.fitbuddy.domain.Exercise
import com.hardiksachan.fitbuddy.domain.ExerciseDay
import com.hardiksachan.fitbuddy.exerciseselectionfragmentsactivity.MainActivitySharedViewModel
import com.hardiksachan.fitbuddy.repository.FitBuddyRepository
import kotlinx.coroutines.launch

class DashboardActivitySharedViewModel(application: Application) : AndroidViewModel(application) {
    var exercise: Exercise? = null

    private val fitBuddyRepository = FitBuddyRepository(application)

    val weightList = fitBuddyRepository.weightList
    val heightList = fitBuddyRepository.heightList

    fun updateExerciseSetsReps(exerciseDay: ExerciseDay) {
        viewModelScope.launch {
            fitBuddyRepository.updateExerciseDay(exerciseDay)
        }
    }

    fun deleteExerciseDay(exerciseDayId: Int) {
        viewModelScope.launch {
            fitBuddyRepository.deleteExerciseDay(exerciseDayId)
        }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DashboardActivitySharedViewModel::class.java)) {
                return DashboardActivitySharedViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}