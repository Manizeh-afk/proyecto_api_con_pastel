package com.example.proyecto_api_con_pastel.data

import com.example.proyecto_api_con_pastel.data.remote.FinalSpaceApiService
import com.example.proyecto_api_con_pastel.model.FinalSpaceCharacter
import com.example.proyecto_api_con_pastel.util.Resource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FinalSpaceRepository(
    private val apiService: FinalSpaceApiService
) {
    fun getCharacters(): Flow<Resource<List<FinalSpaceCharacter>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getCharacters()
            if (response.isSuccessful && response.body() != null) {
                val characters = response.body().orEmpty().map { dto ->
                    FinalSpaceCharacter(
                        id = dto.id,
                        name = dto.name ?: "Unknown",
                        status = dto.status ?: "Unknown",
                        species = dto.species ?: "Unknown",
                        gender = dto.gender ?: "Unknown",
                        hair = dto.hair ?: "Unknown",
                        imgUrl = dto.imgUrl ?: ""
                    )
                }
                emit(Resource.Success(characters))
            } else {
                emit(Resource.Error("Error al cargar personajes (${response.code()})"))
            }
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (e: Exception) {
            emit(Resource.Error("Error de conexión: ${e.localizedMessage ?: "Verifique su conexión"}"))
        }
    }
}
