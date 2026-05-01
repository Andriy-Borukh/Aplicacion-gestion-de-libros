package com.andriy_borukh.aplicaciongestiondelibros.domain.reposirory

import com.andriy_borukh.aplicaciongestiondelibros.domain.model.Libro

interface LibroRepository {
    suspend fun buscarLibrosRemoto(query: String): List<Libro>
}