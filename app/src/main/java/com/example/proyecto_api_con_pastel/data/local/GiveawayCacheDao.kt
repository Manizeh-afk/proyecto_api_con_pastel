package com.example.proyecto_api_con_pastel.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GiveawayCacheDao {
    @Query("SELECT * FROM giveaway_cache")
    fun getCachedGiveaways(): Flow<List<GiveawayCacheEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(giveaways: List<GiveawayCacheEntity>)

    @Query("DELETE FROM giveaway_cache")
    suspend fun clearCache()
}
