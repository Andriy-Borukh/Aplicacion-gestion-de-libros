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

    /**
     * Realiza una búsqueda en la API de Google Books
     * Transforma los DTOs (Data Transfer Objects) recibidos en modelos de Dominio
     */

    override suspend fun buscarLibrosRemoto(query: String): List<Libro> {
        val respuesta = api.buscarLibros(query)

        return respuesta.libroDto?.map { it.toDomain() } ?: emptyList()
    }

    /**
     * Gestiona el estado de favoritos. Si el libro ya es favorito lo elimina
     * de lo contrario, lo inserta en la base de datos local
     */

    override suspend fun alternarFavorito(libro: Libro) {
        val entidad = libro.toEntity()
        if (libro.favorito) {
            dao.eliminarFavorito(entidad)
        } else {
            dao.insertarFavorito(entidad)
        }
    }

    /**
     * Recupera todos los favoritos locales como un flujo reactivo (Flow)
     * Cada vez que la base de datos cambie, este Flow emitirá la lista actualizada
     */

    override fun obtenerTodosLosFavoritos(): Flow<List<Libro>> {
        return dao.obtenerTodosLosFavoritos().map {
            listaEntities -> listaEntities.map { entity -> entity.toDomain() }
        }
    }

    /**
     * Busca un libro por su ID único
     * Primero intenta encontrarlo en la base de datos local (por si es favorito)
     * si no existe, lo solicita a la API remota
     */

    override suspend fun getLibroPorId(id: String): Libro? {
        val libroLocal = dao.getLibroById(id)
        if (libroLocal != null) {
            return libroLocal.toDomain()
        }

        val respuesta = api.obtenerLibroPorId(id)
        return respuesta.toDomain()
    }

    /**
     * Actualiza la información de un libro existente en la base de datos (ej. notas o progreso)
     */

    override suspend fun actualizarLibro(libro: Libro) {
        val entity = libro.toEntity()
        dao.actualizarLibro(entity)
    }

    /**
     * Limpia completamente la tabla de favoritos en la base de datos local
     */

    override suspend fun eliminarTodoFavoritos() {
        dao.eliminarTodosFavoritos()
    }
}
