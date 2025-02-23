package com.example.mealmemoapp.user_interface.detailed_recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mealmemoapp.data.models.Recipe
import com.example.mealmemoapp.data.repository.RecipeRepository
import com.example.mealmemoapp.utilities.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailedRecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

//    // Fetch a single recipe by ID
//    fun getRecipe(id: Int): LiveData<Result<Recipe>> {
//        return recipeRepository.getRecipe(id)
//    }

    private val _chosenItem = MutableLiveData<Recipe>()
    val chosenItem : LiveData<Recipe> get() = _chosenItem
}
