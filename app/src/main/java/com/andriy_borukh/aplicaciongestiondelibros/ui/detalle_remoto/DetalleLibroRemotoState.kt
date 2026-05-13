package com.andriy_borukh.aplicaciongestiondelibros.ui.detalle_remoto

import com.andriy_borukh.aplicaciongestiondelibros.domain.model.Libro
import com.andriy_borukh.aplicaciongestiondelibros.ui.error.UiError

data class DetalleLibroRemotoState (
    val isLoading: Boolean = false,
    val libro: Libro? = null,
    val error: UiError? = null
)