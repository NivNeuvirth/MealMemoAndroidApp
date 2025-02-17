package com.example.mealmemoapp.di

import android.content.Context
import com.example.mealmemoapp.data.local_database.RecipeAppDataBase
import com.example.mealmemoapp.data.remote_database.RecipeService
import com.example.mealmemoapp.utils.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson) : Retrofit {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
    }

    @Provides
    fun provideGson() : Gson = GsonBuilder().create()

    @Provides
    fun provideCharacterService(retrofit: Retrofit) : RecipeService =
        retrofit.create(RecipeService::class.java)

    @Provides
    @Singleton
    fun provideLocalDataBase(@ApplicationContext appContext: Context) : RecipeAppDataBase =
        RecipeAppDataBase.getDataBase(appContext)

    @Provides
    @Singleton
    fun provideCharacterDao(database: RecipeAppDataBase) = database.recipeDao()
}