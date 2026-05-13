package com.andriy_borukh.aplicaciongestiondelibros.domain.model

import java.util.Date

data class Libro (
    val id: String,
    val titulo: String,
    val autores: String,
    val imagen: String,
    val descripcion: String,
    val favorito: Boolean = false,
    val fechaInicio: Date?,
    val fechaFin: Date?,
    val paginaActual: Int?,
    val valoracion: Double?,
    val comentario: String?
)
