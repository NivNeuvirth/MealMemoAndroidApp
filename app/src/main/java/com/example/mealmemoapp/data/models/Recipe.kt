package com.example.mealmemoapp.data.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.mealmemoapp.data.local_database.IngredientTypeConverter
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "recipes")
@TypeConverters(IngredientTypeConverter::class)
data class Recipe (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val image: String,
    val title: String,
    val readyInMinutes: Int,
    val servings: Int,
    val summary: String,
    val extendedIngredients: List<Ingredient> = emptyList(),
    val spoonacularScore: Float,
    @ColumnInfo(name = "isFavorite") var isFavorite: Boolean = false
) : Parcelable

@Parcelize
data class Ingredient(
    val id: Int,
    val name: String
) : Parcelable
