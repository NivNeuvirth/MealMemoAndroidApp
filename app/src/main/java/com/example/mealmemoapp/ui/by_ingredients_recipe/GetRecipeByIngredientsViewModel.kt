package com.example.mealmemoapp.ui.by_ingredients_recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealmemoapp.data.models.Recipe
import com.example.mealmemoapp.data.models.RecipeIdSummary
import com.example.mealmemoapp.data.repository.RecipeRepository
import com.example.mealmemoapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetRecipeByIngredientsViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _recipes = MutableLiveData<Resource<List<Recipe>>>()
    val recipes: LiveData<Resource<List<Recipe>>> get() = _recipes

    fun searchRecipesByIngredients(ingredients: String, number: Int = 5) {
        _recipes.value = Resource.loading()

        viewModelScope.launch {
            try {
                val summaries = repository.searchRecipesByIngredients(ingredients)
                val recipeIds = summaries.map { it.id } // Extract all recipe IDs

                if (recipeIds.isNotEmpty()) {
                    val response = repository.getRecipes(recipeIds) // Fetch recipes (LiveData<Resource<List<Recipe>>>)
                    response.observeForever { resource ->  // Observe LiveData once
                        _recipes.value = resource
                    }
                } else {
                    _recipes.value = Resource.success(emptyList()) // No results found
                }
            } catch (e: Exception) {
                _recipes.value = Resource.error(e.localizedMessage ?: "Error fetching recipes")
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