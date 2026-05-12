package com.andriy_borukh.aplicaciongestiondelibros.domain.model

data class Libro (
    val id: String,
    val titulo: String,
    val autores: String,
    val imagen: String,
    val descripcion: String,
    val favorito: Boolean = false
)
