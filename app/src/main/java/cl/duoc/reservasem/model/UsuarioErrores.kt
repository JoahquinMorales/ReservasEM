package cl.duoc.reservasem.model

/**
 * Contenedor de mensajes de error de validación de negocio.
 * Permite enlazar directamente con `isError` y `supportingText` en componentes Material 3.
 */
//region Errores de Validación de Formulario
data class UsuarioErrores(
    val nombre: String? = null,
    val correo: String? = null,
    val clave: String? = null,
    val direccion: String? = null,
    val aceptaTerminos: String? = null
)
//endregion
