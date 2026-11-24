package com.example.proyecto_api_con_pastel.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyecto_api_con_pastel.GamerPowerApplication
import com.example.proyecto_api_con_pastel.ui.detail.DetailScreen
import com.example.proyecto_api_con_pastel.ui.detail.DetailViewModel
import com.example.proyecto_api_con_pastel.ui.favoritos.FavoritosScreen
import com.example.proyecto_api_con_pastel.ui.favoritos.FavoritosViewModel
import com.example.proyecto_api_con_pastel.ui.finalspace.FinalSpaceScreen
import com.example.proyecto_api_con_pastel.ui.finalspace.FinalSpaceViewModel
import com.example.proyecto_api_con_pastel.ui.list.ListScreen
import com.example.proyecto_api_con_pastel.ui.list.ListViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val app = LocalContext.current.applicationContext as GamerPowerApplication

    NavHost(
        navController = navController,
        startDestination = Screen.List.route
    ) {
        composable(route = Screen.List.route) {
            val listViewModel: ListViewModel = viewModel(
                factory = ListViewModel.Factory(
                    gamerPowerRepository = app.gamerPowerRepository,
                    favoritosRepository = app.favoritosRepository
                )
            )
            ListScreen(
                viewModel = listViewModel,
                onGiveawayClick = { giveawayId ->
                    navController.navigate(Screen.Detail.createRoute(giveawayId))
                },
                onFavoritesClick = {
                    navController.navigate(Screen.Favoritos.route)
                },
                onFinalSpaceClick = {
                    navController.navigate(Screen.FinalSpace.route)
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("giveawayId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val giveawayId = backStackEntry.arguments?.getInt("giveawayId") ?: 0
            val detailViewModel: DetailViewModel = viewModel(
                factory = DetailViewModel.Factory(
                    giveawayId = giveawayId,
                    gamerPowerRepository = app.gamerPowerRepository,
                    favoritosRepository = app.favoritosRepository
                )
            )
            DetailScreen(
                viewModel = detailViewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Screen.Favoritos.route) {
            val favoritosViewModel: FavoritosViewModel = viewModel(
                factory = FavoritosViewModel.Factory(
                    favoritosRepository = app.favoritosRepository
                )
            )
            FavoritosScreen(
                viewModel = favoritosViewModel,
                onGiveawayClick = { giveawayId ->
                    navController.navigate(Screen.Detail.createRoute(giveawayId))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Screen.FinalSpace.route) {
            val finalSpaceViewModel: FinalSpaceViewModel = viewModel(
                factory = FinalSpaceViewModel.Factory(
                    repository = app.finalSpaceRepository
                )
            )
            FinalSpaceScreen(
                viewModel = finalSpaceViewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
