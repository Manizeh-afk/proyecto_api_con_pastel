package com.example.proyecto_api_con_pastel.data.remote

import com.google.gson.annotations.SerializedName

data class GiveawayDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String?,
    @SerializedName("worth") val worth: String?,
    @SerializedName("thumbnail") val thumbnail: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("instructions") val instructions: String?,
    @SerializedName("open_giveaway_url") val openGiveawayUrl: String?,
    @SerializedName("published_date") val publishedDate: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("platforms") val platforms: String?,
    @SerializedName("end_date") val endDate: String?,
    @SerializedName("users") val users: Int?,
    @SerializedName("status") val status: String?,
    @SerializedName("gamerpower_url") val gamerpowerUrl: String?
)
