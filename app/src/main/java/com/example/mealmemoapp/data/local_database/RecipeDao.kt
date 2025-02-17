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

    @Update
    suspend fun update(item: Recipe)

    @Delete
    suspend fun deleteItem(vararg items: Recipe)

    @Update
    suspend fun updateItem(item: Recipe)

    @Query("SELECT * FROM recipes")
    fun getItems() : LiveData<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE id LIKE :id")
    fun getRecipe(id:Int) : LiveData<Recipe>
}