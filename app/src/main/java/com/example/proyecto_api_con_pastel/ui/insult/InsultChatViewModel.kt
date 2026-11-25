package com.example.proyecto_api_con_pastel.ui.insult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.proyecto_api_con_pastel.data.InsultRepository
import com.example.proyecto_api_con_pastel.model.ChatMessage
import com.example.proyecto_api_con_pastel.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

data class InsultChatUiState(
    val messages: List<ChatMessage> = listOf(
        ChatMessage(
            text = "¡Hola! Presiona un botón abajo para generar un chiste o insulto en español.",
            isUser = false
        )
    ),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class InsultChatViewModel(
    private val repository: InsultRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InsultChatUiState())
    val uiState: StateFlow<InsultChatUiState> = _uiState.asStateFlow()

    fun generateInsult(isCorporate: Boolean) {
        if (_uiState.value.isLoading) return

        val userPrompt = if (isCorporate) {
            "Generar insulto corporativo"
        } else {
            "Generar insulto común"
        }

        val userMessage = ChatMessage(text = userPrompt, isUser = true, isCorporate = isCorporate)
        val currentMessages = _uiState.value.messages + userMessage

        _uiState.value = _uiState.value.copy(
            messages = currentMessages,
            isLoading = true,
            errorMessage = null
        )

        repository.fetchInsult(isCorporate).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    // Carga en progreso
                }
                is Resource.Success -> {
                    val botText = result.data ?: "No se pudo obtener el insulto."
                    val botMessage = ChatMessage(text = botText, isUser = false, isCorporate = isCorporate)
                    _uiState.value = _uiState.value.copy(
                        messages = _uiState.value.messages + botMessage,
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

    fun clearChat() {
        _uiState.value = InsultChatUiState()
    }

    class Factory(
        private val repository: InsultRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return InsultChatViewModel(repository) as T
        }
    }
}
