package cl.duoc.reservasem.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//region Configuración de Delegado DataStore
// Delegado top-level para garantizar instancia única del archivo de preferencias
private val Context.estadoDataStore by preferencesDataStore(name = "preferencias_mando_militar")
//endregion

/**
 * Capa de persistencia asíncrona no volátil basada en Jetpack DataStore Preferences.
 * Centraliza el almacenamiento reactivo del estado operativo global del sistema militar.
 */
class EstadoDataStore(private val context: Context) {

    //region Llaves Tipadas de Preferencias
    companion object {
        // Llave booleana que sincroniza el bloqueo/disponibilidad general de recintos
        val MODO_MANTENCION_KEY = booleanPreferencesKey("modo_mantencion_activado")

        // TODO: Migrar llaves de sesión a DataStore proto si se requiere serialización compleja con cifrado Tink
    }
    //endregion

    //region Operaciones Asíncronas de Persistencia
    // Operación transaccional segura ejecutada en contexto de IO subyacente
    suspend fun guardarEstado(nuevoEstado: Boolean) {
        context.estadoDataStore.edit { preferencias ->
            preferencias[MODO_MANTENCION_KEY] = nuevoEstado
        }
    }

    // Exposición reactiva mediante Flow desacoplado del ciclo de vida
    fun obtenerEstado(): Flow<Boolean?> {
        return context.estadoDataStore.data.map { preferencias ->
            preferencias[MODO_MANTENCION_KEY]
        }
    }
    //endregion
}
