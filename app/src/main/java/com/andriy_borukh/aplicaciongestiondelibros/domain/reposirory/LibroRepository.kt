package com.andriy_borukh.aplicaciongestiondelibros.domain.reposirory

import com.andriy_borukh.aplicaciongestiondelibros.domain.model.Libro
import kotlinx.coroutines.flow.Flow

interface LibroRepository {
    suspend fun buscarLibrosRemoto(query: String): List<Libro>
    suspend fun alternarFavorito(libro: Libro)
    fun obtenerTodosLosFavoritos(): Flow<List<Libro>>
}