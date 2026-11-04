package cl.duoc.reservasem.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.ui.components.ScaffoldSecundario
import cl.duoc.reservasem.ui.theme.MilitaryGoldGalon
import cl.duoc.reservasem.viewmodel.CadeteViewModel

@Composable
fun EventosScreen(
    cadeteViewModel: CadeteViewModel,
    esAdmin: Boolean,
    onCrearEvento: () -> Unit,
    onCrearEquipo: () -> Unit,
    onVerDetalleEvento: (Int) -> Unit,
    onVolver: () -> Unit
) {
    val eventos by cadeteViewModel.eventos.collectAsState()

    ScaffoldSecundario(
        titulo = "Torneos y Competencias EM",
        subtitulo = "${eventos.size} eventos institucionales activos",
        onVolver = onVolver
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (esAdmin) {
                    Button(
                        onClick = onCrearEvento,
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("CREAR TORNEO", fontWeight = FontWeight.ExtraBold)
                    }
                }
                OutlinedButton(
                    onClick = onCrearEquipo,
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, MilitaryGoldGalon),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("INSCRIBIR ESCUADRA", fontWeight = FontWeight.ExtraBold)
                }
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(eventos, key = { it.id }) { evento ->
                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onVerDetalleEvento(evento.id) },
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, MilitaryGoldGalon.copy(alpha = 0.7f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = evento.titulo,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text("Disciplina: ${evento.disciplina}", style = MaterialTheme.typography.bodySmall)
                            Text("Recinto: ${evento.recinto}", style = MaterialTheme.typography.bodySmall)
                            Text("Fecha: ${evento.fecha}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
