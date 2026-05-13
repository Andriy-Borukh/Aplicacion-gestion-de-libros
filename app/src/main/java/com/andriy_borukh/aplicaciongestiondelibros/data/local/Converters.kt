package com.andriy_borukh.aplicaciongestiondelibros.data.local

import androidx.room.TypeConverter
import java.util.Date

class Converters {

    // Para guardar en la DB: de Date a Long (milisegundos)
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    // Para leer de la DB: de Long a objeto Date
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

}