package com.andriy_borukh.aplicaciongestiondelibros.ui.detalle_remoto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andriy_borukh.aplicaciongestiondelibros.domain.usecase.GetLibroByIdUseCase
import UiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel que gestiona la visualización de detalles de un libro desde la API.
 * Su ciclo de vida está ligado a la pantalla de detalle remoto.
 */
@HiltViewModel
class DetalleLibroRemotoViewModel @Inject constructor(
    private val getLibroByIdUseCase: GetLibroByIdUseCase
) : ViewModel() {

    // Estado interno mutable encapsulado
    private val _uiState = MutableStateFlow(DetalleLibroRemotoState())
    // Estado expuesto de solo lectura para la composición de Jetpack Compose
    val uiState = _uiState.asStateFlow()

    // Control del trabajo de corrutina para evitar fugas de memoria o peticiones duplicadas
    private var searchJob: Job? = null

    /**
     * Solicita la información extendida de un libro por su identificador único
     *
     * libroId ID del libro (usualmente el ID de Google Books)
     */
    fun cargarDetalle(libroId: String) {
        // Optimización: Si el libro que ya tenemos cargado es el mismo, evitamos una petición innecesaria
        if (_uiState.value.libro?.id == libroId) return

        // Cancelamos cualquier petición de detalle que estuviera en curso
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            // Actualizamos la UI al estado 'Cargando' y limpiamos errores previos
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                // Invocación del Caso de Uso para obtener los datos de la fuente de datos
                val libro = getLibroByIdUseCase(libroId)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        libro = libro
                    )
                }
            } catch (e: Exception) {
                // Mapeo de excepciones de red a tipos de error de interfaz (UiError)
                val errorDetectado = when {
                    e.message?.contains("429") == true -> UiError.LimiteExcedido
                    e.message?.contains("503") == true -> UiError.Servidor
                    // Aquí podrías añadir también UnknownHostException para errores de conexión
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