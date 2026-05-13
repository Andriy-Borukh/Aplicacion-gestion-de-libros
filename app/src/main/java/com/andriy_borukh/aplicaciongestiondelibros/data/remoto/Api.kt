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
        // La clave aqui sirve para poder realizar mas peticiones
        // Sino tuviese la clave después de unas pocas consultas el servidor te envia un error 429 en el que
        // tienes restringido el acceso a la api durante bastante tiempo
        const val API_KEY = "AIzaSyDWE_Kdm2GCt2yCDjV77Zuxo4eM3HdQcSU"
    }

    //Indica que al hacer la llamada a la api se realizara mediante la cabecera GET
    @GET("volumes")
    //Es una función que se lanzará en corrutina
    suspend fun buscarLibros(
        //Definicion del parametro de busqueda
        //Retrofit construira una url asi -> https://www.googleapis.com/books/v1/volumes?q=(texto que haya puesto el usuario para buscar el libro)
        @Query("q") query: String,
        // Añado la clave para que no me de error 429
        @Query("key") apiKey: String = API_KEY
    ): ItemLibroDTO     //Indica en que clase se guarda los datos que devuelve la api

    /**
     * Obtiene el detalle de un libro específico por su ID único.
     * La URL resultante será: https://www.googleapis.com/books/v1/volumes/{libroId}
     */
    @GET("volumes/{libroId}")
    suspend fun obtenerLibroPorId(
        @Path("libroId") id: String,
        // Añado la clave para que no me de error 429
        @Query("key") apiKey: String = API_KEY
    ): ItemLibroDTO.LibroDto // Aquí devuelves el objeto del libro directamente
}