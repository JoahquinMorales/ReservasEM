package cl.duoc.reservasem.model

data class ErroresFormularioAuth(
    val nombreError: String? = null,
    val entidadError: String? = null,
    val correoError: String? = null,
    val claveError: String? = null,
    val generalError: String? = null
)

data class LoginUiState(
    val correo: String = "",
    val clave: String = "",
    val nombreRegistro: String = "",
    val grupoInstitucionId: String = "MILITAR_EM",
    val tipoSolicitanteRegistro: String = "Cadete Escuela Militar",
    val nombreEntidadInstitucion: String = "Escuela Militar del Libertador Bernardo O'Higgins",
    val rolSeleccionado: String = "USUARIO",
    val modoRegistro: Boolean = false,
    val cargando: Boolean = false,
    val loginExitoso: Boolean = false,
    val sugerirCrearCuenta: Boolean = false,
    val mostrarDialogoEvaluadorGoogle: Boolean = false,
    val mensajeExito: String? = null,
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

data class CrearEquipoErrores(
    val nombreEquipoError: String? = null,
    val companiaError: String? = null,
    val capitanError: String? = null,
    val disciplinaError: String? = null,
    val integrantesError: String? = null
)

data class CrearEquipoUiState(
    val nombreEquipo: String = "",
    val compania: String = "1ra Compañía",
    val capitan: String = "",
    val disciplina: String = "Fútbol",
    val integrantes: String = "11",
    val errores: CrearEquipoErrores = CrearEquipoErrores(),
    val guardadoExito: Boolean = false
)

data class ReprogramarReservaUiState(
    val reservaId: Int = 0,
    val nuevoDia: String = "Lunes",
    val nuevoBloque: String = "08:00 - 09:30",
    val nuevoMotivo: String = "",
    val motivoError: String? = null,
    val conflictoError: String? = null,
    val guardadoExito: Boolean = false
)

data class CrearEventoErrores(
    val tituloError: String? = null,
    val disciplinaError: String? = null,
    val recintoError: String? = null,
    val fechaError: String? = null,
    val organizadorError: String? = null
)

data class CrearEventoUiState(
    val titulo: String = "",
    val disciplina: String = "",
    val recinto: String = "Estadio / Cancha de Fútbol y Atletismo",
    val fecha: String = "Viernes 16:00 hrs",
    val organizador: String = "Jefatura de Deportes EM",
    val errores: CrearEventoErrores = CrearEventoErrores(),
    val guardadoExito: Boolean = false
)

data class EditarCanchaUiState(
    val canchaId: Int = 0,
    val enMantencion: Boolean = false,
    val motivoMantencion: String = "",
    val motivoError: String? = null,
    val guardadoExito: Boolean = false
)

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
