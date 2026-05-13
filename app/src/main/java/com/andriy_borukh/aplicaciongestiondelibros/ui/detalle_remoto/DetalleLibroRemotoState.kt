package com.andriy_borukh.aplicaciongestiondelibros.ui.detalle_remoto

import com.andriy_borukh.aplicaciongestiondelibros.domain.model.Libro
import UiError

/**
 * Representa el estado de la pantalla de detalle remoto
 * Se utiliza para mostrar la información completa de un libro consultado en la API
 * antes de que el usuario decida añadirlo a sus favoritos
 */
data class DetalleLibroRemotoState (
    // Indica si se está realizando la petición de red a la API de Google Books.
    val isLoading: Boolean = false,

    // El modelo de dominio 'Libro' que contiene la sinopsis, portada y metadatos.
    // Es null hasta que la respuesta de la API llega con éxito.
    val libro: Libro? = null,

    // Objeto estructurado para gestionar fallos (sin internet, servidor caído, etc.)
    // Permite a la UI mostrar un componente de error específico.
    val error: UiError? = null
)