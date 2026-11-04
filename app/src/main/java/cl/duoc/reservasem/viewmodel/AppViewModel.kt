package cl.duoc.reservasem.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.duoc.reservasem.DeportesApplication
import cl.duoc.reservasem.model.SesionActivaUiState
import cl.duoc.reservasem.repository.DeportesRepository
import cl.duoc.reservasem.repository.PreferenciasRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(
    private val preferenciasRepository: PreferenciasRepository,
    private val deportesRepository: DeportesRepository
) : ViewModel() {

    val sesionState: StateFlow<SesionActivaUiState> = preferenciasRepository.sesionFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SesionActivaUiState()
        )

    // Estado para la Foto de Perfil Táctica (Cámara / Galería)
    private val _fotoPerfilUri = MutableStateFlow<Uri?>(null)
    val fotoPerfilUri: StateFlow<Uri?> = _fotoPerfilUri.asStateFlow()

    // Estado para Modo Mantención Persistido en DataStore + Animación
    private val _modoMantencionActivo = MutableStateFlow<Boolean?>(null)
    val modoMantencionActivo: StateFlow<Boolean?> = _modoMantencionActivo.asStateFlow()

    private val _mostrarMensajeMantencion = MutableStateFlow(false)
    val mostrarMensajeMantencion: StateFlow<Boolean> = _mostrarMensajeMantencion.asStateFlow()

    init {
        viewModelScope.launch {
            deportesRepository.poblarBaseDatosInicialSiVacia()
        }
        cargarEstadoMantencion()
        observarFotoGuardada()
    }

    private fun observarFotoGuardada() {
        viewModelScope.launch {
            preferenciasRepository.sesionFlow.collect { sesion ->
                if (sesion.fotoPerfilUriString.isNotBlank() && _fotoPerfilUri.value == null) {
                    runCatching { Uri.parse(sesion.fotoPerfilUriString) }
                        .onSuccess { _fotoPerfilUri.value = it }
                }
            }
        }
    }

    fun onFotoPerfilChange(uri: Uri?) {
        _fotoPerfilUri.value = uri
        viewModelScope.launch {
            preferenciasRepository.guardarFotoPerfilUri(uri?.toString() ?: "")
        }
    }

    fun cargarEstadoMantencion() {
        viewModelScope.launch {
            delay(800)
            _modoMantencionActivo.value = preferenciasRepository.modoMantencionFlow.first()
        }
    }

    fun alternarModoMantencion() {
        viewModelScope.launch {
            val nuevoEstado = !(_modoMantencionActivo.value ?: false)
            preferenciasRepository.guardarModoMantencion(nuevoEstado)
            _modoMantencionActivo.value = nuevoEstado
            _mostrarMensajeMantencion.value = true
            delay(2500)
            _mostrarMensajeMantencion.value = false
        }
    }

    fun cambiarModoOscuro(activo: Boolean) {
        viewModelScope.launch {
            preferenciasRepository.cambiarModoOscuro(activo)
        }
    }

    fun cambiarNotificaciones(activas: Boolean) {
        viewModelScope.launch {
            preferenciasRepository.cambiarNotificaciones(activas)
        }
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            preferenciasRepository.cerrarSesion()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as DeportesApplication)
                AppViewModel(
                    preferenciasRepository = app.preferenciasRepository,
                    deportesRepository = app.deportesRepository
                )
            }
        }
    }
}
