package cl.duoc.reservasem.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.ui.components.BadgeEstado
import cl.duoc.reservasem.ui.components.EncabezadoSeccionTactico
import cl.duoc.reservasem.ui.components.ScaffoldSecundario
import cl.duoc.reservasem.ui.theme.MilitaryGoldGalon
import cl.duoc.reservasem.viewmodel.AdminViewModel

@Composable
fun UsuariosScreen(
    viewModel: AdminViewModel,
    onVerDetalleUsuario: (Int) -> Unit,
    onVolver: () -> Unit
) {
    val usuarios by viewModel.usuarios.collectAsState()
    var usuarioParaEliminar by remember { mutableStateOf<cl.duoc.reservasem.model.UsuarioModel?>(null) }

    ScaffoldSecundario(
        titulo = "DOTACIÓN DE USUARIOS",
        subtitulo = "Personal habilitado en el sistema (${usuarios.size})",
        onVolver = onVolver
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                EncabezadoSeccionTactico(
                    titulo = "Nómina Institucional",
                    subtitulo = "Cadetes, Oficiales y Profesores con acceso activo"
                )
            }

            items(usuarios, key = { it.id }) { usuario ->
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onVerDetalleUsuario(usuario.id) },
                    border = BorderStroke(1.dp, MilitaryGoldGalon.copy(alpha = 0.55f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = MilitaryGoldGalon
                            )
                            Column {
                                Text(
                                    text = usuario.nombre,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${usuario.tipoSolicitante}\n${usuario.correo}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        BadgeEstado(estado = usuario.rol)

                        // BOTÓN DE ELIMINAR (Ahora está antes de la flecha para que se vea bien)
                        IconButton(onClick = { usuarioParaEliminar = usuario }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar usuario",
                                tint = MaterialTheme.colorScheme.error
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
        }
    }

    // VENTANA DE ALERTA DE CONFIRMACIÓN
    usuarioParaEliminar?.let { usuario ->
        AlertDialog(
            onDismissRequest = { usuarioParaEliminar = null },
            title = { Text("Eliminar Usuario") },
            text = { Text("¿Estás seguro de que deseas eliminar a ${usuario.nombre}? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.eliminarUsuario(usuario.id)
                        usuarioParaEliminar = null
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar")
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