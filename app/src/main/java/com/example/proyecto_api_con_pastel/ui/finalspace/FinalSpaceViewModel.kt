package com.example.proyecto_api_con_pastel.ui.finalspace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.proyecto_api_con_pastel.data.FinalSpaceRepository
import com.example.proyecto_api_con_pastel.model.FinalSpaceCharacter
import com.example.proyecto_api_con_pastel.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

data class FinalSpaceUiState(
    val characters: List<FinalSpaceCharacter> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class FinalSpaceViewModel(
    private val repository: FinalSpaceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FinalSpaceUiState())
    val uiState: StateFlow<FinalSpaceUiState> = _uiState.asStateFlow()

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        repository.getCharacters().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                }
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        characters = result.data ?: emptyList(),
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

    class Factory(
        private val repository: FinalSpaceRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FinalSpaceViewModel(repository) as T
        }
    }
}
