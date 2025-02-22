package com.example.mealmemoapp.user_interface.multiple_recipes

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
class MultipleRecipesViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _recipes = MutableLiveData<Result<List<Recipe>>>()
    val recipes: LiveData<Result<List<Recipe>>> = _recipes

    private var isDataFetched = false  // Flag to track if data is already fetched

    fun getRandomRecipes(forceRefresh: Boolean = false) {
        // If forceRefresh is false and the data has already been fetched, do not fetch again
        if (!forceRefresh && isDataFetched) {
            return
        }

//        // Set loading state before starting to fetch
//        _recipes.postValue(Result.Loading(null))

        // Fetch data from the repository
        viewModelScope.launch {
            try {
                // Generate new random IDs for fetching recipes
                val randomIds = (600000..700000).shuffled().take(1)

                // Fetch the recipes by IDs
                val response = recipeRepository.getRecipes(randomIds)

                // Observe the response
                response.observeForever { resource ->
                    _recipes.value = resource

                    // Mark data as fetched
                    if (true) {
                        isDataFetched = true
                    }
                }
            } catch (e: Exception) {
                // Handle the error if fetching fails
                _recipes.postValue(Result.Error("Failed to fetch recipes"))
            }
        }
    }

    fun getMultipleRecipes(ids: List<Int>) {
        recipeRepository.getRecipes(ids).observeForever { resource ->
            _recipes.value = resource
        }
    }

//    // Fetch multiple recipes by a list of IDs
//    fun getMultipleRecipes(ids: List<Int>): LiveData<Resource<List<Recipe>>> {
//        return recipeRepository.getRecipes(ids)
//    }

    private val _ingredients = MutableLiveData<List<String>>()
    val ingredients: LiveData<List<String>> get() = _ingredients

    fun getRecipeIngredients(id: Int) {
        viewModelScope.launch {
            val result = recipeRepository.getRecipeWithIngredients(id).value // Unwrap LiveData
            when (result) {
                is Result.Success -> {
                    val recipe = result.data // 'data' is a Recipe
                    _ingredients.postValue(recipe?.extendedIngredients?.map { it.name }
                        ?: emptyList())
                }

                is Result.Error -> {
                    // Handle error, possibly display a message
                    _ingredients.postValue(emptyList())
                }

                is Result.Loading -> {
                    // Optionally handle loading state
                }

                null -> TODO()
            }
        }
    }


    // Update the favorite status of the recipe
    fun updateFavoriteStatus(recipe: Recipe) {
        viewModelScope.launch {
            // Toggle the favorite status
            val updatedRecipe = recipe.copy(isFavorite = !recipe.isFavorite)

            // Update in the repository (update locally or remotely as needed)
            recipeRepository.updateRecipe(updatedRecipe)  // Assuming you have a method in your repository
        }
    }

}