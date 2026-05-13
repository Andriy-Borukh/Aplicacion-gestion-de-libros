package com.andriy_borukh.aplicaciongestiondelibros.di

import com.andriy_borukh.aplicaciongestiondelibros.data.local.LibroDAO
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


/**
 * Módulo de Hilt encargado de proveer las dependencias relacionadas con la red
 * y la infraestructura de datos
 */

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Configura y provee la instancia de Retrofit para realizar peticiones HTTP
     * @Singleton: Mantiene una única instancia del cliente de red para optimizar recursos
     */

    @Provides
    @Singleton
    fun provideApi(): Api {
        return Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }

    /**
     * Provee la implementación del Repositorio
     * Hilt inyecta automáticamente la 'Api' (creada arriba) y el 'LibroDAO' (creado en DatabaseModule)
     *
     * Al devolver la interfaz 'LibroRepository' pero instanciar 'LibroRepositoryImpl'
     * aplicamos el principio de inversión de dependencias
     */

    @Provides
    @Singleton
    fun provideLibroRepository(
        api:Api,
        dao: LibroDAO
    ): LibroRepository {
        return LibroRepositoryImpl(api, dao)
    }
}