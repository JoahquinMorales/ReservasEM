package cl.duoc.reservasem.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.ui.components.BadgeEstado
import cl.duoc.reservasem.ui.components.EncabezadoSeccionTactico
import cl.duoc.reservasem.ui.components.ScaffoldSecundario
import cl.duoc.reservasem.ui.theme.MilitaryGoldGalon
import cl.duoc.reservasem.viewmodel.AdminViewModel

@Composable
fun DetalleUsuarioScreen(
    usuarioId: Int,
    viewModel: AdminViewModel,
    onVolver: () -> Unit
) {
    val usuarios by viewModel.usuarios.collectAsState()
    val reservas by viewModel.reservas.collectAsState()
    val usuario = usuarios.find { it.id == usuarioId } ?: usuarios.firstOrNull()

    ScaffoldSecundario(
        titulo = "HOJA DE VIDA DEPORTIVA",
        subtitulo = "Antecedentes del usuario e historial de reservas",
        onVolver = onVolver
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (usuario != null) {
                val reservasUsuario = reservas.filter {
                    it.correoUsuario.equals(usuario.correo, ignoreCase = true)
                }

                EncabezadoSeccionTactico(
                    titulo = usuario.nombre,
                    subtitulo = "Estamento: ${usuario.tipoSolicitante}"
                )

                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.2.dp, MilitaryGoldGalon)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BadgeEstado(estado = usuario.rol)
                        Text("Correo Institucional: ${usuario.correo}", fontWeight = FontWeight.SemiBold)
                        Text("Estamento / Perfil: ${usuario.tipoSolicitante}")
                        Text("Rol Operativo: ${usuario.rol}")
                        HorizontalDivider(color = MilitaryGoldGalon.copy(alpha = 0.25f))
                        Text(
                            "Solicitudes registradas: ${reservasUsuario.size}",
                            fontWeight = FontWeight.Bold,
                            color = MilitaryGoldGalon
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("VOLVER A DOTACIÓN", fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}
