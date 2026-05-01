package com.andriy_borukh.aplicaciongestiondelibros

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//Actúa como el Contenedor Central de dependencias
//
//Provee el Contexto de la Aplicación a cualquier otra clase que lo necesite
//
//Gestiona el ciclo de vida de los objetos marcados con @Singleton (como en NetworkModule)
@HiltAndroidApp
class ApplicationHilt : Application ()