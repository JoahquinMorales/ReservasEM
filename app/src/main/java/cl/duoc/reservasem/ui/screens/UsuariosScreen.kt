package cl.duoc.reservasem.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.model.UsuarioModel
import cl.duoc.reservasem.ui.components.BadgeEstado
import cl.duoc.reservasem.ui.components.EncabezadoSeccionTactico
import cl.duoc.reservasem.ui.components.ScaffoldSecundario
import cl.duoc.reservasem.ui.theme.BadgeAprobadaCamuflaje
import cl.duoc.reservasem.ui.theme.BadgePendienteAmbar
import cl.duoc.reservasem.ui.theme.BadgeRechazadaOxido
import cl.duoc.reservasem.ui.theme.MilitaryGoldGalon
import cl.duoc.reservasem.ui.theme.VibrantMilitaryGreen
import cl.duoc.reservasem.viewmodel.AdminViewModel

@Composable
fun UsuariosScreen(
    viewModel: AdminViewModel,
    onVerDetalleUsuario: (Int) -> Unit,
    onVolver: () -> Unit
) {
    val usuarios by viewModel.usuarios.collectAsState()
    var filtroEstado by remember { mutableStateOf("TODOS") }
    var usuarioParaEliminar by remember { mutableStateOf<UsuarioModel?>(null) }

    val pendientesCount = usuarios.count { it.estadoCuenta.equals("PENDIENTE", ignoreCase = true) }

    val usuariosFiltrados = usuarios.filter { u ->
        when (filtroEstado) {
            "PENDIENTES" -> u.estadoCuenta.equals("PENDIENTE", ignoreCase = true)
            "CONFIRMADOS" -> u.estadoCuenta.equals("APROBADO", ignoreCase = true)
            else -> true
        }
    }

    ScaffoldSecundario(
        titulo = "DOTACIÓN Y ALTA DE USUARIOS",
        subtitulo = "${usuarios.size} registrados • $pendientesCount pendientes de confirmar",
        onVolver = onVolver
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            // Filtros rápidos para confirmar nuevos usuarios
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = filtroEstado == "TODOS",
                    onClick = { filtroEstado = "TODOS" },
                    label = {
                        Text(
                            text = "TODOS (${usuarios.size})",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold
                        )
                    },
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = filtroEstado == "PENDIENTES",
                    onClick = { filtroEstado = "PENDIENTES" },
                    label = {
                        Text(
                            text = "POR CONFIRMAR ($pendientesCount)",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold
                        )
                    },
                    leadingIcon = if (pendientesCount > 0) {
                        {
                            Icon(
                                imageVector = Icons.Default.HowToReg,
                                contentDescription = null,
                                tint = BadgePendienteAmbar,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    } else null,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.weight(1.2f)
                )
                FilterChip(
                    selected = filtroEstado == "CONFIRMADOS",
                    onClick = { filtroEstado = "CONFIRMADOS" },
                    label = {
                        Text(
                            text = "ACTIVOS",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold
                        )
                    },
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.weight(0.9f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            var filtroSector by remember { mutableStateOf("TODOS_SECTORES") }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf(
                    "TODOS_SECTORES" to "Todos",
                    "MILITAR_EM" to "Militar",
                    "COLEGIO_EDUCACION" to "Colegios",
                    "BENEFICENCIA_FUNDACION" to "Beneficencia",
                    "FEDERACION_EXTERNA" to "Federación"
                ).forEach { (idSec, labelSec) ->
                    FilterChip(
                        selected = filtroSector == idSec,
                        onClick = { filtroSector = idSec },
                        label = {
                            Text(
                                text = labelSec,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        },
                        shape = RoundedCornerShape(6.dp)
                    )
                }
            }

            val usuariosPorSector = usuariosFiltrados.filter { u ->
                filtroSector == "TODOS_SECTORES" ||
                    cl.duoc.reservasem.model.Horarios.detectarGrupoDeTipo(u.tipoSolicitante).idGrupo == filtroSector
            }

            Spacer(modifier = Modifier.height(6.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item {
                    EncabezadoSeccionTactico(
                        titulo = "Nómina por Tipo de Institución (${usuariosPorSector.size})",
                        subtitulo = "Militar, Colegios/Universidades, Beneficencia y Federaciones"
                    )
                }

                items(usuariosPorSector, key = { it.id }) { usuario ->
                    val esPendiente = usuario.estadoCuenta.equals("PENDIENTE", ignoreCase = true)
                    val esRechazado = usuario.estadoCuenta.equals("RECHAZADO", ignoreCase = true)

                    val borderColor by animateColorAsState(
                        targetValue = when {
                            esPendiente -> BadgePendienteAmbar
                            esRechazado -> BadgeRechazadaOxido
                            else -> MilitaryGoldGalon
                        },
                        animationSpec = tween(300),
                        label = "userBorder"
                    )

                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize()
                            .clickable { onVerDetalleUsuario(usuario.id) },
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.2.dp, borderColor.copy(alpha = 0.85f)),
                        colors = CardDefaults.outlinedCardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            borderColor.copy(alpha = 0.10f),
                                            Color.Transparent
                                        )
                                    )
                                )
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = if (esPendiente) Icons.Default.HowToReg else Icons.Default.Shield,
                                        contentDescription = null,
                                        tint = borderColor
                                    )
                                    Column {
                                        Text(
                                            text = usuario.nombre,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                        Text(
                                            text = "${usuario.tipoSolicitante} • Rol: ${usuario.rol}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MilitaryGoldGalon,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = usuario.correo,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Column(
                                    horizontalAlignment = Alignment.End,
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    BadgeEstado(
                                        estado = when {
                                            esPendiente -> "POR CONFIRMAR"
                                            esRechazado -> "DENEGADO"
                                            else -> "CONFIRMADO"
                                        }
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(
                                            onClick = { usuarioParaEliminar = usuario },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Eliminar usuario",
                                                tint = MaterialTheme.colorScheme.error,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                        Icon(
                                            imageVector = Icons.Default.ChevronRight,
                                            contentDescription = "Ver ficha",
                                            tint = MilitaryGoldGalon
                                        )
                                    }
                                }
                            }

                            // Botonera de Confirmación de Nuevo Usuario
                            if (esPendiente || esRechazado) {
                                HorizontalDivider(color = MilitaryGoldGalon.copy(alpha = 0.3f))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = { viewModel.confirmarUsuario(usuario) },
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = BadgeAprobadaCamuflaje),
                                        contentPadding = PaddingValues(vertical = 6.dp),
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(36.dp)
                                    ) {
                                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "CONFIRMAR NUEVO USUARIO",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                    if (esPendiente) {
                                        OutlinedButton(
                                            onClick = { viewModel.rechazarUsuario(usuario) },
                                            shape = RoundedCornerShape(6.dp),
                                            border = BorderStroke(1.dp, BadgeRechazadaOxido),
                                            contentPadding = PaddingValues(vertical = 6.dp),
                                            modifier = Modifier.height(36.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Close,
                                                contentDescription = null,
                                                tint = BadgeRechazadaOxido,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "DENEGAR",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = BadgeRechazadaOxido
                                            )
                                        }
                                    }
                                }
                            } else {
                                Text(
                                    text = "✔ Cuenta institucional confirmada y habilitada para operar",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = VibrantMilitaryGreen,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    usuarioParaEliminar?.let { usuario ->
        AlertDialog(
            onDismissRequest = { usuarioParaEliminar = null },
            title = { Text("Eliminar Usuario", fontWeight = FontWeight.ExtraBold) },
            text = { Text("¿Estás seguro de que deseas eliminar a ${usuario.nombre}? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.eliminarUsuario(usuario.id)
                        usuarioParaEliminar = null
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar", fontWeight = FontWeight.ExtraBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { usuarioParaEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
