package com.example.mealmemoapp.utilities

import android.util.Log
import com.example.mealmemoapp.data.remote.api.TranslatorApiService
import com.example.mealmemoapp.data.models.TranslatorRequest
import com.example.mealmemoapp.data.models.TranslatorResponse
import com.example.mealmemoapp.utilities.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale
import javax.inject.Inject

class TranslationHelper @Inject constructor(
    private val translatorApiService: TranslatorApiService // Inject the TranslatorApiService
) {

    fun translateData(
        names: List<String>,
        summaries: List<String>,
        ingredients: List<String>,
        onTranslated: (List<String>, List<String>, List<String>) -> Unit
    ) {
        val deviceLanguage = Locale.getDefault().language // Get device language

        if (deviceLanguage != "iw") {
            onTranslated(names, summaries, ingredients)
            return
        }

        val requestNames = names.map { TranslatorRequest(it) }
        val requestSummaries = summaries.map { TranslatorRequest(it) }
        val requestIngredients = ingredients.map { TranslatorRequest(it) }

        translatorApiService.translateText(Constants.AZURE_TRANSLATOR_API,
            "eastus",
            requestNames).enqueue(object : Callback<List<TranslatorResponse>> {
            override fun onResponse(
                call: Call<List<TranslatorResponse>>,
                response: Response<List<TranslatorResponse>>
            ) {
                val translatedNames = if (response.isSuccessful) {
                    response.body()?.flatMap { it.translations.map { translation -> translation.text } }
                        ?: emptyList()
                } else {
                    Log.e("TranslationHelper", "Translation API failed: ${response.errorBody()?.string()}")
                    emptyList()
                }

                translatorApiService.translateText(Constants.AZURE_TRANSLATOR_API,
                    "eastus",
                    requestSummaries).enqueue(object : Callback<List<TranslatorResponse>> {
                    override fun onResponse(
                        call: Call<List<TranslatorResponse>>,
                        response: Response<List<TranslatorResponse>>
                    ) {
                        val translatedSummaries = if (response.isSuccessful) {
                            response.body()?.flatMap { it.translations.map { translation -> translation.text } }
                                ?: emptyList()
                        } else {
                            Log.e("TranslationHelper", "Translation API failed: ${response.errorBody()?.string()}")
                            emptyList()
                        }

                        translatorApiService.translateText(Constants.AZURE_TRANSLATOR_API,
                            "eastus",
                            requestIngredients).enqueue(object : Callback<List<TranslatorResponse>> {
                            override fun onResponse(
                                call: Call<List<TranslatorResponse>>,
                                response: Response<List<TranslatorResponse>>
                            ) {
                                val translatedIngredients = if (response.isSuccessful) {
                                    response.body()?.flatMap { it.translations.map { translation -> translation.text } }
                                        ?: emptyList()
                                } else {
                                    Log.e("TranslationHelper", "Translation API failed: ${response.errorBody()?.string()}")
                                    emptyList()
                                }

                                onTranslated(translatedNames, translatedSummaries, translatedIngredients)
                            }

                            override fun onFailure(call: Call<List<TranslatorResponse>>, t: Throwable) {
                                Log.e("TranslationHelper", "Translation API call failed: ${t.message}")
                                onTranslated(names, summaries, ingredients)
                            }
                        })
                    }

                    override fun onFailure(call: Call<List<TranslatorResponse>>, t: Throwable) {
                        Log.e("TranslationHelper", "Translation API call failed: ${t.message}")
                        onTranslated(names, summaries, ingredients)
                    }
                })
            }

            override fun onFailure(call: Call<List<TranslatorResponse>>, t: Throwable) {
                Log.e("TranslationHelper", "Translation API call failed: ${t.message}")
                onTranslated(names, summaries, ingredients)
            }
        })
    }
}