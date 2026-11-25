package com.example.proyecto_api_con_pastel.data.remote

import com.google.gson.annotations.SerializedName

data class InsultResponseDto(
    @SerializedName("error") val error: Boolean? = false,
    @SerializedName("insult") val insult: String? = null
)

data class MyMemoryTranslationResponseDto(
    @SerializedName("responseData") val responseData: ResponseDataDto? = null,
    @SerializedName("responseStatus") val responseStatus: Int? = null
)

data class ResponseDataDto(
    @SerializedName("translatedText") val translatedText: String? = null
)
