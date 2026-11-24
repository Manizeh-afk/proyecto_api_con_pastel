package com.example.proyecto_api_con_pastel.data

import com.example.proyecto_api_con_pastel.data.local.FavoritoDao
import com.example.proyecto_api_con_pastel.data.local.GiveawayCacheDao
import com.example.proyecto_api_con_pastel.data.remote.GamerPowerApiService
import com.example.proyecto_api_con_pastel.model.Giveaway
import com.example.proyecto_api_con_pastel.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class GamerPowerRepository(
    private val apiService: GamerPowerApiService,
    private val favoritoDao: FavoritoDao,
    private val cacheDao: GiveawayCacheDao
) {
    fun getGiveaways(platform: String? = null): Flow<Resource<List<Giveaway>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getGiveaways(platform = platform)
            if (response.isSuccessful && response.body() != null) {
                val dtos = response.body()!!
                cacheDao.clearCache()
                cacheDao.insertAll(dtos.map { it.toCacheEntity() })

                val favoriteIds = favoritoDao.getFavoritos().first().map { it.id }.toSet()
                val domainList = dtos.map { dto ->
                    dto.toDomain(isFavorite = favoriteIds.contains(dto.id))
                }
                emit(Resource.Success(domainList))
            } else {
                emitFromCache("Error al cargar datos del servidor (${response.code()})")
            }
        } catch (e: Exception) {
            emitFromCache("Error de conexión: ${e.localizedMessage ?: "Verifique su conexión a internet"}")
        }
    }

    private suspend fun kotlinx.coroutines.flow.FlowCollector<Resource<List<Giveaway>>>.emitFromCache(
        errorMessage: String
    ) {
        val cached = cacheDao.getCachedGiveaways().first()
        if (cached.isNotEmpty()) {
            val favoriteIds = favoritoDao.getFavoritos().first().map { it.id }.toSet()
            val domainList = cached.map { it.toDomain(isFavorite = favoriteIds.contains(it.id)) }
            emit(Resource.Success(domainList))
        } else {
            emit(Resource.Error(errorMessage))
        }
    }

    fun getGiveawayById(id: Int): Flow<Resource<Giveaway>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getGiveawayById(id)
            if (response.isSuccessful && response.body() != null) {
                val isFav = favoritoDao.isFavorito(id).first()
                emit(Resource.Success(response.body()!!.toDomain(isFavorite = isFav)))
            } else {
                // Intento buscar en cache local si falla la red
                val cachedList = cacheDao.getCachedGiveaways().first()
                val cachedItem = cachedList.find { it.id == id }
                if (cachedItem != null) {
                    val isFav = favoritoDao.isFavorito(id).first()
                    emit(Resource.Success(cachedItem.toDomain(isFavorite = isFav)))
                } else {
                    emit(Resource.Error("No se encontró el giveaway con ID $id"))
                }
            }
        } catch (e: Exception) {
            val cachedList = cacheDao.getCachedGiveaways().first()
            val cachedItem = cachedList.find { it.id == id }
            if (cachedItem != null) {
                val isFav = favoritoDao.isFavorito(id).first()
                emit(Resource.Success(cachedItem.toDomain(isFavorite = isFav)))
            } else {
                emit(Resource.Error("Error de conexión: ${e.localizedMessage}"))
            }
        }
    }
}
