package cl.duoc.reservasem.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import cl.duoc.reservasem.model.ReservaModel
import cl.duoc.reservasem.model.Validadores
import cl.duoc.reservasem.navigation.Rutas
import cl.duoc.reservasem.ui.components.AppScaffold
import cl.duoc.reservasem.ui.components.BadgeEstado
import cl.duoc.reservasem.ui.components.CampoTextoTactico
import cl.duoc.reservasem.ui.components.EncabezadoSeccionTactico
import cl.duoc.reservasem.ui.components.KpiCardTactica
import cl.duoc.reservasem.ui.components.agendarEnGoogleCalendar
import cl.duoc.reservasem.ui.theme.BadgeAprobadaCamuflaje
import cl.duoc.reservasem.ui.theme.BadgePendienteAmbar
import cl.duoc.reservasem.ui.theme.BadgeRechazadaOxido
import cl.duoc.reservasem.ui.theme.MilitaryGoldGalon
import cl.duoc.reservasem.ui.theme.VibrantMilitaryGreen
import cl.duoc.reservasem.viewmodel.AdminViewModel
import cl.duoc.reservasem.viewmodel.AppViewModel

@Composable
fun AdminScreen(
    adminViewModel: AdminViewModel,
    appViewModel: AppViewModel,
    onNavegar: (String) -> Unit,
    onCerrarSesion: () -> Unit
) {
    val resumen by adminViewModel.resumenOperativo.collectAsState()
    val reservas by adminViewModel.reservas.collectAsState()
    val sesion by appViewModel.sesionState.collectAsState()
    val context = LocalContext.current

    var reservaARechazar by remember { mutableStateOf<ReservaModel?>(null) }
    var motivoRechazoTexto by remember { mutableStateOf("") }
    var errorMotivoRechazo by remember { mutableStateOf<String?>(null) }

    if (reservaARechazar != null) {
        AlertDialog(
            onDismissRequest = {
                reservaARechazar = null
                errorMotivoRechazo = null
            },
            shape = RoundedCornerShape(10.dp),
            title = {
                Text(
                    text = "RECHAZAR SOLICITUD DE RESERVA",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Recinto: ${reservaARechazar?.nombreCancha}\nSolicitante: ${reservaARechazar?.nombreSolicitante}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    CampoTextoTactico(
                        valor = motivoRechazoTexto,
                        onValorChange = { texto ->
                            motivoRechazoTexto = texto
                            errorMotivoRechazo = Validadores.validarMotivoRechazo(texto)
                        },
                        etiqueta = "Motivo oficial del rechazo (mín. 5 caracteres)",
                        icono = Icons.Default.Gavel,
                        errorMensaje = errorMotivoRechazo,
                        textoAyudaValido = "Motivo oficial válido",
                        singleLine = false
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val error = Validadores.validarMotivoRechazo(motivoRechazoTexto)
                        if (error != null) {
                            errorMotivoRechazo = error
                        } else {
                            reservaARechazar?.let {
                                adminViewModel.rechazarReserva(it, motivoRechazoTexto)
                            }
                            motivoRechazoTexto = ""
                            errorMotivoRechazo = null
                            reservaARechazar = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BadgeRechazadaOxido),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text("CONFIRMAR RECHAZO", fontWeight = FontWeight.ExtraBold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        reservaARechazar = null
                        errorMotivoRechazo = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    AppScaffold(
        titulo = "Puesto de Mando • Admin",
        subtitulo = sesion.nombre.ifBlank { "Oficial Administrador Deportivo" },
        rutaActual = Rutas.HOME_ADMIN,
        esAdmin = true,
        onNavegar = onNavegar,
        onCerrarSesion = onCerrarSesion
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 18.dp)
        ) {
            item {
                EncabezadoSeccionTactico(
                    titulo = "Resumen Operativo en Tiempo Real",
                    accionTexto = "Ver Reportes",
                    onAccionClick = { onNavegar(Rutas.REPORTES) }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    KpiCardTactica(
                        titulo = "Pendientes",
                        valor = "${resumen.reservasPendientes}",
                        icono = Icons.Default.PendingActions,
                        colorAcento = BadgePendienteAmbar,
                        modifier = Modifier.weight(1f)
                    )
                    KpiCardTactica(
                        titulo = "Autorizadas",
                        valor = "${resumen.reservasAprobadas}",
                        icono = Icons.Default.Verified,
                        colorAcento = BadgeAprobadaCamuflaje,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    KpiCardTactica(
                        titulo = "Recintos Activos",
                        valor = "${resumen.canchasOperativas}/${resumen.totalCanchas}",
                        icono = Icons.Default.Stadium,
                        colorAcento = VibrantMilitaryGreen,
                        modifier = Modifier.weight(1f)
                    )
                    KpiCardTactica(
                        titulo = "En Mantención",
                        valor = "${resumen.canchasEnMantencion}",
                        icono = Icons.Default.Build,
                        colorAcento = BadgeRechazadaOxido,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Alerta destacada cuando hay nuevos usuarios pendientes de confirmación
            if (resumen.usuariosPendientesAprobacion > 0) {
                item {
                    OutlinedCard(
                        onClick = { onNavegar(Rutas.USUARIOS_ADMIN) },
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.3.dp, BadgePendienteAmbar),
                        colors = CardDefaults.outlinedCardColors(
                            containerColor = BadgePendienteAmbar.copy(alpha = 0.14f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.HowToReg,
                                    contentDescription = null,
                                    tint = MilitaryGoldGalon
                                )
                                Column {
                                    Text(
                                        text = "CONFIRMAR NUEVOS USUARIOS (${resumen.usuariosPendientesAprobacion})",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MilitaryGoldGalon
                                    )
                                    Text(
                                        text = "Existen cuentas institucionales esperando aprobación de mando",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = MilitaryGoldGalon
                            )
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { onNavegar(Rutas.INSTALACIONES_ADMIN) },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, MilitaryGoldGalon),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Default.Build,
                            contentDescription = null,
                            tint = MilitaryGoldGalon,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "MANTENCIÓN (${resumen.totalCanchas})",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1
                        )
                    }
                    OutlinedButton(
                        onClick = { onNavegar(Rutas.USUARIOS_ADMIN) },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, MilitaryGoldGalon),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Default.HowToReg,
                            contentDescription = null,
                            tint = MilitaryGoldGalon,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "USUARIOS (${resumen.totalUsuariosRegistrados})",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1
                        )
                    }
                }
            }

            item {
                EncabezadoSeccionTactico(
                    titulo = "Solicitudes de Reserva (${reservas.size})",
                    accionTexto = "Ver Todas",
                    onAccionClick = { onNavegar(Rutas.SOLICITUDES_ADMIN) }
                )
            }

            items(reservas, key = { it.id }) { reserva ->
                TarjetaReservaAdmin(
                    reserva = reserva,
                    onAprobar = { adminViewModel.aprobarReserva(reserva) },
                    onRechazar = {
                        motivoRechazoTexto = ""
                        errorMotivoRechazo = null
                        reservaARechazar = reserva
                    },
                    onSincronizarCalendar = {
                        agendarEnGoogleCalendar(
                            context = context,
                            reserva = reserva,
                            esAdmin = true
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun TarjetaReservaAdmin(
    reserva: ReservaModel,
    onAprobar: () -> Unit,
    onRechazar: () -> Unit,
    onSincronizarCalendar: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = when (reserva.estado.uppercase()) {
            "APROBADA" -> VibrantMilitaryGreen
            "RECHAZADA" -> BadgeRechazadaOxido
            else -> MilitaryGoldGalon
        },
        animationSpec = tween(durationMillis = 350),
        label = "adminCardBorder"
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
            ),
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
                text = "SOLICITANTE: ${reserva.nombreSolicitante} (${reserva.correoUsuario})",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "ESTAMENTO: ${reserva.tipoSolicitante}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "HORARIO: ${reserva.diaSemana} • ${reserva.bloqueHorario}",
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
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "✔ Credencial institucional adjunta verificada",
                    style = MaterialTheme.typography.labelSmall,
                    color = VibrantMilitaryGreen,
                    fontWeight = FontWeight.ExtraBold
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
                        text = "MOTIVO RECHAZO: ${reserva.motivoRechazo}",
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
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (reserva.estado == "PENDIENTE") {
                    Button(
                        onClick = onAprobar,
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, MilitaryGoldGalon.copy(alpha = 0.7f)),
                        colors = ButtonDefaults.buttonColors(containerColor = BadgeAprobadaCamuflaje),
                        contentPadding = PaddingValues(vertical = 6.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("APROBAR", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.ExtraBold)
                    }
                    OutlinedButton(
                        onClick = onRechazar,
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, BadgeRechazadaOxido),
                        contentPadding = PaddingValues(vertical = 6.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = BadgeRechazadaOxido
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "RECHAZAR",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = BadgeRechazadaOxido
                        )
                    }
                }

                OutlinedButton(
                    onClick = onSincronizarCalendar,
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, MilitaryGoldGalon),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier
                        .then(if (reserva.estado != "PENDIENTE") Modifier.fillMaxWidth() else Modifier)
                        .height(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Agendar en Google Calendar",
                        modifier = Modifier.size(16.dp),
                        tint = MilitaryGoldGalon
                    )
                    if (reserva.estado != "PENDIENTE") {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "AGENDAR EN GOOGLE CALENDAR",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}
