package com.example.mealmemoapp.dependency_injection

import android.content.Context
import com.example.mealmemoapp.data.local.RecipeAppDataBase
import com.example.mealmemoapp.data.remote.api.PlacesApiService
import com.example.mealmemoapp.data.remote.api.RecipeApiService
import com.example.mealmemoapp.data.remote.api.TranslatorApiService
import com.example.mealmemoapp.utilities.Constants
import com.example.mealmemoapp.utilities.TranslationHelper
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
object NetworkModule {

    @Provides
    @Singleton
    @Named("RecipeApi")
    fun provideRecipeApiRetrofit(gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.SPOONACULAR_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    @Named("PlacesApi")
    fun providePlacesApiRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.GOOGLE_MAP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("TranslationApi")
    fun provideTranslationApiRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.AZURE_TRANSLATOR_BASE_URL) // Use the base URL for Azure Translator
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideRecipeService(@Named("RecipeApi") retrofit: Retrofit): RecipeApiService =
        retrofit.create(RecipeApiService::class.java)

    @Provides
    @Singleton
    fun provideLocalDataBase(@ApplicationContext appContext: Context): RecipeAppDataBase =
        RecipeAppDataBase.getDataBase(appContext)

    @Provides
    @Singleton
    fun provideRecipeDao(database: RecipeAppDataBase) = database.recipeDao()

    @Provides
    @Singleton
    fun providePlacesApiService(@Named("PlacesApi") retrofit: Retrofit): PlacesApiService {
        return retrofit.create(PlacesApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTranslatorApiService(@Named("TranslationApi") retrofit: Retrofit): TranslatorApiService {
        return retrofit.create(TranslatorApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTranslationHelper(translatorApiService: TranslatorApiService): TranslationHelper {
        return TranslationHelper(translatorApiService)
    }
}
