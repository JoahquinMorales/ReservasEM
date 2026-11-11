package cl.duoc.reservasem.ui.screens

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.model.ReservaModel
import cl.duoc.reservasem.ui.components.DialogoCredencialTemporal
import cl.duoc.reservasem.ui.components.ImagenInteligente
import cl.duoc.reservasem.ui.components.ScaffoldSecundario
import cl.duoc.reservasem.ui.components.TarjetaCredencialTemporalRecinto
import cl.duoc.reservasem.ui.theme.MilitaryGoldGalon
import cl.duoc.reservasem.viewmodel.CadeteViewModel

@Composable
fun ConfirmarReservaScreen(
    viewModel: CadeteViewModel,
    onReservaConfirmada: () -> Unit,
    onVolver: () -> Unit
) {
    val state by viewModel.nuevaReservaState.collectAsState()
    var credencialUri by remember { mutableStateOf<Uri?>(null) }
    var reservaRecienCreada by remember { mutableStateOf<ReservaModel?>(null) }

    // Vista previa en tiempo real de la credencial con QR y código de barras que se emitirá
    val reservaPrevia = remember(state) {
        ReservaModel(
            id = state.canchaSeleccionadaId * 10 + 7,
            canchaId = state.canchaSeleccionadaId,
            nombreCancha = state.nombreCanchaSeleccionada,
            correoUsuario = state.correoSolicitante.ifBlank { "cadete@escuelamilitar.cl" },
            nombreSolicitante = state.nombreSolicitante.ifBlank { "Solicitante EM" },
            tipoSolicitante = state.tipoSolicitante,
            diaSemana = state.diaSemana,
            bloqueHorario = state.bloqueHorario,
            motivoActividad = state.motivoActividad.ifBlank { "Actividad Deportiva Institucional" },
            estado = "PENDIENTE",
            tieneCredencial = state.tieneCredencialCapturada
        )
    }

    // Al confirmar la reserva, emerge automáticamente el modal con el Código de Barras / QR Temporal
    // para el ingreso de las personas que entrarán a la instalación deportiva.
    reservaRecienCreada?.let { creada ->
        DialogoCredencialTemporal(
            reserva = creada,
            tituloModal = "¡RESERVA REGISTRADA! • PASE QR / BARRAS",
            textoBotonConfirmar = "GUARDAR PASE E IR A MIS RESERVAS",
            onDismiss = {
                reservaRecienCreada = null
                onReservaConfirmada()
            }
        )
    }

    ScaffoldSecundario(
        titulo = "Confirmación y Pase QR/Barras",
        subtitulo = state.nombreCanchaSeleccionada,
        onVolver = onVolver
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .verticalScroll(rememberScrollState()),
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
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "RESUMEN DE SOLICITUD DE RESERVA",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MilitaryGoldGalon
                    )
                    Text("• Instalación: ${state.nombreCanchaSeleccionada}", style = MaterialTheme.typography.bodySmall)
                    Text("• Solicitante: ${state.nombreSolicitante} (${state.correoSolicitante})", style = MaterialTheme.typography.bodySmall)
                    Text("• Estamento: ${state.tipoSolicitante}", style = MaterialTheme.typography.bodySmall)
                    Text(
                        text = "• Horario: ${state.diaSemana} | ${state.bloqueHorario}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text("• Actividad: ${state.motivoActividad}", style = MaterialTheme.typography.bodySmall)
                }
            }

            // Vista previa del Código de Barras / QR temporal para los asistentes a la instalación
            TarjetaCredencialTemporalRecinto(reserva = reservaPrevia)

            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.2.dp, MilitaryGoldGalon),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f)
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "CAPTURA DE CREDENCIAL MILITAR (CÁMARA / GALERÍA)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MilitaryGoldGalon
                    )
                    Text(
                        text = "Adjunte fotografía de su TUI / Credencial Institucional para validación prioritaria:",
                        style = MaterialTheme.typography.bodySmall
                    )
                    ImagenInteligente(
                        fotoUri = credencialUri,
                        onFotoCambiada = { uri ->
                            credencialUri = uri
                            viewModel.marcarCredencialCapturada(uri != null)
                        }
                    )
                }
            }

            AnimatedVisibility(visible = state.errores.conflictoHorarioError != null) {
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = state.errores.conflictoHorarioError ?: "",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            Button(
                onClick = {
                    viewModel.confirmarYGuardarReserva { reservaGuardada ->
                        reservaRecienCreada = reservaGuardada
                    }
                },
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.2.dp, MilitaryGoldGalon),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Icon(Icons.Default.QrCodeScanner, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "CONFIRMAR Y GENERAR PASE QR / BARRAS",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}
