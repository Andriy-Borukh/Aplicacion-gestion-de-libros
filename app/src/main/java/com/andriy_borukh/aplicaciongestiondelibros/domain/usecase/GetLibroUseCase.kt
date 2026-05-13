package com.andriy_borukh.aplicaciongestiondelibros.domain.usecase

import com.andriy_borukh.aplicaciongestiondelibros.domain.model.Libro
import com.andriy_borukh.aplicaciongestiondelibros.domain.reposirory.LibroRepository
import javax.inject.Inject

/**
 * Caso de Uso encargado de buscar libros en la api
 * Representa una acción única y específica que el usuario puede realizar en la app
 */

class GetLibroUseCase @Inject constructor(
    private val repository: LibroRepository
){
    /**
     * Al usar 'operator fun invoke', permitimos que el caso de uso se ejecute
     * como si fuera una función
     */
    suspend operator fun invoke(query: String): List<Libro> {
        return repository.buscarLibrosRemoto(query)
    }
}
