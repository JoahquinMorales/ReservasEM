package cl.duoc.reservasem.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.duoc.reservasem.DeportesApplication
import cl.duoc.reservasem.model.ErroresFormularioAuth
import cl.duoc.reservasem.model.LoginUiState
import cl.duoc.reservasem.model.UsuarioModel
import cl.duoc.reservasem.model.Validadores
import cl.duoc.reservasem.repository.DeportesRepository
import cl.duoc.reservasem.repository.PreferenciasRepository
import cl.duoc.reservasem.ui.components.iniciarFlujoGoogleSignIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val deportesRepository: DeportesRepository,
    private val preferenciasRepository: PreferenciasRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onCorreoChange(nuevoCorreo: String) {
        _uiState.update { state ->
            state.copy(
                correo = nuevoCorreo,
                sugerirCrearCuenta = false,
                errores = state.errores.copy(
                    correoError = Validadores.validarCorreo(nuevoCorreo),
                    generalError = null
                )
            )
        }
    }

    fun onClaveChange(nuevaClave: String) {
        _uiState.update { state ->
            state.copy(
                clave = nuevaClave,
                errores = state.errores.copy(
                    claveError = Validadores.validarClave(nuevaClave),
                    generalError = null
                )
            )
        }
    }

    fun onNombreRegistroChange(nuevoNombre: String) {
        _uiState.update { state ->
            state.copy(
                nombreRegistro = nuevoNombre,
                errores = state.errores.copy(
                    nombreError = Validadores.validarNombre(nuevoNombre),
                    generalError = null
                )
            )
        }
    }

    fun onTipoSolicitanteChange(tipo: String) {
        _uiState.update { it.copy(tipoSolicitanteRegistro = tipo) }
    }

    fun seleccionarRol(rol: String) {
        _uiState.update {
            it.copy(
                rolSeleccionado = rol,
                errores = ErroresFormularioAuth(),
                sugerirCrearCuenta = false
            )
        }
    }

    fun alternarModoRegistro(activarRegistro: Boolean) {
        _uiState.update {
            it.copy(
                modoRegistro = activarRegistro,
                errores = ErroresFormularioAuth(),
                sugerirCrearCuenta = false
            )
        }
    }

    fun autocompletarCredencialesDemo(esAdmin: Boolean) {
        if (esAdmin) {
            _uiState.update {
                it.copy(
                    correo = "admin@escuelamilitar.cl",
                    clave = "admin_2026",
                    rolSeleccionado = "ADMIN",
                    modoRegistro = false,
                    errores = ErroresFormularioAuth()
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    correo = "cadete@escuelamilitar.cl",
                    clave = "cadete_1234",
                    rolSeleccionado = "USUARIO",
                    modoRegistro = false,
                    errores = ErroresFormularioAuth()
                )
            }
        }
    }

    fun iniciarSesion() {
        val actual = _uiState.value
        val errorCorreo = Validadores.validarCorreo(actual.correo)
        val errorClave = Validadores.validarClave(actual.clave)

        if (errorCorreo != null || errorClave != null) {
            _uiState.update {
                it.copy(
                    errores = ErroresFormularioAuth(
                        correoError = errorCorreo,
                        claveError = errorClave
                    )
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true) }
            val usuarioPorCorreo = deportesRepository.buscarUsuarioPorCorreo(actual.correo)
            if (usuarioPorCorreo == null) {
                _uiState.update {
                    it.copy(
                        cargando = false,
                        sugerirCrearCuenta = true,
                        errores = ErroresFormularioAuth(
                            generalError = "La cuenta '${actual.correo}' no esta registrada en Room. Puede crearla ahora mismo." // <-- SIN TILDE
                        )
                    )
                }
                return@launch
            }

            val usuarioAutenticado = deportesRepository.autenticarUsuario(actual.correo, actual.clave)
            if (usuarioAutenticado == null) {
                _uiState.update {
                    it.copy(
                        cargando = false,
                        errores = ErroresFormularioAuth(
                            generalError = "Contrasena incorrecta para ${actual.correo}." // <-- SIN TILDE NI Ñ
                        )
                    )
                }
                return@launch
            }

            preferenciasRepository.guardarSesion(
                correo = usuarioAutenticado.correo,
                nombre = usuarioAutenticado.nombre,
                rol = usuarioAutenticado.rol,
                tipoSolicitante = usuarioAutenticado.tipoSolicitante
            )
            _uiState.update { it.copy(cargando = false, loginExitoso = true) }
        }
    }

    fun registrarNuevoUsuario() {
        val actual = _uiState.value
        val errorNombre = Validadores.validarNombre(actual.nombreRegistro)
        val errorCorreo = Validadores.validarCorreo(actual.correo)
        val errorClave = Validadores.validarClave(actual.clave)

        if (errorNombre != null || errorCorreo != null || errorClave != null) {
            _uiState.update {
                it.copy(
                    errores = ErroresFormularioAuth(
                        nombreError = errorNombre,
                        correoError = errorCorreo,
                        claveError = errorClave
                    )
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true) }
            val nuevoUsuario = UsuarioModel(
                correo = actual.correo.trim(),
                contrasena = actual.clave,
                nombre = actual.nombreRegistro.trim(),
                rol = actual.rolSeleccionado,
                tipoSolicitante = actual.tipoSolicitanteRegistro
            )
            val resultado = deportesRepository.registrarUsuario(nuevoUsuario)
            resultado.fold(
                onSuccess = { guardado ->
                    preferenciasRepository.guardarSesion(
                        correo = guardado.correo,
                        nombre = guardado.nombre,
                        rol = guardado.rol,
                        tipoSolicitante = guardado.tipoSolicitante
                    )
                    _uiState.update { it.copy(cargando = false, loginExitoso = true) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            cargando = false,
                            errores = ErroresFormularioAuth(
                                generalError = error.message ?: "Error al registrar usuario en Room"
                            )
                        )
                    }
                }
            )
        }
    }

    fun iniciarSesionGoogle(context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true) }
            iniciarFlujoGoogleSignIn(
                context = context,
                onCredencialObtenida = { correoGoogle, nombreGoogle ->
                    autenticarConGoogleEnRoom(correoGoogle, nombreGoogle)
                },
                onRequiereFallbackEvaluador = {
                    _uiState.update {
                        it.copy(
                            cargando = false,
                            mostrarDialogoEvaluadorGoogle = true
                        )
                    }
                }
            )
        }
    }

    fun mostrarDialogoEvaluadorGoogle(mostrar: Boolean) {
        _uiState.update { it.copy(mostrarDialogoEvaluadorGoogle = mostrar) }
    }

    fun autenticarConGoogleEnRoom(correo: String, nombre: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true, mostrarDialogoEvaluadorGoogle = false) }
            val rol = _uiState.value.rolSeleccionado
            val usuario = deportesRepository.autenticarORegistrarConGoogle(
                correo = correo,
                nombre = nombre,
                rolSolicitado = rol
            )
            preferenciasRepository.guardarSesion(
                correo = usuario.correo,
                nombre = usuario.nombre,
                rol = usuario.rol,
                tipoSolicitante = usuario.tipoSolicitante
            )
            _uiState.update { it.copy(cargando = false, loginExitoso = true) }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as DeportesApplication)
                LoginViewModel(
                    deportesRepository = app.deportesRepository,
                    preferenciasRepository = app.preferenciasRepository
                )
            }
        }
    }
}