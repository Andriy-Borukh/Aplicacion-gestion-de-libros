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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleLibroRemotoScreen(
    libroId: String,
    navController: NavController,
    viewModel: DetalleLibroRemotoViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

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
        // Usamos un Box para manejar los estados igual que en Busqueda
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading) {
                // ESTADO 1: Cargando
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            else if (state.error != null) {
                // ESTADO 2: Error (Usando tu componente ErrorEstandar)
                ErrorEstandar(
                    error = state.error!!,
                    onReintentar = { viewModel.cargarDetalle(libroId) },
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else if (state.libro != null) {
                // ESTADO 3: Éxito (Contenido del libro)
                val libro = state.libro!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // --- Sección Imagen ---
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = libro.imagen,
                            contentDescription = "Portada de ${libro.titulo}",
                            modifier = Modifier.fillMaxHeight().padding(16.dp),
                            contentScale = ContentScale.Fit,
                            placeholder = painterResource(R.drawable.ic_book_placeholder),
                            error = painterResource(R.drawable.ic_book_placeholder)
                        )
                    }

                    // --- Sección Información ---
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