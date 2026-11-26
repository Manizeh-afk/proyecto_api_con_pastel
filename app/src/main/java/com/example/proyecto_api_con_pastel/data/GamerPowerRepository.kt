package com.example.proyecto_api_con_pastel.data

import com.example.proyecto_api_con_pastel.data.local.FavoritoDao
import com.example.proyecto_api_con_pastel.data.local.GiveawayCacheDao
import com.example.proyecto_api_con_pastel.data.remote.GamerPowerApiService
import com.example.proyecto_api_con_pastel.model.Giveaway
import com.example.proyecto_api_con_pastel.util.Resource
import kotlinx.coroutines.CancellationException
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
                val dtos = response.body().orEmpty()
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
        } catch (cancelled: CancellationException) {
            throw cancelled
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
                emitCachedGiveaway(id, "No se encontró el giveaway con ID $id")
            }
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (e: Exception) {
            emitCachedGiveaway(id, "Error de conexión: ${e.localizedMessage ?: "Verifique su conexión"}")
        }
    }

    private suspend fun kotlinx.coroutines.flow.FlowCollector<Resource<Giveaway>>.emitCachedGiveaway(
        id: Int,
        errorMessage: String
    ) {
        val cachedItem = cacheDao.getCachedGiveaways().first().find { it.id == id }
        if (cachedItem == null) {
            emit(Resource.Error(errorMessage))
        } else {
            val isFav = favoritoDao.isFavorito(id).first()
            emit(Resource.Success(cachedItem.toDomain(isFavorite = isFav)))
        }
    }
}
