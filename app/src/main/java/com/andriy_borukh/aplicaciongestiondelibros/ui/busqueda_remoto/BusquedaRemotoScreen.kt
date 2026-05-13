package com.andriy_borukh.aplicaciongestiondelibros.ui.busqueda_remoto

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.andriy_borukh.aplicaciongestiondelibros.ui.components.ItemLibro
import com.andriy_borukh.aplicaciongestiondelibros.ui.navigation.Pantalla
import com.andriy_borukh.aplicaciongestiondelibros.ui.error.ErrorEstandar
import kotlinx.coroutines.launch

/**
 * Pantalla principal de búsqueda de libros en la API remota (Google Books)
 *
 * queryInicial Texto que puede venir de otra pantalla para realizar una búsqueda automática
 * navController Controlador de navegación para saltar entre pantallas
 * viewModel Lógica de negocio inyectada mediante Hilt
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusquedaRemotoScreen(
    queryInicial: String="",
    navController: NavController,
    viewModel: BusquedaRemotoViewModel = hiltViewModel()
) {
    // Estado para controlar los mensajes emergentes (Snackbars)
    val snackbarHostState = remember { SnackbarHostState() }
    // Estado para controlar los mensajes emergentes (Snackbars)
    val scope = rememberCoroutineScope()
    // Recolectamos el estado de la UI del ViewModel de forma reactiva
    val state by viewModel.uiState.collectAsState()


    /**
     * LaunchedEffect: Se ejecuta cuando la pantalla entra en la composición
     * Si recibimos una 'queryInicial' (ej. desde Favoritos), actualiza el campo de texto
     * y dispara la búsqueda automáticamente
     */
    LaunchedEffect(key1 = queryInicial) {
        if (queryInicial.isNotBlank()) {
            viewModel.onTextoChanged(queryInicial)
            viewModel.buscarLibros()
        }
    }

    // Estructura base de la pantalla
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buscador de Libros") },
                actions = {
                    // Botón para navegar a la pantalla de Favoritos locales
                    IconButton(onClick = { navController.navigate(Pantalla.Favoritos.ruta) }) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Ir a favoritos",
                            tint = Color.Red
                        )
                    }
                },
                // Contenedor para mostrar las notificaciones flotantes en la parte inferior
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- SECCIÓN DE ENTRADA DE USUARIO ---
            OutlinedTextField(
                value = state.texto,
                onValueChange = { viewModel.onTextoChanged(it) },
                label = { Text("Introduce el título del libro") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botón que dispara la petición a la API
            Button(
                onClick = { viewModel.buscarLibros() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Buscar Libros")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- SECCIÓN DINÁMICA: GESTIÓN DE ESTADOS ---
            Box(modifier = Modifier.fillMaxSize()) {

                // ESTADO 1: Cargando (Muestra el circulito de progreso)
                if (state.estaCargando) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                // ESTADO 2: Error (Muestra componente personalizado con botón de reintento)
                else if (state.error != null) {
                    ErrorEstandar(
                        error = state.error!!,
                        onReintentar = { viewModel.buscarLibros() },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                // ESTADO 3: Lista de Resultados
                else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.listaLibros) { libro ->
                            ItemLibro(
                                libro = libro,
                                onItemClick = { id ->
                                    // Navega al detalle del libro usando su ID único
                                    navController.navigate(Pantalla.DetalleRemoto.crearRuta(id))
                                },
                                onFavoritoLibro = {
                                    // Llama al ViewModel para guardar/quitar de la DB local
                                    viewModel.favoritoLibro(libro)
                                    // Muestra una confirmación visual al usuario
                                    scope.launch {
                                        // Si ya había un mensaje, lo quita para mostrar el nuevo rápido
                                        snackbarHostState.currentSnackbarData?.dismiss()
                                        val mensaje = if (!libro.favorito) "Añadido a favoritos" else "Eliminado de favoritos"
                                        snackbarHostState.showSnackbar(mensaje)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}