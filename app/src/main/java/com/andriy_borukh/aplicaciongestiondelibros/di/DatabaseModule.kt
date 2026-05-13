package com.andriy_borukh.aplicaciongestiondelibros.di

import android.content.Context
import androidx.room.Room
import com.andriy_borukh.aplicaciongestiondelibros.data.local.AppDatabase
import com.andriy_borukh.aplicaciongestiondelibros.data.local.LibroDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @Module: Define esta clase como un módulo de Hilt que "provee" dependencias
 * @InstallIn: Especifica que este módulo vivirá en el 'SingletonComponent'
 * lo que significa que las dependencias creadas aquí durarán tanto como la aplicación
 */

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provee la instancia única de la base de datos (Singleton)
     * @Provides: Indica que esta función devuelve una dependencia que Hilt puede inyectar
     * @Singleton: Garantiza que solo se cree UNA instancia de la base de datos en toda la app,
     * ahorrando memoria y evitando conflictos de escritura.
     */

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        // El método .databaseBuilder configura los parámetros críticos
        // Contexto: Define la ruta del sistema de archivos donde se alojará el archivo .db.
        // Clase de referencia: Vincula el esquema definido en tu código con la base de datos real
        // Nombre de la DB: Establece el identificador único del archivo en el almacenamiento privado de la app
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "libros_db"
        )

             // fallbackToDestructiveMigration: Si cambias la versión de la base de datos
             // y no defines una estrategia de migración, Room borrará todas las tablas y las creará de nuevo.

            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    // El DAO es donde residen las consultas SQL (@Query, @Insert, @Delete)
    // Esta función extrae ese objeto de la instancia de la base de datos ya creada
    fun provideLibroDao(db: AppDatabase): LibroDAO {
        return db.libroDAO()
    }
}