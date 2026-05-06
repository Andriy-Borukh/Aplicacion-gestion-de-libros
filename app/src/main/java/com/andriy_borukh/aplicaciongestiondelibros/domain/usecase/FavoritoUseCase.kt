package com.andriy_borukh.aplicaciongestiondelibros.domain.usecase

import com.andriy_borukh.aplicaciongestiondelibros.domain.model.Libro
import com.andriy_borukh.aplicaciongestiondelibros.domain.reposirory.LibroRepository
import javax.inject.Inject

//Su objetivo es gestionar la acción de añadir o eliminar un libro de la lista de favoritos
class FavoritoUseCase @Inject constructor(
    private val repository: LibroRepository
){
    suspend operator fun invoke(libro: Libro) {
        repository.alternarFavorito(libro)
    }
}
