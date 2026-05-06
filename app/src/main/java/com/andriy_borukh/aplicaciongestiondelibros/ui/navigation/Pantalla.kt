package com.andriy_borukh.aplicaciongestiondelibros.ui.navigation

//Rutas de navegacion
sealed class Pantalla(val ruta: String) {
    object Busqueda : Pantalla("busqueda")
    object Favoritos : Pantalla("favoritos")
}