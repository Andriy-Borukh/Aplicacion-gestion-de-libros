package com.andriy_borukh.aplicaciongestiondelibros.ui.detalle_remoto

import com.andriy_borukh.aplicaciongestiondelibros.domain.model.Libro

data class DetalleLibroRemotoState (
    val isLoading: Boolean = false,
    val libro: Libro? = null,
    val error: String? = null
)