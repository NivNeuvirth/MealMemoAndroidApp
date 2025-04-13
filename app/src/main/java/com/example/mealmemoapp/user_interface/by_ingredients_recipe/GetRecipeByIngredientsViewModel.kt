package com.example.mealmemoapp.user_interface.by_ingredients_recipe

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
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class GetRecipeByIngredientsViewModel @Inject constructor(
    private val repository: RecipeRepository,
    private val translationHelper: TranslationHelper
) : ViewModel() {

    private val _recipes = MutableLiveData<Result<List<Recipe>>>()
    val recipes: LiveData<Result<List<Recipe>>> get() = _recipes

    fun searchRecipesByIngredients(ingredients: String, number: Int = 5) {

        viewModelScope.launch {
            try {

                val translatedIngredients = if (Locale.getDefault().language == "iw") {

                    val ingredientList = ingredients.split(",").map { it.trim() }
                    val translatedList = ingredientList.map { ingredient ->
                        translationHelper.translateText(ingredient, "he", "en") ?: ingredient
                    }
                    translatedList.joinToString(", ")
                } else {
                    ingredients
                }

                val summaries = repository.searchRecipesByIngredients(translatedIngredients)
                val recipeIds = summaries.map { it.id }

                if (recipeIds.isNotEmpty()) {
                    val response = repository.getRecipes(recipeIds)
                    response.observeForever { result ->
                        _recipes.value = result

                        if (result is Result.Success) {
                            val recipes = result.data
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
                    }
                } else {
                    _recipes.value = Result.Success(emptyList())
                }
            } catch (e: Exception) {
                _recipes.value = Result.Failure("Failure fetching recipes, Network Error...")
            }
        }
    }

    fun updateFavoriteStatus(recipe: Recipe) {
        viewModelScope.launch {
            val updatedRecipe = recipe.copy(isFavorite = !recipe.isFavorite)
            repository.updateRecipe(updatedRecipe)
        }
    }
}