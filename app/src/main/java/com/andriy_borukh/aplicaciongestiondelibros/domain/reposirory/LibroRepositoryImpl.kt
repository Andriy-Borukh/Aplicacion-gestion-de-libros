package com.andriy_borukh.aplicaciongestiondelibros.domain.reposirory

import com.andriy_borukh.aplicaciongestiondelibros.data.local.LibroDAO
import com.andriy_borukh.aplicaciongestiondelibros.data.remoto.Api
import com.andriy_borukh.aplicaciongestiondelibros.data.toDomain
import com.andriy_borukh.aplicaciongestiondelibros.data.toEntity
import com.andriy_borukh.aplicaciongestiondelibros.domain.model.Libro
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Traduce los datos obtenidos de la API ha la logica de negocio
 */

class LibroRepositoryImpl
    (
    private val api: Api,
    private val dao: LibroDAO
): LibroRepository {
    override suspend fun buscarLibrosRemoto(query: String): List<Libro> {
        //Ejecuta una llamada a la API
        val respuesta = api.buscarLibros(query)

        //Recorre la busqueda y convierte objetos de la clase a Libro
        return respuesta.libroDto?.map { it.toDomain() } ?: emptyList()
    }

    override suspend fun alternarFavorito(libro: Libro) {
        val entidad = libro.toEntity()
        if (libro.favorito) {
            dao.eliminarFavorito(entidad)
        } else {
            dao.insertarFavorito(entidad)
        }
    }

    override fun obtenerTodosLosFavoritos(): Flow<List<Libro>> {
        return dao.obtenerTodosLosFavoritos().map {
            listaEntities -> listaEntities.map { entity -> entity.toDomain() }
        }
    }

    override suspend fun getLibroPorId(id: String): Libro? {
        val libroLocal = dao.getLibroById(id)
        if (libroLocal != null) {
            return libroLocal.toDomain()
        }

        val respuesta = api.obtenerLibroPorId(id)
        return respuesta.toDomain()
    }

    override suspend fun actualizarLibro(libro: Libro) {
        val entity = libro.toEntity()
        dao.actualizarLibro(entity)
    }

    override suspend fun eliminarTodoFavoritos() {
        dao.eliminarTodosFavoritos()
    }
}
