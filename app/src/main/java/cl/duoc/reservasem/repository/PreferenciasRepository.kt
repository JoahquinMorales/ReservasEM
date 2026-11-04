package cl.duoc.reservasem.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import cl.duoc.reservasem.model.SesionActivaUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "ajustes_escuela_militar_prefs")

class PreferenciasRepository(private val context: Context) {

    private object Keys {
        val SESION_INICIADA = booleanPreferencesKey("sesion_iniciada")
        val CORREO = stringPreferencesKey("correo_usuario")
        val NOMBRE = stringPreferencesKey("nombre_usuario")
        val ROL = stringPreferencesKey("rol_usuario")
        val TIPO_SOLICITANTE = stringPreferencesKey("tipo_solicitante")
        val MODO_OSCURO = booleanPreferencesKey("modo_oscuro")
        val MODO_MANTENCION = booleanPreferencesKey("modo_mantencion_activado")
        val FOTO_PERFIL_URI = stringPreferencesKey("foto_perfil_uri")
        val NOTIFICACIONES = booleanPreferencesKey("notificaciones_activas")
    }

    val sesionFlow: Flow<SesionActivaUiState> = context.dataStore.data.map { prefs ->
        SesionActivaUiState(
            sesionIniciada = prefs[Keys.SESION_INICIADA] ?: false,
            correo = prefs[Keys.CORREO] ?: "",
            nombre = prefs[Keys.NOMBRE] ?: "",
            rol = prefs[Keys.ROL] ?: "USUARIO",
            tipoSolicitante = prefs[Keys.TIPO_SOLICITANTE] ?: "Cadete",
            modoOscuro = prefs[Keys.MODO_OSCURO] ?: false,
            modoMantencionGlobal = prefs[Keys.MODO_MANTENCION] ?: false,
            fotoPerfilUriString = prefs[Keys.FOTO_PERFIL_URI] ?: "",
            notificacionesActivas = prefs[Keys.NOTIFICACIONES] ?: true
        )
    }

    val modoMantencionFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.MODO_MANTENCION] ?: false
    }

    suspend fun guardarSesion(
        correo: String,
        nombre: String,
        rol: String,
        tipoSolicitante: String
    ) {
        context.dataStore.edit { prefs ->
            prefs[Keys.SESION_INICIADA] = true
            prefs[Keys.CORREO] = correo
            prefs[Keys.NOMBRE] = nombre
            prefs[Keys.ROL] = rol
            prefs[Keys.TIPO_SOLICITANTE] = tipoSolicitante
        }
    }

    suspend fun cerrarSesion() {
        context.dataStore.edit { prefs ->
            prefs[Keys.SESION_INICIADA] = false
            prefs[Keys.CORREO] = ""
            prefs[Keys.NOMBRE] = ""
            prefs[Keys.ROL] = "USUARIO"
            prefs[Keys.TIPO_SOLICITANTE] = "Cadete"
        }
    }

    suspend fun cambiarModoOscuro(activo: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.MODO_OSCURO] = activo
        }
    }

    suspend fun guardarModoMantencion(activo: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.MODO_MANTENCION] = activo
        }
    }

    suspend fun guardarFotoPerfilUri(uriString: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.FOTO_PERFIL_URI] = uriString
        }
    }

    suspend fun cambiarNotificaciones(activas: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.NOTIFICACIONES] = activas
        }
    }
}
