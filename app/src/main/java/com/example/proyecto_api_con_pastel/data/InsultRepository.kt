package com.example.proyecto_api_con_pastel.data

import com.example.proyecto_api_con_pastel.data.remote.InsultApiService
import com.example.proyecto_api_con_pastel.data.remote.TranslationApiService
import com.example.proyecto_api_con_pastel.util.Resource
import kotlinx.coroutines.CancellationException
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

            val englishInsult = response.body()?.insult
            if (response.isSuccessful && !englishInsult.isNullOrBlank()) {
                emit(Resource.Success(translateToSpanish(englishInsult)))
            } else {
                emit(Resource.Error("Error al obtener el texto (${response.code()})"))
            }
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (e: Exception) {
            emit(Resource.Error("Error de conexión: ${e.localizedMessage ?: "Verifique su conexión"}"))
        }
    }

    private suspend fun translateToSpanish(text: String): String {
        return try {
            val translationResponse = translationApiService.translateText(text)
            val translated = translationResponse.body()?.responseData?.translatedText
            if (translationResponse.isSuccessful && !translated.isNullOrBlank()) {
                translated
            } else {
                fallbackTranslate(text)
            }
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (e: Exception) {
            fallbackTranslate(text)
        }
    }

    private fun fallbackTranslate(text: String): String {
        return text.replace("You are as ", "Eres tan ")
            .replace(" as a ", " como un ")
            .replace(" as an ", " como un ")
            .replace(" as ", " como ")
            .replace("We ", "Nosotros ")
            .replace(" of ", " de ")
    }
}
