package com.example.mealmemoapp.ui.favorite_recipes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    fun updateRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repository.updateRecipe(recipe)
        }
    }

    // Update the favorite status of the recipe
    fun updateFavoriteStatus(recipe: Recipe) {
        viewModelScope.launch {
            // Toggle the favorite status
            val updatedRecipe = recipe.copy(isFavorite = !recipe.isFavorite)

            // Update in the repository (update locally or remotely as needed)
            repository.updateRecipe(updatedRecipe)  // Assuming you have a method in your repository
        }
    }
}

