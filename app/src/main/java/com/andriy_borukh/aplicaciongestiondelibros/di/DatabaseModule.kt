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

//Indica que es una fabrica y define las instrucciones para crear objetos
@Module
//Define cuanto tiempo viven los objetos creados
//Singleton significa que los objetos viven mientras este la aplicacion abierta
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    //Instanciación de la implementación de Room: AppDatabase es una clase abstracta
    //Room, en tiempo de compilación, genera una implementación concreta (AppDatabase_Impl)
    //Esta función le pide a Room que recupere esa implementación generada
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        //El método .databaseBuilder configura los parámetros críticos:
        //Contexto: Define la ruta del sistema de archivos donde se alojará el archivo .db.
        //Clase de referencia: Vincula el esquema definido en tu código con la base de datos real
        //Nombre de la DB: Establece el identificador único del archivo en el almacenamiento privado de la app
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "libros_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    //El DAO es donde residen las consultas SQL (@Query, @Insert, @Delete)
    // Esta función extrae ese objeto de la instancia de la base de datos ya creada
    fun provideLibroDao(db: AppDatabase): LibroDAO {
        return db.libroDAO()
    }
}