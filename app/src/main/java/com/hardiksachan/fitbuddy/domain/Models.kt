package com.hardiksachan.fitbuddy.domain

import androidx.lifecycle.LiveData
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hardiksachan.fitbuddy.database.DatabaseExercise
import com.hardiksachan.fitbuddy.database.DatabaseHeight
import com.hardiksachan.fitbuddy.database.DatabaseUser
import com.hardiksachan.fitbuddy.database.DatabaseWeight
import com.hardiksachan.fitbuddy.repository.FitBuddyRepository
import java.util.*


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


data class User constructor(
    var name: String = "",
    var gender: Int = -1,
    var dob: Date = Date()
) {
    fun asDatabaseModel(): DatabaseUser {
        return DatabaseUser(
            name = name,
            gender = gender,
            dob = dob
        )
    }
}


data class Height constructor(
    var height: Int,
    var date: Date = Date()
)

data class Weight constructor(
    var weight: Float = -1f,
    var date: Date = Date()
)

@JvmName("asDatabaseModelHeight")
fun List<Height>.asDatabaseModel() : List<DatabaseHeight> {
    return map {
        DatabaseHeight(
            height = it.height,
            date = it.date
        )
    }
}


fun List<Weight>.asDatabaseModel() : List<DatabaseWeight> {
    return map {
        DatabaseWeight(
            weight = it.weight,
            date = it.date
        )
    }
}