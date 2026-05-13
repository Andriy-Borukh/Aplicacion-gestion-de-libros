package com.andriy_borukh.aplicaciongestiondelibros.ui.navigation

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

/**
 * Gráfico de navegación principal de la aplicación.
 * Define la relación entre las rutas (Strings) y las pantallas (Composables).
 */
@Composable
fun AppNavigation() {
    // Gestiona la pila de retroceso (backstack) y el estado de la navegación
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Pantalla.Busqueda.ruta // Punto de entrada: Buscador global
    ) {

        // --- RUTA: BÚSQUEDA REMOTA ---
        composable(
            route = Pantalla.Busqueda.ruta, // Soporta "busqueda?query={query}"
            arguments = listOf(
                navArgument("query") {
                    defaultValue = "" // Permite navegar sin proporcionar una búsqueda
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            // Recuperamos el argumento opcional de la navegación
            val query = backStackEntry.arguments?.getString("query") ?: ""

            BusquedaRemotoScreen(
                navController = navController,
                queryInicial = query
            )
        }

        // --- RUTA: BIBLIOTECA DE FAVORITOS ---
        composable(Pantalla.Favoritos.ruta) {
            ListaFavoritosScreen(navController = navController)
        }

        // --- RUTA: DETALLE API (LECTURA) ---
        composable(
            route = Pantalla.DetalleRemoto.ruta,
            arguments = listOf(
                navArgument("libroId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            // Extraemos el ID obligatorio para pasárselo a la pantalla
            val id = backStackEntry.arguments?.getString("libroId")
            DetalleLibroRemotoScreen(id ?: "", navController = navController)
        }

        // --- RUTA: DETALLE LOCAL (EDICIÓN) ---
        composable(
            route = Pantalla.DetalleLocal.ruta,
            arguments = listOf(
                navArgument("libroId") { type = NavType.StringType }
            )
        ) {
            /**
             * Nota: No extraemos 'libroId' aquí manualmente porque el ViewModel de
             * DetalleLibroLocalScreen lo recupera mediante SavedStateHandle.
             */
            DetalleLibroLocalScreen(
                navController = navController
            )
        }
    }
}