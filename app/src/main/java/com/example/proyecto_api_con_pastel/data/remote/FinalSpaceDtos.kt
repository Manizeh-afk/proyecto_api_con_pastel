package com.example.proyecto_api_con_pastel.data.remote

import com.google.gson.annotations.SerializedName

data class FinalSpaceCharacterDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("species") val species: String?,
    @SerializedName("gender") val gender: String?,
    @SerializedName("hair") val hair: String?,
    @SerializedName("img_url") val imgUrl: String?
)
