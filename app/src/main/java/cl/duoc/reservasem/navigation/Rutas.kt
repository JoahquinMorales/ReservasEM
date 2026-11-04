package cl.duoc.reservasem.navigation

object Rutas {
    const val LOGIN = "login"
    const val HOME_CADETE = "home_cadete"
    const val HOME_ADMIN = "home_admin"

    // Flujo de Reservas
    const val NUEVA_RESERVA = "nueva_reserva"
    const val RESULTADOS_BUSQUEDA = "resultados_busqueda"
    const val FICHA_CANCHA = "ficha_cancha/{canchaId}"
    const val CONFIRMAR_RESERVA = "confirmar_reserva"
    const val MIS_RESERVAS = "mis_reservas"
    const val DETALLE_RESERVA = "detalle_reserva/{reservaId}"
    const val REPROGRAMAR_RESERVA = "reprogramar_reserva/{reservaId}"

    // Flujo Admin y Catálogo
    const val SOLICITUDES_ADMIN = "solicitudes_admin"
    const val CALENDARIO = "calendario"
    const val INSTALACIONES_USUARIO = "instalaciones_usuario"
    const val INSTALACIONES_ADMIN = "instalaciones_admin"
    const val EDITAR_CANCHA = "editar_cancha/{canchaId}"

    // Eventos, Equipos y Usuarios
    const val EVENTOS = "eventos"
    const val DETALLE_EVENTO = "detalle_evento/{eventoId}"
    const val CREAR_EVENTO = "crear_evento"
    const val CREAR_EQUIPO = "crear_equipo"
    const val USUARIOS_ADMIN = "usuarios_admin"
    const val DETALLE_USUARIO = "detalle_usuario/{usuarioId}"
    const val REPORTES = "reportes"

    // Perfil y Ajustes
    const val NOTIFICACIONES = "notificaciones"
    const val PERFIL = "perfil"
    const val PREFERENCIAS = "preferencias"
    const val CONFIGURACION = "configuracion"
    const val POLITICAS = "politicas"
    const val AYUDA = "ayuda"

    fun fichaCancha(canchaId: Int) = "ficha_cancha/$canchaId"
    fun detalleReserva(reservaId: Int) = "detalle_reserva/$reservaId"
    fun reprogramarReserva(reservaId: Int) = "reprogramar_reserva/$reservaId"
    fun editarCancha(canchaId: Int) = "editar_cancha/$canchaId"
    fun detalleEvento(eventoId: Int) = "detalle_evento/$eventoId"
    fun detalleUsuario(usuarioId: Int) = "detalle_usuario/$usuarioId"
}
