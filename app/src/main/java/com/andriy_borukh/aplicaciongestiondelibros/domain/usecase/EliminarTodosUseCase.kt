package com.andriy_borukh.aplicaciongestiondelibros.domain.usecase

import com.andriy_borukh.aplicaciongestiondelibros.domain.reposirory.LibroRepository
import javax.inject.Inject

class EliminarTodosUseCase @Inject constructor(
    private val repository: LibroRepository
) {
    suspend operator fun invoke() {
        repository.eliminarTodoFavoritos()
    }
}