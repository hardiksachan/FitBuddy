package com.hardiksachan.fitbuddy.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

private const val DATABASE_VERSION = 18

private const val SEARCH_QUERY =
    "(name LIKE :searchText OR category = (SELECT id from database_exercise_category WHERE name LIKE :searchText)) ORDER BY name"

@Dao
interface ExerciseDao {

    @Query(
        """SELECT *
        from database_exercise as ex
        WHERE 
        EXISTS (SELECT eq.equipmentId FROM database_exercise_equipment as eq WHERE (eq.equipmentId IN (:equipments) OR -255 in (:equipments)) AND eq.exerciseId = ex.id)
        AND (ex.category IN (:categories) OR -255 in (:categories) )
        AND EXISTS (SELECT mu.muscleId FROM database_exercise_muscle as mu WHERE (mu.muscleId IN (:primaryMuscles) OR -255 in (:primaryMuscles)) AND mu.exerciseId = ex.id AND mu.isSecondary = 0)
        AND EXISTS (SELECT mu.muscleId FROM database_exercise_muscle as mu WHERE (mu.muscleId IN (:secondaryMuscles) OR -255 in (:primaryMuscles)) AND mu.exerciseId = ex.id AND mu.isSecondary = 1)
        AND $SEARCH_QUERY"""
    )
    fun getExercises(
        equipments: List<Int>,
        categories: List<Int>,
        primaryMuscles: List<Int>,
        secondaryMuscles: List<Int>,
        searchText: String
    ): LiveData<List<DatabaseExercise>>

    @Query("SELECT * FROM database_exercise WHERE id = :id")
    fun getExerciseFromId(id: Int): LiveData<DatabaseExercise>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllExercises(vararg exercises: DatabaseExercise)

    @Query("SELECT * from database_exercise_category")
    fun getExerciseCategories(): LiveData<List<DatabaseExerciseCategory>>

    @Query("SELECT id FROM database_exercise_category")
    fun getExerciseCategoriesIds(): List<Int>

    @Query("SELECT name from database_exercise_category WHERE id = :id")
    fun getExerciseCategoryNameFromId(id: Int): LiveData<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllExerciseCategories(vararg exercises: DatabaseExerciseCategory)

    @Query("SELECT * from database_equipment")
    fun getEquipments(): LiveData<List<DatabaseEquipment>>

    @Query("SELECT id FROM database_equipment")
    fun getEquipmentIds(): List<Int>

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

    @Query("SELECT id FROM database_muscle")
    fun getMuscleIds(): List<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllMuscles(vararg muscle: DatabaseMuscle)

    @Query("SELECT * from database_muscle WHERE id in (SELECT muscleId from database_exercise_muscle WHERE exerciseId = :exerciseId AND isSecondary = :isSecondary ) ")
    fun getMusclesForExercise(exerciseId: Int, isSecondary: Boolean): LiveData<List<DatabaseMuscle>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMuscleForExercise(vararg muscle: DatabaseExerciseMuscle)

    @Query("DELETE FROM database_exercise_muscle WHERE exerciseId = :exerciseId")
    fun clearMusclesForExercise(exerciseId: Int)

    @Query("SELECT * from database_user ORDER BY id DESC LIMIT 1")
    fun getUser(): LiveData<DatabaseUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: DatabaseUser)

    @Query("DELETE FROM database_user")
    fun clearAllUsers()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHeight(vararg height: DatabaseHeight)

    @Query("SELECT * FROM database_height")
    fun getHeightList(): LiveData<List<DatabaseHeight>>

    @Query("SELECT height FROM database_height ORDER BY id DESC LIMIT 1")
    fun getCurrentHeight(): LiveData<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeight(vararg weight: DatabaseWeight)

    @Query("SELECT * FROM database_weight")
    fun getWeightList(): LiveData<List<DatabaseWeight>>

    @Query("SELECT weight FROM database_weight ORDER BY id DESC LIMIT 1")
    fun getCurrentWeight(): LiveData<Float>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExerciseDayByDay(exerciseByDay: DatabaseExerciseByDay)

    @Query("SELECT * FROM database_exercise_by_day WHERE day = :day")
    fun getExerciseByDay(day: Int): LiveData<List<DatabaseExerciseByDay>>

    @Query("SELECT exerciseId FROM database_exercise_by_day WHERE day = :day")
    fun getExerciseIdListOfDay(day: Int) : LiveData<List<Int>>

    @Update
    fun updateExerciseByDat(exerciseByDay: DatabaseExerciseByDay)

    @Query("DELETE FROM database_exercise_by_day WHERE id = :id")
    fun deleteExerciseDaybyId(id: Int)
}

@Database(
    entities = [
        DatabaseExercise::class,
        DatabaseExerciseCategory::class,
        DatabaseEquipment::class,
        DatabaseExerciseEquipment::class,
        DatabaseMuscle::class,
        DatabaseExerciseMuscle::class,
        DatabaseUser::class,
        DatabaseHeight::class,
        DatabaseWeight::class,
        DatabaseExerciseByDay::class
    ],
    version = DATABASE_VERSION,
    exportSchema = false
)
@TypeConverters(ListConverter::class, DateConverter::class)
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
                "fit_buddy_db.db"
            ).fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}