package com.example.proyecto_api_con_pastel.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoritos")
data class FavoritoEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val worth: String,
    val thumbnail: String,
    val image: String,
    val description: String,
    val instructions: String,
    val openGiveawayUrl: String,
    val publishedDate: String,
    val type: String,
    val platforms: String,
    val endDate: String,
    val status: String
)
