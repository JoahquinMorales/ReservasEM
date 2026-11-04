package cl.duoc.reservasem.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.model.CanchaModel
import cl.duoc.reservasem.ui.components.BadgeEstado
import cl.duoc.reservasem.ui.components.ScaffoldSecundario
import cl.duoc.reservasem.ui.theme.MilitaryGoldGalon
import cl.duoc.reservasem.viewmodel.CadeteViewModel

@Composable
fun FichaCanchaScreen(
    canchaId: Int,
    viewModel: CadeteViewModel,
    onSolicitarReserva: (CanchaModel) -> Unit,
    onVolver: () -> Unit
) {
    val canchas by viewModel.canchas.collectAsState()
    val cancha = canchas.find { it.id == canchaId } ?: canchas.firstOrNull()

    ScaffoldSecundario(
        titulo = "Ficha Técnica de Instalación",
        subtitulo = cancha?.nombre ?: "Recinto Escuela Militar",
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
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.2.dp, MilitaryGoldGalon),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = cancha.nombre,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold
                        )
                        BadgeEstado(if (cancha.enMantencion) "MANTENCIÓN" else "OPERATIVO")
                        HorizontalDivider(color = MilitaryGoldGalon.copy(alpha = 0.35f))
                        Text("• Categoría: ${cancha.categoria}", style = MaterialTheme.typography.bodyMedium)
                        Text("• Capacidad Máxima: ${cancha.capacidad} personas", style = MaterialTheme.typography.bodyMedium)
                        Text("• Horario Operativo: Lunes a Viernes (08:00 a 20:30 hrs)", style = MaterialTheme.typography.bodyMedium)
                        if (cancha.enMantencion) {
                            Text(
                                text = "⚠ Motivo Mantención: ${cancha.motivoMantencion}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                if (!cancha.enMantencion) {
                    Button(
                        onClick = { onSolicitarReserva(cancha) },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.2.dp, MilitaryGoldGalon),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text("SOLICITAR RESERVA EN ESTE RECINTO", fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }
    }
}
