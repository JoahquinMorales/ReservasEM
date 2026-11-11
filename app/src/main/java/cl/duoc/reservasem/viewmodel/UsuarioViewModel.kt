package cl.duoc.reservasem.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import cl.duoc.reservasem.model.UsuarioErrores
import cl.duoc.reservasem.model.UsuarioUiState
import cl.duoc.reservasem.model.Validadores
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel encargado del ciclo de vida del formulario de registro y validación reactiva.
 * Implementa Flujo Unidireccional de Datos (UDF) consumiendo de forma centralizada [Validadores].
 */
class UsuarioViewModel : ViewModel() {

    private val _estado = MutableStateFlow(UsuarioUiState())
    val estado: StateFlow<UsuarioUiState> = _estado.asStateFlow()

    private val _fotoUri = MutableStateFlow<Uri?>(null)
    val fotoUri: StateFlow<Uri?> = _fotoUri.asStateFlow()

    fun onNombreChange(valor: String) {
        _estado.update { state ->
            state.copy(
                nombre = valor,
                errores = state.errores.copy(
                    nombre = Validadores.validarNombre(valor)
                )
            )
        }
    }

    fun onCorreoChange(valor: String) {
        _estado.update { state ->
            state.copy(
                correo = valor,
                errores = state.errores.copy(
                    correo = Validadores.validarCorreo(valor)
                )
            )
        }
    }

    fun onClaveChange(valor: String) {
        _estado.update { state ->
            state.copy(
                clave = valor,
                errores = state.errores.copy(
                    clave = Validadores.validarClave(valor)
                )
            )
        }
    }

    fun onDireccionChange(valor: String) {
        _estado.update { state ->
            state.copy(
                direccion = valor,
                errores = state.errores.copy(
                    direccion = Validadores.validarRecinto(valor)
                )
            )
        }
    }

    fun onAceptarTerminosChange(valor: Boolean) {
        _estado.update { state ->
            state.copy(
                aceptaTerminos = valor,
                errores = state.errores.copy(
                    aceptaTerminos = Validadores.validarTerminos(valor)
                )
            )
        }
    }

    fun onFotoChange(uri: Uri?) {
        _fotoUri.value = uri
    }

    fun validarFormulario(): Boolean {
        val actual = _estado.value
        val nuevosErrores = UsuarioErrores(
            nombre = Validadores.validarNombre(actual.nombre),
            correo = Validadores.validarCorreo(actual.correo),
            clave = Validadores.validarClave(actual.clave),
            direccion = Validadores.validarRecinto(actual.direccion),
            aceptaTerminos = Validadores.validarTerminos(actual.aceptaTerminos)
        )

        val tieneErrores = listOfNotNull(
            nuevosErrores.nombre,
            nuevosErrores.correo,
            nuevosErrores.clave,
            nuevosErrores.direccion,
            nuevosErrores.aceptaTerminos
        ).isNotEmpty()

        _estado.update { it.copy(errores = nuevosErrores) }
        return !tieneErrores
    }

    fun limpiarFormulario() {
        _estado.value = UsuarioUiState()
        _fotoUri.value = null
    }
}
