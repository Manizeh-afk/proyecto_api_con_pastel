package com.example.proyecto_api_con_pastel.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyecto_api_con_pastel.ui.components.ErrorView
import com.example.proyecto_api_con_pastel.ui.components.GiveawayCard
import com.example.proyecto_api_con_pastel.ui.components.LoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    viewModel: ListViewModel,
    onGiveawayClick: (Int) -> Unit,
    onFavoritesClick: () -> Unit,
    onFinalSpaceClick: () -> Unit,
    onInsultChatClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val platforms = listOf("pc", "steam", "ps4", "xbox-one", "switch", "android")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GamerPower Giveaways") },
                actions = {
                    IconButton(onClick = onInsultChatClick) {
                        Icon(
                            imageVector = Icons.Default.SentimentVeryDissatisfied,
                            contentDescription = "Chat de Insultos",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    IconButton(onClick = onFinalSpaceClick) {
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = "Final Space",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    IconButton(onClick = onFavoritesClick) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favoritos",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Buscar juegos, plataformas...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar"
                    )
                },
                singleLine = true
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(platforms) { platform ->
                    val isSelected = state.selectedPlatform == platform
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.onPlatformSelected(platform) },
                        label = { Text(platform.uppercase()) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            when {
                state.isLoading -> {
                    LoadingIndicator(message = "Cargando ofertas de videojuegos...")
                }
                state.errorMessage != null -> {
                    ErrorView(
                        message = state.errorMessage!!,
                        onRetry = { viewModel.loadGiveaways() }
                    )
                }
                state.filteredGiveaways.isEmpty() -> {
                    Text(
                        text = "No se encontraron giveaways.",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(
                            items = state.filteredGiveaways,
                            key = { it.id }
                        ) { giveaway ->
                            GiveawayCard(
                                giveaway = giveaway,
                                onClick = { onGiveawayClick(giveaway.id) },
                                onFavoriteToggle = { viewModel.toggleFavorite(giveaway) }
                            )
                        }
                    }
                }
            }
        }
    }
}
