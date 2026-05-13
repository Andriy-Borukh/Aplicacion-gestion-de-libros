package com.andriy_borukh.aplicaciongestiondelibros.ui.busqueda_remoto

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andriy_borukh.aplicaciongestiondelibros.domain.model.Libro
import com.andriy_borukh.aplicaciongestiondelibros.domain.reposirory.LibroRepository
import com.andriy_borukh.aplicaciongestiondelibros.domain.usecase.FavoritoUseCase
import com.andriy_borukh.aplicaciongestiondelibros.domain.usecase.GetLibroUseCase
import com.andriy_borukh.aplicaciongestiondelibros.ui.error.UiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

//Le dice a Hilt cómo instanciar esta clase y garantiza que el ViewModel sobreviva a cambios de configuración (como rotar la pantalla)
@HiltViewModel
class BusquedaRemotoViewModel @Inject constructor(
    private val getLibroUseCase: GetLibroUseCase,
    private val favoritoLibroUseCase: FavoritoUseCase,
    private val repository: LibroRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    //Estado privado (mutable) para control interno del ViewModel
    private val _uiState = MutableStateFlow(BusquedaRemotoState())

    private var searchJob: Job? = null

    //Estado público (de solo lectura) para que la UI observe los cambios
    val uiState: StateFlow<BusquedaRemotoState> = _uiState.asStateFlow()

    private val _error = MutableStateFlow<UiError?>(null)
    val error = _error.asStateFlow()

    init {
        // Buscamos el parámetro "query" que definiste en la ruta de navegación
        val queryDesdeNavegacion: String? = savedStateHandle["query"]

        if (!queryDesdeNavegacion.isNullOrBlank()) {
            // Actualizamos el texto de la barra y buscamos
            _uiState.update { it.copy(texto = queryDesdeNavegacion) }
            buscarLibros()
        }
    }

    //Cada vez que el usuario escriba el ViewModel recibe el evento y actualiza el estado
    fun onTextoChanged(nuevoTexto: String) {
        _uiState.update { it.copy(texto = nuevoTexto) }
    }

    //Realiza la busqueda de libros a traves del GetLibroUseCase que invoca a su vez el metodo que tiene el repositorio
    //Añade al estado el listado de libros
    fun buscarLibros() {
        val queryActual = _uiState.value.texto

        searchJob?.cancel()

        if (queryActual.isBlank()) return

        searchJob = viewModelScope.launch {
            try {
                _uiState.update { it.copy(estaCargando = true, error = null) }

                val libros = getLibroUseCase(queryActual)

                if (libros.isEmpty()) {
                    _uiState.update { it.copy(estaCargando = false, error = UiError.SinResultados) }
                    return@launch
                }

                val librosFavoritos = repository.obtenerTodosLosFavoritos().first()
                val idFavoritos = librosFavoritos.map { it.id }.toSet()

                val listaLibrosActualizada = libros.map { libro ->
                    libro.copy(favorito = idFavoritos.contains(libro.id))
                }

                _uiState.update { it.copy(estaCargando = false, listaLibros = listaLibrosActualizada) }
            } catch (e: Exception) {
                val errorDetectado = when {
                    e.message?.contains("429") == true -> UiError.LimiteExcedido
                    e is java.net.UnknownHostException -> UiError.Red
                    e is java.net.SocketTimeoutException -> UiError.TiempoAgotado
                    e.message?.contains("503") == true -> UiError.Servidor
                    else -> UiError.Desconocido
                }
                Log.e("API_ERROR", "Error buscando libros: ${e.message}", e)
                _uiState.update { it.copy(estaCargando = false, error = errorDetectado) }
            } finally {
                _uiState.update { it.copy(estaCargando = false) }
            }
        }
    }

    fun favoritoLibro(libro: Libro) {
        viewModelScope.launch {
            favoritoLibroUseCase(libro)

            // Actualizamos la lista en memoria para que el corazón cambie de color
            _uiState.update { estado ->
                val listaActualizada = estado.listaLibros.map {
                    if (it.id == libro.id) it.copy(favorito = !it.favorito) else it
                }
                estado.copy(listaLibros = listaActualizada)
            }
        }
    }

}