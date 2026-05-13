package com.andriy_borukh.aplicaciongestiondelibros.ui.detalle_local

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andriy_borukh.aplicaciongestiondelibros.domain.usecase.ActualizarLibroUseCase
import com.andriy_borukh.aplicaciongestiondelibros.domain.usecase.GetLibroByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DetalleLibroLocalViewModel @Inject constructor(
    private val getLibroByIdUseCase: GetLibroByIdUseCase,
    private val actualizarLibroUseCase: ActualizarLibroUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetalleLibroLocalState())
    val uiState: StateFlow<DetalleLibroLocalState> = _uiState.asStateFlow()

    private val libroId: String = checkNotNull(savedStateHandle["libroId"])


    init {
        cargarLibro()
    }

    fun cargarLibro() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val libro = getLibroByIdUseCase(libroId)

            _uiState.update { it.copy(
                isLoading = false,
                libro = libro,
                nuevaPagina = libro?.paginaActual?.toString() ?: "0",
                nuevaValoracion = libro?.valoracion ?: 0.0,
                nuevoComentario = libro?.comentario ?: "",
                nuevaFechaInicio = libro?.fechaInicio,
                nuevaFechaFin = libro?.fechaFin
            ) }
        }
    }

    fun onValoracionChange(valor: Double) {
        _uiState.update { it.copy(nuevaValoracion = valor) }
    }

    fun onComentarioChange(texto: String) {
        _uiState.update { it.copy(nuevoComentario = texto) }
    }

    fun onPaginaChange(pagina: String) {
        if (pagina.all { it.isDigit() }) {
            _uiState.update { it.copy(nuevaPagina = pagina) }
        }
    }

    fun establecerFechaInicio(fecha: Date) {
        _uiState.update { it.copy(nuevaFechaInicio = fecha) }

        guardarCambios()
    }

    fun establecerFechaFin(fecha: Date) {
        _uiState.update { it.copy(nuevaFechaFin = fecha) }

        guardarCambios()
    }

    fun toggleDialogo(mostrar: Boolean) {
        _uiState.update { it.copy(mostrarDialogoConfirmacion = mostrar) }
    }

    fun guardarCambios() {
        val libroActual = _uiState.value.libro ?: return
        val libroEditado = libroActual.copy(
            valoracion = _uiState.value.nuevaValoracion,
            comentario = _uiState.value.nuevoComentario,
            paginaActual = _uiState.value.nuevaPagina.toIntOrNull() ?: 0,
            fechaInicio = _uiState.value.nuevaFechaInicio,
            fechaFin = _uiState.value.nuevaFechaFin
        )

        viewModelScope.launch {
            actualizarLibroUseCase(libroEditado)
            _uiState.update { it.copy(libro = libroEditado) }
        }
    }
}