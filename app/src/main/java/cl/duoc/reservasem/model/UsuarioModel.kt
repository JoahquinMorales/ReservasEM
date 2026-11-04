package cl.duoc.reservasem.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "usuarios",
    indices = [Index(value = ["correo"], unique = true)]
)
data class UsuarioModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val correo: String,
    val contrasena: String,
    val nombre: String,
    val rol: String,
    val tipoSolicitante: String
)
