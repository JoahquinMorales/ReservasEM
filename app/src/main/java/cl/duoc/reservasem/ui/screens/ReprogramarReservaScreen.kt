package cl.duoc.reservasem.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.model.Horarios
import cl.duoc.reservasem.ui.components.BannerEstadoValidacion
import cl.duoc.reservasem.ui.components.CampoTextoTactico
import cl.duoc.reservasem.ui.components.ScaffoldSecundario
import cl.duoc.reservasem.ui.components.SelectorTactico
import cl.duoc.reservasem.ui.theme.MilitaryGoldGalon
import cl.duoc.reservasem.viewmodel.CadeteViewModel

@Composable
fun ReprogramarReservaScreen(
    reservaId: Int,
    viewModel: CadeteViewModel,
    onVolver: () -> Unit
) {
    val reservas by viewModel.todasLasReservas.collectAsState()
    val state by viewModel.reprogramarState.collectAsState()
    val reserva = reservas.find { it.id == reservaId } ?: reservas.firstOrNull()

    LaunchedEffect(reserva?.id) {
        if (reserva != null) {
            viewModel.inicializarReprogramacion(reserva)
        }
    }

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
                    SelectorTactico(
                        opciones = Horarios.diasHabiles,
                        seleccionActual = state.nuevoDia,
                        onSeleccionChange = viewModel::onReprogramarDiaChange,
                        etiqueta = "Nuevo Día Hábil"
                    )

                    SelectorTactico(
                        opciones = Horarios.bloquesOficiales,
                        seleccionActual = state.nuevoBloque,
                        onSeleccionChange = viewModel::onReprogramarBloqueChange,
                        etiqueta = "Nuevo Bloque Horario (90m)"
                    )

                    CampoTextoTactico(
                        valor = state.nuevoMotivo,
                        onValorChange = viewModel::onReprogramarMotivoChange,
                        etiqueta = "Motivo o Actividad Deportiva",
                        icono = Icons.Default.Description,
                        errorMensaje = state.motivoError,
                        textoAyudaValido = "Motivo de reprogramación válido",
                        singleLine = false
                    )

                    BannerEstadoValidacion(
                        erroresActivos = listOf(state.motivoError, state.conflictoError),
                        mensajeListo = "Reprogramación validada y lista para actualizar"
                    )

                    Button(
                        onClick = {
                            viewModel.validarYGuardarReprogramacion(onExito = onVolver)
                        },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.2.dp, MilitaryGoldGalon),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                    ) {
                        Icon(Icons.Default.EditCalendar, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("GUARDAR REPROGRAMACIÓN", fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }
    }
}
