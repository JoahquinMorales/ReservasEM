package cl.duoc.reservasem.model

data class ResumenOperativoModel(
    val totalCanchas: Int = 0,
    val canchasOperativas: Int = 0,
    val canchasEnMantencion: Int = 0,
    val totalReservas: Int = 0,
    val reservasPendientes: Int = 0,
    val reservasAprobadas: Int = 0,
    val reservasRechazadas: Int = 0,
    val totalUsuariosRegistrados: Int = 0,
    val usuariosPendientesAprobacion: Int = 0,
    val totalEventosActivos: Int = 0
)

data class EstadisticaCategoriaModel(
    val categoria: String,
    val cantidadSolicitudes: Int
)
