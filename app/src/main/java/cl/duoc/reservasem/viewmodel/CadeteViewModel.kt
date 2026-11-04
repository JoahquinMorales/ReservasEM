package cl.duoc.reservasem.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.duoc.reservasem.DeportesApplication
import cl.duoc.reservasem.model.CanchaModel
import cl.duoc.reservasem.model.EquipoModel
import cl.duoc.reservasem.model.EventoModel
import cl.duoc.reservasem.model.NuevaReservaErrores
import cl.duoc.reservasem.model.NuevaReservaUiState
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

    // Validación Reactiva en Tiempo Real para los OutlinedTextField (isError + supportingText)
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

    fun confirmarYGuardarReserva(onExito: () -> Unit) {
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
                onSuccess = {
                    _nuevaReservaState.update { state ->
                        state.copy(
                            motivoActividad = "",
                            reservaGuardadaExito = true,
                            mensajeEstado = "Solicitud registrada exitosamente en estado PENDIENTE.",
                            errores = NuevaReservaErrores()
                        )
                    }
                    onExito()
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

    fun reprogramarReserva(
        reservaId: Int,
        nuevoDia: String,
        nuevoBloque: String,
        nuevoMotivo: String,
        onExito: () -> Unit
    ) {
        viewModelScope.launch {
            val original = deportesRepository.obtenerReservaPorId(reservaId) ?: return@launch
            val actualizada = original.copy(
                diaSemana = nuevoDia,
                bloqueHorario = nuevoBloque,
                motivoActividad = nuevoMotivo.ifBlank { original.motivoActividad },
                estado = "PENDIENTE"
            )
            deportesRepository.actualizarReserva(actualizada)
            onExito()
        }
    }

    fun cancelarReserva(reserva: ReservaModel) {
        viewModelScope.launch {
            deportesRepository.eliminarReserva(reserva)
        }
    }

    fun registrarEquipo(
        nombreEquipo: String,
        compania: String,
        capitan: String,
        disciplina: String,
        integrantes: Int
    ) {
        viewModelScope.launch {
            deportesRepository.crearEquipo(
                EquipoModel(
                    nombreEquipo = nombreEquipo,
                    compania = compania,
                    capitan = capitan,
                    disciplina = disciplina,
                    integrantes = integrantes
                )
            )
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
