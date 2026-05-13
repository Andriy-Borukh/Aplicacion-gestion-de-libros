package com.andriy_borukh.aplicaciongestiondelibros.ui.detalle_local

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.andriy_borukh.aplicaciongestiondelibros.R
import com.andriy_borukh.aplicaciongestiondelibros.ui.components.DatePickerModal
import com.andriy_borukh.aplicaciongestiondelibros.ui.components.FilaFecha

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleLibroLocalScreen(
    navController: NavController,
    viewModel: DetalleLibroLocalViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Estados para controlar los DatePickers
    var showInicioPicker by remember { mutableStateOf(false) }
    var showFinPicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Lectura Personal") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    // Botón de Guardar: Ejecuta lógica, muestra Toast y navega atrás
                    IconButton(onClick = {
                        viewModel.guardarCambios()
                        Toast.makeText(context, "Información guardada correctamente", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Guardar", tint = Color(0xFF4CAF50))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        state.libro?.let { libro ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // --- 1. CABECERA: IMAGEN Y TÍTULOS ---
                Row(modifier = Modifier.fillMaxWidth()) {
                    AsyncImage(
                        model = libro.imagen,
                        contentDescription = "Portada",
                        modifier = Modifier
                            .size(120.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentScale = ContentScale.Fit,
                        placeholder = painterResource(R.drawable.ic_book_placeholder)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = libro.titulo, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(text = "Autor: ${libro.autores}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))

                // --- 2. PROGRESO DE PÁGINAS ---
                Text(text = "Progreso", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = state.nuevaPagina,
                    onValueChange = { viewModel.onPaginaChange(it) },
                    label = { Text("Página actual") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // --- 3. SECCIÓN DE FECHAS ---
                Text(text = "Fechas de lectura", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column {
                        FilaFecha(
                            label = "Fecha de inicio",
                            fecha = state.nuevaFechaInicio,
                            onClick = { showInicioPicker = true }
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                        FilaFecha(
                            label = "Fecha de finalización",
                            fecha = state.nuevaFechaFin,
                            onClick = { showFinPicker = true }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- 4. VALORACIÓN ---
                Text(text = "Tu valoración: ${"%.1f".format(state.nuevaValoracion)} ", fontWeight = FontWeight.Bold)
                Slider(
                    value = state.nuevaValoracion.toFloat(),
                    onValueChange = { viewModel.onValoracionChange(it.toDouble()) },
                    valueRange = 0f..5f,
                    steps = 9 // Permite pasos de 0.5 en 0.5
                )

                Spacer(modifier = Modifier.height(16.dp))

                // --- 5. COMENTARIOS O NOTAS ---
                Text(text = "Tus notas personales", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = state.nuevoComentario,
                    onValueChange = { viewModel.onComentarioChange(it) },
                    label = { Text("Escribe tus impresiones sobre el libro...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(150.dp),
                    singleLine = false,
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(16.dp))

                // --- 6. SINOPSIS (Informativa) ---
                Text(text = "Sinopsis oficial", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    text = libro.descripcion ?: "Sin descripción disponible.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        // --- MODALES DATEPICKER ---
        if (showInicioPicker) {
            DatePickerModal(
                onDateSelected = {
                    viewModel.establecerFechaInicio(it)
                    showInicioPicker = false // Cerramos al confirmar
                },
                onDismiss = { showInicioPicker = false }
            )
        }

        if (showFinPicker) {
            DatePickerModal(
                onDateSelected = {
                    viewModel.establecerFechaFin(it)
                    showFinPicker = false // Cerramos al confirmar
                },
                onDismiss = { showFinPicker = false }
            )
        }
    }
}