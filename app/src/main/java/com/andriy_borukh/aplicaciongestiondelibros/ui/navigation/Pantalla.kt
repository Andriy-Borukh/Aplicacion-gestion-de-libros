package com.andriy_borukh.aplicaciongestiondelibros.ui.navigation

import androidx.room.Query

//Rutas de navegacion
sealed class Pantalla(val ruta: String) {
    object Busqueda : Pantalla("busqueda?query={query}") {
        fun crearRuta(query: String) = "busqueda?query=$query"
    }
    object Favoritos : Pantalla("favoritos")
    object DetalleRemoto : Pantalla("vista_detalle/{libroId}") {
        fun crearRuta(libroID: String) = "vista_detalle/$libroID"
    }
    object DetalleLocal : Pantalla("detalle_local/{libroId}") {
        fun crearRuta(libroID: String) = "detalle_local/$libroID"
    }
}