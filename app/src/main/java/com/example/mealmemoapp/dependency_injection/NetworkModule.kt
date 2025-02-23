package com.example.mealmemoapp.dependency_injection

import com.example.mealmemoapp.data.remote.api.PlacesApiService
import com.example.mealmemoapp.data.remote.api.TranslatorApiService
import com.example.mealmemoapp.utilities.Constants
import com.example.mealmemoapp.utilities.TranslationHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    @Named("PlacesApi")
    fun provideRetrofit():Retrofit{
        return Retrofit.Builder().
        baseUrl(Constants.GOOGLE_MAP_BASE_URL).
        addConverterFactory(GsonConverterFactory.create()).
        build()
    }

    // Provides Retrofit instance for Translator API
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
    @Singleton
    fun providePlacesApiService(@Named("PlacesApi") retrofit: Retrofit): PlacesApiService {
        return retrofit.create(PlacesApiService::class.java)
    }

    // Provides TranslatorApiService instance
    @Provides
    @Singleton
    fun provideTranslatorApiService(@Named("TranslationApi") retrofit: Retrofit): TranslatorApiService {
        return retrofit.create(TranslatorApiService::class.java)
    }

    // Provides TranslationHelper instance
    @Provides
    @Singleton
    fun provideTranslationHelper(translatorApiService: TranslatorApiService): TranslationHelper {
        return TranslationHelper(translatorApiService)
    }
}