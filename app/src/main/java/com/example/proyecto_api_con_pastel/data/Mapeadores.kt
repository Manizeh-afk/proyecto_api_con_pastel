package com.example.proyecto_api_con_pastel.data

import com.example.proyecto_api_con_pastel.data.local.FavoritoEntity
import com.example.proyecto_api_con_pastel.data.local.GiveawayCacheEntity
import com.example.proyecto_api_con_pastel.data.remote.GiveawayDto
import com.example.proyecto_api_con_pastel.model.Giveaway

fun GiveawayDto.toDomain(isFavorite: Boolean = false): Giveaway {
    return Giveaway(
        id = id,
        title = title ?: "Sin título",
        worth = worth ?: "Gratis",
        thumbnail = thumbnail ?: "",
        image = image ?: "",
        description = description ?: "Sin descripción disponible.",
        instructions = instructions ?: "Sigue las instrucciones en la página oficial.",
        openGiveawayUrl = openGiveawayUrl ?: gamerpowerUrl ?: "",
        publishedDate = publishedDate ?: "",
        type = type ?: "Juego",
        platforms = platforms ?: "PC",
        endDate = endDate ?: "N/A",
        status = status ?: "Activo",
        isFavorite = isFavorite
    )
}

fun Giveaway.toFavoritoEntity(): FavoritoEntity {
    return FavoritoEntity(
        id = id,
        title = title,
        worth = worth,
        thumbnail = thumbnail,
        image = image,
        description = description,
        instructions = instructions,
        openGiveawayUrl = openGiveawayUrl,
        publishedDate = publishedDate,
        type = type,
        platforms = platforms,
        endDate = endDate,
        status = status
    )
}

fun FavoritoEntity.toDomain(): Giveaway {
    return Giveaway(
        id = id,
        title = title,
        worth = worth,
        thumbnail = thumbnail,
        image = image,
        description = description,
        instructions = instructions,
        openGiveawayUrl = openGiveawayUrl,
        publishedDate = publishedDate,
        type = type,
        platforms = platforms,
        endDate = endDate,
        status = status,
        isFavorite = true
    )
}

fun GiveawayDto.toCacheEntity(): GiveawayCacheEntity {
    return GiveawayCacheEntity(
        id = id,
        title = title ?: "Sin título",
        worth = worth ?: "Gratis",
        thumbnail = thumbnail ?: "",
        image = image ?: "",
        description = description ?: "Sin descripción disponible.",
        instructions = instructions ?: "",
        openGiveawayUrl = openGiveawayUrl ?: gamerpowerUrl ?: "",
        publishedDate = publishedDate ?: "",
        type = type ?: "Juego",
        platforms = platforms ?: "PC",
        endDate = endDate ?: "N/A",
        status = status ?: "Activo"
    )
}

fun GiveawayCacheEntity.toDomain(isFavorite: Boolean = false): Giveaway {
    return Giveaway(
        id = id,
        title = title,
        worth = worth,
        thumbnail = thumbnail,
        image = image,
        description = description,
        instructions = instructions,
        openGiveawayUrl = openGiveawayUrl,
        publishedDate = publishedDate,
        type = type,
        platforms = platforms,
        endDate = endDate,
        status = status,
        isFavorite = isFavorite
    )
}
