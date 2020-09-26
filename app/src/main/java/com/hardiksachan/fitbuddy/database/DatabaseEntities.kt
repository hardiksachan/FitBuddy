package com.hardiksachan.fitbuddy.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.hardiksachan.fitbuddy.domain.Exercise

@Entity(tableName = "database_exercise")
data class DatabaseExercise constructor(
    @PrimaryKey
    var id: Int,
    val license_author: String,
    val status: String,
    val description: String,
    val name: String,
    val category: Int,
    val language: Int,
    val muscles: MutableList<Int>,
    val muscles_secondary: MutableList<Int>,
    val equipment: MutableList<Int>
)

fun List<DatabaseExercise>.asDomainModel(): List<Exercise> {
    return map {
        Exercise(
            id = it.id,
            license_author = it.license_author,
            status = it.status,
            description = it.description,
            name = it.name,
            category = it.category,
            language = it.language,
            muscles = it.muscles,
            muscles_secondary = it.muscles_secondary,
            equipment = it.equipment
        )
    }
}