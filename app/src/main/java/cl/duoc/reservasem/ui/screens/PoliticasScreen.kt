package cl.duoc.reservasem.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.ui.components.EncabezadoSeccionTactico
import cl.duoc.reservasem.ui.components.ScaffoldSecundario
import cl.duoc.reservasem.ui.theme.MilitaryGoldGalon

@Composable
fun PoliticasScreen(
    onVolver: () -> Unit
) {
    ScaffoldSecundario(
        titulo = "REGLAMENTO DE RECINTOS",
        subtitulo = "Normativa interna de uso de instalaciones deportivas",
        onVolver = onVolver
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            EncabezadoSeccionTactico(
                titulo = "Disposiciones Reglamentarias",
                subtitulo = "Escuela Militar del Libertador Bernardo O'Higgins"
            )

            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, MilitaryGoldGalon.copy(alpha = 0.65f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("1. Puntualidad y Bloques Horarios", fontWeight = FontWeight.Bold, color = MilitaryGoldGalon)
                    Text("El ingreso al recinto deportivo debe efectuarse estrictamente dentro del bloque autorizado (90 minutos por turno).")

                    Text("2. Tenida Reglamentaria", fontWeight = FontWeight.Bold, color = MilitaryGoldGalon)
                    Text("Es obligatorio el uso de tenida deportiva institucional acorde a la disciplina (fútbol, tenis, natación, equitación o tiro).")

                    Text("3. Prioridad Institucional", fontWeight = FontWeight.Bold, color = MilitaryGoldGalon)
                    Text("Las actividades de instrucción militar y seleccionados deportivos oficiales tienen prioridad sobre reservas recreativas.")

                    Text("4. Cuidado del Material y Recintos", fontWeight = FontWeight.Bold, color = MilitaryGoldGalon)
                    Text("Cualquier desperfecto o novedad debe ser reportado inmediatamente a la Jefatura de Deportes.")
                }
            }

            Button(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("ENTENDIDO • VOLVER", fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}
