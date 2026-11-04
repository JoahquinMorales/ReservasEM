package cl.duoc.reservasem.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.ui.components.BadgeEstado
import cl.duoc.reservasem.ui.components.EncabezadoSeccionTactico
import cl.duoc.reservasem.ui.components.ScaffoldSecundario
import cl.duoc.reservasem.ui.theme.MilitaryGoldGalon
import cl.duoc.reservasem.viewmodel.CadeteViewModel

@Composable
fun NotificacionesScreen(
    viewModel: CadeteViewModel,
    onVolver: () -> Unit
) {
    val reservas by viewModel.todasLasReservas.collectAsState()

    ScaffoldSecundario(
        titulo = "COMUNICADOS Y ALERTAS",
        subtitulo = "Resoluciones de Jefatura de Deportes",
        onVolver = onVolver
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                EncabezadoSeccionTactico(
                    titulo = "Bandeja Táctica de Avisos",
                    subtitulo = "Estado actualizado de sus solicitudes de cancha"
                )
            }

            items(reservas, key = { it.id }) { reserva ->
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, MilitaryGoldGalon.copy(alpha = 0.55f))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsActive,
                                    contentDescription = null,
                                    tint = MilitaryGoldGalon
                                )
                                Text(
                                    text = reserva.nombreCancha,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            BadgeEstado(estado = reserva.estado)
                        }
                        Text(
                            text = "Horario: ${reserva.diaSemana} (${reserva.bloqueHorario}) • Solicitante: ${reserva.nombreSolicitante}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        if (reserva.motivoRechazo.isNotBlank()) {
                            Text(
                                text = "Observación Jefatura: ${reserva.motivoRechazo}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
