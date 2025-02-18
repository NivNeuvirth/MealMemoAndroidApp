package com.example.mealmemoapp.data.local_database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mealmemoapp.data.models.Recipe

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecipe(item: Recipe)

    // Add multiple recipes to the local database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecipes(recipes: List<Recipe>)

    @Update
    suspend fun update(item: Recipe)

    @Delete
    suspend fun deleteItem(vararg items: Recipe)

    @Update
    suspend fun updateItem(item: Recipe)

    @Query("SELECT * FROM recipes WHERE id IN (:ids)")
    fun getRecipes(ids: List<Int>): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE id LIKE :id")
    fun getRecipe(id:Int) : LiveData<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteRecipe(recipe: Recipe)

    @Delete
    suspend fun deleteFavoriteRecipe(recipe: Recipe)

    @Query("SELECT * FROM recipes WHERE isFavorite = 1")
    fun getFavoriteRecipes(): LiveData<List<Recipe>>

    @Update
    suspend fun updateRecipe(recipe: Recipe)
}