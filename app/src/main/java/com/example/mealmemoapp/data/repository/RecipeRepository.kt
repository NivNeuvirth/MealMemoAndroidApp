//package com.example.mealmemoapp.data.repository
//
//import androidx.lifecycle.LiveData
//import com.example.mealmemoapp.data.local.RecipeDao
//import com.example.mealmemoapp.data.models.Recipe
//import com.example.mealmemoapp.data.models.RecipeIdSummary
//import com.example.mealmemoapp.data.remote.RecipeRemoteDataSource
//import com.example.mealmemoapp.utilities.performFetchingAndSaving
//import javax.inject.Inject
//import javax.inject.Singleton
//
//@Singleton
//class RecipeRepository @Inject constructor(
//    private val remoteDataSource: RecipeRemoteDataSource,
//    private val localDataSource : RecipeDao
//) {
//
//    fun getRecipe(id:Int) = performFetchingAndSaving(
//        {localDataSource.getRecipe(id)},
//        {remoteDataSource.getRecipe(id)},
//        {localDataSource.addRecipe(it)},
//        shouldFetch = { localDataSource.getRecipeSync(id) == null } // Check if local data exists
//    )
//
//    // Fetch multiple recipes
//    fun getRecipes(ids: List<Int>) = performFetchingAndSaving(
//        { localDataSource.getRecipes(ids) },
//        { remoteDataSource.getRecipes(ids) },
//        { localDataSource.addRecipes(it) },
//        shouldFetch = { localDataSource.getRecipesSync(ids).isEmpty() } // Avoid fetching if already cached
//    )
//
//    fun getRecipeWithIngredients(id: Int) = performFetchingAndSaving(
//        { localDataSource.getRecipe(id) },
//        { remoteDataSource.getRecipe(id) },
//        { localDataSource.addRecipe(it) },
//        shouldFetch = { localDataSource.getRecipeSync(id) == null }
//    )
//
//
//    // New methods for favorites
//    fun getFavoriteRecipes(): LiveData<List<Recipe>> = localDataSource.getFavoriteRecipes()
//
//    suspend fun addToFavorites(recipe: Recipe) {
//        recipe.isFavorite = true
//        localDataSource.addFavoriteRecipe(recipe) // Save updated favorite status
//    }
//
//    suspend fun removeFromFavorites(recipe: Recipe) {
//        recipe.isFavorite = false
//        localDataSource.updateRecipe(recipe) // Save updated favorite status
//    }
//
//    suspend fun updateRecipe(recipe: Recipe) = localDataSource.updateRecipe(recipe)
//
//    suspend fun searchRecipesByIngredients(ingredients: String): List<RecipeIdSummary> {
//        return remoteDataSource.searchRecipesByIngredients(ingredients)
//    }
//}

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

    // not sure if used
    fun getRecipe(id: Int): LiveData<Result<Recipe>> = dataSource.retrieveAndStoreData(
        getLocalData = { localDataSource.getRecipe(id) },
        fetchRemoteData = { remoteDataSource.getRecipe(id) },
        saveToLocalDb = { localDataSource.addRecipe(it) },
        shouldFetchFromRemote = { localDataSource.getRecipeSync(id) == null }
    )

    fun getRecipes(ids: List<Int>): LiveData<Result<List<Recipe>>> = dataSource.retrieveAndStoreData(
        getLocalData = { localDataSource.getRecipes(ids) },
        fetchRemoteData = { remoteDataSource.getRecipes(ids) },
        saveToLocalDb = { localDataSource.addRecipes(it) },
        shouldFetchFromRemote = { localDataSource.getRecipesSync(ids).isEmpty() }
    )

    // not sure if used
    fun getRecipeWithIngredients(id: Int): LiveData<Result<Recipe>> = dataSource.retrieveAndStoreData(
        getLocalData = { localDataSource.getRecipe(id) },
        fetchRemoteData = { remoteDataSource.getRecipe(id) },
        saveToLocalDb = { localDataSource.addRecipe(it) },
        shouldFetchFromRemote = { localDataSource.getRecipeSync(id) == null }
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