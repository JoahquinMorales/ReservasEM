package cl.duoc.reservasem.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.ui.components.BannerEstadoValidacion
import cl.duoc.reservasem.ui.components.CampoTextoTactico
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
    val state by viewModel.editarCanchaState.collectAsState()
    val cancha = canchas.find { it.id == canchaId } ?: canchas.firstOrNull()

    LaunchedEffect(cancha?.id) {
        if (cancha != null) {
            viewModel.inicializarEdicionCancha(cancha)
        }
    }

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
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.2.dp, MilitaryGoldGalon),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Activar Bloqueo por Mantención",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = if (state.enMantencion) "Recinto bloqueado (requiere motivo técnico)"
                                    else "Recinto habilitado para reservas",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = state.enMantencion,
                                onCheckedChange = viewModel::onEnMantencionChange
                            )
                        }

                        CampoTextoTactico(
                            valor = state.motivoMantencion,
                            onValorChange = viewModel::onMotivoMantencionChange,
                            etiqueta = "Motivo Técnico de Mantención",
                            icono = Icons.Default.Build,
                            errorMensaje = state.motivoError,
                            textoAyudaValido = "Motivo técnico registrado correctamente",
                            singleLine = false
                        )

                        BannerEstadoValidacion(
                            erroresActivos = listOf(state.motivoError),
                            mensajeListo = if (state.enMantencion) {
                                "Bloqueo técnico validado y listo para guardar en Room"
                            } else {
                                "Recinto operativo validado para guardar en Room"
                            }
                        )

                        Button(
                            onClick = {
                                viewModel.guardarEstadoCanchaValidado(cancha, onExito = onVolver)
                            },
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.2.dp, MilitaryGoldGalon),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                        ) {
                            Text("GUARDAR ESTADO EN ROOM", fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            }
        }
    }
}
