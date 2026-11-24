package com.example.proyecto_api_con_pastel.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FinalSpaceRetrofitClient {
    private const val BASE_URL = "https://finalspaceapi.com/api/v0/"

    val apiService: FinalSpaceApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FinalSpaceApiService::class.java)
    }
}
