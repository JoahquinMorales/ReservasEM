package cl.duoc.reservasem.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.model.CanchaModel
import cl.duoc.reservasem.ui.components.ScaffoldSecundario
import cl.duoc.reservasem.ui.components.TarjetaCancha
import cl.duoc.reservasem.viewmodel.CadeteViewModel

@Composable
fun ResultadosScreen(
    viewModel: CadeteViewModel,
    onSeleccionarCancha: (CanchaModel) -> Unit,
    onVolver: () -> Unit
) {
    val canchas by viewModel.canchas.collectAsState()
    val operativas = canchas.filter { !it.enMantencion }

    ScaffoldSecundario(
        titulo = "Resultados de Disponibilidad",
        subtitulo = "${operativas.size} recintos disponibles",
        onVolver = onVolver
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 14.dp, vertical = 8.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(operativas, key = { it.id }) { cancha ->
                TarjetaCancha(
                    cancha = cancha,
                    onReservarClick = onSeleccionarCancha
                )
            }
        }
    }
}
