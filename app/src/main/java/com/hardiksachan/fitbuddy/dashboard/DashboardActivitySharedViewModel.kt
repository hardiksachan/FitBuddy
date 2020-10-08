package com.hardiksachan.fitbuddy.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hardiksachan.fitbuddy.domain.Exercise
import com.hardiksachan.fitbuddy.exerciseselectionfragmentsactivity.MainActivitySharedViewModel

class DashboardActivitySharedViewModel(application: Application) : AndroidViewModel(application) {
    var exercise: Exercise? = null

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DashboardActivitySharedViewModel::class.java)) {
                return DashboardActivitySharedViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}