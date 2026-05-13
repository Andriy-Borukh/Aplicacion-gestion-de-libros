package com.andriy_borukh.aplicaciongestiondelibros.data

import com.andriy_borukh.aplicaciongestiondelibros.data.local.LibroEntity
import com.andriy_borukh.aplicaciongestiondelibros.data.remoto.ItemLibroDTO
import com.andriy_borukh.aplicaciongestiondelibros.data.remoto.ItemLibroDTO.LibroDto
import com.andriy_borukh.aplicaciongestiondelibros.domain.model.Libro


//Se encarga de transformar los datos

//Transforma los datos del dto a libro
fun LibroDto.toDomain(): Libro {
    return Libro(
        id = this.id,
        titulo = this.infoLibro.titulo,
        autores = this.infoLibro.autores?.joinToString(", ") ?: "Desconocido",
        imagen = this.infoLibro.linksImagen?.miniatura?.replace("http:", "https:") ?: "",
        descripcion = this.infoLibro.descripcion.toString(),
        fechaInicio = null,
        fechaFin = null,
        paginaActual = null,
        valoracion = null,
        comentario = null
    )
}

//Transforma los datos de la entidad a libro
fun LibroEntity.toDomain(): Libro {
    return Libro(
        id = this.id,
        titulo = this.titulo,
        autores = this.autores,
        imagen = this.imagen,
        favorito = true,
        descripcion = this.descripcion,
        fechaInicio = this.fechInicio,
        fechaFin = this.fechaFin,
        paginaActual = this.paginaActual,
        valoracion = this.valoracion,
        comentario = this.comentario
    )
}

//Transforma los datos de libro a entidad
fun Libro.toEntity(): LibroEntity {
    return LibroEntity(
        id = this.id,
        titulo = this.titulo,
        autores = this.autores,
        imagen = this.imagen,
        descripcion = this.descripcion,
        fechInicio = this.fechaInicio,
        fechaFin = this.fechaFin,
        valoracion = this.valoracion,
        paginaActual = this.paginaActual,
        comentario = this.comentario
    )
}