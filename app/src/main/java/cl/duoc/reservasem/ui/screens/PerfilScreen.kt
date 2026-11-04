package cl.duoc.reservasem.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.ui.components.ImagenInteligente
import cl.duoc.reservasem.ui.components.ScaffoldSecundario
import cl.duoc.reservasem.ui.theme.MilitaryGoldGalon
import cl.duoc.reservasem.viewmodel.AppViewModel
import cl.duoc.reservasem.viewmodel.CadeteViewModel

@Composable
fun PerfilScreen(
    appViewModel: AppViewModel,
    cadeteViewModel: CadeteViewModel,
    onIrAPreferencias: () -> Unit,
    onIrAConfiguracion: () -> Unit = {},
    onCerrarSesion: () -> Unit,
    onVolver: () -> Unit
) {
    val sesion by appViewModel.sesionState.collectAsState()
    val fotoUri by appViewModel.fotoPerfilUri.collectAsState()
    val reservas by cadeteViewModel.todasLasReservas.collectAsState()

    val misReservasCount = reservas.count {
        sesion.correo.isBlank() || it.correoUsuario.equals(sesion.correo, ignoreCase = true)
    }

    ScaffoldSecundario(
        titulo = "Perfil Institucional y Credencial",
        subtitulo = "Validación Biométrica / Fotográfica EM",
        onVolver = onVolver
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. Componente Circular de Cámara Nativa y Galería (Rúbrica: Recurso Nativo)
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.2.dp, MilitaryGoldGalon),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "FOTOGRAFÍA TÁCTICA DE IDENTIDAD",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MilitaryGoldGalon
                    )
                    Text(
                        text = "Capture su credencial militar con la cámara o seleccione desde galería",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    ImagenInteligente(
                        fotoUri = fotoUri,
                        onFotoCambiada = appViewModel::onFotoPerfilChange
                    )

                    if (fotoUri != null) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(0.8.dp, MilitaryGoldGalon)
                        ) {
                            Text(
                                text = "✔ Fotografía táctica actualizada y persistida en DataStore",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = MilitaryGoldGalon,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // 2. Datos de la Cuenta Institucional Activa
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Badge,
                            contentDescription = null,
                            tint = MilitaryGoldGalon
                        )
                        Text(
                            text = sesion.nombre.ifBlank { "Usuario Escuela Militar" },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    HorizontalDivider(color = MilitaryGoldGalon.copy(alpha = 0.3f))
                    Text(
                        text = "Correo Institucional: ${sesion.correo.ifBlank { "cadete@escuelamilitar.cl" }}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Rol en Sistema: ${sesion.rol} • Estamento: ${sesion.tipoSolicitante}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Solicitudes de Reserva Registradas: $misReservasCount",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MilitaryGoldGalon
                    )
                }
            }

            // 3. Accesos a Preferencias y Configuración de Modo Mantención (DataStore + Animaciones)
            OutlinedButton(
                onClick = onIrAConfiguracion,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.2.dp, MilitaryGoldGalon),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = MilitaryGoldGalon,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "PANEL DE MANDO Y MODO MANTENCIÓN (DATASTORE)",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            OutlinedButton(
                onClick = onIrAPreferencias,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, MilitaryGoldGalon.copy(alpha = 0.7f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = null,
                    tint = MilitaryGoldGalon,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "PREFERENCIAS VISUALES (MODO OSCURO)",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Button(
                onClick = onCerrarSesion,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "CERRAR SESIÓN INSTITUCIONAL",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}
