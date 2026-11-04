package cl.duoc.reservasem.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.model.Horarios
import cl.duoc.reservasem.ui.components.BadgeEstado
import cl.duoc.reservasem.ui.components.ScaffoldSecundario
import cl.duoc.reservasem.ui.theme.MilitaryGoldGalon
import cl.duoc.reservasem.viewmodel.CadeteViewModel

@Composable
fun CalendarioScreen(
    cadeteViewModel: CadeteViewModel,
    onSeleccionarBloque: (String, String) -> Unit,
    onVolver: () -> Unit
) {
    val reservas by cadeteViewModel.todasLasReservas.collectAsState()
    var diaSeleccionado by remember { mutableStateOf("Lunes") }

    ScaffoldSecundario(
        titulo = "Matriz Horaria Táctica",
        subtitulo = "Bloques Oficiales de 90 Minutos (Lun-Vie)",
        onVolver = onVolver
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(14.dp)
        ) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(Horarios.diasHabiles) { dia ->
                    FilterChip(
                        selected = diaSeleccionado == dia,
                        onClick = { diaSeleccionado = dia },
                        label = { Text(dia, fontWeight = FontWeight.Bold) },
                        shape = RoundedCornerShape(6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(Horarios.bloquesOficiales) { bloque ->
                    val estado = obtenerEstadoBloque(reservas, diaSeleccionado, bloque)
                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSeleccionarBloque(diaSeleccionado, bloque) },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, MilitaryGoldGalon.copy(alpha = 0.65f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "$diaSeleccionado • $bloque",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = estado.descripcion,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            BadgeEstado(if (estado.ocupado) "RESERVADO" else "OPERATIVO")
                        }
                    }
                }
            }
        }
    }
}
