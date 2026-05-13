package com.andriy_borukh.aplicaciongestiondelibros.ui.lista_favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andriy_borukh.aplicaciongestiondelibros.domain.model.Libro
import com.andriy_borukh.aplicaciongestiondelibros.domain.usecase.EliminarTodosUseCase
import com.andriy_borukh.aplicaciongestiondelibros.domain.usecase.FavoritoUseCase
import com.andriy_borukh.aplicaciongestiondelibros.domain.usecase.GetFavoritosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel encargado de gestionar la lógica de la biblioteca personal.
 * Utiliza programación reactiva para mantener la UI sincronizada con la base de datos.
 */
@HiltViewModel
class ListaFavoritosViewModel @Inject constructor(
    private val getFavoritosUseCase: GetFavoritosUseCase,
    private val favoritoUseCase: FavoritoUseCase,
    private val eliminarTodosUseCase: EliminarTodosUseCase
): ViewModel() {

    // Estado para la carga inicial o estados generales de la pantalla
    private val _uiState = MutableStateFlow(ListaFavoritosState())
    val uiState = _uiState.asStateFlow()

    // Estado independiente para el texto de búsqueda (Buffer de entrada)
    private val _texto_busqueda = MutableStateFlow("")
    val texto = _texto_busqueda.asStateFlow()

    init {
        observarFavoritos()
    }

    /**
     * FLUJO COMBINADO: Es el núcleo de la pantalla.
     * Combina el texto de búsqueda con la lista real de la base de datos.
     * Si el usuario escribe o si un libro se borra, este flujo se recalcula automáticamente.
     */
    val favoritosFiltrados = combine(
        _texto_busqueda,
        getFavoritosUseCase() // Flow directo desde Room
    ) { texto, lista ->
        if (texto.isBlank()) {
            lista
        } else {
            // Filtrado local insensible a mayúsculas/minúsculas
            lista.filter { libro ->
                libro.titulo.contains(texto, ignoreCase = true) ||
                        libro.autores.contains(texto, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        // Mantiene el flujo activo 5 segundos después de que la UI desaparezca (optimización de recursos)
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    /**
     * Actualiza el valor del filtro de búsqueda.
     */
    fun onTextoBusquedaChanged(nuevoTexto: String) {
        _texto_busqueda.value = nuevoTexto
    }

    /**
     * Suscripción manual a la base de datos para actualizar el estado general (opcional si usas favoritosFiltrados).
     */
    private fun observarFavoritos() {
        viewModelScope.launch {
            getFavoritosUseCase().collect { lista ->
                _uiState.update { it.copy(listaFavoritos = lista) }
            }
        }
    }

    /**
     * Elimina un libro específico. Room notificará el cambio y la lista se actualizará sola.
     */
    fun eliminarDeFavoritos(libro: Libro) {
        viewModelScope.launch {
            favoritoUseCase(libro)
        }
    }

    /**
     * Acción masiva para limpiar la base de datos local.
     */
    fun eliminarTodoFavoritos() {
        viewModelScope.launch {
            eliminarTodosUseCase()
        }
    }
}