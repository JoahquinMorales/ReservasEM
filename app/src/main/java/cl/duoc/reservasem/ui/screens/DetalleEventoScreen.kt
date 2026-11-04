package cl.duoc.reservasem.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
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
fun DetalleEventoScreen(
    eventoId: Int,
    viewModel: CadeteViewModel,
    onVolver: () -> Unit
) {
    val eventos by viewModel.eventos.collectAsState()
    val evento = eventos.find { it.id == eventoId } ?: eventos.firstOrNull()

    ScaffoldSecundario(
        titulo = "ORDEN DEL DÍA DEPORTIVA",
        subtitulo = "Detalle de competencia institucional",
        onVolver = onVolver
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (evento != null) {
                EncabezadoSeccionTactico(
                    titulo = evento.titulo,
                    subtitulo = "Disciplina oficial: ${evento.disciplina}"
                )

                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.2.dp, MilitaryGoldGalon)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = MilitaryGoldGalon,
                                modifier = Modifier.size(32.dp)
                            )
                            BadgeEstado(estado = evento.estado)
                        }
                        HorizontalDivider(color = MilitaryGoldGalon.copy(alpha = 0.25f))
                        Text("Recinto: ${evento.recinto}", fontWeight = FontWeight.Bold)
                        Text("Fecha y Horario: ${evento.fecha}")
                        Text("Organiza: ${evento.organizador}")
                        Text(
                            "Tenida reglamentaria: Buzo institucional o tenida deportiva de competencia autorizada por Jefatura de Deportes.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                Text("Competencia no encontrada.")
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("VOLVER A EVENTOS", fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}
