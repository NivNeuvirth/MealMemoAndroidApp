package com.example.mealmemoapp.data.remote.api

import com.example.mealmemoapp.data.models.TranslatorRequest
import com.example.mealmemoapp.data.models.TranslatorResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface TranslatorApiService {
    @Headers("Content-Type: application/json")
    @POST("translate?api-version=3.0")
    fun translateText(
        @Header("Ocp-Apim-Subscription-Key") apiKey: String,
        @Header("Ocp-Apim-Subscription-Region") region: String,
        @Body request: List<TranslatorRequest>,
        @Query("from") fromLanguage: String,
        @Query("to") toLanguage: String
    ): Call<List<TranslatorResponse>>
}