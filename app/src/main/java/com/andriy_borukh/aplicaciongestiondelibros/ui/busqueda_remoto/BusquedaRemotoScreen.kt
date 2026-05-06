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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusquedaRemotoScreen(
    navController: NavController,
    viewModel: BusquedaRemotoViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buscador de Libros") },
                actions = {
                    IconButton(onClick = { navController.navigate(Pantalla.Favoritos.ruta) }) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Ir a favoritos",
                            tint = Color.Red
                        )
                    }
                },
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
            OutlinedTextField(
                value = state.texto,
                onValueChange = { viewModel.onTextoChanged(it) },
                label = { Text("Introduce el título del libro") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.buscarLibros() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Buscar Libros")
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.listaLibros) { libro ->
                    ItemLibro(
                        libro = libro,
                        onFavoritoLibro = {
                            viewModel.favoritoLibro(libro)
                            scope.launch {
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