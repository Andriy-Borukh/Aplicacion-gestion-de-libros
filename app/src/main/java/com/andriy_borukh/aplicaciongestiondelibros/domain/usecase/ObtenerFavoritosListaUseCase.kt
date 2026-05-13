package com.andriy_borukh.aplicaciongestiondelibros.domain.usecase

import com.andriy_borukh.aplicaciongestiondelibros.domain.model.Libro
import com.andriy_borukh.aplicaciongestiondelibros.domain.reposirory.LibroRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Caso de Uso encargado de buscar libros en la api
 * Representa una acción única y específica que el usuario puede realizar en la app
 */

class ObtenerFavoritosListaUseCase @Inject constructor(
    private val repository: LibroRepository
) {
    /**
     * Al usar 'operator fun invoke', permitimos que el caso de uso se ejecute
     * como si fuera una función
     */
    suspend operator fun invoke(): List<Libro> {
        return repository.obtenerTodosLosFavoritos().first()
    }
}