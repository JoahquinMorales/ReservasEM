package cl.duoc.reservasem.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.ui.components.ScaffoldSecundario
import cl.duoc.reservasem.ui.theme.MilitaryGoldGalon
import cl.duoc.reservasem.viewmodel.AppViewModel

@Composable
fun ConfiguracionScreen(
    appViewModel: AppViewModel,
    onIrAPoliticas: () -> Unit,
    onIrAAyuda: () -> Unit,
    onVolver: () -> Unit
) {
    val estadoMantencion by appViewModel.modoMantencionActivo.collectAsState()
    val mostrarMensaje by appViewModel.mostrarMensajeMantencion.collectAsState()

    ScaffoldSecundario(
        titulo = "Configuración y Mando Táctico",
        subtitulo = "DataStore Preferences y Animaciones Declarativas",
        onVolver = onVolver
    ) { padding ->
        if (estadoMantencion == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = MilitaryGoldGalon)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Sincronizando estado desde DataStore...",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        } else {
            val estaActivo = estadoMantencion ?: false
            val colorAnimado by animateColorAsState(
                targetValue = if (estaActivo) Color(0xFF8A3324) else Color(0xFF3E5C38),
                animationSpec = tween(durationMillis = 500),
                label = "colorModoMantencion"
            )
            val textoBoton = if (estaActivo) {
                "DESACTIVAR MODO MANTENCIÓN"
            } else {
                "ACTIVAR MODO MANTENCIÓN"
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Panel de Control de Modo Mantención con DataStore + animateColorAsState + AnimatedVisibility
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.2.dp, colorAnimado),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = if (estaActivo) Icons.Default.Build else Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = colorAnimado
                            )
                            Text(
                                text = "MODO MANTENCIÓN GENERAL EM",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = colorAnimado
                            )
                        }

                        Text(
                            text = if (estaActivo) {
                                "Estado Actual: MANTENCIÓN ACTIVA (Persistido en DataStore Preferences)"
                            } else {
                                "Estado Actual: RECINTOS OPERATIVOS (Persistido en DataStore Preferences)"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold
                        )

                        Button(
                            onClick = { appViewModel.alternarModoMantencion() },
                            colors = ButtonDefaults.buttonColors(containerColor = colorAnimado),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, MilitaryGoldGalon),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(
                                text = textoBoton,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }

                        AnimatedVisibility(visible = mostrarMensaje) {
                            Surface(
                                color = colorAnimado.copy(alpha = 0.16f),
                                shape = RoundedCornerShape(6.dp),
                                border = BorderStroke(1.dp, colorAnimado),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "✔ ¡Estado de Modo Mantención guardado en DataStore exitosamente!",
                                    color = colorAnimado,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }

                // Información del Sistema e Institucional
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, MilitaryGoldGalon.copy(alpha = 0.7f)),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "ARQUITECTURA DEL SISTEMA",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MilitaryGoldGalon
                        )
                        Text(
                            text = "• Persistencia Dual: Room SQLite 2.7.1 + Jetpack DataStore Preferences.",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "• Recursos Nativos: Cámara (TakePicture + FileProvider), Galería, Google Calendar.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                OutlinedButton(
                    onClick = onIrAPoliticas,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, MilitaryGoldGalon),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Policy, contentDescription = null, tint = MilitaryGoldGalon, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("REGLAMENTO Y POLÍTICAS DE USO", fontWeight = FontWeight.ExtraBold)
                }

                OutlinedButton(
                    onClick = onIrAAyuda,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, MilitaryGoldGalon),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.HelpOutline, contentDescription = null, tint = MilitaryGoldGalon, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("SOPORTE Y MESA DE AYUDA EM", fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}
