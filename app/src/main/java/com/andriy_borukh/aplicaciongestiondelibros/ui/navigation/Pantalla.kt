package com.andriy_borukh.aplicaciongestiondelibros.ui.navigation

//Rutas de navegacion
sealed class Pantalla(val ruta: String) {
    object Busqueda : Pantalla("busqueda")
    object Favoritos : Pantalla("favoritos")
    object Detalle : Pantalla("vista_detalle/{libroId}") {
        fun crearRuta(libroID: String) = "vista_detalle/$libroID"
    }
}