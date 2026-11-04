package cl.duoc.reservasem.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.ui.components.EncabezadoSeccionTactico
import cl.duoc.reservasem.ui.components.KpiCardTactica
import cl.duoc.reservasem.ui.components.ScaffoldSecundario
import cl.duoc.reservasem.ui.theme.MilitaryGoldGalon
import cl.duoc.reservasem.viewmodel.AdminViewModel

@Composable
fun ReportesScreen(
    viewModel: AdminViewModel,
    onVolver: () -> Unit
) {
    val resumen by viewModel.resumenOperativo.collectAsState()
    val estadisticas by viewModel.estadisticasPorCategoria.collectAsState()

    ScaffoldSecundario(
        titulo = "REPORTES E INDICADORES",
        subtitulo = "Métricas operativas de infraestructura deportiva",
        onVolver = onVolver
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                EncabezadoSeccionTactico(
                    titulo = "Balance General de Operaciones",
                    subtitulo = "Estado consolidado en tiempo real"
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    KpiCardTactica(
                        titulo = "Operativas",
                        valor = "${resumen.canchasOperativas}/${resumen.totalCanchas}",
                        icono = Icons.Default.BarChart,
                        modifier = Modifier.weight(1f)
                    )
                    KpiCardTactica(
                        titulo = "Aprobadas",
                        valor = resumen.reservasAprobadas.toString(),
                        icono = Icons.Default.CheckCircle,
                        modifier = Modifier.weight(1f)
                    )
                    KpiCardTactica(
                        titulo = "Pendientes",
                        valor = resumen.reservasPendientes.toString(),
                        icono = Icons.Default.PendingActions,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                EncabezadoSeccionTactico(
                    titulo = "Demanda por Categoría Deportiva",
                    subtitulo = "Distribución de reservas según especialidad"
                )
            }

            items(estadisticas) { item ->
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, MilitaryGoldGalon.copy(alpha = 0.6f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.categoria,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${item.cantidadSolicitudes} solicitudes",
                            style = MaterialTheme.typography.labelLarge,
                            color = MilitaryGoldGalon,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}
