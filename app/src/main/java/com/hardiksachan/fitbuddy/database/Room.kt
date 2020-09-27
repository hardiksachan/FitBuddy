package com.hardiksachan.fitbuddy.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

private const val DATABASE_VERSION = 8

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

    @Query("SELECT * from database_equipment")
    fun getEquipments(): LiveData<List<DatabaseEquipment>>

    @Query("SELECT name from database_equipment WHERE id = :id")
    fun getEquipmentNameFromId(id: Int): LiveData<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllEquipments(vararg equipment: DatabaseEquipment)

    @Query("SELECT * from database_equipment WHERE id in (SELECT equipmentId from database_exercise_equipment WHERE exerciseId = :exerciseId )")
    fun getEquipmentsForExercise(exerciseId: Int): LiveData<List<DatabaseEquipment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEquipmentForExercise(vararg equipments : DatabaseExerciseEquipment)

    @Query("DELETE FROM database_exercise_equipment WHERE exerciseId = :exerciseId")
    fun clearEquipmentsForExercise(exerciseId: Int)
}

@Database(
    entities = [DatabaseExercise::class,
        DatabaseExerciseCategory::class,
        DatabaseEquipment::class,
        DatabaseExerciseEquipment::class],
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