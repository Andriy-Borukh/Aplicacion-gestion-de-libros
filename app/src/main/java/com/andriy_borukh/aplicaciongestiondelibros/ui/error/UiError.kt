package com.andriy_borukh.aplicaciongestiondelibros.ui.error

sealed class UiError(val mensaje: String) {
    // Errores de Red
    object Red : UiError("No tienes conexión a internet.")
    object TiempoAgotado : UiError("La conexión es muy lenta. Reintenta en unos segundos.")

    // Errores de API (Google Books)
    object LimiteExcedido : UiError("Has superado el límite de búsquedas.")
    object Servidor : UiError("El servicio de Google Books no está disponible ahora mismo.")

    // Errores de Usuario/Lógica
    object SinResultados : UiError("No se encontraron libros para esta búsqueda.")

    // Error genérico
    object Desconocido : UiError("Ha ocurrido un error inesperado.")
}