package com.example.proyecto_api_con_pastel.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.proyecto_api_con_pastel.data.FavoritosRepository
import com.example.proyecto_api_con_pastel.data.GamerPowerRepository
import com.example.proyecto_api_con_pastel.model.Giveaway
import com.example.proyecto_api_con_pastel.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class ListUiState(
    val giveaways: List<Giveaway> = emptyList(),
    val filteredGiveaways: List<Giveaway> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedPlatform: String? = null,
    val searchQuery: String = ""
)

class ListViewModel(
    private val gamerPowerRepository: GamerPowerRepository,
    private val favoritosRepository: FavoritosRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListUiState())
    val uiState: StateFlow<ListUiState> = _uiState.asStateFlow()
    private var giveawaysJob: Job? = null

    init {
        loadGiveaways()
        observeFavoritos()
    }

    fun loadGiveaways(platform: String? = _uiState.value.selectedPlatform) {
        _uiState.value = _uiState.value.copy(
            selectedPlatform = platform,
            isLoading = true,
            errorMessage = null
        )

        giveawaysJob?.cancel()
        giveawaysJob = gamerPowerRepository.getGiveaways(platform)
            .onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                    }
                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            giveaways = result.data.orEmpty(),
                            isLoading = false,
                            errorMessage = null
                        )
                        applyFilterAndSearch()
                    }
                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeFavoritos() {
        favoritosRepository.getFavoritos().onEach { favList ->
            val favIds = favList.map { it.id }.toSet()
            val updated = _uiState.value.giveaways.map { giveaway ->
                giveaway.copy(isFavorite = favIds.contains(giveaway.id))
            }
            _uiState.value = _uiState.value.copy(giveaways = updated)
            applyFilterAndSearch()
        }.launchIn(viewModelScope)
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        applyFilterAndSearch()
    }

    fun onPlatformSelected(platform: String?) {
        val newPlatform = if (_uiState.value.selectedPlatform == platform) null else platform
        loadGiveaways(newPlatform)
    }

    fun toggleFavorite(giveaway: Giveaway) {
        viewModelScope.launch {
            favoritosRepository.toggleFavorito(giveaway)
        }
    }

    private fun applyFilterAndSearch() {
        val query = _uiState.value.searchQuery.trim().lowercase()
        val currentList = _uiState.value.giveaways
        val filtered = if (query.isEmpty()) {
            currentList
        } else {
            currentList.filter {
                it.title.contains(query, ignoreCase = true) ||
                    it.platforms.contains(query, ignoreCase = true) ||
                    it.type.contains(query, ignoreCase = true)
            }
        }
        _uiState.value = _uiState.value.copy(filteredGiveaways = filtered)
    }

    class Factory(
        private val gamerPowerRepository: GamerPowerRepository,
        private val favoritosRepository: FavoritosRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ListViewModel(gamerPowerRepository, favoritosRepository) as T
        }
    }
}
