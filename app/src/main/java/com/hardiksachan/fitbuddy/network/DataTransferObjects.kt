package com.hardiksachan.fitbuddy.network

import com.hardiksachan.fitbuddy.database.DatabaseExercise
import com.squareup.moshi.Json
import java.lang.Exception


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

fun List<NetworkExercise>.asDatabaseModel() : Array<DatabaseExercise>{
    return map{
        DatabaseExercise(
            id = it.id ?: throw Exception("Exercise must have Id"),
            license_author = it.licenseAuthor ?: "",
            status = it.status ?: "",
            description = it.description ?: "",
            name = it.name ?: "",
            category = it.category ?: -1,
            language = it.language ?: -1,
            muscles = it.muscles?.toMutableList() ?: arrayListOf(),
            muscles_secondary = it.musclesSecondary?.toMutableList() ?: arrayListOf(),
            equipment = it.equipment?.toMutableList() ?: arrayListOf()
        )
    }.toTypedArray()
}