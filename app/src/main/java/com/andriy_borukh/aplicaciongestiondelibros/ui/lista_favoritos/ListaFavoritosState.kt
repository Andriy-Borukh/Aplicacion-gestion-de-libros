package com.andriy_borukh.aplicaciongestiondelibros.ui.lista_favoritos

import com.andriy_borukh.aplicaciongestiondelibros.domain.model.Libro

data class ListaFavoritosState (
    val listaFavoritos: List<Libro> = emptyList(),
    val estaCargando: Boolean = false
)
