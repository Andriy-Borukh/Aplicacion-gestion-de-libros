package com.andriy_borukh.aplicaciongestiondelibros.ui.error

import UiError
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Componente reutilizable para mostrar estados de error en toda la aplicación
 * Centraliza la visualización de fallos de red, base de datos o resultados vacíos
 *
 * error Objeto de tipo UiError que contiene el mensaje localizado
 * onReintentar Acción que se ejecutará cuando el usuario pulse el botón
 * modifier Permite personalizar el posicionamiento desde la pantalla padre
 */
@Composable
fun ErrorEstandar(
    error: UiError,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        // fillMaxSize asegura que el mensaje de error ocupe el centro de la pantalla
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icono de advertencia visual usando el color 'error' del tema Material 3
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Icono de error",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        /**
         * Texto descriptivo del error
         * Al usar 'error.mensaje', nos apoyamos en la lógica definida en la
         * sealed class/enum UiError para mostrar el texto adecuado
         */
        Text(
            text = error.mensaje,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de acción para recuperar el flujo de la aplicación
        Button(
            onClick = onReintentar,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Text("Reintentar")
        }
    }
}