package com.example.mealmemoapp.ui.multiple_recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mealmemoapp.data.models.Recipe
import com.example.mealmemoapp.data.repository.RecipeRepository
import com.example.mealmemoapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MultipleRecipesViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    fun getRecipe(id: Int): LiveData<Resource<Recipe>> {
        return recipeRepository.getRecipe(id)
    }
}