package com.andriy_borukh.aplicaciongestiondelibros.ui.lista_favoritos

import com.andriy_borukh.aplicaciongestiondelibros.domain.model.Libro

/**
 * Representa el estado de la pantalla de favoritos.
 * Al trabajar con datos locales, este estado suele ser el resultado de observar
 * un Flow proveniente de la base de datos.
 */
data class ListaFavoritosState (
    /**
     * La fuente de datos principal: una lista de objetos [Libro].
     * Se inicializa como una lista vacía para evitar nulos y facilitar el renderizado inicial.
     */
    val listaFavoritos: List<Libro> = emptyList(),

    /**
     * Flag de control para la UI. Aunque el acceso a Room es muy rápido,
     * es una buena práctica mantener este flag para mostrar un indicador de carga
     * durante la consulta inicial o procesos de filtrado pesados.
     */
    val estaCargando: Boolean = false
)
