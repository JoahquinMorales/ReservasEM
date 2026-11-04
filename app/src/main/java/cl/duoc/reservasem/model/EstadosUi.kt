package cl.duoc.reservasem.model

data class ErroresFormularioAuth(
    val nombreError: String? = null,
    val correoError: String? = null,
    val claveError: String? = null,
    val generalError: String? = null
)

data class LoginUiState(
    val correo: String = "",
    val clave: String = "",
    val nombreRegistro: String = "",
    val tipoSolicitanteRegistro: String = "Cadete",
    val rolSeleccionado: String = "USUARIO",
    val modoRegistro: Boolean = false,
    val cargando: Boolean = false,
    val loginExitoso: Boolean = false,
    val sugerirCrearCuenta: Boolean = false,
    val mostrarDialogoEvaluadorGoogle: Boolean = false,
    val errores: ErroresFormularioAuth = ErroresFormularioAuth()
)

data class NuevaReservaErrores(
    val nombreSolicitanteError: String? = null,
    val correoSolicitanteError: String? = null,
    val motivoError: String? = null,
    val credencialError: String? = null,
    val conflictoHorarioError: String? = null
)

data class NuevaReservaUiState(
    val canchaSeleccionadaId: Int = 1,
    val nombreCanchaSeleccionada: String = "Cancha de Fútbol Principal",
    val nombreSolicitante: String = "",
    val correoSolicitante: String = "",
    val tipoSolicitante: String = "Cadete",
    val diaSemana: String = "Lunes",
    val bloqueHorario: String = "08:00 - 09:30",
    val motivoActividad: String = "",
    val tieneCredencialCapturada: Boolean = false,
    val errores: NuevaReservaErrores = NuevaReservaErrores(),
    val reservaGuardadaExito: Boolean = false,
    val mensajeEstado: String? = null
) {
    // Compatibilidad directa con accesos rápidos de error
    val nombreError: String? get() = errores.nombreSolicitanteError
    val correoError: String? get() = errores.correoSolicitanteError
    val motivoError: String? get() = errores.motivoError
}

data class SesionActivaUiState(
    val sesionIniciada: Boolean = false,
    val correo: String = "",
    val nombre: String = "",
    val rol: String = "USUARIO",
    val tipoSolicitante: String = "Cadete",
    val modoOscuro: Boolean = false,
    val modoMantencionGlobal: Boolean = false,
    val fotoPerfilUriString: String = "",
    val notificacionesActivas: Boolean = true
)
