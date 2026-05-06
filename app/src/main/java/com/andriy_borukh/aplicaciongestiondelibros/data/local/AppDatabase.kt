package com.andriy_borukh.aplicaciongestiondelibros.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

// 1. Definimos las entidades (tablas) y la versión de la base de datos
@Database(entities = [LibroEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    // 2. Declaramos el DAO para que Room sepa cómo acceder a los datos
    abstract fun libroDAO(): LibroDAO
}