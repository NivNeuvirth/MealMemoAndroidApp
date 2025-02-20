package com.example.mealmemoapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mealmemoapp.data.local_database.RecipeDao
import com.example.mealmemoapp.data.models.Recipe
import com.example.mealmemoapp.data.remote_database.RecipeRemoteDataSource
import com.example.mealmemoapp.utils.performFetchingAndSaving
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepository @Inject constructor(
    private val remoteDataSource: RecipeRemoteDataSource,
    private val localDataSource : RecipeDao
) {

    fun getRecipe(id:Int) = performFetchingAndSaving(
        {localDataSource.getRecipe(id)},
        {remoteDataSource.getRecipe(id)},
        {localDataSource.addRecipe(it)},
        shouldFetch = { localDataSource.getRecipeSync(id) == null } // Check if local data exists
    )

    // Fetch multiple recipes
    fun getRecipes(ids: List<Int>) = performFetchingAndSaving(
        { localDataSource.getRecipes(ids) },
        { remoteDataSource.getRecipes(ids) },
        { localDataSource.addRecipes(it) },
        shouldFetch = { localDataSource.getRecipesSync(ids).isEmpty() } // Avoid fetching if already cached
    )

    fun getRecipeWithIngredients(id: Int) = performFetchingAndSaving(
        { localDataSource.getRecipe(id) },
        { remoteDataSource.getRecipe(id) },
        { localDataSource.addRecipe(it) },
        shouldFetch = { localDataSource.getRecipeSync(id) == null }
    )

    // New methods for favorites
    fun getFavoriteRecipes(): LiveData<List<Recipe>> = localDataSource.getFavoriteRecipes()

    suspend fun addToFavorites(recipe: Recipe) {
        recipe.isFavorite = true
        localDataSource.addFavoriteRecipe(recipe) // Save updated favorite status
    }

    suspend fun removeFromFavorites(recipe: Recipe) {
        recipe.isFavorite = false
        localDataSource.updateRecipe(recipe) // Save updated favorite status
    }

    suspend fun updateRecipe(recipe: Recipe) = localDataSource.updateRecipe(recipe)

}