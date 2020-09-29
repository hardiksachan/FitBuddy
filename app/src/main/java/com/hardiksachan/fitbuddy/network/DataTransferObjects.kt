package com.hardiksachan.fitbuddy.network

import com.hardiksachan.fitbuddy.database.DatabaseEquipment
import com.hardiksachan.fitbuddy.database.DatabaseExercise
import com.hardiksachan.fitbuddy.database.DatabaseExerciseCategory
import com.hardiksachan.fitbuddy.database.DatabaseMuscle
import com.hardiksachan.fitbuddy.repository.FitBuddyRepository
import com.squareup.moshi.Json


data class ListResponse<T>(
    @Json(name = "count")
    var count: Int? = null,

    @Json(name = "next")
    var next: String? = null,

    @Json(name = "previous")
    var previous: Any? = null,

    @Json(name = "results")
    var results: List<T>? = null
)

data class NetworkExercise(
    @Json(name = "id")
    var id: Int? = null,

    @Json(name = "license_author")
    var licenseAuthor: String? = null,

    @Json(name = "status")
    var status: String? = null,

    @Json(name = "description")
    var description: String? = null,

    @Json(name = "name")
    var name: String? = null,

    @Json(name = "name_original")
    var nameOriginal: String? = null,

    @Json(name = "creation_date")
    var creationDate: String? = null,

    @Json(name = "uuid")
    var uuid: String? = null,

    @Json(name = "license")
    var license: Int? = null,

    @Json(name = "category")
    var category: Int? = null,

    @Json(name = "language")
    var language: Int? = null,

    @Json(name = "muscles")
    var muscles: List<Int>? = null,

    @Json(name = "muscles_secondary")
    var musclesSecondary: List<Int>? = null,

    @Json(name = "equipment")
    var equipment: List<Int>? = null
)

data class NetworkExerciseCategory(
    @Json(name = "id")
    var id: Int? = null,

    @Json(name = "name")
    var name: String? = null
)

data class NetworkEquipment(
    @Json(name = "id")
    var id: Int? = null,

    @Json(name = "name")
    var name: String? = null
)

data class NetworkMuscle(
    @Json(name = "id")
    var id: Int? = null,

    @Json(name = "name")
    var name: String? = null,

    @Json(name = "is_front")
    var isFront: Boolean? = null
)

suspend fun List<NetworkExercise>.asDatabaseModel(repository: FitBuddyRepository): Array<DatabaseExercise> {
    return map {
        repository.deleteAllEquipmentsOfExercise(it.id ?: throw Exception("Exercise must have Id"))
        repository.saveExerciseEquipment(it.equipment, it.id)

        repository.deleteAllMusclesOfExercise(it.id ?: throw Exception("Exercise must have Id"))
        repository.saveExerciseMuscle(it.muscles, it.id, false)
        repository.saveExerciseMuscle(it.musclesSecondary, it.id, true)

        DatabaseExercise(
            id = it.id ?: throw Exception("Exercise must have Id"),
            license_author = it.licenseAuthor ?: "",
            status = it.status ?: "",
            description = it.description ?: "",
            name = it.name ?: "",
            category = it.category ?: -1,
            language = it.language ?: -1
        )

    }.toTypedArray()
}

fun List<NetworkExerciseCategory>.asDatabaseModel(): Array<DatabaseExerciseCategory> {
    return map {
        DatabaseExerciseCategory(
            id = it.id ?: throw Exception("Exercise Category must have Id"),
            name = it.name ?: "",
        )
    }.toTypedArray()
}

@JvmName("asDatabaseModelNetworkEquipment")
fun List<NetworkEquipment>.asDatabaseModel(): Array<DatabaseEquipment> {
    return map {
        DatabaseEquipment(
            id = it.id ?: throw Exception("Equipment must have Id"),
            name = it.name ?: "",
        )
    }.toTypedArray()
}

@JvmName("asDatabaseModelNetworkMuscle")
fun List<NetworkMuscle>.asDatabaseModel(): Array<DatabaseMuscle> {
    return map {
        DatabaseMuscle(
            id = it.id ?: throw Exception("Muscle must have Id"),
            name = it.name ?: "",
        )
    }.toTypedArray()
}