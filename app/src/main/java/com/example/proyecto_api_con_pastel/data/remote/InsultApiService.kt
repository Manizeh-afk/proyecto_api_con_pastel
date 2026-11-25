package com.example.proyecto_api_con_pastel.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface InsultApiService {
    @GET("api/insult.json")
    suspend fun getStandardInsult(): Response<InsultResponseDto>

    @GET("api/en_corporate/insult.json")
    suspend fun getCorporateInsult(): Response<InsultResponseDto>
}

interface TranslationApiService {
    @GET("get")
    suspend fun translateText(
        @Query("q") text: String,
        @Query("langpair") langPair: String = "en|es"
    ): Response<MyMemoryTranslationResponseDto>
}
