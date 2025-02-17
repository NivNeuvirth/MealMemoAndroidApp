package com.example.mealmemoapp.data.repository

import com.example.mealmemoapp.data.local_database.RecipeDao
import com.example.mealmemoapp.data.remote_database.RecipeRemoteDataSource
import com.example.mealmemoapp.utils.performFetchingAndSaving
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepository @Inject constructor(
    private val remoteDataSource: RecipeRemoteDataSource,
    private val localDataSource : RecipeDao
) {

    fun getRecipe(id:Int) = performFetchingAndSaving(
        {localDataSource.getRecipe(id)},
        {remoteDataSource.getRecipe(id)},
        {localDataSource.addRecipe(it)}
    )
}