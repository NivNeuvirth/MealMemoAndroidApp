package com.example.mealmemoapp.data.remote_database

import com.example.mealmemoapp.data.models.Recipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeService {

    @GET("recipes/{id}")
    suspend fun getRecipe(@Path("id")id :Int) : Response<Recipe>
}