package com.hardiksachan.fitbuddy.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

private const val DATABASE_VERSION = 4

@Dao
interface ExerciseDao {
    @Query("SELECT * from database_exercise")
    fun getExercises(): LiveData<List<DatabaseExercise>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg exercises: DatabaseExercise)
}

@Database(entities = [DatabaseExercise::class], version = DATABASE_VERSION, exportSchema = false)
@TypeConverters(ListConverter::class)
abstract class ExerciseDatabase : RoomDatabase() {
    abstract val exerciseDao: ExerciseDao
}

private lateinit var INSTANCE: ExerciseDatabase

fun getDatabase(context: Context) : ExerciseDatabase {
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