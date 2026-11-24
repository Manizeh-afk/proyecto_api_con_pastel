package com.example.proyecto_api_con_pastel.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.proyecto_api_con_pastel.data.FavoritosRepository
import com.example.proyecto_api_con_pastel.data.GamerPowerRepository
import com.example.proyecto_api_con_pastel.model.Giveaway
import com.example.proyecto_api_con_pastel.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class DetailUiState(
    val giveaway: Giveaway? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class DetailViewModel(
    private val giveawayId: Int,
    private val gamerPowerRepository: GamerPowerRepository,
    private val favoritosRepository: FavoritosRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        loadDetail()
        observeFavoritoStatus()
    }

    fun loadDetail() {
        gamerPowerRepository.getGiveawayById(giveawayId).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                }
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        giveaway = result.data,
                        isLoading = false,
                        errorMessage = null
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun observeFavoritoStatus() {
        favoritosRepository.isFavorito(giveawayId).onEach { isFav ->
            val current = _uiState.value.giveaway
            if (current != null) {
                _uiState.value = _uiState.value.copy(
                    giveaway = current.copy(isFavorite = isFav)
                )
            }
        }.launchIn(viewModelScope)
    }

    fun toggleFavorite() {
        val current = _uiState.value.giveaway ?: return
        viewModelScope.launch {
            favoritosRepository.toggleFavorito(current)
        }
    }

    class Factory(
        private val giveawayId: Int,
        private val gamerPowerRepository: GamerPowerRepository,
        private val favoritosRepository: FavoritosRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DetailViewModel(giveawayId, gamerPowerRepository, favoritosRepository) as T
        }
    }
}
