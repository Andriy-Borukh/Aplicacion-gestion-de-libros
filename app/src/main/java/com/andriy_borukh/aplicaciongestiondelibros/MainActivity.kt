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
import com.andriy_borukh.aplicaciongestiondelibros.ui.detalle_remoto.DetalleLibroRemotoScreen
import com.andriy_borukh.aplicaciongestiondelibros.ui.lista_favoritos.ListaFavoritosScreen
import com.andriy_borukh.aplicaciongestiondelibros.ui.navigation.Pantalla
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Pantalla.Busqueda.ruta // Empezamos en la búsqueda
    ) {
        // Definición de la ruta de Búsqueda
        composable(Pantalla.Busqueda.ruta) {
            BusquedaRemotoScreen(navController = navController)
        }

        // Definición de la ruta de Favoritos
        composable(Pantalla.Favoritos.ruta) {
            ListaFavoritosScreen(navController = navController)
        }

        composable(
            route = Pantalla.Detalle.ruta,
            // CAMBIO AQUÍ: "libroId" en lugar de "libroID"
            arguments = listOf(navArgument("libroId") { type = NavType.StringType })
        ) { backStackEntry ->
            // CAMBIO AQUÍ: Asegúrate de que coincida con el nombre de arriba
            val id = backStackEntry.arguments?.getString("libroId")
            DetalleLibroRemotoScreen(id ?: "", navController = navController)
        }
    }
}
