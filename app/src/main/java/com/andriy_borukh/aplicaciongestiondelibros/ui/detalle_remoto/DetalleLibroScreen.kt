package com.andriy_borukh.aplicaciongestiondelibros.ui.detalle_remoto

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.andriy_borukh.aplicaciongestiondelibros.R
import com.andriy_borukh.aplicaciongestiondelibros.ui.error.ErrorEstandar // Importamos tu componente

/**
 * Pantalla de detalle para libros obtenidos de la API externa
 * Muestra información extendida como la sinopsis completa y una portada de mayor tamaño
 *
 * libroId Identificador único del libro para realizar la consulta
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleLibroRemotoScreen(
    libroId: String,
    navController: NavController,
    viewModel: DetalleLibroRemotoViewModel = hiltViewModel()
) {
    // Suscripción al estado del ViewModel
    val state by viewModel.uiState.collectAsState()

    /**
     * LaunchedEffect: Se dispara cada vez que el 'libroId' cambia
     * Es crucial para cargar los datos en cuanto la pantalla se compone por primera vez
     */
    LaunchedEffect(libroId) {
        Log.d("DEBUG", "Solicitando detalle libro: $libroId")
        viewModel.cargarDetalle(libroId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Libro") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        // Contenedor principal que gestiona la superposición de estados (Carga, Error, Éxito)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // ESTADO 1: CARGANDO
            // Se muestra mientras la petición de red está en curso
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            // ESTADO 2: ERROR
            // Se activa si la API falla. Reutiliza el componente ErrorEstandar para coherencia visual
            else if (state.error != null) {
                ErrorEstandar(
                    error = state.error!!,
                    onReintentar = { viewModel.cargarDetalle(libroId) },
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // ESTADO 3: ÉXITO (MOSTRAR CONTENIDO)
            else if (state.libro != null) {
                val libro = state.libro!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        // verticalScroll permite leer sinopsis largas sin que se corte el contenido
                        .verticalScroll(rememberScrollState())
                ) {
                    // --- CABECERA VISUAL: PORTADA ---
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp) // Espacio generoso para la imagen
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = libro.imagen,
                            contentDescription = "Portada de ${libro.titulo}",
                            modifier = Modifier.fillMaxHeight().padding(16.dp),
                            contentScale = ContentScale.Fit, // Mantiene la proporción de la portada
                            placeholder = painterResource(R.drawable.ic_book_placeholder),
                            error = painterResource(R.drawable.ic_book_placeholder)
                        )
                    }

                    // --- SECCIÓN DE TEXTO: METADATOS Y SINOPSIS ---
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = libro.titulo,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = "Por ${libro.autores}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                        Text(
                            text = "Sinopsis",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Texto justificado y con interlineado aumentado para mejorar la lectura
                        Text(
                            text = libro.descripcion ?: "Sin descripción disponible.",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Justify,
                            lineHeight = 24.sp
                        )
                    }
                }
            }
        }
    }
}