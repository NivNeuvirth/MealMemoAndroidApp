package com.example.mealmemoapp.data.remote_database

import com.example.mealmemoapp.data.models.Recipe
import retrofit2.Response
import javax.inject.Inject

class RecipeRemoteDataSource @Inject constructor(
    private val recipeService: RecipeService
)   : BaseDataSource() {

    //suspend fun getRecipes(ids: List<Int>) = getResult { recipeService.getRecipe(ids) }
    suspend fun getRecipe(id : Int) = getResult { recipeService.getRecipe(id) }

    suspend fun getRecipes(ids: List<Int>) = getResult {
        val responses = mutableListOf<Response<Recipe>>()
        for (id in ids) {
            val response = recipeService.getRecipe(id)
            responses.add(response)
        }

        // Extract the successful recipes from the responses
        val successfulRecipes = responses.filter { it.isSuccessful }
            .mapNotNull { it.body() }

        // Wrap the successful recipes in a Response object
        // Here we're assuming getResult can handle a list of Recipe, but if it expects Response<List<Recipe>>,
        // we need to construct that as well.

        // Use Response.success() to wrap the list of recipes in a successful Response
        Response.success(successfulRecipes)
    }
}