package com.hardiksachan.fitbuddy.domain

import androidx.lifecycle.LiveData
import com.hardiksachan.fitbuddy.database.DatabaseExercise
import com.hardiksachan.fitbuddy.repository.FitBuddyRepository


data class Exercise(
    val id: Int = -1,
    val license_author: String,
    val status: String = "3",
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

fun List<Exercise>.asDomainModel(): List<DatabaseExercise> {
    return map {
        DatabaseExercise(
            license_author = it.license_author,
            status = it.status,
            description = it.description,
            name = it.name,
            category = it.category,
            language = it.language
        )
    }
}