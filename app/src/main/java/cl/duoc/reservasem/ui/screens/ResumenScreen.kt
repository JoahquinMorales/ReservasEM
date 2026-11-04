package cl.duoc.reservasem.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.reservasem.navigation.Screen
import cl.duoc.reservasem.viewmodel.UsuarioViewModel

/**
 * Pantalla intermedia de confirmación de antecedentes de registro antes de pasar a la captura biométrica/fotográfica.
 */
@Composable
fun ResumenScreen(navController: NavController, viewModel: UsuarioViewModel) {
    // Lectura del estado consolidado previamente validado
    val estado by viewModel.estado.collectAsState()

    //region Layout Principal y Contenedor de Resumen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Resumen Operativo", style = MaterialTheme.typography.headlineMedium)

        //region Tarjeta de Datos Consolidados
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Grado/Nombre: ${estado.nombre}")
                Text("Correo: ${estado.correo}")
                Text("Unidad: ${estado.direccion}")
                Text("Términos: ${if (estado.aceptaTerminos) "Aprobados" else "Rechazados"}")
            }
        }
        //endregion

        Spacer(modifier = Modifier.height(24.dp))

        //region Acciones de Navegación
        // Transición hacia la captura y validación con cámara nativa
        Button(
            onClick = { navController.navigate(Screen.Perfil.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continuar a Validación de Credencial (Cámara)")
        }
        //endregion
    }
    //endregion
}
