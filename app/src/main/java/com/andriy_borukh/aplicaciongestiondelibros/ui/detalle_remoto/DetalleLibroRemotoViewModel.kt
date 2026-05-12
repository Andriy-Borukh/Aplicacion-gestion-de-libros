package com.andriy_borukh.aplicaciongestiondelibros.ui.detalle_remoto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andriy_borukh.aplicaciongestiondelibros.domain.usecase.GetLibroByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun cargarDetalle(libroId: String) {
        viewModelScope.launch {
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
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "No se pudo cargar el detalle: ${e.message}"
                    )
                }
            }
        }
    }
}