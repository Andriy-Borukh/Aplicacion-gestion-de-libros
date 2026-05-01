package com.andriy_borukh.aplicaciongestiondelibros.data

import com.andriy_borukh.aplicaciongestiondelibros.data.remoto.LibroDTO.ItemLibroDTO
import com.andriy_borukh.aplicaciongestiondelibros.domain.model.Libro

fun ItemLibroDTO.toDomain(): Libro {
    return Libro(
        id = this.id,
        titulo = this.infoLibro.titulo,
        autores = this.infoLibro.autores?.joinToString(", ") ?: "Desconocido",
        imagen = this.infoLibro.linksImagen?.miniatura?.replace("http:", "https:") ?: ""
    )
}