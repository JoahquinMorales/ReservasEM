package cl.duoc.reservasem.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "eventos")
data class EventoModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val disciplina: String,
    val recinto: String,
    val fecha: String,
    val organizador: String,
    val estado: String = "PROGRAMADO"
)

@Entity(tableName = "equipos")
data class EquipoModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombreEquipo: String,
    val compania: String,
    val capitan: String,
    val disciplina: String,
    val integrantes: Int
)
