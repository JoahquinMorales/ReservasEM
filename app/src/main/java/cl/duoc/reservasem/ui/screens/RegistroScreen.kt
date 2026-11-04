package cl.duoc.reservasem.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.reservasem.navigation.Screen
import cl.duoc.reservasem.viewmodel.UsuarioViewModel

/**
 * Pantalla de registro de antecedentes de usuario institucional.
 * Enlaza los campos de entrada de forma reactiva con el UiState del ViewModel.
 */
@Composable
fun RegistroScreen(navController: NavController, viewModel: UsuarioViewModel) {
    // Sincronización del estado observable mediante recolección con ciclo de vida Compose
    val estado by viewModel.estado.collectAsState()

    //region Layout Principal y Scroll
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Ficha de Registro Militar", style = MaterialTheme.typography.headlineMedium)

        //region Campos del Formulario
        OutlinedTextField(
            value = estado.nombre,
            onValueChange = viewModel::onNombreChange,
            label = { Text("Grado y Nombre") },
            modifier = Modifier.fillMaxWidth(),
            isError = estado.errores.nombre != null,
            supportingText = { estado.errores.nombre?.let { Text(it) } }
        )

        OutlinedTextField(
            value = estado.correo,
            onValueChange = viewModel::onCorreoChange,
            label = { Text("Correo Institucional") },
            modifier = Modifier.fillMaxWidth(),
            isError = estado.errores.correo != null,
            supportingText = { estado.errores.correo?.let { Text(it) } }
        )

        OutlinedTextField(
            value = estado.clave,
            onValueChange = viewModel::onClaveChange,
            label = { Text("Clave Táctica") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = estado.errores.clave != null,
            supportingText = { estado.errores.clave?.let { Text(it) } }
        )

        OutlinedTextField(
            value = estado.direccion,
            onValueChange = viewModel::onDireccionChange,
            label = { Text("Recinto o Compañía") },
            modifier = Modifier.fillMaxWidth(),
            isError = estado.errores.direccion != null,
            supportingText = { estado.errores.direccion?.let { Text(it) } }
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = estado.aceptaTerminos,
                onCheckedChange = viewModel::onAceptarTerminosChange
            )
            Text("Acepto el reglamento deportivo institucional")
        }
        //endregion

        //region Retroalimentación Visual y Errores
        estado.errores.aceptaTerminos?.let {
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Disparo de validación y navegación condicional
        Button(
            onClick = {
                if (viewModel.validarFormulario()) {
                    navController.navigate(Screen.Resumen.route)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Procesar Registro")
        }
        //endregion
    }
    //endregion
}
