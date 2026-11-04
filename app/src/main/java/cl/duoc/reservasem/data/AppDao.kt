package cl.duoc.reservasem.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cl.duoc.reservasem.model.CanchaModel
import cl.duoc.reservasem.model.EquipoModel
import cl.duoc.reservasem.model.EventoModel
import cl.duoc.reservasem.model.ReservaModel
import cl.duoc.reservasem.model.UsuarioModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    // ================= USUARIOS =================
    @Query("SELECT * FROM usuarios ORDER BY id ASC")
    fun obtenerTodosLosUsuarios(): Flow<List<UsuarioModel>>

    @Query("SELECT * FROM usuarios WHERE LOWER(correo) = LOWER(:correo) AND contrasena = :contrasena LIMIT 1")
    suspend fun validarLogin(correo: String, contrasena: String): UsuarioModel?

    @Query("SELECT * FROM usuarios WHERE LOWER(correo) = LOWER(:correo) LIMIT 1")
    suspend fun buscarUsuarioPorCorreo(correo: String): UsuarioModel?

    @Query("SELECT * FROM usuarios WHERE id = :usuarioId LIMIT 1")
    suspend fun buscarUsuarioPorId(usuarioId: Int): UsuarioModel?

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contarUsuarios(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuario: UsuarioModel): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarUsuariosIniciales(usuarios: List<UsuarioModel>)

    @Update
    suspend fun actualizarUsuario(usuario: UsuarioModel)

    @Query("DELETE FROM usuarios WHERE id = :usuarioId")
    suspend fun eliminarUsuarioPorId(usuarioId: Int)

    // ================= CANCHAS (14 Instalaciones EM) =================
    @Query("SELECT * FROM canchas ORDER BY id ASC")
    fun obtenerTodasLasCanchas(): Flow<List<CanchaModel>>

    @Query("SELECT * FROM canchas WHERE id = :canchaId LIMIT 1")
    suspend fun obtenerCanchaPorId(canchaId: Int): CanchaModel?

    @Query("SELECT COUNT(*) FROM canchas")
    suspend fun contarCanchas(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarCanchas(canchas: List<CanchaModel>)

    @Update
    suspend fun actualizarCancha(cancha: CanchaModel)

    // ================= RESERVAS =================
    @Query("SELECT * FROM reservas ORDER BY id DESC")
    fun obtenerTodasLasReservas(): Flow<List<ReservaModel>>

    @Query("SELECT * FROM reservas WHERE LOWER(correoUsuario) = LOWER(:correo) ORDER BY id DESC")
    fun obtenerReservasPorUsuario(correo: String): Flow<List<ReservaModel>>

    @Query("SELECT * FROM reservas WHERE id = :reservaId LIMIT 1")
    suspend fun obtenerReservaPorId(reservaId: Int): ReservaModel?

    @Query(
        """
        SELECT COUNT(*) FROM reservas 
        WHERE canchaId = :canchaId 
        AND diaSemana = :dia 
        AND bloqueHorario = :bloque 
        AND estado != 'RECHAZADA'
        """
    )
    suspend fun contarReservasEnBloque(canchaId: Int, dia: String, bloque: String): Int

    @Query("SELECT COUNT(*) FROM reservas")
    suspend fun contarReservas(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarReserva(reserva: ReservaModel): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarReservasIniciales(reservas: List<ReservaModel>)

    @Update
    suspend fun actualizarReserva(reserva: ReservaModel)

    @Delete
    suspend fun eliminarReserva(reserva: ReservaModel)

    // ================= EVENTOS Y EQUIPOS =================
    @Query("SELECT * FROM eventos ORDER BY id DESC")
    fun obtenerTodosLosEventos(): Flow<List<EventoModel>>

    @Query("SELECT COUNT(*) FROM eventos")
    suspend fun contarEventos(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarEvento(evento: EventoModel): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarEventosIniciales(eventos: List<EventoModel>)

    @Query("SELECT * FROM equipos ORDER BY id DESC")
    fun obtenerTodosLosEquipos(): Flow<List<EquipoModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarEquipo(equipo: EquipoModel): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarEquiposIniciales(equipos: List<EquipoModel>)
}
