package com.andriy_borukh.aplicaciongestiondelibros.ui.lista_favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andriy_borukh.aplicaciongestiondelibros.domain.model.Libro
import com.andriy_borukh.aplicaciongestiondelibros.domain.reposirory.LibroRepository
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

//Le dice a Hilt cómo instanciar esta clase y garantiza que el ViewModel sobreviva a cambios de configuración (como rotar la pantalla)
@HiltViewModel
class ListaFavoritosViewModel @Inject constructor(
    private val getFavoritosUseCase: GetFavoritosUseCase,
    private val favoritoUseCase: FavoritoUseCase,
    private val eliminarTodosUseCase: EliminarTodosUseCase
): ViewModel() {

    //Es el contenedor privado donde se guarda el estado actual de la pantalla
    // Al ser mutable, solo el ViewModel puede cambiar su contenido
    private val _uiState = MutableStateFlow(ListaFavoritosState())
    private val _texto_busqueda = MutableStateFlow("")

    //Expone una versión de solo lectura
    //La UI observa los cambios pero no puede modificarlos directamente
    val uiState = _uiState.asStateFlow()
    val texto = _texto_busqueda.asStateFlow()

    init {
        observarFavoritos()
    }

    val favoritosFiltrados = combine(
        _texto_busqueda,
        getFavoritosUseCase()
    ) { texto, lista ->
        if (texto.isBlank()) {
            lista
        } else {
            lista.filter { libro ->
                libro.titulo.contains(texto, ignoreCase = true) ||
                        libro.autores.contains(texto, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onTextoBusquedaChanged(nuevoTexto: String) {
        _texto_busqueda.value = nuevoTexto
    }

    //Si el usuario elimina un libro, Room detecta el cambio, el Flow emite una nueva lista y el ViewModel actualiza el uiState
    //La pantalla se refresca sola sin que el usuario tenga que hacer nada
    private fun observarFavoritos() {
        //Asegura que esta escucha se detenga automáticamente si el usuario cierra la pantalla evitando fugas de memoria
        viewModelScope.launch {
            //Al usar un Flow que viene desde Room el ViewModel se queda escuchando
            getFavoritosUseCase().collect { lista ->
                _uiState.update { it.copy(listaFavoritos = lista) }
            }
        }
    }

    //Cuando el usuario pulsa el botón en la UI, se dispara esta corrutina
    //Llama al FavoritoUseCase, el cual probablemente cambiará el estado de favorito a false en la base de datos
    fun eliminarDeFavoritos(libro: Libro) {
        viewModelScope.launch {
            favoritoUseCase(libro)
        }
    }

    fun eliminarTodoFavoritos() {
        viewModelScope.launch {
            eliminarTodosUseCase()
        }
    }
}