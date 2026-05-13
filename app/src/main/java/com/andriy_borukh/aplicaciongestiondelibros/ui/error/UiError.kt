/**
 * Jerarquía sellada para la gestión de estados de error en la UI.
 *
 * Al ser una 'sealed class', garantizamos que el manejo de errores sea exhaustivo.
 * Cada objeto representa un escenario específico que la interfaz debe comunicar al usuario.
 */
sealed class UiError(val mensaje: String) {

    // --- ERRORES DE CONECTIVIDAD ---

    /** Se dispara cuando no hay interfaz de red activa (Sin Wi-Fi o datos). */
    object Red : UiError("No tienes conexión a internet.")

    /** Se dispara tras un SocketTimeoutException (el servidor no respondió a tiempo). */
    object TiempoAgotado : UiError("La conexión es muy lenta. Reintenta en unos segundos.")

    // --- ERRORES DE SERVICIO (API GOOGLE BOOKS) ---

    /** Error 429: Demasiadas peticiones en un corto periodo de tiempo. */
    object LimiteExcedido : UiError("Has superado el límite de búsquedas.")

    /** Error 500/503: El servidor externo tiene problemas técnicos. */
    object Servidor : UiError("El servicio de Google Books no está disponible ahora mismo.")

    // --- ERRORES DE LÓGICA DE NEGOCIO ---

    /** Caso de éxito técnico pero vacío de contenido para el usuario. */
    object SinResultados : UiError("No se encontraron libros para esta búsqueda.")

    // --- FALLBACK ---

    /** Captura cualquier excepción no controlada para evitar cierres inesperados (crashes). */
    object Desconocido : UiError("Ha ocurrido un error inesperado.")
}