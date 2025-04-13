package com.example.mealmemoapp.data.repository

import androidx.lifecycle.LiveData
import com.example.mealmemoapp.data.local.RecipeDao
import com.example.mealmemoapp.data.models.Recipe
import com.example.mealmemoapp.data.models.RecipeIdSummary
import com.example.mealmemoapp.data.remote.RecipeRemoteDataSource
import com.example.mealmemoapp.utilities.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepository @Inject constructor(
    private val remoteDataSource: RecipeRemoteDataSource,
    private val localDataSource: RecipeDao,
    private val dataSource: DataSource
) {

    fun getRecipes(ids: List<Int>): LiveData<Result<List<Recipe>>> = dataSource.fetchRemoteData(
        fetchRemoteData = { remoteDataSource.getRecipes(ids) },
    )

    fun getFavoriteRecipes(): LiveData<List<Recipe>> = localDataSource.getFavoriteRecipes()

    suspend fun addToFavorites(recipe: Recipe) {
        recipe.isFavorite = true
        localDataSource.addFavoriteRecipe(recipe)
    }

    suspend fun removeFromFavorites(recipe: Recipe) {
        recipe.isFavorite = false
        localDataSource.updateRecipe(recipe)
    }

    suspend fun updateRecipe(recipe: Recipe) = localDataSource.updateRecipe(recipe)

    suspend fun searchRecipesByIngredients(ingredients: String): List<RecipeIdSummary> {
        return remoteDataSource.searchRecipesByIngredients(ingredients)
    }
}