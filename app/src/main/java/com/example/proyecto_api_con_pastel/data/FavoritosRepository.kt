package com.example.proyecto_api_con_pastel.data

import com.example.proyecto_api_con_pastel.data.local.FavoritoDao
import com.example.proyecto_api_con_pastel.model.Giveaway
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritosRepository(
    private val favoritoDao: FavoritoDao
) {
    fun getFavoritos(): Flow<List<Giveaway>> {
        return favoritoDao.getFavoritos().map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun toggleFavorito(giveaway: Giveaway) {
        if (favoritoDao.isFavoritoNow(giveaway.id)) {
            favoritoDao.deleteFavoritoById(giveaway.id)
        } else {
            favoritoDao.insertFavorito(giveaway.toFavoritoEntity())
        }
    }

    fun isFavorito(id: Int): Flow<Boolean> {
        return favoritoDao.isFavorito(id)
    }
}
