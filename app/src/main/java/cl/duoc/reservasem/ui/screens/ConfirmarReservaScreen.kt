package cl.duoc.reservasem.ui.screens

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.ui.components.ImagenInteligente
import cl.duoc.reservasem.ui.components.ScaffoldSecundario
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

    ScaffoldSecundario(
        titulo = "Confirmación y Credencial",
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
                    Text("• Horario: ${state.diaSemana} | ${state.bloqueHorario}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                    Text("• Actividad: ${state.motivoActividad}", style = MaterialTheme.typography.bodySmall)
                }
            }

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

            Button(
                onClick = { viewModel.confirmarYGuardarReserva(onReservaConfirmada) },
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.2.dp, MilitaryGoldGalon),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = "CONFIRMAR Y ENVIAR SOLICITUD A MANDO",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}
