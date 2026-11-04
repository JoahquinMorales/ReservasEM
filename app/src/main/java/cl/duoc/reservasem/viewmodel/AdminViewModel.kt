package cl.duoc.reservasem.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.duoc.reservasem.DeportesApplication
import cl.duoc.reservasem.model.CanchaModel
import cl.duoc.reservasem.model.EstadisticaCategoriaModel
import cl.duoc.reservasem.model.EventoModel
import cl.duoc.reservasem.model.ReservaModel
import cl.duoc.reservasem.model.ResumenOperativoModel
import cl.duoc.reservasem.model.UsuarioModel
import cl.duoc.reservasem.repository.DeportesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdminViewModel(
    private val deportesRepository: DeportesRepository
) : ViewModel() {

    val canchas: StateFlow<List<CanchaModel>> = deportesRepository.todasLasCanchas
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reservas: StateFlow<List<ReservaModel>> = deportesRepository.todasLasReservas
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val usuarios: StateFlow<List<UsuarioModel>> = deportesRepository.todosLosUsuarios
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val eventos: StateFlow<List<EventoModel>> = deportesRepository.todosLosEventos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val resumenOperativo: StateFlow<ResumenOperativoModel> = combine(
        canchas,
        reservas,
        usuarios,
        eventos
    ) { listaCanchas, listaReservas, listaUsuarios, listaEventos ->
        ResumenOperativoModel(
            totalCanchas = listaCanchas.size,
            canchasOperativas = listaCanchas.count { !it.enMantencion },
            canchasEnMantencion = listaCanchas.count { it.enMantencion },
            totalReservas = listaReservas.size,
            reservasPendientes = listaReservas.count { it.estado == "PENDIENTE" },
            reservasAprobadas = listaReservas.count { it.estado == "APROBADA" },
            reservasRechazadas = listaReservas.count { it.estado == "RECHAZADA" },
            totalUsuariosRegistrados = listaUsuarios.size,
            totalEventosActivos = listaEventos.size
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ResumenOperativoModel()
    )

    val estadisticasPorCategoria: StateFlow<List<EstadisticaCategoriaModel>> = combine(
        canchas,
        reservas
    ) { listaCanchas, listaReservas ->
        val mapaCategorias = listaCanchas.associate { it.id to it.categoria }
        listaReservas
            .groupBy { mapaCategorias[it.canchaId] ?: "General" }
            .map { (cat, items) -> EstadisticaCategoriaModel(cat, items.size) }
            .sortedByDescending { it.cantidadSolicitudes }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun aprobarReserva(reserva: ReservaModel) {
        viewModelScope.launch {
            deportesRepository.actualizarReserva(
                reserva.copy(estado = "APROBADA", motivoRechazo = "")
            )
        }
    }

    fun rechazarReserva(reserva: ReservaModel, motivo: String) {
        viewModelScope.launch {
            deportesRepository.actualizarReserva(
                reserva.copy(
                    estado = "RECHAZADA",
                    motivoRechazo = motivo.trim().ifBlank { "No autorizada por jefatura de deportes" }
                )
            )
        }
    }

    fun alternarMantencionCancha(cancha: CanchaModel, enMantencion: Boolean, motivo: String) {
        viewModelScope.launch {
            deportesRepository.actualizarEstadoCancha(
                cancha.copy(
                    enMantencion = enMantencion,
                    motivoMantencion = if (enMantencion) {
                        motivo.trim().ifBlank { "Mantención técnica programada" }
                    } else ""
                )
            )
        }
    }

    fun crearEventoDeportivo(
        titulo: String,
        disciplina: String,
        recinto: String,
        fecha: String,
        organizador: String
    ) {
        viewModelScope.launch {
            deportesRepository.crearEvento(
                EventoModel(
                    titulo = titulo.trim(),
                    disciplina = disciplina.trim(),
                    recinto = recinto.trim(),
                    fecha = fecha.trim(),
                    organizador = organizador.trim(),
                    estado = "CONFIRMADO"
                )
            )
        }
    }

    fun eliminarUsuario(idUsuario: Int) {
        viewModelScope.launch {
            deportesRepository.eliminarUsuario(idUsuario)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as DeportesApplication)
                AdminViewModel(deportesRepository = app.deportesRepository)
            }
        }
    }
}
