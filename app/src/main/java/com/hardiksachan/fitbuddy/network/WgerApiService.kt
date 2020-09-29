package com.hardiksachan.fitbuddy.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://wger.de/api/v2/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build()

interface WgerApiService {
    @GET("exercise/")
    fun getExercises(
        @Query(value = "language", encoded = true) language: Int = 2, // English
        @Query(value = "status", encoded = true) status: Int = 2, // Verified Exercises
        @Query(value = "limit", encoded = true) limit: Int = 20,
        @Query(value = "offset", encoded = true) offset: Int = 40
    ): Deferred<ListResponse<NetworkExercise>>

    @GET("exercisecategory/")
    fun getExerciseCategories() : Deferred<ListResponse<NetworkExerciseCategory>>

    @GET("equipment/")
    fun getEquipments() : Deferred<ListResponse<NetworkEquipment>>

    @GET("muscle/")
    fun getMuscles() : Deferred<ListResponse<NetworkMuscle>>

}

object WgerApi {
    val retrofitService: WgerApiService by lazy {
        retrofit.create(WgerApiService::class.java)
    }
}