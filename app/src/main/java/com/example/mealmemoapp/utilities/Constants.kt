package com.example.mealmemoapp.utilities

import com.example.mealmemoapp.BuildConfig

class Constants {
    companion object{
        const val SPOONACULAR_BASE_URL = "https://api.spoonacular.com/"
        const val GOOGLE_MAP_BASE_URL = "https://maps.googleapis.com/maps/api/"
        const val AZURE_TRANSLATOR_BASE_URL = "https://api.cognitive.microsofttranslator.com/"
        const val SPOONACULAR_API_KEY = BuildConfig.SPOONACULAR_API_KEY
        const val GOOGLE_MAP_API_KEY = BuildConfig.GOOGLE_MAP_API_KEY
        const val AZURE_TRANSLATOR_API = BuildConfig.AZURE_TRANSLATOR_API
    }
}