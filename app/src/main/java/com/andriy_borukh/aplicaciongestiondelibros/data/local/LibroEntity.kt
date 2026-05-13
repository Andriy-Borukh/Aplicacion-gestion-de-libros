package com.andriy_borukh.aplicaciongestiondelibros.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "favoritos")
data class LibroEntity (
    @PrimaryKey val id: String,
    val titulo: String,
    val autores: String,
    val imagen: String,
    val descripcion: String,
    val fechInicio: Date?,
    val fechaFin: Date?,
    val valoracion: Double?,
    val paginaActual: Int?,
    val comentario: String?
)