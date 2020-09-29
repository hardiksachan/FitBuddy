package com.hardiksachan.fitbuddy.domain

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hardiksachan.fitbuddy.database.getDatabase
import com.hardiksachan.fitbuddy.repository.FitBuddyRepository


data class Exercise constructor(
    val id: Int,
    val license_author: String,
    val status: String,
    val description: String,
    val name: String,
    val category: Int,
    val language: Int,
    val muscles: LiveData<List<Muscle>>,
    val muscles_secondary: LiveData<List<Muscle>>,
    val equipment: LiveData<List<Equipment>>
)

data class ExerciseCategory constructor(
    val id: Int,
    val name: String
)

data class Equipment constructor(
    val id: Int,
    val name: String
)

data class Muscle constructor(
    val id: Int,
    val name: String
)