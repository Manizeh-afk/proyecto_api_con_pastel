package com.example.proyecto_api_con_pastel.ui.navigation

sealed class Screen(val route: String) {
    object List : Screen("giveaway_list")
    object Detail : Screen("giveaway_detail/{giveawayId}") {
        fun createRoute(giveawayId: Int) = "giveaway_detail/$giveawayId"
    }
    object Favoritos : Screen("favoritos")
}
