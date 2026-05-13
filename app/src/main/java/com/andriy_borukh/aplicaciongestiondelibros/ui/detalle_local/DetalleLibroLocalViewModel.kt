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

/**
 * ViewModel que gestiona la lógica de la pantalla de detalles de un libro local
 * Se encarga de la carga inicial, validación de datos de entrada y persistencia de cambios
 */
@HiltViewModel
class DetalleLibroLocalViewModel @Inject constructor(
    private val getLibroByIdUseCase: GetLibroByIdUseCase,
    private val actualizarLibroUseCase: ActualizarLibroUseCase,
    savedStateHandle: SavedStateHandle // Recibe los argumentos de navegación de forma segura
) : ViewModel() {

    // Estado interno mutable (Single Source of Truth para esta pantalla)
    private val _uiState = MutableStateFlow(DetalleLibroLocalState())
    // Estado expuesto a la vista como flujo de solo lectura
    val uiState: StateFlow<DetalleLibroLocalState> = _uiState.asStateFlow()

    // Recuperamos el ID del libro pasado por la ruta de navegación.
    // checkNotNull asegura que si el ID no existe, la app lance una excepción controlada en desarrollo
    private val libroId: String = checkNotNull(savedStateHandle["libroId"])

    init {
        cargarLibro()
    }

    /**
     * Recupera la información del libro desde la base de datos local (Room)
     * e inicializa los campos del formulario con los valores actuales
     */
    fun cargarLibro() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val libro = getLibroByIdUseCase(libroId)

            _uiState.update { it.copy(
                isLoading = false,
                libro = libro,
                // Inicializamos los estados de edición con los datos del libro cargado
                nuevaPagina = libro?.paginaActual?.toString() ?: "0",
                nuevaValoracion = libro?.valoracion ?: 0.0,
                nuevoComentario = libro?.comentario ?: "",
                nuevaFechaInicio = libro?.fechaInicio,
                nuevaFechaFin = libro?.fechaFin
            ) }
        }
    }

    // --- MÉTODOS DE ACTUALIZACIÓN DEL ESTADO (UI EVENTS) ---

    fun onValoracionChange(valor: Double) {
        _uiState.update { it.copy(nuevaValoracion = valor) }
    }

    fun onComentarioChange(texto: String) {
        _uiState.update { it.copy(nuevoComentario = texto) }
    }

    /**
     * Actualiza la página actual asegurando que solo se introduzcan dígitos
     */
    fun onPaginaChange(pagina: String) {
        if (pagina.all { it.isDigit() }) {
            _uiState.update { it.copy(nuevaPagina = pagina) }
        }
    }

    fun establecerFechaInicio(fecha: Date) {
        _uiState.update { it.copy(nuevaFechaInicio = fecha) }
        // Nota: Guardar aquí es opcional, depende de si quieres auto-guardado o esperar al botón 'Check'
        guardarCambios()
    }

    fun establecerFechaFin(fecha: Date) {
        _uiState.update { it.copy(nuevaFechaFin = fecha) }
        guardarCambios()
    }

    fun toggleDialogo(mostrar: Boolean) {
        _uiState.update { it.copy(mostrarDialogoConfirmacion = mostrar) }
    }

    /**
     * Toma los valores temporales del estado, crea una nueva instancia del modelo
     * 'Libro' y solicita al caso de uso que actualice la base de datos
     */
    fun guardarCambios() {
        val libroActual = _uiState.value.libro ?: return

        // Creamos una copia del libro original con los nuevos valores del formulario
        val libroEditado = libroActual.copy(
            valoracion = _uiState.value.nuevaValoracion,
            comentario = _uiState.value.nuevoComentario,
            paginaActual = _uiState.value.nuevaPagina.toIntOrNull() ?: 0,
            fechaInicio = _uiState.value.nuevaFechaInicio,
            fechaFin = _uiState.value.nuevaFechaFin
        )

        viewModelScope.launch {
            // Persistencia en Room
            actualizarLibroUseCase(libroEditado)
            // Actualizamos el estado local para reflejar que el libro "original" ahora es el editado
            _uiState.update { it.copy(libro = libroEditado) }
        }
    }
}