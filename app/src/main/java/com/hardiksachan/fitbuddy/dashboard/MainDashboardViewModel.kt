package com.hardiksachan.fitbuddy.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hardiksachan.fitbuddy.domain.Height
import com.hardiksachan.fitbuddy.domain.Weight
import com.hardiksachan.fitbuddy.repository.FitBuddyRepository
import kotlinx.coroutines.launch
import java.util.*

class MainDashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val fitBuddyRepository = FitBuddyRepository(application)

    val user = fitBuddyRepository.user
    val weightList = fitBuddyRepository.weightList
    val currentWeight = fitBuddyRepository.currentWeight
    val heightList = fitBuddyRepository.heightList
    val currentHeight = fitBuddyRepository.currentHeight


    fun updateHeight(h: List<Int>){
        viewModelScope.launch {
            fitBuddyRepository.insertHeights(h.map {
                Height(
                    height = it,
                    date = Calendar.getInstance().time
                )
            })
        }
    }

    fun updateWeight(w: List<Float>){
        viewModelScope.launch {
            fitBuddyRepository.insertWeights(w.map {
                Weight(
                    weight = it,
                    date = Calendar.getInstance().time
                )
            })
        }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainDashboardViewModel::class.java)) {
                return MainDashboardViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}