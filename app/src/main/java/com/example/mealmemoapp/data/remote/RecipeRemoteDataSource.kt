package com.example.mealmemoapp.data.remote

import com.example.mealmemoapp.data.models.Recipe
import com.example.mealmemoapp.data.models.RecipeIdSummary
import com.example.mealmemoapp.data.remote.api.RecipeApiService
import retrofit2.Response
import javax.inject.Inject

class RecipeRemoteDataSource @Inject constructor(
    private val recipeService: RecipeApiService
)   : ApiResponseHandler() {

    suspend fun getRecipe(id : Int) = fetchResult { recipeService.getRecipe(id) }

    suspend fun getRecipes(ids: List<Int>) = fetchResult {
        val responses = mutableListOf<Response<Recipe>>()
        for (id in ids) {
            val response = recipeService.getRecipe(id)
            responses.add(response)
        }

        val successfulRecipes = responses.filter { it.isSuccessful }
            .mapNotNull { it.body() }

        Response.success(successfulRecipes)
    }

    suspend fun searchRecipesByIngredients(ingredients: String): List<RecipeIdSummary> {
        return recipeService.getRecipesByIngredients(ingredients, number = 2) // Example: fetch 10 recipes
    }
}