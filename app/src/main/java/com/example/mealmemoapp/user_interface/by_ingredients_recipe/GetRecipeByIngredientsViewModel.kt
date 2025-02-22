package com.example.mealmemoapp.user_interface.by_ingredients_recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealmemoapp.data.models.Recipe
import com.example.mealmemoapp.data.repository.RecipeRepository
import com.example.mealmemoapp.utilities.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetRecipeByIngredientsViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _recipes = MutableLiveData<Result<List<Recipe>>>()
    val recipes: LiveData<Result<List<Recipe>>> get() = _recipes

    fun searchRecipesByIngredients(ingredients: String, number: Int = 5) {
        _recipes.value = Result.Loading()

        viewModelScope.launch {
            try {
                val summaries = repository.searchRecipesByIngredients(ingredients)
                val recipeIds = summaries.map { it.id } // Extract all recipe IDs

                if (recipeIds.isNotEmpty()) {
                    val response = repository.getRecipes(recipeIds) // Fetch recipes (LiveData<Resource<List<Recipe>>>)
                    response.observeForever { result ->  // Observe LiveData once
                        _recipes.value = result
                    }
                } else {
                    _recipes.value = Result.Success(emptyList()) // No results found
                }
            } catch (e: Exception) {
                _recipes.value = Result.Error(e.localizedMessage ?: "Error fetching recipes")
            }
        }
    }

    fun updateFavoriteStatus(recipe: Recipe) {
        viewModelScope.launch {
            // Toggle the favorite status
            val updatedRecipe = recipe.copy(isFavorite = !recipe.isFavorite)

            // Update in the repository (update locally or remotely as needed)
            repository.updateRecipe(updatedRecipe)  // Assuming you have a method in your repository
        }
    }
}