package com.andriy_borukh.aplicaciongestiondelibros.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Define como se accede a los datos en la base de datos
 */

@Dao
interface LibroDAO {

    // Inserta un libro. Si ya existe, lo reemplaza (evita errores de duplicados)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarFavorito(libro: LibroEntity)

    // Borra un libro específico de la lista de favoritos
    @Delete
    suspend fun eliminarFavorito(libro: LibroEntity)

    // Obtiene todos los favoritos. Usamos Flow para que la UI se actualice sola
    // cuando un libro se añada o se borre.
    @Query("SELECT * FROM favoritos")
    fun obtenerTodosLosFavoritos(): Flow<List<LibroEntity>>

    // Verifica si un libro concreto existe en la base de datos por su ID
    @Query("SELECT EXISTS(SELECT * FROM favoritos WHERE id = :id)")
    suspend fun esFavorito(id: String): Boolean

    // Devuelve la entidad segun la id
    @Query("SELECT * FROM favoritos WHERE id = :id")
    suspend fun getLibroById(id: String): LibroEntity?

    // Actualiza la entidad
    @Update
    suspend fun actualizarLibro(libro: LibroEntity)

    // Elimina todos los libros guardados en la base de datos
    @Query("DELETE FROM favoritos")
    suspend fun eliminarTodosFavoritos()

}