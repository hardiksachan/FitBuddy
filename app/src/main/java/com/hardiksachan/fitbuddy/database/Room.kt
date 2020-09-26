package com.hardiksachan.fitbuddy.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.Deferred

private const val DATABASE_VERSION = 5

@Dao
interface ExerciseDao {
    @Query("SELECT * from database_exercise")
    fun getExercises(): LiveData<List<DatabaseExercise>>

    @Query("SELECT * from database_exercise WHERE category IN (:categories)")
    fun getExercises(categories: List<Int>): LiveData<List<DatabaseExercise>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllExercises(vararg exercises: DatabaseExercise)

    @Query("SELECT * from database_exercise_category")
    fun getExerciseCategories(): LiveData<List<DatabaseExerciseCategory>>

    @Query("SELECT name from database_exercise_category WHERE id = :id")
    fun getExerciseCategoryNameFromId(id: Int): LiveData<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllExerciseCategories(vararg exercises: DatabaseExerciseCategory)
}

@Database(
    entities = [DatabaseExercise::class, DatabaseExerciseCategory::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
@TypeConverters(ListConverter::class)
abstract class ExerciseDatabase : RoomDatabase() {
    abstract val exerciseDao: ExerciseDao
}

private lateinit var INSTANCE: ExerciseDatabase

fun getDatabase(context: Context): ExerciseDatabase {
    synchronized(ExerciseDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                ExerciseDatabase::class.java,
                "fit_buddy_db"
            ).fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}