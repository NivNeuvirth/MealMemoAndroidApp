package com.example.mealmemoapp.data.remote_database

import javax.inject.Inject

class RecipeRemoteDataSource @Inject constructor(
    private val recipeService: RecipeService
)   : BaseDataSource() {

    suspend fun getRecipe(id : Int) = getResult { recipeService.getRecipe(id) }
}