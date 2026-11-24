package com.example.proyecto_api_con_pastel.ui.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.proyecto_api_con_pastel.data.FavoritosRepository
import com.example.proyecto_api_con_pastel.model.Giveaway
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritosViewModel(
    private val favoritosRepository: FavoritosRepository
) : ViewModel() {

    val favoritosState: StateFlow<List<Giveaway>> = favoritosRepository.getFavoritos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleFavorite(giveaway: Giveaway) {
        viewModelScope.launch {
            favoritosRepository.toggleFavorito(giveaway)
        }
    }

    class Factory(
        private val favoritosRepository: FavoritosRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavoritosViewModel(favoritosRepository) as T
        }
    }
}
