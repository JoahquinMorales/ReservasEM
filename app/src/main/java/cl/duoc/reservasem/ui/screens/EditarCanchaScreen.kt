package cl.duoc.reservasem.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.ui.components.ScaffoldSecundario
import cl.duoc.reservasem.ui.theme.MilitaryGoldGalon
import cl.duoc.reservasem.viewmodel.AdminViewModel

@Composable
fun EditarCanchaScreen(
    canchaId: Int,
    viewModel: AdminViewModel,
    onVolver: () -> Unit
) {
    val canchas by viewModel.canchas.collectAsState()
    val cancha = canchas.find { it.id == canchaId } ?: canchas.firstOrNull()

    var enMantencion by remember(cancha) { mutableStateOf(cancha?.enMantencion ?: false) }
    var motivo by remember(cancha) { mutableStateOf(cancha?.motivoMantencion ?: "") }

    ScaffoldSecundario(
        titulo = "Estado Operativo de Recinto",
        subtitulo = cancha?.nombre ?: "Mantención Técnica",
        onVolver = onVolver
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (cancha != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Activar Bloqueo por Mantención",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Switch(
                        checked = enMantencion,
                        onCheckedChange = { enMantencion = it }
                    )
                }

                OutlinedTextField(
                    value = motivo,
                    onValueChange = { motivo = it },
                    label = { Text("Motivo Técnico de Mantención") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        viewModel.alternarMantencionCancha(cancha, enMantencion, motivo)
                        onVolver()
                    },
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.2.dp, MilitaryGoldGalon),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("GUARDAR ESTADO EN ROOM", fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}
