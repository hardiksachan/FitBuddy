package com.hardiksachan.fitbuddy.firstRun

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hardiksachan.fitbuddy.domain.Height
import com.hardiksachan.fitbuddy.domain.User
import com.hardiksachan.fitbuddy.domain.Weight
import com.hardiksachan.fitbuddy.repository.FitBuddyRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*

class FirstRunActivitySharedViewModel(application: Application) : AndroidViewModel(application) {

    val user = User()

    private val fitBuddyRepository = FitBuddyRepository(application)

    fun saveUser(){
        viewModelScope.launch {
            fitBuddyRepository.insertUser(user)
        }
    }

    fun saveHeight(h: List<Int>){
        viewModelScope.launch {
            fitBuddyRepository.insertHeights(h.map {
                Height(
                    height = it,
                    date = Calendar.getInstance().time
                )
            })
        }
    }

    fun saveWeight(w: List<Int>){
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
            if (modelClass.isAssignableFrom(FirstRunActivitySharedViewModel::class.java)) {
                return FirstRunActivitySharedViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}