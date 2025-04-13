package com.example.mealmemoapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mealmemoapp.data.models.Recipe

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecipe(item: Recipe)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecipes(recipes: List<Recipe>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteRecipe(recipe: Recipe)

    @Update
    suspend fun updateRecipe(recipe: Recipe)

    @Query("SELECT * FROM recipes WHERE id IN (:ids)")
    fun getRecipes(ids: List<Int>): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE id LIKE :id")
    fun getRecipe(id:Int) : LiveData<Recipe>

    @Query("SELECT * FROM recipes WHERE isFavorite = 1")
    fun getFavoriteRecipes(): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE id = :id")
    fun getRecipeSync(id: Int): Recipe?

    @Query("SELECT * FROM recipes WHERE id IN (:ids)")
    fun getRecipesSync(ids: List<Int>): List<Recipe>
}