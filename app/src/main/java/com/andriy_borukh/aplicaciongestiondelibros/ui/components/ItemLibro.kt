package com.andriy_borukh.aplicaciongestiondelibros.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CardDefaults
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

/**
 * Componente de lista que muestra una vista previa de un libro
 *
 * libro Objeto de dominio con los datos a mostrar
 * onItemClick Callback que devuelve el ID del libro al pulsar sobre la tarjeta
 * onFavoritoLibro Callback que notifica al ViewModel para alternar el estado de favorito
 */
@Composable
fun ItemLibro(
    libro: Libro,
    onItemClick: (String) -> Unit,
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
                .fillMaxWidth()
                // El clic se aplica a la fila interna para no perder el efecto visual de la Card
                .clickable { onItemClick(libro.id) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            /**
             * AsyncImage (Coil): Gestiona la carga asíncrona de imágenes desde la URL
             * Incluye gestión de memoria y caché de forma automática
             */
            AsyncImage(
                model = libro.imagen,
                contentDescription = "Portada de ${libro.titulo}",
                modifier = Modifier
                    .height(100.dp)
                    .width(70.dp)
                    .background(Color.LightGray), // Marcador de posición mientras descarga
                contentScale = Crop // Recorta la imagen para que rellene el espacio definido
            )

            Spacer(modifier = Modifier.width(16.dp))

            /**
             * Contenedor de texto
             * Esto asegura que el texto ocupe todo el espacio disponible
             * empujando el icono de favorito hacia el extremo derecho
             */
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = libro.titulo,
                    fontSize = 18.sp,
                    fontWeight = Bold,
                    maxLines = 2, // Evita que títulos muy largos rompan el diseño
                    overflow = Ellipsis // Añade "..." si el texto se corta
                )
                Text(
                    text = libro.autores,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            /**
             * Botón de acción para favoritos
             * Cambia dinámicamente el icono y el color según el estado del modelo
             */
            IconButton(onClick = onFavoritoLibro) {
                Icon(
                    imageVector = if (libro.favorito) Icons.Filled.Favorite
                    else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorito",
                    // El rojo indica estado activo, el gris estado inactivo
                    tint = if (libro.favorito) Color.Red else Color.Gray
                )
            }
        }
    }
}