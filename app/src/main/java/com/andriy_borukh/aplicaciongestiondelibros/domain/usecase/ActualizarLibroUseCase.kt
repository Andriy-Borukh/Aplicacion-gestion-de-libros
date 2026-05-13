package com.andriy_borukh.aplicaciongestiondelibros.domain.usecase

import com.andriy_borukh.aplicaciongestiondelibros.domain.model.Libro
import com.andriy_borukh.aplicaciongestiondelibros.domain.reposirory.LibroRepository
import javax.inject.Inject

class ActualizarLibroUseCase @Inject constructor(private val repository: LibroRepository) {
    suspend operator fun invoke(libro: Libro) = repository.actualizarLibro(libro)
}