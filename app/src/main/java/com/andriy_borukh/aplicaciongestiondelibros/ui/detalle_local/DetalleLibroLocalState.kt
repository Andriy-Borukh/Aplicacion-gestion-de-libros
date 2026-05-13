package com.andriy_borukh.aplicaciongestiondelibros.ui.detalle_local

import com.andriy_borukh.aplicaciongestiondelibros.domain.model.Libro
import java.util.Date

/**
 * Representa el estado inmutable de la pantalla de detalles locales
 * Contiene tanto los datos originales del libro como los campos editables del formulario
 */
data class DetalleLibroLocalState(
    // Indica si se está realizando la carga inicial del libro desde la base de datos
    val isLoading: Boolean = false,

    // El objeto de dominio original recuperado de Room
    val libro: Libro? = null,

    // Almacena mensajes de error en caso de que la lectura de la DB falle
    val error: String? = null,

    // Flag para controlar si la UI debe mostrar componentes en modo edición o lectura
    val editando: Boolean = false,

    // Guardamos la página como String para facilitar la entrada de texto en el OutlinedTextField
    val nuevaPagina: String = "0",

    // Valoración del usuario (0.0 a 5.0)
    val nuevaValoracion: Double = 0.0,

    // Notas o impresiones personales del lector
    val nuevoComentario: String = "",

    // Controla la visibilidad de diálogos de confirmación (ej: al querer borrar el libro)
    val mostrarDialogoConfirmacion: Boolean = false,

    // Flag para que la UI sepa cuándo navegar hacia atrás o mostrar un mensaje de éxito tras guardar
    val operacionExitosa: Boolean = false,

    // Almacena la fecha de inicio de lectura seleccionada en el DatePicker
    val nuevaFechaInicio: Date? = null,

    // Almacena la fecha de fin de lectura seleccionada en el DatePicker
    val nuevaFechaFin: Date? = null
)