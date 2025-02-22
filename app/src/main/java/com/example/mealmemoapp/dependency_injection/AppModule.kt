package com.example.mealmemoapp.dependency_injection

import android.content.Context
import com.example.mealmemoapp.data.local.RecipeAppDataBase
import com.example.mealmemoapp.data.remote.api.RecipeApiService
import com.example.mealmemoapp.utilities.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    @Named("RecipeApi")
    fun provideRetrofit(gson: Gson) : Retrofit {
        return Retrofit.Builder().baseUrl(Constants.SPOONACULAR_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
    }

    @Provides
    fun provideGson() : Gson = GsonBuilder().create()

    @Provides
    fun provideRecipeService(@Named("RecipeApi") retrofit: Retrofit) : RecipeApiService =
        retrofit.create(RecipeApiService::class.java)

    @Provides
    @Singleton
    fun provideLocalDataBase(@ApplicationContext appContext: Context) : RecipeAppDataBase =
        RecipeAppDataBase.getDataBase(appContext)

    @Provides
    @Singleton
    fun provideRecipeDao(database: RecipeAppDataBase) = database.recipeDao()
}