package com.example.mealmemoapp.data.remote.api

import com.example.mealmemoapp.data.models.PlacesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApiService {
    @GET("place/nearbysearch/json")
    suspend fun getNearbyPlaces(
        @Query("location")location: String,
        @Query("radius")radius:Int=10000,
        @Query("type")type:String,
        @Query("key")apiKey:String)
            :Response<PlacesResponse>

}