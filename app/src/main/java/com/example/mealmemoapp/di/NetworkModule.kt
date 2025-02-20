package com.example.mealmemoapp.di

import com.example.mealmemoapp.data.remote_database.PlacesApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit():Retrofit{
        return Retrofit.Builder().
        baseUrl("https://maps.googleapis.com/maps/api/").
        addConverterFactory(GsonConverterFactory.create()).
        build()
    }

    @Provides
    @Singleton
    fun providePlacesApiService(retrofit: Retrofit):PlacesApiService{
        return retrofit.create(PlacesApiService::class.java)
    }
}