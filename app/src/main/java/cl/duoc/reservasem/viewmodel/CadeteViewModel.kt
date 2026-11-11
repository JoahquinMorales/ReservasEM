package cl.duoc.reservasem.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.duoc.reservasem.DeportesApplication
import cl.duoc.reservasem.model.CanchaModel
import cl.duoc.reservasem.model.CrearEquipoErrores
import cl.duoc.reservasem.model.CrearEquipoUiState
import cl.duoc.reservasem.model.EquipoModel
import cl.duoc.reservasem.model.EventoModel
import cl.duoc.reservasem.model.NuevaReservaErrores
import cl.duoc.reservasem.model.NuevaReservaUiState
import cl.duoc.reservasem.model.ReprogramarReservaUiState
import cl.duoc.reservasem.model.ReservaModel
import cl.duoc.reservasem.model.Validadores
import cl.duoc.reservasem.repository.DeportesRepository
import cl.duoc.reservasem.repository.PreferenciasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CadeteViewModel(
    private val deportesRepository: DeportesRepository,
    private val preferenciasRepository: PreferenciasRepository
) : ViewModel() {

    val canchas: StateFlow<List<CanchaModel>> = deportesRepository.todasLasCanchas
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val todasLasReservas: StateFlow<List<ReservaModel>> = deportesRepository.todasLasReservas
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val eventos: StateFlow<List<EventoModel>> = deportesRepository.todosLosEventos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val equipos: StateFlow<List<EquipoModel>> = deportesRepository.todosLosEquipos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _busquedaTexto = MutableStateFlow("")
    val busquedaTexto: StateFlow<String> = _busquedaTexto.asStateFlow()

    private val _categoriaSeleccionada = MutableStateFlow("Todas")
    val categoriaSeleccionada: StateFlow<String> = _categoriaSeleccionada.asStateFlow()

    private val _nuevaReservaState = MutableStateFlow(NuevaReservaUiState())
    val nuevaReservaState: StateFlow<NuevaReservaUiState> = _nuevaReservaState.asStateFlow()

    // Estado centralizado para Crear Equipo (IE 2.2.1)
    private val _crearEquipoState = MutableStateFlow(CrearEquipoUiState())
    val crearEquipoState: StateFlow<CrearEquipoUiState> = _crearEquipoState.asStateFlow()

    // Estado centralizado para Reprogramar Reserva (IE 2.2.1)
    private val _reprogramarState = MutableStateFlow(ReprogramarReservaUiState())
    val reprogramarState: StateFlow<ReprogramarReservaUiState> = _reprogramarState.asStateFlow()

    init {
        viewModelScope.launch {
            preferenciasRepository.sesionFlow.collect { sesion ->
                if (sesion.correo.isNotBlank()) {
                    _nuevaReservaState.update { actual ->
                        actual.copy(
                            nombreSolicitante = actual.nombreSolicitante.ifBlank { sesion.nombre },
                            correoSolicitante = actual.correoSolicitante.ifBlank { sesion.correo },
                            tipoSolicitante = sesion.tipoSolicitante.ifBlank { "Cadete" }
                        )
                    }
                    _crearEquipoState.update { actual ->
                        actual.copy(
                            capitan = actual.capitan.ifBlank { sesion.nombre }
                        )
                    }
                }
            }
        }
    }

    fun actualizarBusqueda(texto: String) {
        _busquedaTexto.value = texto
    }

    fun seleccionarCategoria(categoria: String) {
        _categoriaSeleccionada.value = categoria
    }

    fun preseleccionarCancha(cancha: CanchaModel) {
        _nuevaReservaState.update { state ->
            state.copy(
                canchaSeleccionadaId = cancha.id,
                nombreCanchaSeleccionada = cancha.nombre,
                reservaGuardadaExito = false,
                mensajeEstado = null,
                errores = state.errores.copy(conflictoHorarioError = null)
            )
        }
    }

    // ================= FORMULARIO CENTRALIZADO: NUEVA RESERVA (IE 2.2.1) =================
    fun onNombreSolicitanteChange(nombre: String) {
        _nuevaReservaState.update { state ->
            state.copy(
                nombreSolicitante = nombre,
                errores = state.errores.copy(
                    nombreSolicitanteError = Validadores.validarNombre(nombre)
                )
            )
        }
    }

    fun onCorreoSolicitanteChange(correo: String) {
        _nuevaReservaState.update { state ->
            state.copy(
                correoSolicitante = correo,
                errores = state.errores.copy(
                    correoSolicitanteError = Validadores.validarCorreo(correo)
                )
            )
        }
    }

    fun onTipoSolicitanteChange(tipo: String) {
        _nuevaReservaState.update { it.copy(tipoSolicitante = tipo) }
    }

    fun onDiaSemanaChange(dia: String) {
        _nuevaReservaState.update { state ->
            state.copy(
                diaSemana = dia,
                errores = state.errores.copy(conflictoHorarioError = null)
            )
        }
    }

    fun onBloqueHorarioChange(bloque: String) {
        _nuevaReservaState.update { state ->
            state.copy(
                bloqueHorario = bloque,
                errores = state.errores.copy(conflictoHorarioError = null)
            )
        }
    }

    fun onMotivoChange(motivo: String) {
        _nuevaReservaState.update { state ->
            state.copy(
                motivoActividad = motivo,
                errores = state.errores.copy(
                    motivoError = Validadores.validarMotivo(motivo)
                )
            )
        }
    }

    fun marcarCredencialCapturada(capturada: Boolean) {
        _nuevaReservaState.update { state ->
            state.copy(
                tieneCredencialCapturada = capturada,
                errores = state.errores.copy(credencialError = null)
            )
        }
    }

    fun validarFormularioReserva(): Boolean {
        val actual = _nuevaReservaState.value
        val nuevosErrores = NuevaReservaErrores(
            nombreSolicitanteError = Validadores.validarNombre(actual.nombreSolicitante),
            correoSolicitanteError = Validadores.validarCorreo(actual.correoSolicitante),
            motivoError = Validadores.validarMotivo(actual.motivoActividad)
        )
        val esValido = nuevosErrores.nombreSolicitanteError == null &&
            nuevosErrores.correoSolicitanteError == null &&
            nuevosErrores.motivoError == null

        _nuevaReservaState.update { it.copy(errores = nuevosErrores) }
        return esValido
    }

    fun confirmarYGuardarReserva(onExito: (ReservaModel) -> Unit) {
        if (!validarFormularioReserva()) return

        val actual = _nuevaReservaState.value
        viewModelScope.launch {
            val nueva = ReservaModel(
                canchaId = actual.canchaSeleccionadaId,
                nombreCancha = actual.nombreCanchaSeleccionada,
                correoUsuario = actual.correoSolicitante.trim(),
                nombreSolicitante = actual.nombreSolicitante.trim(),
                tipoSolicitante = actual.tipoSolicitante,
                diaSemana = actual.diaSemana,
                bloqueHorario = actual.bloqueHorario,
                motivoActividad = actual.motivoActividad.trim(),
                estado = "PENDIENTE",
                tieneCredencial = actual.tieneCredencialCapturada
            )
            val resultado = deportesRepository.crearReservaValidada(nueva)
            resultado.fold(
                onSuccess = { idGenerado ->
                    val reservaCreada = nueva.copy(id = idGenerado.toInt())
                    _nuevaReservaState.update { state ->
                        state.copy(
                            motivoActividad = "",
                            reservaGuardadaExito = true,
                            mensajeEstado = "Solicitud registrada exitosamente en estado PENDIENTE.",
                            errores = NuevaReservaErrores()
                        )
                    }
                    onExito(reservaCreada)
                },
                onFailure = { error ->
                    _nuevaReservaState.update { state ->
                        state.copy(
                            mensajeEstado = error.message,
                            errores = state.errores.copy(conflictoHorarioError = error.message)
                        )
                    }
                }
            )
        }
    }

    // ================= FORMULARIO CENTRALIZADO: REPROGRAMAR RESERVA (IE 2.2.1) =================
    fun inicializarReprogramacion(reserva: ReservaModel) {
        _reprogramarState.value = ReprogramarReservaUiState(
            reservaId = reserva.id,
            nuevoDia = reserva.diaSemana,
            nuevoBloque = reserva.bloqueHorario,
            nuevoMotivo = reserva.motivoActividad,
            motivoError = null,
            conflictoError = null,
            guardadoExito = false
        )
    }

    fun onReprogramarDiaChange(dia: String) {
        _reprogramarState.update { it.copy(nuevoDia = dia, conflictoError = null) }
    }

    fun onReprogramarBloqueChange(bloque: String) {
        _reprogramarState.update { it.copy(nuevoBloque = bloque, conflictoError = null) }
    }

    fun onReprogramarMotivoChange(motivo: String) {
        _reprogramarState.update {
            it.copy(
                nuevoMotivo = motivo,
                motivoError = Validadores.validarMotivo(motivo)
            )
        }
    }

    fun validarYGuardarReprogramacion(onExito: () -> Unit) {
        val actual = _reprogramarState.value
        val errorMotivo = Validadores.validarMotivo(actual.nuevoMotivo)
        if (errorMotivo != null) {
            _reprogramarState.update { it.copy(motivoError = errorMotivo) }
            return
        }

        viewModelScope.launch {
            val original = deportesRepository.obtenerReservaPorId(actual.reservaId) ?: return@launch
            val actualizada = original.copy(
                diaSemana = actual.nuevoDia,
                bloqueHorario = actual.nuevoBloque,
                motivoActividad = actual.nuevoMotivo.trim(),
                estado = "PENDIENTE"
            )
            deportesRepository.actualizarReserva(actualizada)
            _reprogramarState.update { it.copy(guardadoExito = true) }
            onExito()
        }
    }

    fun cancelarReserva(reserva: ReservaModel) {
        viewModelScope.launch {
            deportesRepository.eliminarReserva(reserva)
        }
    }

    // ================= FORMULARIO CENTRALIZADO: CREAR EQUIPO / ESCUADRA (IE 2.2.1) =================
    fun onNombreEquipoChange(nombre: String) {
        _crearEquipoState.update { state ->
            state.copy(
                nombreEquipo = nombre,
                errores = state.errores.copy(nombreEquipoError = Validadores.validarNombreEquipo(nombre))
            )
        }
    }

    fun onCompaniaEquipoChange(compania: String) {
        _crearEquipoState.update { state ->
            state.copy(
                compania = compania,
                errores = state.errores.copy(companiaError = Validadores.validarCompania(compania))
            )
        }
    }

    fun onCapitanEquipoChange(capitan: String) {
        _crearEquipoState.update { state ->
            state.copy(
                capitan = capitan,
                errores = state.errores.copy(capitanError = Validadores.validarNombre(capitan))
            )
        }
    }

    fun onDisciplinaEquipoChange(disciplina: String) {
        _crearEquipoState.update { state ->
            state.copy(
                disciplina = disciplina,
                errores = state.errores.copy(disciplinaError = Validadores.validarDisciplina(disciplina))
            )
        }
    }

    fun onIntegrantesEquipoChange(integrantes: String) {
        val soloDigitos = integrantes.filter { it.isDigit() }
        _crearEquipoState.update { state ->
            state.copy(
                integrantes = soloDigitos,
                errores = state.errores.copy(integrantesError = Validadores.validarIntegrantes(soloDigitos))
            )
        }
    }

    fun validarYRegistrarEquipo(onExito: () -> Unit) {
        val actual = _crearEquipoState.value
        val nuevosErrores = CrearEquipoErrores(
            nombreEquipoError = Validadores.validarNombreEquipo(actual.nombreEquipo),
            companiaError = Validadores.validarCompania(actual.compania),
            capitanError = Validadores.validarNombre(actual.capitan),
            disciplinaError = Validadores.validarDisciplina(actual.disciplina),
            integrantesError = Validadores.validarIntegrantes(actual.integrantes)
        )
        val esValido = listOfNotNull(
            nuevosErrores.nombreEquipoError,
            nuevosErrores.companiaError,
            nuevosErrores.capitanError,
            nuevosErrores.disciplinaError,
            nuevosErrores.integrantesError
        ).isEmpty()

        if (!esValido) {
            _crearEquipoState.update { it.copy(errores = nuevosErrores) }
            return
        }

        viewModelScope.launch {
            deportesRepository.crearEquipo(
                EquipoModel(
                    nombreEquipo = actual.nombreEquipo.trim(),
                    compania = actual.compania.trim(),
                    capitan = actual.capitan.trim(),
                    disciplina = actual.disciplina.trim(),
                    integrantes = actual.integrantes.toIntOrNull() ?: 11
                )
            )
            _crearEquipoState.value = CrearEquipoUiState()
            onExito()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as DeportesApplication)
                CadeteViewModel(
                    deportesRepository = app.deportesRepository,
                    preferenciasRepository = app.preferenciasRepository
                )
            }
        }
    }
}
