package com.andriy_borukh.aplicaciongestiondelibros.ui.busqueda_remoto

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andriy_borukh.aplicaciongestiondelibros.domain.model.Libro
import com.andriy_borukh.aplicaciongestiondelibros.domain.usecase.FavoritoUseCase
import com.andriy_borukh.aplicaciongestiondelibros.domain.usecase.GetLibroUseCase
import com.andriy_borukh.aplicaciongestiondelibros.domain.usecase.ObtenerFavoritosListaUseCase
import UiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel encargado de la lógica de búsqueda remota.
 * @HiltViewModel permite la inyección de dependencias y asegura que los datos
 * no se pierdan al rotar la pantalla o cambiar el lenguaje del sistema.
 */

@HiltViewModel
class BusquedaRemotoViewModel @Inject constructor(
    private val getLibroUseCase: GetLibroUseCase,
    private val favoritoLibroUseCase: FavoritoUseCase,
    private val obtenerFavoritosListaUseCase: ObtenerFavoritosListaUseCase,
    savedStateHandle: SavedStateHandle // Permite recuperar argumentos de navegación (query)
): ViewModel() {

    // Estado interno (mutable) encapsulado para seguir el patrón UDF (Unidirectional Data Flow)
    private val _uiState = MutableStateFlow(BusquedaRemotoState())

    // Estado expuesto a la UI como solo lectura (inmutable)
    val uiState: StateFlow<BusquedaRemotoState> = _uiState.asStateFlow()

    // Variable para controlar la corrutina de búsqueda y poder cancelarla si el usuario lanza una nueva
    private var searchJob: Job? = null

    init {
        /**
         * Recuperación de argumentos: Si entramos a esta pantalla desde un enlace o
         * desde otra pantalla (como Favoritos), extraemos el texto de búsqueda
         */
        val queryDesdeNavegacion: String? = savedStateHandle["query"]

        if (!queryDesdeNavegacion.isNullOrBlank()) {
            _uiState.update { it.copy(texto = queryDesdeNavegacion) }
            buscarLibros()
        }
    }

    /**
     * Actualiza el estado del texto conforme el usuario escribe en el TextField.
     */
    fun onTextoChanged(nuevoTexto: String) {
        _uiState.update { it.copy(texto = nuevoTexto) }
    }

    /**
     * Lógica principal de búsqueda.
     * Se comunica con la API, maneja estados de carga y mapea posibles errores.
     */
    fun buscarLibros() {
        val queryActual = _uiState.value.texto

        // Cancelamos cualquier búsqueda previa para evitar colisiones de datos
        searchJob?.cancel()

        if (queryActual.isBlank()) return

        searchJob = viewModelScope.launch {
            try {
                // Iniciamos estado de carga y limpiamos errores previos
                _uiState.update { it.copy(estaCargando = true, error = null) }

                // Llamada al caso de uso para obtener resultados de la API
                val libros = getLibroUseCase(queryActual)

                if (libros.isEmpty()) {
                    _uiState.update { it.copy(estaCargando = false, error = UiError.SinResultados) }
                    return@launch
                }

                /**
                 * Sincronización de Favoritos:
                 * Al traer libros de Internet, no sabemos si ya los tenemos en nuestra DB
                 * Obtenemos los IDs locales y marcamos los resultados remotos correspondientes
                 */
                val librosFavoritos = obtenerFavoritosListaUseCase()
                val idFavoritos = librosFavoritos.map { it.id }.toSet()

                val listaLibrosActualizada = libros.map { libro ->
                    libro.copy(favorito = idFavoritos.contains(libro.id))
                }

                _uiState.update { it.copy(estaCargando = false, listaLibros = listaLibrosActualizada) }

            } catch (e: Exception) {
                // Mapeo de excepciones técnicas a errores comprensibles por la UI
                val errorDetectado = when {
                    e.message?.contains("429") == true -> UiError.LimiteExcedido
                    e is java.net.UnknownHostException -> UiError.Red
                    e is java.net.SocketTimeoutException -> UiError.TiempoAgotado
                    e.message?.contains("503") == true -> UiError.Servidor
                    else -> UiError.Desconocido
                }
                Log.e("API_ERROR", "Error: ${e.message}")
                _uiState.update { it.copy(estaCargando = false, error = errorDetectado) }
            } finally {
                // Aseguramos que el indicador de carga se detenga siempre
                _uiState.update { it.copy(estaCargando = false) }
            }
        }
    }

    /**
     * Alterna el estado de favorito de un libro.
     * Realiza el cambio en la DB local y actualiza la lista en memoria para feedback instantáneo.
     */
    fun favoritoLibro(libro: Libro) {
        viewModelScope.launch {
            // Ejecutamos la acción en el dominio/data
            favoritoLibroUseCase(libro)

            // Actualización optimista de la UI: cambiamos el estado visual sin esperar a recargar todo
            _uiState.update { estado ->
                val listaActualizada = estado.listaLibros.map {
                    if (it.id == libro.id) it.copy(favorito = !it.favorito) else it
                }
                estado.copy(listaLibros = listaActualizada)
            }
        }
    }
}