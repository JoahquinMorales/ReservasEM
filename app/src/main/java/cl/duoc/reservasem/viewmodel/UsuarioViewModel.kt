package cl.duoc.reservasem.viewmodel

import android.net.Uri
import android.util.Patterns
import androidx.lifecycle.ViewModel
import cl.duoc.reservasem.model.UsuarioErrores
import cl.duoc.reservasem.model.UsuarioUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel encargado del ciclo de vida del formulario de registro y validación reactiva.
 * Implementa Flujo Unidireccional de Datos (UDF) hacia la UI.
 */
class UsuarioViewModel : ViewModel() {

    //region Estado Reactivo y Flujos
    // Inmutabilidad del UiState con StateFlow protegiendo la mutabilidad interna
    private val _estado = MutableStateFlow(UsuarioUiState())
    val estado: StateFlow<UsuarioUiState> = _estado.asStateFlow()

    // Manejo de referencia URI para el recurso nativo fotográfico
    private val _fotoUri = MutableStateFlow<Uri?>(null)
    val fotoUri: StateFlow<Uri?> = _fotoUri.asStateFlow()
    //endregion

    //region Manejo de Eventos de Entrada
    // Actualización granular de nombre con validación en tiempo de captura
    fun onNombreChange(valor: String) {
        _estado.update { state ->
            state.copy(
                nombre = valor,
                errores = state.errores.copy(
                    nombre = if (valor.trim().isEmpty()) "El grado y nombre del cadete es obligatorio" else null
                )
            )
        }
    }

    // Validación reactiva de correo institucional según patrón RFC 5322
    fun onCorreoChange(valor: String) {
        _estado.update { state ->
            val errorCorreo = when {
                valor.trim().isEmpty() -> "El correo institucional es obligatorio"
                !Patterns.EMAIL_ADDRESS.matcher(valor.trim()).matches() ->
                    "Formato de correo inválido (ej: cadete@escuelamilitar.cl)"
                else -> null
            }
            state.copy(
                correo = valor,
                errores = state.errores.copy(correo = errorCorreo)
            )
        }
    }

    // Validación de longitud mínima para directivas de seguridad de acceso
    fun onClaveChange(valor: String) {
        _estado.update { state ->
            val errorClave = when {
                valor.isEmpty() -> "Debe ingresar su clave de acceso"
                valor.length < 8 -> "La clave debe contener al menos 8 caracteres"
                else -> null
            }
            state.copy(
                clave = valor,
                errores = state.errores.copy(clave = errorClave)
            )
        }
    }

    // Registro de compañía o recinto de asignación operativa
    fun onDireccionChange(valor: String) {
        _estado.update { state ->
            state.copy(
                direccion = valor,
                errores = state.errores.copy(
                    direccion = if (valor.trim().isEmpty()) {
                        "Indique el recinto deportivo o compañía asignada"
                    } else null
                )
            )
        }
    }

    // Registro de aceptación legal de normativa institucional interna
    fun onAceptarTerminosChange(valor: Boolean) {
        _estado.update { state ->
            state.copy(
                aceptaTerminos = valor,
                errores = state.errores.copy(
                    aceptaTerminos = if (!valor) {
                        "Debe aceptar el reglamento deportivo de la Escuela Militar"
                    } else null
                )
            )
        }
    }

    // Sincronización de URI obtenida desde contratos nativos de cámara o galería
    fun onFotoChange(uri: Uri?) {
        _fotoUri.value = uri
    }
    //endregion

    //region Lógica de Validación de Negocio
    // Validación atómica previa al avance de pantalla o persistencia definitiva
    fun validarFormulario(): Boolean {
        val actual = _estado.value
        val nuevosErrores = UsuarioErrores(
            nombre = if (actual.nombre.trim().isEmpty()) {
                "El grado y nombre del cadete es obligatorio"
            } else null,
            correo = when {
                actual.correo.trim().isEmpty() -> "El correo institucional es obligatorio"
                !Patterns.EMAIL_ADDRESS.matcher(actual.correo.trim()).matches() ->
                    "Formato de correo inválido (ej: cadete@escuelamilitar.cl)"
                else -> null
            },
            clave = when {
                actual.clave.isEmpty() -> "Debe ingresar su clave de acceso"
                actual.clave.length < 8 -> "La clave debe contener al menos 8 caracteres"
                else -> null
            },
            direccion = if (actual.direccion.trim().isEmpty()) {
                "Indique el recinto deportivo o compañía asignada"
            } else null,
            aceptaTerminos = if (!actual.aceptaTerminos) {
                "Debe aceptar el reglamento deportivo de la Escuela Militar"
            } else null
        )

        // Verificación de nulidad en todo el árbol de errores
        val tieneErrores = listOfNotNull(
            nuevosErrores.nombre,
            nuevosErrores.correo,
            nuevosErrores.clave,
            nuevosErrores.direccion,
            nuevosErrores.aceptaTerminos
        ).isNotEmpty()

        // Emisión del estado consolidado de errores a los componentes visuales
        _estado.update { it.copy(errores = nuevosErrores) }
        return !tieneErrores
    }

    // Reseteo del formulario tras navegación exitosa
    // TODO: Integrar Room Database para persistir perfil de usuario registrado si se desacopla de la sesión activa
    fun limpiarFormulario() {
        _estado.value = UsuarioUiState()
        _fotoUri.value = null
    }
    //endregion
}
