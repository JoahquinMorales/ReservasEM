package cl.duoc.reservasem.model

/**
 * Representación inmutable del estado visual del formulario de usuario militar.
 * Centraliza los valores capturados y el árbol de errores reactivos.
 */
//region Estado Inmutable de Usuario
data class UsuarioUiState(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val direccion: String = "",
    val aceptaTerminos: Boolean = false,
    val errores: UsuarioErrores = UsuarioErrores()
)
//endregion
