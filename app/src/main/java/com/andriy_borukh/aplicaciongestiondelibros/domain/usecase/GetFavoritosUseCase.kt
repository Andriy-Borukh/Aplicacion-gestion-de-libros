package com.andriy_borukh.aplicaciongestiondelibros.domain.usecase

import com.andriy_borukh.aplicaciongestiondelibros.domain.model.Libro
import com.andriy_borukh.aplicaciongestiondelibros.domain.reposirory.LibroRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//Su objetivo es recuperar la lista de libros que el usuario ha marcado como favoritos
class GetFavoritosUseCase @Inject constructor(
    private val repository: LibroRepository
) {
    operator fun invoke(): Flow<List<Libro>> = repository.obtenerTodosLosFavoritos()
}