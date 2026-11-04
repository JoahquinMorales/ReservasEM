package cl.duoc.reservasem.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.ui.components.CampoTextoTactico
import cl.duoc.reservasem.ui.components.EncabezadoSeccionTactico
import cl.duoc.reservasem.ui.components.ScaffoldSecundario
import cl.duoc.reservasem.viewmodel.CadeteViewModel

@Composable
fun CrearEquipoScreen(
    viewModel: CadeteViewModel,
    onVolver: () -> Unit
) {
    var nombreEquipo by remember { mutableStateOf("") }
    var compania by remember { mutableStateOf("1ra Compañía") }
    var capitan by remember { mutableStateOf("") }
    var disciplina by remember { mutableStateOf("Fútbol") }
    var integrantes by remember { mutableStateOf("11") }

    var errorNombre by remember { mutableStateOf<String?>(null) }
    var errorCapitan by remember { mutableStateOf<String?>(null) }

    ScaffoldSecundario(
        titulo = "INSCRIBIR ESCUADRA",
        subtitulo = "Registro de equipo representativo",
        onVolver = onVolver
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            EncabezadoSeccionTactico(
                titulo = "Ficha de Escuadra Deportiva",
                subtitulo = "Inscripción oficial para torneos intercompañías"
            )

            CampoTextoTactico(
                valor = nombreEquipo,
                onValorChange = { texto ->
                    nombreEquipo = texto
                    errorNombre = if (texto.isBlank()) "Ingrese el nombre de la escuadra" else null
                },
                etiqueta = "Nombre de la Escuadra",
                errorMensaje = errorNombre
            )

            CampoTextoTactico(
                valor = compania,
                onValorChange = { compania = it },
                etiqueta = "Compañía / Unidad"
            )

            CampoTextoTactico(
                valor = capitan,
                onValorChange = { texto ->
                    capitan = texto
                    errorCapitan = if (texto.isBlank()) "Ingrese el grado y nombre del capitán" else null
                },
                etiqueta = "Capitán Responsable",
                errorMensaje = errorCapitan
            )

            CampoTextoTactico(
                valor = disciplina,
                onValorChange = { disciplina = it },
                etiqueta = "Disciplina"
            )

            CampoTextoTactico(
                valor = integrantes,
                onValorChange = { texto -> integrantes = texto.filter { ch: Char -> ch.isDigit() } },
                etiqueta = "Dotación de Integrantes"
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    errorNombre = if (nombreEquipo.isBlank()) "Ingrese el nombre de la escuadra" else null
                    errorCapitan = if (capitan.isBlank()) "Ingrese el grado y nombre del capitán" else null
                    if (errorNombre == null && errorCapitan == null) {
                        viewModel.registrarEquipo(
                            nombreEquipo = nombreEquipo,
                            compania = compania,
                            capitan = capitan,
                            disciplina = disciplina,
                            integrantes = integrantes.toIntOrNull() ?: 10
                        )
                        onVolver()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.GroupAdd, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("REGISTRAR ESCUADRA", fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}
