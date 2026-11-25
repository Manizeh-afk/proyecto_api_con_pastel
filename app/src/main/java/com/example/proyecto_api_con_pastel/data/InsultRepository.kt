package com.example.proyecto_api_con_pastel.data

import com.example.proyecto_api_con_pastel.data.remote.InsultApiService
import com.example.proyecto_api_con_pastel.data.remote.TranslationApiService
import com.example.proyecto_api_con_pastel.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InsultRepository(
    private val insultApiService: InsultApiService,
    private val translationApiService: TranslationApiService
) {
    fun fetchInsult(isCorporate: Boolean): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = if (isCorporate) {
                insultApiService.getCorporateInsult()
            } else {
                insultApiService.getStandardInsult()
            }

            if (response.isSuccessful && response.body()?.insult != null) {
                val englishInsult = response.body()!!.insult!!
                val spanishInsult = translateToSpanish(englishInsult)
                emit(Resource.Success(spanishInsult))
            } else {
                emit(Resource.Error("Error al obtener insulto (${response.code()})"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error de conexión: ${e.localizedMessage ?: "Verifique su conexión"}"))
        }
    }

    private suspend fun translateToSpanish(text: String): String {
        return try {
            val translationResponse = translationApiService.translateText(text)
            if (translationResponse.isSuccessful && translationResponse.body()?.responseData?.translatedText != null) {
                val translated = translationResponse.body()!!.responseData!!.translatedText!!
                if (translated.isNotBlank()) translated else text
            } else {
                fallbackTranslate(text)
            }
        } catch (e: Exception) {
            fallbackTranslate(text)
        }
    }

    private fun fallbackTranslate(text: String): String {
        // Quick fallback replace for common English insult patterns if offline
        var result = text
        result = result.replace("You are as ", "Eres tan ")
            .replace(" as a ", " como un ")
            .replace(" as an ", " como un ")
            .replace(" as ", " como ")
            .replace("We ", "Nosotros ")
            .replace(" of ", " de ")
        return result
    }
}
