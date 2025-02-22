package com.example.mealmemoapp.dependency_injection

import com.example.mealmemoapp.data.remote.api.PlacesApiService
import com.example.mealmemoapp.utilities.Constants
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

    @Provides
    @Singleton
    fun providePlacesApiService(@Named("PlacesApi") retrofit: Retrofit): PlacesApiService {
        return retrofit.create(PlacesApiService::class.java)
    }
}