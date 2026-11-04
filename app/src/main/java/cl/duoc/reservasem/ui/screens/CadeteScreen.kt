package cl.duoc.reservasem.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.model.CanchaModel
import cl.duoc.reservasem.model.Horarios
import cl.duoc.reservasem.navigation.Rutas
import cl.duoc.reservasem.ui.components.AppScaffold
import cl.duoc.reservasem.ui.components.EncabezadoSeccionTactico
import cl.duoc.reservasem.ui.components.KpiCardTactica
import cl.duoc.reservasem.ui.components.TarjetaCancha
import cl.duoc.reservasem.ui.theme.BadgeAprobadaCamuflaje
import cl.duoc.reservasem.ui.theme.BadgePendienteAmbar
import cl.duoc.reservasem.viewmodel.AppViewModel
import cl.duoc.reservasem.viewmodel.CadeteViewModel

@Composable
fun CadeteScreen(
    cadeteViewModel: CadeteViewModel,
    appViewModel: AppViewModel,
    onNavegar: (String) -> Unit,
    onSeleccionarCancha: (CanchaModel) -> Unit,
    onReservarDirecto: (CanchaModel) -> Unit,
    onCerrarSesion: () -> Unit
) {
    val sesion by appViewModel.sesionState.collectAsState()
    val canchas by cadeteViewModel.canchas.collectAsState()
    val reservas by cadeteViewModel.todasLasReservas.collectAsState()
    val busqueda by cadeteViewModel.busquedaTexto.collectAsState()
    val categoriaFiltro by cadeteViewModel.categoriaSeleccionada.collectAsState()

    val canchasFiltradas = canchas.filter { cancha ->
        val coincideTexto = cancha.nombre.contains(busqueda, ignoreCase = true) ||
            cancha.categoria.contains(busqueda, ignoreCase = true)
        val coincideCategoria = categoriaFiltro == "Todas" || cancha.categoria == categoriaFiltro
        coincideTexto && coincideCategoria
    }

    val operativasCount = canchas.count { !it.enMantencion }
    val misReservasCount = reservas.count {
        sesion.correo.isBlank() || it.correoUsuario.equals(sesion.correo, ignoreCase = true)
    }

    AppScaffold(
        titulo = "Recintos Escuela Militar",
        subtitulo = sesion.nombre.ifBlank { sesion.correo.ifBlank { "Solicitante Institucional" } },
        rutaActual = Rutas.HOME_CADETE,
        esAdmin = false,
        onNavegar = onNavegar,
        onCerrarSesion = onCerrarSesion
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                KpiCardTactica(
                    titulo = "Recintos Operativos",
                    valor = "$operativasCount / ${canchas.size}",
                    icono = Icons.Default.CheckCircle,
                    colorAcento = BadgeAprobadaCamuflaje,
                    modifier = Modifier.weight(1f)
                )
                KpiCardTactica(
                    titulo = "Mis Solicitudes",
                    valor = "$misReservasCount",
                    icono = Icons.Default.EventAvailable,
                    colorAcento = BadgePendienteAmbar,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = busqueda,
                onValueChange = cadeteViewModel::actualizarBusqueda,
                placeholder = { Text("Buscar recinto, cancha o gimnasio...", style = MaterialTheme.typography.bodySmall) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
                trailingIcon = {
                    if (busqueda.isNotBlank()) {
                        IconButton(onClick = { cadeteViewModel.actualizarBusqueda("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpiar", modifier = Modifier.size(16.dp))
                        }
                    }
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium,
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(Horarios.categoriasRecintos) { cat ->
                    FilterChip(
                        selected = categoriaFiltro == cat,
                        onClick = { cadeteViewModel.seleccionarCategoria(cat) },
                        label = { Text(cat, style = MaterialTheme.typography.labelMedium) },
                        shape = RoundedCornerShape(6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            EncabezadoSeccionTactico(
                titulo = "Catálogo de Instalaciones (${canchasFiltradas.size})",
                accionTexto = "Ver Horario",
                onAccionClick = { onNavegar(Rutas.CALENDARIO) }
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(canchasFiltradas, key = { it.id }) { cancha ->
                    TarjetaCancha(
                        cancha = cancha,
                        onReservarClick = onReservarDirecto,
                        onVerFichaClick = onSeleccionarCancha
                    )
                }
            }
        }
    }
}
