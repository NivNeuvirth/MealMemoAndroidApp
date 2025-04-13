package com.example.mealmemoapp.data.remote.api

import com.example.mealmemoapp.data.models.Recipe
import com.example.mealmemoapp.data.models.RecipeIdSummary
import com.example.mealmemoapp.utilities.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {

    @GET("recipes/{id}/information")
    suspend fun getRecipe(
        @Path("id")id :Int,
        @Query("apiKey")apiKey: String = Constants.SPOONACULAR_API_KEY
    ) : Response<Recipe>

    @GET("recipes/findByIngredients")
    suspend fun getRecipesByIngredients(
        @Query("ingredients") ingredients: String,
        @Query("apiKey") apiKey: String = Constants.SPOONACULAR_API_KEY,
        @Query("number") number: Int = 5
    ): List<RecipeIdSummary>
}