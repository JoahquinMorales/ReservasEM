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
fun AyudaScreen(
    onVolver: () -> Unit
) {
    ScaffoldSecundario(
        titulo = "MESA DE AYUDA TÁCTICA",
        subtitulo = "Soporte operativo del Sistema de Reservas EM",
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
                titulo = "Preguntas Frecuentes",
                subtitulo = "Guía rápida para Cadetes, Oficiales y Profesores"
            )

            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, MilitaryGoldGalon.copy(alpha = 0.65f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("¿Cómo solicito una instalación deportiva?", fontWeight = FontWeight.Bold, color = MilitaryGoldGalon)
                    Text("Seleccione el recinto en el panel principal, complete el formulario validado en tiempo real y confirme la solicitud.")

                    Text("¿Cómo actualizo mi fotografía de credencial táctica?", fontWeight = FontWeight.Bold, color = MilitaryGoldGalon)
                    Text("Diríjase a la pantalla Mi Perfil y utilice los botones 'Tomar Foto' (Cámara nativa con FileProvider) o 'Galería'.")

                    Text("¿Cómo funciona el Modo Mantención?", fontWeight = FontWeight.Bold, color = MilitaryGoldGalon)
                    Text("En la pantalla Configuración puede activar o desactivar el Modo Mantención general, cuyo estado queda persistido en DataStore Preferences con animaciones declarativas.")
                }
            }

            Button(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("VOLVER A CONFIGURACIÓN", fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}
