package cl.duoc.reservasem.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.ui.components.BadgeEstado
import cl.duoc.reservasem.ui.components.ScaffoldSecundario
import cl.duoc.reservasem.ui.components.agendarEnGoogleCalendar
import cl.duoc.reservasem.ui.theme.BadgeRechazadaOxido
import cl.duoc.reservasem.ui.theme.MilitaryGoldGalon
import cl.duoc.reservasem.ui.theme.VibrantMilitaryGreen
import cl.duoc.reservasem.viewmodel.CadeteViewModel

@Composable
fun MisReservasScreen(
    viewModel: CadeteViewModel,
    onVerDetalle: (Int) -> Unit,
    onReprogramar: (Int) -> Unit,
    onVolver: () -> Unit
) {
    val reservas by viewModel.todasLasReservas.collectAsState()
    var filtroEstado by remember { mutableStateOf("TODAS") }
    val context = LocalContext.current

    val reservasFiltradas = reservas.filter {
        filtroEstado == "TODAS" || it.estado == filtroEstado
    }

    ScaffoldSecundario(
        titulo = "Mis Reservas y Programación",
        subtitulo = "${reservasFiltradas.size} solicitudes registradas",
        onVolver = onVolver
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("TODAS", "PENDIENTE", "APROBADA", "RECHAZADA").forEach { estado ->
                    FilterChip(
                        selected = filtroEstado == estado,
                        onClick = { filtroEstado = estado },
                        label = {
                            Text(
                                text = estado,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                maxLines = 1
                            )
                        },
                        shape = RoundedCornerShape(6.dp),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = filtroEstado == estado,
                            borderColor = MilitaryGoldGalon.copy(alpha = 0.55f),
                            selectedBorderColor = MilitaryGoldGalon
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(reservasFiltradas, key = { it.id }) { reserva ->
                    val borderColor by animateColorAsState(
                        targetValue = when (reserva.estado.uppercase()) {
                            "APROBADA" -> VibrantMilitaryGreen
                            "RECHAZADA" -> BadgeRechazadaOxido
                            else -> MilitaryGoldGalon
                        },
                        animationSpec = tween(durationMillis = 350),
                        label = "reservaBorderColor"
                    )
                    val shape = RoundedCornerShape(10.dp)

                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 4.dp,
                                shape = shape,
                                ambientColor = borderColor.copy(alpha = 0.25f),
                                spotColor = borderColor.copy(alpha = 0.35f)
                            )
                            .animateContentSize(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                    stiffness = Spring.StiffnessMediumLow
                                )
                            )
                            .clickable { onVerDetalle(reserva.id) },
                        shape = shape,
                        border = BorderStroke(1.2.dp, borderColor.copy(alpha = 0.8f)),
                        colors = CardDefaults.outlinedCardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            borderColor.copy(alpha = 0.08f),
                                            Color.Transparent
                                        )
                                    )
                                )
                                .padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = reserva.nombreCancha,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                BadgeEstado(reserva.estado)
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "SOLICITANTE: ${reserva.nombreSolicitante} (${reserva.tipoSolicitante})",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "BLOQUE: ${reserva.diaSemana} • ${reserva.bloqueHorario}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = MilitaryGoldGalon
                            )
                            Text(
                                text = "ACTIVIDAD: ${reserva.motivoActividad}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            if (reserva.tieneCredencial) {
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "✔ Credencial institucional verificada adjunta",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = VibrantMilitaryGreen
                                )
                            }

                            if (reserva.estado == "RECHAZADA" && reserva.motivoRechazo.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Surface(
                                    color = BadgeRechazadaOxido.copy(alpha = 0.14f),
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(0.8.dp, BadgeRechazadaOxido.copy(alpha = 0.6f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "OBSERVACIÓN DE MANDO: ${reserva.motivoRechazo}",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(
                                thickness = 0.6.dp,
                                color = MilitaryGoldGalon.copy(alpha = 0.28f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        agendarEnGoogleCalendar(
                                            context = context,
                                            reserva = reserva,
                                            esAdmin = false
                                        )
                                    },
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(1.dp, MilitaryGoldGalon),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Icon(
                                        Icons.Default.CalendarMonth,
                                        contentDescription = null,
                                        tint = MilitaryGoldGalon,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = "Agendar en Google Calendar",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }

                                if (reserva.estado != "APROBADA") {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    OutlinedButton(
                                        onClick = { onReprogramar(reserva.id) },
                                        shape = RoundedCornerShape(6.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        modifier = Modifier.height(34.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.EditCalendar,
                                            contentDescription = null,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Reprogramar",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                if (reserva.estado == "PENDIENTE") {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    IconButton(
                                        onClick = { viewModel.cancelarReserva(reserva) },
                                        modifier = Modifier.size(34.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Cancelar reserva",
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
