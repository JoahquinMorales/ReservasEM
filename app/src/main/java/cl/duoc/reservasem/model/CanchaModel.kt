package cl.duoc.reservasem.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "canchas")
data class CanchaModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val categoria: String,
    val capacidad: Int,
    val enMantencion: Boolean = false,
    val motivoMantencion: String = ""
)
