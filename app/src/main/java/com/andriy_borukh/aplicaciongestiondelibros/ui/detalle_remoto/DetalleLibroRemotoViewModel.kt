package com.andriy_borukh.aplicaciongestiondelibros.ui.detalle_remoto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andriy_borukh.aplicaciongestiondelibros.domain.reposirory.LibroRepository
import com.andriy_borukh.aplicaciongestiondelibros.domain.usecase.GetLibroByIdUseCase
import com.andriy_borukh.aplicaciongestiondelibros.ui.error.UiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetalleLibroRemotoViewModel @Inject constructor(
    private val getLibroByIdUseCase: GetLibroByIdUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(DetalleLibroRemotoState())
    val uiState = _uiState.asStateFlow()


    private var searchJob: Job ?= null

    fun cargarDetalle(libroId: String) {

        if (_uiState.value.libro?.id == libroId) return

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            // Indicamos que estamos cargando
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                // Llamamos al caso de uso para obtener el libro
                val libro = getLibroByIdUseCase(libroId)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        libro = libro
                    )
                }
            } catch (e: Exception) {

                val errorDetectado = when {
                    e.message?.contains("429") == true -> UiError.LimiteExcedido
                    e.message?.contains("503") == true -> UiError.Servidor
                    else -> UiError.Desconocido
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = errorDetectado,
                        libro = null
                    )
                }
            }
        }
    }
}