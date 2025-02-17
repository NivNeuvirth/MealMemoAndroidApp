package com.example.mealmemoapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "recipes")
data class Recipe (
    @PrimaryKey
    val id: Int,
    val image: String,
    val title: String,
    val readyInMinutes: Int,
    val servings: Int,
)