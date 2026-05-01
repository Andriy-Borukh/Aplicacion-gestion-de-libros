package com.andriy_borukh.aplicaciongestiondelibros.ui.busqueda_remoto

import com.andriy_borukh.aplicaciongestiondelibros.domain.model.Libro


data class BusquedaRemotoState(
    //Contendra la cadena de texto que el usuario introduzca
    val texto: String = "",
    val estaCargando: Boolean = false,
    //Mensaje de error
    val mensajeError: String? = "",
    //Contiene la lista de libros ya procesada (no la que la api nos da sino la que el programa transforma)
    val listaLibros: List<Libro> = emptyList()
)
