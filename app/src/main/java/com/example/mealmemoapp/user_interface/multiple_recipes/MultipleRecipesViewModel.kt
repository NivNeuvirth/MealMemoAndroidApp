//package com.example.mealmemoapp.user_interface.multiple_recipes
//
//import android.util.Log
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.mealmemoapp.data.models.Recipe
//import com.example.mealmemoapp.data.remote.api.TranslatorApiService
//import com.example.mealmemoapp.data.models.TranslatorRequest
//import com.example.mealmemoapp.data.models.TranslatorResponse
//import com.example.mealmemoapp.data.repository.RecipeRepository
//import com.example.mealmemoapp.utilities.Constants
//import com.example.mealmemoapp.utilities.Result
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.util.Locale
//import javax.inject.Inject
//
//// Inside MultipleRecipesViewModel
//
//@HiltViewModel
//class MultipleRecipesViewModel @Inject constructor(
//    private val recipeRepository: RecipeRepository,
//    private val translatorApiService: TranslatorApiService // Inject TranslatorApiService
//) : ViewModel() {
//
//    private val _recipes = MutableLiveData<Result<List<Recipe>>>()
//    val recipes: LiveData<Result<List<Recipe>>> = _recipes
//
//    private var isDataFetched = false  // Flag to track if data is already fetched
//
//    fun getRandomRecipes(forceRefresh: Boolean = false) {
//        // If forceRefresh is false and the data has already been fetched, do not fetch again
//        if (!forceRefresh && isDataFetched) {
//            return
//        }
//
//        // Fetch data from the repository
//        viewModelScope.launch {
//            try {
//                // Generate new random IDs for fetching recipes
//                val randomIds = (600000..700000).shuffled().take(1)
//
//                // Fetch the recipes by IDs
//                val response = recipeRepository.getRecipes(randomIds)
//
//                // Observe the response
//                response.observeForever { resource ->
//                    _recipes.value = resource
//
//                    if (resource is Result.Success) {
//                        // Step 2: Translate recipe names once the data is fetched
//                        val recipes = resource.data
//                        val recipeNames = recipes?.map { it.title } ?: emptyList()
//                        val recipeSummaries = recipes?.map { it.summary } ?: emptyList()
//                        val recipeIngredients = recipes?.flatMap { it.extendedIngredients.map { ingredient -> ingredient.name } } ?: emptyList()
//
//                        Log.d("MultipleRecipesViewModel", "Recipe Names: $recipeNames")
//                        Log.d("MultipleRecipesViewModel", "Recipe Summaries: $recipeSummaries")
//                        Log.d("MultipleRecipesViewModel", "Recipe Ingredients: $recipeIngredients")
//
//                        // Make sure all lists are non-null
//                        val nonNullableRecipeNames = recipeNames.filterNotNull()
//                        val nonNullableRecipeSummaries = recipeSummaries.filterNotNull()
//                        val nonNullableRecipeIngredients = recipeIngredients.filterNotNull()
//
//                        // Step 3: Translate all recipe data (names, summaries, ingredients)
//                        translateRecipeData(nonNullableRecipeNames, nonNullableRecipeSummaries, nonNullableRecipeIngredients) { translatedNames, translatedSummaries, translatedIngredients ->
//                            // Step 4: Update the recipes with translated data
//                            val updatedRecipes = recipes?.mapIndexed { index, recipe ->
//                                recipe.copy(
//                                    title = translatedNames.getOrNull(index) ?: recipe.title,
//                                    summary = translatedSummaries.getOrNull(index) ?: recipe.summary,
//                                    extendedIngredients = recipe.extendedIngredients.mapIndexed { ingredientIndex, ingredient ->
//                                        ingredient.copy(name = translatedIngredients.getOrNull(ingredientIndex) ?: ingredient.name)
//                                    }
//                                )
//                            }
//                            _recipes.value = Result.Success(updatedRecipes ?: emptyList())
//                        }
//                    }
//
//                    // Mark data as fetched
//                    isDataFetched = true
//                }
//            } catch (e: Exception) {
//                // Handle the error if fetching fails
//                _recipes.postValue(Result.Error("Failed to fetch recipes"))
//            }
//        }
//    }
//
//    private val _ingredients = MutableLiveData<List<String>>()
//    val ingredients: LiveData<List<String>> get() = _ingredients
//
//
//    // Update the favorite status of the recipe
//    fun updateFavoriteStatus(recipe: Recipe) {
//        viewModelScope.launch {
//            // Toggle the favorite status
//            val updatedRecipe = recipe.copy(isFavorite = !recipe.isFavorite)
//
//            // Update in the repository (update locally or remotely as needed)
//            recipeRepository.updateRecipe(updatedRecipe)  // Assuming you have a method in your repository
//        }
//    }
//
//    // Function to translate recipe data (names, summaries, ingredients)
//    private fun translateRecipeData(names: List<String>, summaries: List<String>, ingredients: List<String>, onTranslated: (List<String>, List<String>, List<String>) -> Unit) {
//        val deviceLanguage = Locale.getDefault().language // Get device language
//
//        if (deviceLanguage != "iw") {
//            // If not Hebrew, don't translate, return original data
//            onTranslated(names, summaries, ingredients)
//            return
//        }
//
//        val requestNames = names.map { TranslatorRequest(it) }
//        val requestSummaries = summaries.map { TranslatorRequest(it) }
//        val requestIngredients = ingredients.map { TranslatorRequest(it) }
//
//        // Translate recipe names
//        translatorApiService.translateText(Constants.AZURE_TRANSLATOR_API,
//            "eastus",
//            requestNames).enqueue(object : Callback<List<TranslatorResponse>> {
//            override fun onResponse(
//                call: Call<List<TranslatorResponse>>,
//                response: Response<List<TranslatorResponse>>
//            ) {
//                val translatedNames = if (response.isSuccessful) {
//                    response.body()?.flatMap { it.translations.map { translation -> translation.text } }
//                        ?: emptyList()
//                } else {
//                    Log.e("MultipleRecipesViewModel", "Translation API failed: ${response.errorBody()?.string()}")
//                    emptyList()
//                }
//
//                // Translate summaries
//                translatorApiService.translateText(Constants.AZURE_TRANSLATOR_API,
//                    "eastus",
//                    requestSummaries).enqueue(object : Callback<List<TranslatorResponse>> {
//                    override fun onResponse(
//                        call: Call<List<TranslatorResponse>>,
//                        response: Response<List<TranslatorResponse>>
//                    ) {
//                        val translatedSummaries = if (response.isSuccessful) {
//                            response.body()?.flatMap { it.translations.map { translation -> translation.text } }
//                                ?: emptyList()
//                        } else {
//                            Log.e("MultipleRecipesViewModel", "Translation API failed: ${response.errorBody()?.string()}")
//                            emptyList()
//                        }
//
//                        // Translate ingredients
//                        translatorApiService.translateText(Constants.AZURE_TRANSLATOR_API,
//                            "eastus",
//                            requestIngredients).enqueue(object : Callback<List<TranslatorResponse>> {
//                            override fun onResponse(
//                                call: Call<List<TranslatorResponse>>,
//                                response: Response<List<TranslatorResponse>>
//                            ) {
//                                val translatedIngredients = if (response.isSuccessful) {
//                                    response.body()?.flatMap { it.translations.map { translation -> translation.text } }
//                                        ?: emptyList()
//                                } else {
//                                    Log.e("MultipleRecipesViewModel", "Translation API failed: ${response.errorBody()?.string()}")
//                                    emptyList()
//                                }
//
//                                // Return all translated data
//                                onTranslated(translatedNames, translatedSummaries, translatedIngredients)
//                            }
//
//                            override fun onFailure(call: Call<List<TranslatorResponse>>, t: Throwable) {
//                                Log.e("MultipleRecipesViewModel", "Translation API call failed: ${t.message}")
//                                onTranslated(names, summaries, ingredients) // Fallback to original data if the API request fails
//                            }
//                        })
//                    }
//
//                    override fun onFailure(call: Call<List<TranslatorResponse>>, t: Throwable) {
//                        Log.e("MultipleRecipesViewModel", "Translation API call failed: ${t.message}")
//                        onTranslated(names, summaries, ingredients) // Fallback to original data if the API request fails
//                    }
//                })
//            }
//
//            override fun onFailure(call: Call<List<TranslatorResponse>>, t: Throwable) {
//                Log.e("MultipleRecipesViewModel", "Translation API call failed: ${t.message}")
//                onTranslated(names, summaries, ingredients) // Fallback to original data if the API request fails
//            }
//        })
//    }
//}
//
//

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

    private var isDataFetched = false  // Flag to track if data is already fetched

    fun getRandomRecipes(forceRefresh: Boolean = false) {
        // If forceRefresh is false and the data has already been fetched, do not fetch again
        if (!forceRefresh && isDataFetched) {
            return
        }

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

                    if (resource is Result.Success) {
                        // Step 2: Translate recipe names once the data is fetched
                        val recipes = resource.data
                        val recipeNames = recipes?.map { it.title } ?: emptyList()
                        val recipeSummaries = recipes?.map { it.summary } ?: emptyList()
                        val recipeIngredients = recipes?.flatMap { it.extendedIngredients.map { ingredient -> ingredient.name } } ?: emptyList()

                        Log.d("MultipleRecipesViewModel", "Recipe Names: $recipeNames")
                        Log.d("MultipleRecipesViewModel", "Recipe Summaries: $recipeSummaries")
                        Log.d("MultipleRecipesViewModel", "Recipe Ingredients: $recipeIngredients")

                        // Make sure all lists are non-null
                        val nonNullableRecipeNames = recipeNames.filterNotNull()
                        val nonNullableRecipeSummaries = recipeSummaries.filterNotNull()
                        val nonNullableRecipeIngredients = recipeIngredients.filterNotNull()

                        // Step 3: Translate all recipe data (names, summaries, ingredients)
                        translationHelper.translateData(
                            nonNullableRecipeNames,
                            nonNullableRecipeSummaries,
                            nonNullableRecipeIngredients
                        ) { translatedNames, translatedSummaries, translatedIngredients ->
                            // Step 4: Update the recipes with translated data
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

                    // Mark data as fetched
                    isDataFetched = true
                }
            } catch (e: Exception) {
                // Handle the error if fetching fails
                _recipes.postValue(Result.Error("Failed to fetch recipes"))
            }
        }
    }

    private val _ingredients = MutableLiveData<List<String>>()
    val ingredients: LiveData<List<String>> get() = _ingredients

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


