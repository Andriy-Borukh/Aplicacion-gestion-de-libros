package com.andriy_borukh.aplicaciongestiondelibros

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.andriy_borukh.aplicaciongestiondelibros.ui.busqueda_remoto.BusquedaRemotoScreen
import com.andriy_borukh.aplicaciongestiondelibros.ui.detalle_local.DetalleLibroLocalScreen
import com.andriy_borukh.aplicaciongestiondelibros.ui.detalle_remoto.DetalleLibroRemotoScreen
import com.andriy_borukh.aplicaciongestiondelibros.ui.lista_favoritos.ListaFavoritosScreen
import com.andriy_borukh.aplicaciongestiondelibros.ui.navigation.Pantalla
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    //@Inject lateinit var repository: LibroRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //insertarLibrosDePrueba(repository)
        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavigation()
                }
            }
        }
    }
/*
    private fun insertarLibrosDePrueba(repository: LibroRepository) {
        val librosPrueba = listOf(
            Libro(
                id = "test_1",
                titulo = "El Quijote (Prueba)",
                autores = "Miguel de Cervantes",
                imagen = "https://m.media-amazon.com/images/I/719S79XshAL._AC_UF1000,1000_QL80_.jpg",
                descripcion = "Una aventura de prueba para verificar el detalle.",
                favorito = false,
                paginaActual = 120,
                valoracion = 4,
                comentario = "Me está gustando mucho esta parte.",
                fechaInicio = null,
                fechaFin = null
            ),
            Libro(
                id = "test_2",
                titulo = "1984 (Prueba)",
                autores = "George Orwell",
                imagen = "https://m.media-amazon.com/images/I/91Skt9KSH3L._AC_UF1000,1000_QL80_.jpg",
                descripcion = "Distopía para probar los favoritos locales.",
                favorito = false,
                paginaActual = 50,
                valoracion = 5,
                comentario = "Increíble clásico.",
                fechaInicio = null,
                fechaFin = null
            )
        )

        // Los insertamos en la base de datos usando el scope de la corrutina
        lifecycleScope.launch {
            librosPrueba.forEach { libro ->
                repository.alternarFavorito(libro)
            }
        }
    }*/
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Pantalla.Busqueda.ruta // Empezamos en la búsqueda
    ) {
        // Definición de la ruta de Búsqueda
        composable(
            route = Pantalla.Busqueda.ruta, // "busqueda?query={query}"
            arguments = listOf(
                navArgument("query") {
                    defaultValue = ""
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            // Extraemos el string de la URL
            val query = backStackEntry.arguments?.getString("query") ?: ""

            // Ahora sí puedes pasarlo porque la Screen ya lo acepta
            BusquedaRemotoScreen(
                navController = navController,
                queryInicial = query
            )
        }

        // Definición de la ruta de Favoritos
        composable(Pantalla.Favoritos.ruta) {
            ListaFavoritosScreen(navController = navController)
        }

        composable(
            route = Pantalla.DetalleRemoto.ruta,
            arguments = listOf(navArgument("libroId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("libroId")
            DetalleLibroRemotoScreen(id ?: "", navController = navController)
        }

        composable(
            route = Pantalla.DetalleLocal.ruta,
            arguments = listOf(navArgument("libroId") { type = NavType.StringType })
        ) {
            DetalleLibroLocalScreen(
                navController = navController
            )
        }
    }
}

