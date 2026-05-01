package com.andriy_borukh.aplicaciongestiondelibros.domain.usecase

import com.andriy_borukh.aplicaciongestiondelibros.domain.model.Libro
import com.andriy_borukh.aplicaciongestiondelibros.domain.reposirory.LibroRepository
import javax.inject.Inject


//Al igual que el Repositorio, esta clase no crea sus propias herramientas
//Le dice a Hilt: "Para que yo pueda trabajar, necesito que me entregues alguien que sepa manejar el LibroRepository"
//Hilt busca en el NetworkModule que vimos antes y le entrega la implementación necesaria.
class GetLibroUseCase @Inject constructor(
    private val repository: LibroRepository
){
    //Al marcarse como operator fun invoke, permite llamar a la clase como si fuera una función de primer orden (useCase())
    suspend operator fun invoke(query: String): List<Libro> {
        return repository.buscarLibrosRemoto(query)
    }
}