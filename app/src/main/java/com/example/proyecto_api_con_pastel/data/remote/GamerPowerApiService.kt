package com.example.proyecto_api_con_pastel.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GamerPowerApiService {
    @GET("giveaways")
    suspend fun getGiveaways(
        @Query("platform") platform: String? = null,
        @Query("sort-by") sortBy: String? = null
    ): Response<List<GiveawayDto>>

    @GET("giveaway")
    suspend fun getGiveawayById(
        @Query("id") id: Int
    ): Response<GiveawayDto>
}
