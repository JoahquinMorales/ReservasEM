package cl.duoc.reservasem.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.duoc.reservasem.DeportesApplication
import cl.duoc.reservasem.model.CanchaModel
import cl.duoc.reservasem.model.CrearEventoErrores
import cl.duoc.reservasem.model.CrearEventoUiState
import cl.duoc.reservasem.model.EditarCanchaUiState
import cl.duoc.reservasem.model.EstadisticaCategoriaModel
import cl.duoc.reservasem.model.EventoModel
import cl.duoc.reservasem.model.ReservaModel
import cl.duoc.reservasem.model.ResumenOperativoModel
import cl.duoc.reservasem.model.UsuarioModel
import cl.duoc.reservasem.model.Validadores
import cl.duoc.reservasem.repository.DeportesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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

    // Estado centralizado para Crear Evento (IE 2.2.1)
    private val _crearEventoState = MutableStateFlow(CrearEventoUiState())
    val crearEventoState: StateFlow<CrearEventoUiState> = _crearEventoState.asStateFlow()

    // Estado centralizado para Editar Cancha / Mantención (IE 2.2.1)
    private val _editarCanchaState = MutableStateFlow(EditarCanchaUiState())
    val editarCanchaState: StateFlow<EditarCanchaUiState> = _editarCanchaState.asStateFlow()

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
            usuariosPendientesAprobacion = listaUsuarios.count { it.estadoCuenta == "PENDIENTE" },
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

    // ================= GESTIÓN DE RESERVAS =================
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

    // ================= GESTIÓN Y CONFIRMACIÓN DE USUARIOS =================
    fun confirmarUsuario(usuario: UsuarioModel) {
        viewModelScope.launch {
            deportesRepository.actualizarUsuario(
                usuario.copy(estadoCuenta = "APROBADO")
            )
        }
    }

    fun rechazarUsuario(usuario: UsuarioModel) {
        viewModelScope.launch {
            deportesRepository.actualizarUsuario(
                usuario.copy(estadoCuenta = "RECHAZADO")
            )
        }
    }

    fun eliminarUsuario(idUsuario: Int) {
        viewModelScope.launch {
            deportesRepository.eliminarUsuario(idUsuario)
        }
    }

    // ================= FORMULARIO CENTRALIZADO: EDITAR CANCHA / MANTENCIÓN =================
    fun inicializarEdicionCancha(cancha: CanchaModel) {
        _editarCanchaState.value = EditarCanchaUiState(
            canchaId = cancha.id,
            enMantencion = cancha.enMantencion,
            motivoMantencion = cancha.motivoMantencion,
            motivoError = null,
            guardadoExito = false
        )
    }

    fun onEnMantencionChange(activo: Boolean) {
        _editarCanchaState.update { state ->
            state.copy(
                enMantencion = activo,
                motivoError = Validadores.validarMotivoMantencion(activo, state.motivoMantencion)
            )
        }
    }

    fun onMotivoMantencionChange(motivo: String) {
        _editarCanchaState.update { state ->
            state.copy(
                motivoMantencion = motivo,
                motivoError = Validadores.validarMotivoMantencion(state.enMantencion, motivo)
            )
        }
    }

    fun guardarEstadoCanchaValidado(cancha: CanchaModel, onExito: () -> Unit) {
        val actual = _editarCanchaState.value
        val error = Validadores.validarMotivoMantencion(actual.enMantencion, actual.motivoMantencion)
        if (error != null) {
            _editarCanchaState.update { it.copy(motivoError = error) }
            return
        }
        viewModelScope.launch {
            deportesRepository.actualizarEstadoCancha(
                cancha.copy(
                    enMantencion = actual.enMantencion,
                    motivoMantencion = if (actual.enMantencion) actual.motivoMantencion.trim() else ""
                )
            )
            _editarCanchaState.update { it.copy(guardadoExito = true) }
            onExito()
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

    // ================= FORMULARIO CENTRALIZADO: CREAR EVENTO (IE 2.2.1) =================
    fun onTituloEventoChange(titulo: String) {
        _crearEventoState.update { state ->
            state.copy(
                titulo = titulo,
                errores = state.errores.copy(tituloError = Validadores.validarTituloEvento(titulo))
            )
        }
    }

    fun onDisciplinaEventoChange(disciplina: String) {
        _crearEventoState.update { state ->
            state.copy(
                disciplina = disciplina,
                errores = state.errores.copy(disciplinaError = Validadores.validarDisciplina(disciplina))
            )
        }
    }

    fun onRecintoEventoChange(recinto: String) {
        _crearEventoState.update { state ->
            state.copy(
                recinto = recinto,
                errores = state.errores.copy(recintoError = Validadores.validarRecinto(recinto))
            )
        }
    }

    fun onFechaEventoChange(fecha: String) {
        _crearEventoState.update { state ->
            state.copy(
                fecha = fecha,
                errores = state.errores.copy(fechaError = Validadores.validarFechaHora(fecha))
            )
        }
    }

    fun onOrganizadorEventoChange(organizador: String) {
        _crearEventoState.update { state ->
            state.copy(
                organizador = organizador,
                errores = state.errores.copy(organizadorError = Validadores.validarOrganizador(organizador))
            )
        }
    }

    fun validarYPublicarEvento(onExito: () -> Unit) {
        val actual = _crearEventoState.value
        val nuevosErrores = CrearEventoErrores(
            tituloError = Validadores.validarTituloEvento(actual.titulo),
            disciplinaError = Validadores.validarDisciplina(actual.disciplina),
            recintoError = Validadores.validarRecinto(actual.recinto),
            fechaError = Validadores.validarFechaHora(actual.fecha),
            organizadorError = Validadores.validarOrganizador(actual.organizador)
        )
        val esValido = listOfNotNull(
            nuevosErrores.tituloError,
            nuevosErrores.disciplinaError,
            nuevosErrores.recintoError,
            nuevosErrores.fechaError,
            nuevosErrores.organizadorError
        ).isEmpty()

        if (!esValido) {
            _crearEventoState.update { it.copy(errores = nuevosErrores) }
            return
        }

        viewModelScope.launch {
            deportesRepository.crearEvento(
                EventoModel(
                    titulo = actual.titulo.trim(),
                    disciplina = actual.disciplina.trim(),
                    recinto = actual.recinto.trim(),
                    fecha = actual.fecha.trim(),
                    organizador = actual.organizador.trim(),
                    estado = "CONFIRMADO"
                )
            )
            _crearEventoState.value = CrearEventoUiState()
            onExito()
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
