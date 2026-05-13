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

/**
 * Pantalla que muestra la colección local de libros guardados por el usuario
 * Permite la gestión de la biblioteca: búsqueda local, eliminación individual y borrado masivo
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaFavoritosScreen(
    navController: NavController,
    viewModel: ListaFavoritosViewModel = hiltViewModel()
) {
    // Suscripción a los flujos de datos del ViewModel (State Flows)
    val favoritos by viewModel.favoritosFiltrados.collectAsState()
    val textoBusqueda by viewModel.texto.collectAsState()
    val context = LocalContext.current

    /**
     * Estado local para el Diálogo de Confirmación
     * Se mantiene en la UI porque es un estado efímero que no afecta a la lógica de negocio
     */
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
                    // Acción de "Destructive Path": Solo visible si hay contenido que borrar
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
                .background(Color(0xFFF5F5F5)) // Gris claro para resaltar las Cards blancas
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // --- 1. BUSCADOR LOCAL: Filtrado en tiempo real sobre la base de datos ---
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

            // --- 2. GESTIÓN DE ESTADOS DE LA LISTA ---
            if (favoritos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    if (textoBusqueda.isBlank()) {
                        // Caso: La base de datos está vacía
                        Text(text = "Aún no tienes libros guardados.", color = Color.Gray)
                    } else {
                        /**
                         * Caso: El filtro no devuelve resultados
                         * UX Proactiva: Sugerimos al usuario buscar el término en la API global
                         */
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "No se encontró '$textoBusqueda' localmente.", color = Color.Gray)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = {
                                navController.navigate(Pantalla.Busqueda.crearRuta(textoBusqueda))
                            }) {
                                Text("Buscar '$textoBusqueda' en Internet")
                            }
                        }
                    }
                }
            } else {
                // Listado optimizado para grandes cantidades de datos
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(favoritos) { libro ->
                        ItemLibro(
                            libro = libro,
                            onItemClick = { id ->
                                // Navegación a la edición de metadatos locales (página, notas, etc.)
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

        // --- 3. DIÁLOGOS DE SEGURIDAD ---
        if (mostrarDialogoBorrado) {
            AlertDialog(
                onDismissRequest = { mostrarDialogoBorrado = false },
                title = { Text("¿Eliminar toda tu biblioteca?") },
                text = { Text("Esta acción borrará permanentemente todos tus libros, notas y progresos.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.eliminarTodoFavoritos()
                            mostrarDialogoBorrado = false
                            Toast.makeText(context, "Biblioteca vaciada", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        // El color 'error' (rojo) indica una acción irreversible
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