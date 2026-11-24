package com.example.proyecto_api_con_pastel

import android.app.Application
import com.example.proyecto_api_con_pastel.data.FavoritosRepository
import com.example.proyecto_api_con_pastel.data.FinalSpaceRepository
import com.example.proyecto_api_con_pastel.data.GamerPowerRepository
import com.example.proyecto_api_con_pastel.data.local.GamerPowerDatabase
import com.example.proyecto_api_con_pastel.data.remote.RetrofitClient

class GamerPowerApplication : Application() {
    val database by lazy { GamerPowerDatabase.getDatabase(this) }
    val gamerPowerRepository by lazy {
        GamerPowerRepository(
            apiService = RetrofitClient.apiService,
            favoritoDao = database.favoritoDao(),
            cacheDao = database.giveawayCacheDao()
        )
    }
    val favoritosRepository by lazy {
        FavoritosRepository(
            favoritoDao = database.favoritoDao()
        )
    }
    val finalSpaceRepository by lazy {
        FinalSpaceRepository(
            apiService = com.example.proyecto_api_con_pastel.data.remote.FinalSpaceRetrofitClient.apiService
        )
    }
}
