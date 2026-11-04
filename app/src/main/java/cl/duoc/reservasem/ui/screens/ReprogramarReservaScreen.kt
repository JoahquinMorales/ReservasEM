package cl.duoc.reservasem.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.model.Horarios
import cl.duoc.reservasem.ui.components.ScaffoldSecundario
import cl.duoc.reservasem.ui.theme.MilitaryGoldGalon
import cl.duoc.reservasem.viewmodel.CadeteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReprogramarReservaScreen(
    reservaId: Int,
    viewModel: CadeteViewModel,
    onVolver: () -> Unit
) {
    val reservas by viewModel.todasLasReservas.collectAsState()
    val reserva = reservas.find { it.id == reservaId } ?: reservas.firstOrNull()

    var nuevoDia by remember(reserva) { mutableStateOf(reserva?.diaSemana ?: "Lunes") }
    var nuevoBloque by remember(reserva) { mutableStateOf(reserva?.bloqueHorario ?: "08:00 - 09:30") }
    var nuevoMotivo by remember(reserva) { mutableStateOf(reserva?.motivoActividad ?: "") }
    var expDia by remember { mutableStateOf(false) }
    var expBloque by remember { mutableStateOf(false) }

    ScaffoldSecundario(
        titulo = "Reprogramar Solicitud",
        subtitulo = reserva?.nombreCancha ?: "Ajuste de Bloque Horario",
        onVolver = onVolver
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expDia,
                onExpandedChange = { expDia = !expDia }
            ) {
                OutlinedTextField(
                    value = nuevoDia,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Nuevo Día Hábil") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expDia) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expDia, onDismissRequest = { expDia = false }) {
                    Horarios.diasHabiles.forEach { d ->
                        DropdownMenuItem(
                            text = { Text(d) },
                            onClick = { nuevoDia = d; expDia = false }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = expBloque,
                onExpandedChange = { expBloque = !expBloque }
            ) {
                OutlinedTextField(
                    value = nuevoBloque,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Nuevo Bloque (90m)") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expBloque) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expBloque, onDismissRequest = { expBloque = false }) {
                    Horarios.bloquesOficiales.forEach { b ->
                        DropdownMenuItem(
                            text = { Text(b) },
                            onClick = { nuevoBloque = b; expBloque = false }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = nuevoMotivo,
                onValueChange = { nuevoMotivo = it },
                label = { Text("Motivo o Actividad") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    viewModel.reprogramarReserva(
                        reservaId = reservaId,
                        nuevoDia = nuevoDia,
                        nuevoBloque = nuevoBloque,
                        nuevoMotivo = nuevoMotivo,
                        onExito = onVolver
                    )
                },
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.2.dp, MilitaryGoldGalon),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("GUARDAR REPROGRAMACIÓN", fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}
