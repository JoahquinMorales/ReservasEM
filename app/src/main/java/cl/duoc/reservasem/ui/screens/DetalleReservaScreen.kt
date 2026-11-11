package cl.duoc.reservasem.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.ui.components.BadgeEstado
import cl.duoc.reservasem.ui.components.EncabezadoSeccionTactico
import cl.duoc.reservasem.ui.components.ScaffoldSecundario
import cl.duoc.reservasem.ui.components.TarjetaCredencialTemporalRecinto
import cl.duoc.reservasem.ui.components.agendarEnGoogleCalendar
import cl.duoc.reservasem.ui.theme.MilitaryGoldGalon
import cl.duoc.reservasem.viewmodel.CadeteViewModel

@Composable
fun DetalleReservaScreen(
    reservaId: Int,
    viewModel: CadeteViewModel,
    esAdmin: Boolean,
    onReprogramar: (Int) -> Unit,
    onVolver: () -> Unit
) {
    val reservas by viewModel.todasLasReservas.collectAsState()
    val reserva = reservas.find { it.id == reservaId } ?: reservas.firstOrNull()
    val context = LocalContext.current

    ScaffoldSecundario(
        titulo = "Ficha y Credencial Temporal",
        subtitulo = "Folio #${reserva?.id ?: reservaId}",
        onVolver = onVolver
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (reserva != null) {
                EncabezadoSeccionTactico(
                    titulo = "Credencial Temporal de Ingreso al Recinto",
                    subtitulo = "Presente este pase digital en la guardia del recinto deportivo"
                )

                TarjetaCredencialTemporalRecinto(reserva = reserva)

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
                            text = reserva.nombreCancha,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold
                        )
                        BadgeEstado(reserva.estado)
                        HorizontalDivider(color = MilitaryGoldGalon.copy(alpha = 0.35f))
                        Text("• Solicitante: ${reserva.nombreSolicitante}", style = MaterialTheme.typography.bodyMedium)
                        Text("• Correo: ${reserva.correoUsuario}", style = MaterialTheme.typography.bodyMedium)
                        Text("• Estamento: ${reserva.tipoSolicitante}", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = "• Día y Bloque: ${reserva.diaSemana} | ${reserva.bloqueHorario}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text("• Actividad: ${reserva.motivoActividad}", style = MaterialTheme.typography.bodyMedium)
                        if (reserva.motivoRechazo.isNotBlank()) {
                            Text(
                                text = "• Observación de Mando: ${reserva.motivoRechazo}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Button(
                    onClick = { agendarEnGoogleCalendar(context, reserva, esAdmin) },
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.2.dp, MilitaryGoldGalon),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("AGENDAR EN GOOGLE CALENDAR", fontWeight = FontWeight.ExtraBold)
                }

                if (reserva.estado != "APROBADA") {
                    OutlinedButton(
                        onClick = { onReprogramar(reserva.id) },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, MilitaryGoldGalon),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.EditCalendar, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("REPROGRAMAR BLOQUE HORARIO", fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }
    }
}
