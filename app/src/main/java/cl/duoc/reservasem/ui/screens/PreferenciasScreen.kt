package cl.duoc.reservasem.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.ui.components.EncabezadoSeccionTactico
import cl.duoc.reservasem.ui.components.ScaffoldSecundario
import cl.duoc.reservasem.ui.theme.MilitaryGoldGalon
import cl.duoc.reservasem.viewmodel.AppViewModel

@Composable
fun PreferenciasScreen(
    appViewModel: AppViewModel,
    onIrAConfiguracion: () -> Unit,
    onVolver: () -> Unit
) {
    val sesion by appViewModel.sesionState.collectAsState()

    ScaffoldSecundario(
        titulo = "PREFERENCIAS VISUALES",
        subtitulo = "Ajustes de tema táctico y alertas locales",
        onVolver = onVolver
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            EncabezadoSeccionTactico(
                titulo = "Personalización de Interfaz",
                subtitulo = "Sincronizado con DataStore Preferences"
            )

            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, MilitaryGoldGalon.copy(alpha = 0.6f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Modo Oscuro Táctico", fontWeight = FontWeight.Bold)
                            Text(
                                "Paleta nocturna de bajo contraste lumínico",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = sesion.modoOscuro,
                            onCheckedChange = appViewModel::cambiarModoOscuro
                        )
                    }

                    HorizontalDivider(color = MilitaryGoldGalon.copy(alpha = 0.25f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Notificaciones de Reserva", fontWeight = FontWeight.Bold)
                            Text(
                                "Avisos inmediatos de aprobación o rechazo",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = sesion.notificacionesActivas,
                            onCheckedChange = appViewModel::cambiarNotificaciones
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onIrAConfiguracion,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Build, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("IR A CONFIGURACIÓN Y MODO MANTENCIÓN", fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}
