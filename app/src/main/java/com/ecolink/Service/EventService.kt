package com.ecolink.Service

import com.ecolink.modéle.Event
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import android.util.Log
import com.ecolink.modéle.EventResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path


interface EventService {
    @GET("evenements")
    suspend fun getEvents(): EventResponse
    @GET("evenements/{eventId}")
    suspend fun getEventDetails(@Path("eventId") eventId: String): Event
    @GET("evenement/allLikedEvenement")
    suspend fun getEvents2(): EventResponse
}






object RetrofitInstance {
    private const val BASE_URL = "https://ecolink.onrender.com/api/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val eventService: EventService by lazy {
        retrofit.create(EventService::class.java)
    }

    init {
        // Log pour indiquer que l'instance Retrofit est créée
        Log.d("RetrofitInstance", "Instance Retrofit créée avec succès")
    }
}
