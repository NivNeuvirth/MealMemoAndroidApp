package com.example.mealmemoapp.user_interface.multiple_recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealmemoapp.data.models.Recipe
import com.example.mealmemoapp.data.repository.RecipeRepository
import com.example.mealmemoapp.utilities.Result
import com.example.mealmemoapp.utilities.TranslationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MultipleRecipesViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val translationHelper: TranslationHelper
) : ViewModel() {

    private val _recipes = MutableLiveData<Result<List<Recipe>>>()
    val recipes: LiveData<Result<List<Recipe>>> = _recipes

    private var isDataFetched = false

    fun getRandomRecipes(forceRefresh: Boolean = false) {

        if (!forceRefresh && isDataFetched) {
            return
        }

        viewModelScope.launch {
            try {

                val randomIds = (600000..700000).shuffled().take(5)
                val response = recipeRepository.getRecipes(randomIds)

                response.observeForever { resource ->
                    _recipes.value = resource

                    if (resource is Result.Success) {

                        val recipes = resource.data
                        val recipeNames = recipes?.map { it.title } ?: emptyList()
                        val recipeSummaries = recipes?.map { it.summary } ?: emptyList()
                        val recipeIngredients = recipes?.flatMap { it.extendedIngredients.map { ingredient -> ingredient.name } } ?: emptyList()

                        translationHelper.translateData(
                            recipeNames,
                            recipeSummaries,
                            recipeIngredients
                        ) { translatedNames, translatedSummaries, translatedIngredients ->

                            val updatedRecipes = recipes?.mapIndexed { index, recipe ->
                                recipe.copy(
                                    title = translatedNames.getOrNull(index) ?: recipe.title,
                                    summary = translatedSummaries.getOrNull(index) ?: recipe.summary,
                                    extendedIngredients = recipe.extendedIngredients.mapIndexed { ingredientIndex, ingredient ->
                                        ingredient.copy(name = translatedIngredients.getOrNull(ingredientIndex) ?: ingredient.name)
                                    }
                                )
                            }
                            _recipes.value = Result.Success(updatedRecipes ?: emptyList())
                        }
                    }

                    isDataFetched = true
                }
            } catch (e: Exception) {
                _recipes.postValue(Result.Failure("Failed to fetch recipes"))
            }
        }
    }

    private val _ingredients = MutableLiveData<List<String>>()
    val ingredients: LiveData<List<String>> get() = _ingredients

    fun updateFavoriteStatus(recipe: Recipe) {
        viewModelScope.launch {
            val updatedRecipe = recipe.copy(isFavorite = !recipe.isFavorite)
            recipeRepository.updateRecipe(updatedRecipe)
        }
    }
}