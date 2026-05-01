package com.andriy_borukh.aplicaciongestiondelibros.domain.reposirory

import com.andriy_borukh.aplicaciongestiondelibros.data.remoto.Api
import com.andriy_borukh.aplicaciongestiondelibros.data.toDomain
import com.andriy_borukh.aplicaciongestiondelibros.domain.model.Libro

/**
 * Traduce los datos obtenidos de la API ha la logica de negocio
 */

class LibroRepositoryImpl
    (
    //Objeto creado por NetworkModule
    private val api: Api
): LibroRepository {
    override suspend fun buscarLibrosRemoto(query: String): List<Libro> {
        //Ejecuta una llamada a la API
        val respuesta = api.buscarLibros(query)

        //Recorre la busqueda y convierte objetos de la clase a Libro
        return respuesta.itemLibroDTO?.map { it.toDomain() } ?: emptyList()
    }
}
