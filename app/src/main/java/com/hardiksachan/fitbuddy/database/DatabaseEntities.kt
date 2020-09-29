package com.hardiksachan.fitbuddy.database

import androidx.lifecycle.Transformations
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.hardiksachan.fitbuddy.domain.Equipment
import com.hardiksachan.fitbuddy.domain.Exercise
import com.hardiksachan.fitbuddy.domain.ExerciseCategory
import com.hardiksachan.fitbuddy.domain.Muscle
import com.hardiksachan.fitbuddy.repository.FitBuddyRepository

@Entity(tableName = "database_exercise")
data class DatabaseExercise constructor(
    @PrimaryKey
    var id: Int,
    val license_author: String,
    val status: String,
    val description: String,
    val name: String,
    val category: Int,
    val language: Int
)

@Entity(tableName = "database_exercise_category")
data class DatabaseExerciseCategory constructor(
    @PrimaryKey
    val id: Int,
    val name: String
)

@Entity(tableName = "database_equipment")
data class DatabaseEquipment constructor(
    @PrimaryKey
    val id: Int,
    val name: String
)

@Entity(tableName = "database_muscle")
data class DatabaseMuscle constructor(
    @PrimaryKey
    val id: Int,
    val name: String
)

@Entity(
    tableName = "database_exercise_equipment",
    foreignKeys = [ForeignKey(
        entity = DatabaseExercise::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("exerciseId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class DatabaseExerciseEquipment constructor(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val equipmentId: Int,
    val exerciseId: Int
)

@Entity(tableName = "database_exercise_muscle",
    foreignKeys = [ForeignKey(
        entity = DatabaseExercise::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("exerciseId"),
        onDelete = ForeignKey.CASCADE
    )])
data class DatabaseExerciseMuscle constructor(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val muscleId: Int,
    val exerciseId: Int,
    val isSecondary: Boolean
)

@JvmName("asDomainModelDatabaseExercise")
fun List<DatabaseExercise>.asDomainModel(repository: FitBuddyRepository): List<Exercise> {
    return map {
        Exercise(
            id = it.id,
            license_author = it.license_author,
            status = it.status,
            description = it.description,
            name = it.name,
            category = it.category,
            language = it.language,
            muscles = Transformations.map(repository.getPrimaryMuscleFromExercise(it.id)) { dbMuscleList ->
                dbMuscleList.asDomainModel()
            },
            muscles_secondary = Transformations.map(repository.getSecondaryMuscleFromExercise(it.id)) { dbMuscleList ->
                dbMuscleList.asDomainModel()
            },
            equipment = Transformations.map(repository.getEquipmentsFromExercise(it.id)) { dbEquipmentList ->
                dbEquipmentList.asDomainModel()
            }
        )
    }
}

@JvmName("asDomainModelDatabaseExerciseCategory")
fun List<DatabaseExerciseCategory>.asDomainModel(): List<ExerciseCategory> {
    return map {
        ExerciseCategory(
            id = it.id,
            name = it.name
        )
    }
}

@JvmName("asDomainModelDatabaseEquipment")
fun List<DatabaseEquipment>.asDomainModel(): List<Equipment> {
    return map {
        Equipment(
            id = it.id,
            name = it.name
        )
    }
}


@JvmName("asDomainModelDatabaseMuscle")
fun List<DatabaseMuscle>.asDomainModel(): List<Muscle> {
    return map {
        Muscle(
            id = it.id,
            name = it.name
        )
    }
}

