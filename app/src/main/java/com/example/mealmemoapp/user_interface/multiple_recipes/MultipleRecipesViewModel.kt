package com.example.mealmemoapp.user_interface.multiple_recipes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealmemoapp.data.models.Recipe
import com.example.mealmemoapp.data.remote.api.TranslatorApiService
import com.example.mealmemoapp.data.repository.RecipeRepository
import com.example.mealmemoapp.utilities.Result
import com.example.mealmemoapp.utilities.TranslationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MultipleRecipesViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val translationHelper: TranslationHelper // Inject the TranslationHelper
) : ViewModel() {

    private val _recipes = MutableLiveData<Result<List<Recipe>>>()
    val recipes: LiveData<Result<List<Recipe>>> = _recipes

    private val _chosenItem = MutableLiveData<Recipe>()
    val chosenItem : LiveData<Recipe> get() = _chosenItem

    private var isDataFetched = false

    fun getRandomRecipes(forceRefresh: Boolean = false) {

        if (!forceRefresh && isDataFetched) {
            return
        }

        viewModelScope.launch {
            try {

                val randomIds = (600000..700000).shuffled().take(1)
                val response = recipeRepository.getRecipes(randomIds)

                response.observeForever { resource ->
                    _recipes.value = resource

                    if (resource is Result.Success) {

                        val recipes = resource.data
                        val recipeNames = recipes?.map { it.title } ?: emptyList()
                        val recipeSummaries = recipes?.map { it.summary } ?: emptyList()
                        val recipeIngredients = recipes?.flatMap { it.extendedIngredients.map { ingredient -> ingredient.name } } ?: emptyList()

                        Log.d("MultipleRecipesViewModel", "Recipe Names: $recipeNames")
                        Log.d("MultipleRecipesViewModel", "Recipe Summaries: $recipeSummaries")
                        Log.d("MultipleRecipesViewModel", "Recipe Ingredients: $recipeIngredients")

                        val nonNullableRecipeNames = recipeNames.filterNotNull()
                        val nonNullableRecipeSummaries = recipeSummaries.filterNotNull()
                        val nonNullableRecipeIngredients = recipeIngredients.filterNotNull()

                        translationHelper.translateData(
                            nonNullableRecipeNames,
                            nonNullableRecipeSummaries,
                            nonNullableRecipeIngredients
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
                _recipes.postValue(Result.Error("Failed to fetch recipes"))
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