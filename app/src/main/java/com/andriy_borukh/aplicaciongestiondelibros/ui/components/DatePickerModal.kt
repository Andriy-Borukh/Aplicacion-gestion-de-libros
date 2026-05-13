package com.andriy_borukh.aplicaciongestiondelibros.ui.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import java.util.Date

/**
 * Componente de interfaz de usuario que muestra un selector de fecha tipo modal
 *
 * onDateSelected Callback que se dispara cuando el usuario confirma una fecha
 *                       Devuelve un objeto Date de Java
 * onDismiss Callback que se dispara cuando el usuario cierra el diálogo sin seleccionar nada
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Date) -> Unit,
    onDismiss: () -> Unit
) {
    /**
     * Estado del DatePicker
     * Mantiene la fecha seleccionada internamente en milisegundos
     * Al usar 'rememberDatePickerState', el estado sobrevive a recomposiciones de la UI
     */
    val datePickerState = rememberDatePickerState()

    /**
     * DatePickerDialog: El contenedor visual del diálogo (el marco con los botones)
     */
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                // Verificamos que se haya seleccionado una fecha (que no sea null)
                datePickerState.selectedDateMillis?.let { millis ->
                    // Transformamos los milisegundos (Long) a un objeto Date para el dominio de nuestra app
                    onDateSelected(Date(millis))
                }
            }) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    ) {
        /**
         * DatePicker: El calendario interactivo que se dibuja
         * dentro del diálogo
         */
        DatePicker(state = datePickerState)
    }
}