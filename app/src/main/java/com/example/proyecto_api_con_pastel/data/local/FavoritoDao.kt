package com.example.proyecto_api_con_pastel.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritoDao {
    @Query("SELECT * FROM favoritos")
    fun getFavoritos(): Flow<List<FavoritoEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favoritos WHERE id = :id)")
    fun isFavorito(id: Int): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favoritos WHERE id = :id)")
    suspend fun isFavoritoNow(id: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorito(favorito: FavoritoEntity)

    @Delete
    suspend fun deleteFavorito(favorito: FavoritoEntity)

    @Query("DELETE FROM favoritos WHERE id = :id")
    suspend fun deleteFavoritoById(id: Int)
}
