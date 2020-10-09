package com.hardiksachan.fitbuddy.database

import androidx.lifecycle.Transformations
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.hardiksachan.fitbuddy.domain.*
import com.hardiksachan.fitbuddy.repository.FitBuddyRepository
import java.util.*

@Entity(tableName = "database_exercise")
data class DatabaseExercise constructor(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
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

@Entity(
    tableName = "database_exercise_muscle",
    foreignKeys = [ForeignKey(
        entity = DatabaseExercise::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("exerciseId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class DatabaseExerciseMuscle constructor(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val muscleId: Int,
    val exerciseId: Int,
    val isSecondary: Boolean
)

@Entity(tableName = "database_user")
data class DatabaseUser constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val gender: Int,
    val dob: Date

) {
    fun asDomainModel(): User {
        return User(
            name, gender, dob
        )
    }
}

@Entity(tableName = "database_height")
data class DatabaseHeight constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var height: Int,
    var date: Date
)

@Entity(tableName = "database_weight")
data class DatabaseWeight constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var weight: Float = -1f,
    var date: Date
)

@Entity(tableName = "database_exercise_by_day")
data class DatabaseExerciseByDay constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val day: Int,
    val exerciseId: Int? = null,
    val sets: Int? = null,
    val reps: Int? = null
) {
    fun asDomainModel(repository: FitBuddyRepository): ExerciseDay {
        return ExerciseDay(
            exercise = Transformations.map(repository.getExerciseFromId(exerciseId ?: -255)) {
                listOf(it).asDomainModel(repository)[0]
            },
            id = id,
            sets = sets ?: 0,
            reps = reps ?: 0,
            day = day
        )
    }
}

// Also used as Domain Model
@Entity(tableName = "database_sleep")
data class SleepNight constructor(
    @PrimaryKey(autoGenerate = true)
    var nightId: Long = 0L,
    val startTimeMilli: Long = System.currentTimeMillis(),
    var endTimeMilli: Long = startTimeMilli,
    var sleepQuality: Int = -1
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

@JvmName("asDatabaseModelHeight")
fun List<DatabaseHeight>.asDomainModel(): List<Height> {
    return map {
        Height(
            height = it.height,
            date = it.date
        )
    }
}


fun List<DatabaseWeight>.asDomainModel(): List<Weight> {
    return map {
        Weight(
            weight = it.weight,
            date = it.date
        )
    }
}

