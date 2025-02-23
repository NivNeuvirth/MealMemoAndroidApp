package com.example.mealmemoapp.data.remote.api

import com.example.mealmemoapp.data.models.TranslatorRequest
import com.example.mealmemoapp.data.models.TranslatorResponse
import com.example.mealmemoapp.data.models.Translation
import com.example.mealmemoapp.utilities.Constants
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface TranslatorApiService {
    @Headers("Content-Type: application/json")
    @POST("translate?api-version=3.0&from=en&to=he")
    fun translateText(
        @Header("Ocp-Apim-Subscription-Key") apiKey: String,
        @Header("Ocp-Apim-Subscription-Region") region: String,
        @Body request: List<TranslatorRequest>
    ): Call<List<TranslatorResponse>>
}