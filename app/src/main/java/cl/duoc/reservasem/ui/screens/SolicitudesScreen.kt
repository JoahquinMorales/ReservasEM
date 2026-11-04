package cl.duoc.reservasem.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.ui.components.ScaffoldSecundario
import cl.duoc.reservasem.ui.components.agendarEnGoogleCalendar
import cl.duoc.reservasem.viewmodel.AdminViewModel

@Composable
fun SolicitudesScreen(
    viewModel: AdminViewModel,
    onVerDetalle: (Int) -> Unit,
    onVolver: () -> Unit
) {
    val reservas by viewModel.reservas.collectAsState()
    var filtro by remember { mutableStateOf("TODAS") }
    var motivoRechazo by remember { mutableStateOf("") }
    val context = LocalContext.current

    val filtradas = reservas.filter { filtro == "TODAS" || it.estado == filtro }

    ScaffoldSecundario(
        titulo = "Gestión de Solicitudes • EM",
        subtitulo = "${filtradas.size} solicitudes en lista",
        onVolver = onVolver
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("TODAS", "PENDIENTE", "APROBADA", "RECHAZADA").forEach { est ->
                    FilterChip(
                        selected = filtro == est,
                        onClick = { filtro = est },
                        label = { Text(est, style = MaterialTheme.typography.labelSmall) },
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = motivoRechazo,
                onValueChange = { motivoRechazo = it },
                label = { Text("Observación / Motivo de rechazo (opcional)", style = MaterialTheme.typography.bodySmall) },
                singleLine = true,
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(filtradas, key = { it.id }) { reserva ->
                    TarjetaReservaAdmin(
                        reserva = reserva,
                        onAprobar = { viewModel.aprobarReserva(reserva) },
                        onRechazar = {
                            viewModel.rechazarReserva(reserva, motivoRechazo)
                            motivoRechazo = ""
                        },
                        onSincronizarCalendar = {
                            agendarEnGoogleCalendar(context, reserva, esAdmin = true)
                        }
                    )
                }
            }
        }
    }
}
