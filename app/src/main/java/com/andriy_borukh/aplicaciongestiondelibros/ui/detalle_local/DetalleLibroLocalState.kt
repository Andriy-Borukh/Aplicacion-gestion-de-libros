package com.andriy_borukh.aplicaciongestiondelibros.ui.detalle_local

import com.andriy_borukh.aplicaciongestiondelibros.domain.model.Libro
import java.util.Date

data class DetalleLibroLocalState(
    val isLoading: Boolean = false,
    val libro: Libro? = null,
    val error: String? = null,
    val editando: Boolean = false,
    val nuevaPagina: String = "0",
    val nuevaValoracion: Double = 0.0,
    val nuevoComentario: String = "",
    val mostrarDialogoConfirmacion: Boolean = false,
    val operacionExitosa: Boolean = false,
    val nuevaFechaInicio: Date? = null,
    val nuevaFechaFin: Date? = null
)