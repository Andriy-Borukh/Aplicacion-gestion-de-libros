package com.andriy_borukh.aplicaciongestiondelibros.data.remoto

import com.google.gson.annotations.SerializedName

/**
 *  Clase que va a guardar los datos JSON de la api
 *  Se usa la etiqueta SerializedName para poder poner el nombre del atributo en español
 */

class ItemLibroDTO (
    @SerializedName("items")
    //La api devuelve un objeto que contiene la lista items
    val libroDto: List<LibroDto>?
) {
    //Dentro de items hay mas objetos en los que se encuentra la id y volumeInfo que guarda
    // datos del libro
    data class LibroDto (
        val id: String,
        @SerializedName("volumeInfo")
        val infoLibro: InfoLibro
        )

    //Guarda los datos del libro como son: el titulo, los autores, descripcion y el link de la imagen
    data class InfoLibro (
        @SerializedName("title")
        val titulo: String,
        @SerializedName("authors")
        val autores: List<String>?,
        @SerializedName("description")
        val descripcion: String?,
        @SerializedName("imageLinks")
        val linksImagen: LinksImagen?
    )

    //Clase que contiene la url de la portada
    data class LinksImagen (
        @SerializedName("thumbnail")
        val miniatura: String
    )
}

//El json que devuelve la api se ve asi
//{
//  "items": [
//    {
//      "id": "123",
//      "volumeInfo": {
//        "title": "Titulo del libro",
//        "authors": ["Autor"],
//        "imageLinks": {
//          "thumbnail": "http://imagen.jpg"
//        }
//      }
//    }
//  ]
//}