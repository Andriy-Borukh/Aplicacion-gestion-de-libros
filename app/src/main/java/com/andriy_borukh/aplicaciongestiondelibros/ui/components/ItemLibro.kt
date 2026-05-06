package com.andriy_borukh.aplicaciongestiondelibros.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.andriy_borukh.aplicaciongestiondelibros.domain.model.Libro

@Composable
fun ItemLibro(
    libro: Libro,
    onFavoritoLibro: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = cardElevation(defaultElevation = 4.dp),
        colors = cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen de la portada usando Coil
            AsyncImage(
                model = libro.imagen,
                contentDescription = "Portada de ${libro.titulo}",
                modifier = Modifier
                    .height(100.dp)
                    .width(70.dp)
                    .background(Color.LightGray), // Fondo mientras carga
                contentScale = Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Información del libro
            Column (modifier = Modifier.weight(1f)) {
                Text(
                    text = libro.titulo,
                    fontSize = 18.sp,
                    fontWeight = Bold,
                    maxLines = 2,
                    overflow = Ellipsis
                )
                Text(
                    text = libro.autores,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            IconButton(onClick = onFavoritoLibro) {
                Icon(
                    imageVector =
                        if (libro.favorito) Icons.Filled.Favorite
                        else
                            Icons.Default.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = if (libro.favorito) Color.Red else Color.Gray
                )
            }
        }
    }
}