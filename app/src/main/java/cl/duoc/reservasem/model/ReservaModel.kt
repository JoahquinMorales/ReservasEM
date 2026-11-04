package cl.duoc.reservasem.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservas")
data class ReservaModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val canchaId: Int,
    val nombreCancha: String,
    val correoUsuario: String,
    val nombreSolicitante: String,
    val tipoSolicitante: String,
    val diaSemana: String,
    val bloqueHorario: String,
    val motivoActividad: String,
    val estado: String = "PENDIENTE",
    val motivoRechazo: String = "",
    val tieneCredencial: Boolean = false
)
