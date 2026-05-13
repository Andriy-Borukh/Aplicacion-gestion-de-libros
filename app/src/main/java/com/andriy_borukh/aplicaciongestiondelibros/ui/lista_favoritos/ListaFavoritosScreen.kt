package com.andriy_borukh.aplicaciongestiondelibros.ui.lista_favoritos

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.andriy_borukh.aplicaciongestiondelibros.ui.components.ItemLibro
import com.andriy_borukh.aplicaciongestiondelibros.ui.navigation.Pantalla

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaFavoritosScreen(
    navController: NavController,
    viewModel: ListaFavoritosViewModel = hiltViewModel()
) {
    val favoritos by viewModel.favoritosFiltrados.collectAsState()
    val textoBusqueda by viewModel.texto.collectAsState()
    val context = LocalContext.current

    // Estado para el diálogo de confirmación de borrado total
    var mostrarDialogoBorrado by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Libros Guardados", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    // Solo mostramos el botón de borrar todo si hay libros en la lista
                    if (favoritos.isNotEmpty()) {
                        IconButton(onClick = { mostrarDialogoBorrado = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Borrar todos los favoritos",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // --- 1. BUSCADOR LOCAL ---
            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { viewModel.onTextoBusquedaChanged(it) },
                label = { Text("Buscar en mis favoritos...") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(20.dp))

            // --- 2. LISTADO O MENSAJES DE ESTADO ---
            if (favoritos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    if (textoBusqueda.isBlank()) {
                        Text(text = "Aún no tienes libros guardados.", color = Color.Gray)
                    } else {
                        // CASO: No hay coincidencias en local, sugerimos buscar en Internet
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "No se encontró '$textoBusqueda' en tus favoritos.", color = Color.Gray)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = {
                                // Navegamos a la búsqueda remota pasando el texto actual
                                navController.navigate(Pantalla.Busqueda.crearRuta(textoBusqueda))
                            }) {
                                Text("Buscar '$textoBusqueda' en Internet")
                            }
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(favoritos) { libro ->
                        ItemLibro(
                            libro = libro,
                            onItemClick = { id ->
                                navController.navigate(Pantalla.DetalleLocal.crearRuta(id))
                            },
                            onFavoritoLibro = {
                                viewModel.eliminarDeFavoritos(libro)
                                Toast.makeText(context, "Libro eliminado", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }

        // --- 3. DIÁLOGOS (Fuera del flujo de la Column) ---
        if (mostrarDialogoBorrado) {
            AlertDialog(
                onDismissRequest = { mostrarDialogoBorrado = false },
                title = { Text("¿Eliminar toda tu biblioteca?") },
                text = { Text("Se borrarán todos los libros guardados y sus notas. Esta acción no se puede deshacer.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.eliminarTodoFavoritos()
                            mostrarDialogoBorrado = false
                            Toast.makeText(context, "Biblioteca vaciada", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Text("ELIMINAR TODO", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogoBorrado = false }) {
                        Text("CANCELAR")
                    }
                }
            )
        }
    }
}