package com.example.mealmemoapp.ui.detailed_recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mealmemoapp.data.models.Recipe
import com.example.mealmemoapp.data.repository.RecipeRepository
import com.example.mealmemoapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailedRecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    // Fetch a single recipe by ID
    fun getRecipe(id: Int): LiveData<Resource<Recipe>> {
        return recipeRepository.getRecipe(id)
    }
}
