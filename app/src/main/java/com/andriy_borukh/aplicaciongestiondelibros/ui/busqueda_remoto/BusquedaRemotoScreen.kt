package com.andriy_borukh.aplicaciongestiondelibros.ui.busqueda_remoto

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun BusquedaRemotoScreen(
    viewModel: BusquedaRemotoViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    )
    {

        OutlinedTextField(
            value = state.texto,
            onValueChange = { viewModel.onTextoChanged(it) },
            label = { Text("Buscar libro...") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { viewModel.buscarLibros() }) {
                    Icon(Icons.Default.Search, contentDescription = "Buscar")
                }
            }
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Aplicación de gestión de libros",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = state.texto,
            onValueChange = { viewModel.onTextoChanged(it) },
            label = { Text("Introduce el titulo del libro") }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {viewModel.buscarLibros()},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Buscar")
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(state.listaLibros) { libro ->
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = libro.titulo, fontSize = 18.sp)
                    Text(text = libro.autores)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BusquedaRemotoScreenPreview() {
    BusquedaRemotoScreen()
}

