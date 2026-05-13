package com.andriy_borukh.aplicaciongestiondelibros.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Componente interactivo que muestra una etiqueta y una fecha formateada
 * Diseñado para integrarse en formularios o pantallas de detalles
 *
 * label El título de la fila (ej: "Fecha de inicio")
 * fecha El objeto Date a mostrar. Si es null, muestra un texto por defecto
 * onClick Acción que se ejecuta al pulsar cualquier parte de la fila
 */
@Composable
fun FilaFecha(
    label: String,
    fecha: Date?,
    onClick: () -> Unit
) {
    /**
     * Optimizamos el formateador con 'remember'
     * SimpleDateFormat es una operación costosa; al recordarlo, evitamos crear
     * una instancia nueva en cada redibujado (recomposición) de la pantalla
     */
    val formatter = remember {
        SimpleDateFormat("dd 'de' MMMM, yyyy", Locale.getDefault())
    }

    // Si la fecha existe, se formatea; si no, se muestra un valor informativo
    val fechaTexto = fecha?.let { formatter.format(it) } ?: "Sin asignar"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            // Hacemos que toda la fila sea clicable para mejorar la accesibilidad
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            // Etiqueta secundaria (pequeña y gris)
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
            // Valor principal (la fecha formateada)
            Text(
                text = fechaTexto,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Indicador visual de acción ("Editar")
        Text(
            text = "Editar",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}