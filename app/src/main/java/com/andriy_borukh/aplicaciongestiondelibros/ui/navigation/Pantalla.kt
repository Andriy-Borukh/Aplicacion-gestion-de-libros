package com.andriy_borukh.aplicaciongestiondelibros.ui.navigation

/**
 * Jerarquía de pantallas de la aplicación.
 * Define las rutas base y las funciones auxiliares para construir rutas con argumentos.
 */
sealed class Pantalla(val ruta: String) {

    /**
     * Pantalla de búsqueda global.
     * Utiliza parámetros opcionales (?query={query}) para permitir
     * navegar a ella con un término pre-cargado.
     */
    object Busqueda : Pantalla("busqueda?query={query}") {
        fun crearRuta(query: String) = "busqueda?query=$query"
    }

    /** Pantalla que lista los libros guardados en la base de datos local (Room). */
    object Favoritos : Pantalla("favoritos")

    /**
     * Pantalla de detalle para libros de la API.
     * Requiere un argumento obligatorio {libroId}.
     */
    object DetalleRemoto : Pantalla("vista_detalle/{libroId}") {
        fun crearRuta(libroID: String) = "vista_detalle/$libroID"
    }

    /**
     * Pantalla de gestión personal (notas, progreso).
     * Diferenciada de la remota para manejar estados de edición local.
     */
    object DetalleLocal : Pantalla("detalle_local/{libroId}") {
        fun crearRuta(libroID: String) = "detalle_local/$libroID"
    }
}