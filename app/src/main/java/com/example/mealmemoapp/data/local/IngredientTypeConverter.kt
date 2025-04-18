package com.example.mealmemoapp.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.mealmemoapp.data.models.Ingredient

class IngredientTypeConverter {

    @TypeConverter
    fun fromIngredientsList(ingredients: List<Ingredient>?): String? {
        return Gson().toJson(ingredients)
    }

    @TypeConverter
    fun toIngredientsList(ingredientsString: String?): List<Ingredient>? {
        val listType = object : TypeToken<List<Ingredient>>() {}.type
        return Gson().fromJson(ingredientsString, listType)
    }
}