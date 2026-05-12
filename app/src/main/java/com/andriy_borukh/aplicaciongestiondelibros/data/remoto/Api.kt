package com.andriy_borukh.aplicaciongestiondelibros.data.remoto

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interfaz que se encarga de hacer la llamada a la api
 */

interface Api {

    //Se defina la url base, es la url que se uasara para todas las llamadas a la api
    companion object{
        const val BASE_URL = "https://www.googleapis.com/books/v1/"
    }

    //Indica que al hacer la llamada a la api se realizara mediante la cabecera GET
    @GET("volumes")
    //Es una función que se lanzará en corrutina
    suspend fun buscarLibros(
        //Definicion del parametro de busqueda
        //Retrofit construira una url asi -> https://www.googleapis.com/books/v1/volumes?q=(texto que haya puesto el usuario para buscar el libro)
        @Query("q") query: String
    ): ItemLibroDTO     //Indica en que clase se guarda los datos que devuelve la api

    /**
     * Obtiene el detalle de un libro específico por su ID único.
     * La URL resultante será: https://www.googleapis.com/books/v1/volumes/{libroId}
     */
    @GET("volumes/{libroId}")
    suspend fun obtenerLibroPorId(
        @Path("libroId") id: String
    ): ItemLibroDTO.LibroDto // Aquí devuelves el objeto del libro directamente
}