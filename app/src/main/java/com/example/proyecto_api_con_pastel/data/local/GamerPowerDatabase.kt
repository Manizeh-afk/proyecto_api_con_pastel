package com.example.proyecto_api_con_pastel.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FavoritoEntity::class, GiveawayCacheEntity::class],
    version = 1,
    exportSchema = false
)
abstract class GamerPowerDatabase : RoomDatabase() {
    abstract fun favoritoDao(): FavoritoDao
    abstract fun giveawayCacheDao(): GiveawayCacheDao

    companion object {
        @Volatile
        private var INSTANCE: GamerPowerDatabase? = null

        fun getDatabase(context: Context): GamerPowerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GamerPowerDatabase::class.java,
                    "gamerpower_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
