package com.example.proyecto_api_con_pastel.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object InsultRetrofitClient {
    private const val INSULT_BASE_URL = "https://insult.mattbas.org/"
    private const val TRANSLATION_BASE_URL = "https://api.mymemory.translated.net/"

    val insultApiService: InsultApiService by lazy {
        Retrofit.Builder()
            .baseUrl(INSULT_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(InsultApiService::class.java)
    }

    val translationApiService: TranslationApiService by lazy {
        Retrofit.Builder()
            .baseUrl(TRANSLATION_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TranslationApiService::class.java)
    }
}
