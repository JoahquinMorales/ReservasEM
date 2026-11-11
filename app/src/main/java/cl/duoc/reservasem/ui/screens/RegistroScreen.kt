package cl.duoc.reservasem.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.reservasem.navigation.Screen
import cl.duoc.reservasem.ui.components.BannerEstadoValidacion
import cl.duoc.reservasem.ui.components.CampoTextoTactico
import cl.duoc.reservasem.viewmodel.UsuarioViewModel

/**
 * Pantalla de registro de antecedentes de usuario institucional.
 * Enlaza los campos de entrada de forma reactiva con el UiState del ViewModel y componentes estandarizados.
 */
@Composable
fun RegistroScreen(navController: NavController, viewModel: UsuarioViewModel) {
    val estado by viewModel.estado.collectAsState()

    val listaErrores = listOf(
        estado.errores.nombre,
        estado.errores.correo,
        estado.errores.clave,
        estado.errores.direccion,
        estado.errores.aceptaTerminos
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Ficha de Registro Militar",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold
        )

        CampoTextoTactico(
            valor = estado.nombre,
            onValorChange = viewModel::onNombreChange,
            etiqueta = "Grado y Nombre",
            icono = Icons.Default.Badge,
            errorMensaje = estado.errores.nombre,
            textoAyudaValido = "Identificación válida"
        )

        CampoTextoTactico(
            valor = estado.correo,
            onValorChange = viewModel::onCorreoChange,
            etiqueta = "Correo Institucional",
            icono = Icons.Default.Email,
            errorMensaje = estado.errores.correo,
            textoAyudaValido = "Formato de correo válido"
        )

        CampoTextoTactico(
            valor = estado.clave,
            onValorChange = viewModel::onClaveChange,
            etiqueta = "Clave Táctica",
            icono = Icons.Default.Lock,
            visualTransformation = PasswordVisualTransformation(),
            errorMensaje = estado.errores.clave,
            textoAyudaValido = "Seguridad de contraseña verificada"
        )

        CampoTextoTactico(
            valor = estado.direccion,
            onValorChange = viewModel::onDireccionChange,
            etiqueta = "Recinto o Compañía",
            icono = Icons.Default.MilitaryTech,
            errorMensaje = estado.errores.direccion
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = estado.aceptaTerminos,
                onCheckedChange = viewModel::onAceptarTerminosChange
            )
            Text("Acepto el reglamento deportivo institucional")
        }

        estado.errores.aceptaTerminos?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
        }

        BannerEstadoValidacion(
            erroresActivos = listaErrores,
            mensajeListo = "Ficha de registro validada correctamente"
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (viewModel.validarFormulario()) {
                    navController.navigate(Screen.Resumen.route)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Procesar Registro", fontWeight = FontWeight.ExtraBold)
        }
    }
}
