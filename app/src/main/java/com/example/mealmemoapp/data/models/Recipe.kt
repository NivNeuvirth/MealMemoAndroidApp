package com.example.mealmemoapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "recipes")
data class Recipe (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val image: String,
    val title: String,
    val readyInMinutes: Int,
    val servings: Int,
)