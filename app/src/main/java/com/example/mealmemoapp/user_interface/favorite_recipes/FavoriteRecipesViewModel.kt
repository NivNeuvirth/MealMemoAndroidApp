package com.example.mealmemoapp.user_interface.favorite_recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealmemoapp.data.models.Recipe
import com.example.mealmemoapp.data.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteRecipesViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    val favoriteRecipes: LiveData<List<Recipe>> = repository.getFavoriteRecipes()

    fun addFavorite(recipe: Recipe) {
        viewModelScope.launch {
            repository.addToFavorites(recipe)
        }
    }

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

    fun updateFavoriteStatus(recipe: Recipe) {
        viewModelScope.launch {

            val updatedRecipe = recipe.copy(isFavorite = !recipe.isFavorite)
            repository.updateRecipe(updatedRecipe)
        }
    }
}