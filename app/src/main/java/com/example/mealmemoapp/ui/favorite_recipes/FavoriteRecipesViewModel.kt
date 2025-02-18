package com.example.mealmemoapp.ui.favorite_recipes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealmemoapp.data.local_database.RecipeAppDataBase
import com.example.mealmemoapp.data.models.Recipe
import com.example.mealmemoapp.data.remote_database.RecipeRemoteDataSource
import com.example.mealmemoapp.data.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteRecipesViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {
    // LiveData for favorite recipes
    val favoriteRecipes: LiveData<List<Recipe>> = repository.getFavoriteRecipes()

    // Add recipe to favorites
    fun addFavorite(recipe: Recipe) {
        viewModelScope.launch {
            repository.addToFavorites(recipe)
        }
    }

    // Remove recipe from favorites
    fun removeFavorite(recipe: Recipe) {
        viewModelScope.launch {
            repository.removeFromFavorites(recipe)
        }
    }
}

