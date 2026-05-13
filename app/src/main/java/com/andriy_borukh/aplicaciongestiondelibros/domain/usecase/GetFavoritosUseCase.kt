package com.andriy_borukh.aplicaciongestiondelibros.domain.usecase

import com.andriy_borukh.aplicaciongestiondelibros.domain.model.Libro
import com.andriy_borukh.aplicaciongestiondelibros.domain.reposirory.LibroRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de Uso encargado de recuperar todos los libros de la base de datos
 * Representa una acción única y específica que el usuario puede realizar en la app
 */


class GetFavoritosUseCase @Inject constructor(
    private val repository: LibroRepository
) {
    /**
     * Al usar 'operator fun invoke', permitimos que el caso de uso se ejecute
     * como si fuera una función
     */
    operator fun invoke(): Flow<List<Libro>> = repository.obtenerTodosLosFavoritos()
}