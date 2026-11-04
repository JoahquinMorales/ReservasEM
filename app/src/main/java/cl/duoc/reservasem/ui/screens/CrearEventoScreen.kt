package cl.duoc.reservasem.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
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
import cl.duoc.reservasem.viewmodel.AdminViewModel

@Composable
fun CrearEventoScreen(
    viewModel: AdminViewModel,
    onVolver: () -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var disciplina by remember { mutableStateOf("") }
    var recinto by remember { mutableStateOf("Estadio / Cancha de Fútbol y Atletismo") }
    var fecha by remember { mutableStateOf("Viernes 16:00 hrs") }
    var organizador by remember { mutableStateOf("Jefatura de Deportes EM") }

    var errorTitulo by remember { mutableStateOf<String?>(null) }
    var errorDisciplina by remember { mutableStateOf<String?>(null) }

    ScaffoldSecundario(
        titulo = "PROGRAMAR COMPETENCIA",
        subtitulo = "Publicación oficial de torneo intercompañías",
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
                titulo = "Nuevo Evento Institucional",
                subtitulo = "Complete los antecedentes para la orden del día"
            )

            CampoTextoTactico(
                valor = titulo,
                onValorChange = { texto ->
                    titulo = texto
                    errorTitulo = if (texto.isBlank()) "El título del evento es obligatorio" else null
                },
                etiqueta = "Título del Torneo / Competencia",
                errorMensaje = errorTitulo
            )

            CampoTextoTactico(
                valor = disciplina,
                onValorChange = { texto ->
                    disciplina = texto
                    errorDisciplina = if (texto.isBlank()) "La disciplina es obligatoria" else null
                },
                etiqueta = "Disciplina Deportiva",
                errorMensaje = errorDisciplina
            )

            CampoTextoTactico(
                valor = recinto,
                onValorChange = { recinto = it },
                etiqueta = "Recinto Asignado"
            )

            CampoTextoTactico(
                valor = fecha,
                onValorChange = { fecha = it },
                etiqueta = "Fecha y Hora Programada"
            )

            CampoTextoTactico(
                valor = organizador,
                onValorChange = { organizador = it },
                etiqueta = "Unidad Organizadora"
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    errorTitulo = if (titulo.isBlank()) "El título del evento es obligatorio" else null
                    errorDisciplina = if (disciplina.isBlank()) "La disciplina es obligatoria" else null
                    if (errorTitulo == null && errorDisciplina == null) {
                        viewModel.crearEventoDeportivo(
                            titulo = titulo,
                            disciplina = disciplina,
                            recinto = recinto,
                            fecha = fecha,
                            organizador = organizador
                        )
                        onVolver()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.EmojiEvents, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("PUBLICAR EVENTO OFICIAL", fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}
