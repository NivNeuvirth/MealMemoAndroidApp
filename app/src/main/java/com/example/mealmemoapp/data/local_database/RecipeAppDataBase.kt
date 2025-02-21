package com.example.mealmemoapp.data.local_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mealmemoapp.data.models.Recipe

@Database(entities = [Recipe::class], version = 6, exportSchema = false)
@TypeConverters(IngredientTypeConverter::class)
abstract class RecipeAppDataBase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao

    companion object {

        @Volatile
        private var instance: RecipeAppDataBase? = null

        fun getDataBase(context: Context): RecipeAppDataBase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    RecipeAppDataBase::class.java,
                    "recipes_database"
                )
                    .fallbackToDestructiveMigration().build().also {
                        instance = it
                    }
            }
        }
    }
}