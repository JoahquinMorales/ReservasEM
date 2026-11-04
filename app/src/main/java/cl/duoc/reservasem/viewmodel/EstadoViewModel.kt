package cl.duoc.reservasem.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.reservasem.data.EstadoDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel puente entre la capa de persistencia Jetpack DataStore y los componentes de UI.
 * Expone flujos reactivos que gobiernan las animaciones declarativas de modo mantención.
 */
class EstadoViewModel(application: Application) : AndroidViewModel(application) {

    //region Dependencias y Estado Reactivo
    // Delegación del repositorio de preferencias locales
    private val estadoDataStore = EstadoDataStore(application)

    // Estado del modo mantención (null representa estado de carga inicial en disco)
    private val _activo = MutableStateFlow<Boolean?>(null)
    val activo: StateFlow<Boolean?> = _activo.asStateFlow()

    // Trigger para feedback visual animado con AnimatedVisibility
    private val _mostrarMensaje = MutableStateFlow(false)
    val mostrarMensaje: StateFlow<Boolean> = _mostrarMensaje.asStateFlow()
    //endregion

    //region Inicialización
    init {
        cargarEstado()
    }
    //endregion

    //region Lógica de Negocio y Transiciones
    // Lectura asíncrona de DataStore simulando sincronización de centro de mando
    fun cargarEstado() {
        viewModelScope.launch {
            delay(1200)
            _activo.value = estadoDataStore.obtenerEstado().first() ?: false
        }
    }

    // Inversión atómica de estado con persistencia inmediata y temporizador de alerta
    // TODO: Centralizar dispatcher en Dispatchers.IO mediante inyección de dependencias con Hilt
    fun alternarEstado() {
        viewModelScope.launch {
            val nuevoValor = !(_activo.value ?: false)
            estadoDataStore.guardarEstado(nuevoValor)
            _activo.value = nuevoValor
            _mostrarMensaje.value = true
            delay(2200)
            _mostrarMensaje.value = false
        }
    }
    //endregion
}
