package com.example.mealmemoapp.data.models

data class TranslatorRequest(
    val Text: String
)

data class TranslatorResponse(
    val translations: List<Translation>
)

data class Translation(
    val text: String,
    val to: String
)