package cl.duoc.reservasem.repository

import cl.duoc.reservasem.data.AppDao
import cl.duoc.reservasem.data.DatosDemostracion
import cl.duoc.reservasem.model.CanchaModel
import cl.duoc.reservasem.model.EquipoModel
import cl.duoc.reservasem.model.EventoModel
import cl.duoc.reservasem.model.ReservaModel
import cl.duoc.reservasem.model.UsuarioModel
import kotlinx.coroutines.flow.Flow

class DeportesRepository(private val appDao: AppDao) {

    val todasLasCanchas: Flow<List<CanchaModel>> = appDao.obtenerTodasLasCanchas()
    val todasLasReservas: Flow<List<ReservaModel>> = appDao.obtenerTodasLasReservas()
    val todosLosUsuarios: Flow<List<UsuarioModel>> = appDao.obtenerTodosLosUsuarios()
    val todosLosEventos: Flow<List<EventoModel>> = appDao.obtenerTodosLosEventos()
    val todosLosEquipos: Flow<List<EquipoModel>> = appDao.obtenerTodosLosEquipos()

    suspend fun poblarBaseDatosInicialSiVacia() {
        if (appDao.contarUsuarios() == 0) {
            appDao.insertarUsuariosIniciales(DatosDemostracion.usuariosIniciales)
        }
        if (appDao.contarCanchas() == 0) {
            appDao.insertarCanchas(DatosDemostracion.canchasOficiales)
        }
        if (appDao.contarReservas() == 0) {
            appDao.insertarReservasIniciales(DatosDemostracion.reservasIniciales)
        }
        if (appDao.contarEventos() == 0) {
            appDao.insertarEventosIniciales(DatosDemostracion.eventosIniciales)
            appDao.insertarEquiposIniciales(DatosDemostracion.equiposIniciales)
        }
    }

    // ================= USUARIOS Y AUTENTICACIÓN =================
    suspend fun autenticarUsuario(correo: String, contrasena: String): UsuarioModel? {
        return appDao.validarLogin(correo.trim(), contrasena)
    }

    suspend fun buscarUsuarioPorCorreo(correo: String): UsuarioModel? {
        return appDao.buscarUsuarioPorCorreo(correo.trim())
    }

    suspend fun buscarUsuarioPorId(id: Int): UsuarioModel? {
        return appDao.buscarUsuarioPorId(id)
    }

    suspend fun registrarUsuario(usuario: UsuarioModel): Result<UsuarioModel> {
        return try {
            val existente = appDao.buscarUsuarioPorCorreo(usuario.correo.trim())
            if (existente != null) {
                Result.failure(IllegalArgumentException("El correo ya se encuentra registrado en el sistema."))
            } else {
                val idGenerado = appDao.insertarUsuario(usuario.copy(correo = usuario.correo.trim()))
                Result.success(usuario.copy(id = idGenerado.toInt()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun autenticarORegistrarConGoogle(
        correo: String,
        nombre: String,
        rolSolicitado: String
    ): UsuarioModel {
        val correoLimpio = correo.trim().lowercase()
        val existente = appDao.buscarUsuarioPorCorreo(correoLimpio)
        if (existente != null) {
            return existente
        }
        val nuevoUsuario = UsuarioModel(
            correo = correoLimpio,
            contrasena = "google_oauth_login",
            nombre = nombre.ifBlank { correoLimpio.substringBefore("@") },
            rol = rolSolicitado,
            tipoSolicitante = if (rolSolicitado == "ADMIN") "Oficial" else "Cadete"
        )
        val id = appDao.insertarUsuario(nuevoUsuario)
        return nuevoUsuario.copy(id = id.toInt())
    }

    suspend fun eliminarUsuario(idUsuario: Int) {
        appDao.eliminarUsuarioPorId(idUsuario)
    }

    // ================= CANCHAS =================
    suspend fun obtenerCanchaPorId(canchaId: Int): CanchaModel? {
        return appDao.obtenerCanchaPorId(canchaId)
    }

    suspend fun actualizarEstadoCancha(cancha: CanchaModel) {
        appDao.actualizarCancha(cancha)
    }

    // ================= RESERVAS =================
    fun obtenerReservasDeUsuario(correo: String): Flow<List<ReservaModel>> {
        return appDao.obtenerReservasPorUsuario(correo)
    }

    suspend fun obtenerReservaPorId(reservaId: Int): ReservaModel? {
        return appDao.obtenerReservaPorId(reservaId)
    }

    suspend fun crearReservaValidada(reserva: ReservaModel): Result<Long> {
        val cancha = appDao.obtenerCanchaPorId(reserva.canchaId)
        if (cancha != null && cancha.enMantencion) {
            return Result.failure(
                IllegalStateException("El recinto '${cancha.nombre}' está en mantención: ${cancha.motivoMantencion}")
            )
        }
        val choques = appDao.contarReservasEnBloque(
            canchaId = reserva.canchaId,
            dia = reserva.diaSemana,
            bloque = reserva.bloqueHorario
        )
        if (choques > 0) {
            return Result.failure(
                IllegalStateException("El bloque ${reserva.bloqueHorario} del día ${reserva.diaSemana} ya se encuentra reservado.")
            )
        }
        val id = appDao.insertarReserva(reserva)
        return Result.success(id)
    }

    suspend fun actualizarReserva(reserva: ReservaModel) {
        appDao.actualizarReserva(reserva)
    }

    suspend fun eliminarReserva(reserva: ReservaModel) {
        appDao.eliminarReserva(reserva)
    }

    // ================= EVENTOS Y EQUIPOS =================
    suspend fun crearEvento(evento: EventoModel) {
        appDao.insertarEvento(evento)
    }

    suspend fun crearEquipo(equipo: EquipoModel) {
        appDao.insertarEquipo(equipo)
    }
}
