package com.andriy_borukh.aplicaciongestiondelibros.ui.busqueda_remoto

import UiError
import com.andriy_borukh.aplicaciongestiondelibros.domain.model.Libro


/**
 * Representa el estado atómico de la pantalla de búsqueda remota
 * Al usar una 'data class', garantizamos que la UI solo se redibuje cuando
 * alguno de estos valores cambie realmente
 */
data class BusquedaRemotoState(

    // Almacena el texto actual que el usuario escribe en el buscador (State Hoisting)
    val texto: String = "",

    // Controla la visibilidad de los indicadores de progreso (ProgressBar/CircularProgress)
    val estaCargando: Boolean = false,

    // Mensaje de error simplificado para diálogos o Snackbars
    val mensajeError: String? = null,

    // Lista de modelos de Dominio listos para ser mostrados en el LazyColumn.
    // No son DTOs (datos crudos de API) ni Entities (datos de DB), sino objetos 'Libro'
    val listaLibros: List<Libro> = emptyList(),

    // Objeto de error estructurado que permite clasificar fallos (Red, Servidor, etc.)
    // para mostrar componentes de error personalizados (ErrorEstandar)
    val error: UiError? = null
)
