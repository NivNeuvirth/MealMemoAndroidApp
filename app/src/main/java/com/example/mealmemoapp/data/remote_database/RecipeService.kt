package com.example.mealmemoapp.data.remote_database

import com.example.mealmemoapp.data.models.Recipe
import com.example.mealmemoapp.data.models.RecipeIdSummary
import com.example.mealmemoapp.data.models.RecipeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeService {

    @GET("recipes/{id}/information")
    suspend fun getRecipe(
        @Path("id")id :Int,
        @Query("apiKey")apiKey: String = API_KEY
    ) : Response<Recipe>

    @GET("recipes/findByIngredients")
    suspend fun getRecipesByIngredients(
        @Query("ingredients") ingredients: String,
        @Query("apiKey") apiKey: String = API_KEY,
        @Query("number") number: Int = 5
    ): List<RecipeIdSummary>

    companion object {
        private const val API_KEY = "56fddadda1b54080beddacc520a22753" // Replace with your actual Spoonacular API key
    }
}