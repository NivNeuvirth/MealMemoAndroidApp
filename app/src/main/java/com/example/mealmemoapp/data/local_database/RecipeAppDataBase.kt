package com.example.mealmemoapp.data.local_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mealmemoapp.data.models.Recipe

@Database(entities = [Recipe::class], version = 1, exportSchema = false)
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