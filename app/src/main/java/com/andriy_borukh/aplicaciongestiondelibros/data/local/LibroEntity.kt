package com.andriy_borukh.aplicaciongestiondelibros.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoritos")
data class LibroEntity (
    @PrimaryKey val id: String,
    val titulo: String,
    val autores: String,
    val imagen: String,
    val descripcion: String
)