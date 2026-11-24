package com.example.proyecto_api_con_pastel.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface FinalSpaceApiService {
    @GET("character")
    suspend fun getCharacters(): Response<List<FinalSpaceCharacterDto>>
}
