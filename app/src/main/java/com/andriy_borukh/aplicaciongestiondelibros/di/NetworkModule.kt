package com.andriy_borukh.aplicaciongestiondelibros.di

import com.andriy_borukh.aplicaciongestiondelibros.data.remoto.Api
import com.andriy_borukh.aplicaciongestiondelibros.domain.reposirory.LibroRepository
import com.andriy_borukh.aplicaciongestiondelibros.domain.reposirory.LibroRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Dice como se crean y reparten los objetos obtenidos por la API
 */

//Indica que es una fabrica y define las instrucciones para crear objetos
@Module
//Define cuanto tiempo viven los objetos creados
//Singleton significa que los objetos viven mientras este la aplicacion abierta
@InstallIn(SingletonComponent::class)
object NetworkModule {
    //Indica que el metodo devuelve una dependencia
    @Provides
    @Singleton
    //Esta funcion aplica la url definida en la interfaz Api y crea el codigo necesario
    // para hacer las peticiones
    fun provideApi(): Api {
        return Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }

    @Provides
    @Singleton
    //Conecta la API con la logica de negocio
    fun provideLibroRepository(api:Api): LibroRepository {
        //Con el objeto que se ha creado en el metodo anterior se lo pasamos al repositorio
        return LibroRepositoryImpl(api)
    }
}