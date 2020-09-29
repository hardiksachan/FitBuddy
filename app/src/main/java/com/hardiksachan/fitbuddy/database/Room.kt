package com.hardiksachan.fitbuddy.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

private const val DATABASE_VERSION = 11

private const val SEARCH_QUERY =
    "(name LIKE :searchText OR category = (SELECT id from database_exercise_category WHERE name LIKE :searchText))"

@Dao
interface ExerciseDao {

    @Query("SELECT * from database_exercise WHERE $SEARCH_QUERY")
    fun getExercises(searchText: String): LiveData<List<DatabaseExercise>>

    @Query("SELECT * from database_exercise WHERE category IN (:categories) AND $SEARCH_QUERY")
    fun getExercises(categories: List<Int>, searchText: String): LiveData<List<DatabaseExercise>>


    @Query("SELECT ex.id, ex.name, ex.category, ex.description, ex.language, ex.license_author,ex.status from database_exercise as ex JOIN database_exercise_equipment as eq WHERE ex.id = eq.exerciseId AND eq.equipmentId IN (:equipments) AND $SEARCH_QUERY")
    fun getExercisesUsingEquipment(
        equipments: List<Int>,
        searchText: String
    ): LiveData<List<DatabaseExercise>>

    @Query("SELECT ex.id, ex.name, ex.category, ex.description, ex.language, ex.license_author,  ex.status from database_exercise as ex JOIN database_exercise_equipment as eq WHERE ex.id = eq.exerciseId AND eq.equipmentId IN (:equipments) AND ex.category IN (:categories) AND $SEARCH_QUERY")
    fun getExercises(
        equipments: List<Int>,
        categories: List<Int>,
        searchText: String
    ): LiveData<List<DatabaseExercise>>


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
    fun insertEquipmentForExercise(vararg equipments: DatabaseExerciseEquipment)

    @Query("DELETE FROM database_exercise_equipment WHERE exerciseId = :exerciseId")
    fun clearEquipmentsForExercise(exerciseId: Int)

    @Query("SELECT * from database_muscle")
    fun getMuscles(): LiveData<List<DatabaseMuscle>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllMuscles(vararg muscle: DatabaseMuscle)

    @Query("SELECT * from database_muscle WHERE id in (SELECT muscleId from database_exercise_muscle WHERE exerciseId = :exerciseId AND isSecondary = :isSecondary ) ")
    fun getMusclesForExercise(exerciseId: Int, isSecondary: Boolean): LiveData<List<DatabaseMuscle>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMuscleForExercise(vararg muscle: DatabaseExerciseMuscle)

    @Query("DELETE FROM database_exercise_muscle WHERE exerciseId = :exerciseId")
    fun clearMusclesForExercise(exerciseId: Int)
}

@Database(
    entities = [
        DatabaseExercise::class,
        DatabaseExerciseCategory::class,
        DatabaseEquipment::class,
        DatabaseExerciseEquipment::class,
        DatabaseMuscle::class,
        DatabaseExerciseMuscle::class
    ],
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