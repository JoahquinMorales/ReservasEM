package cl.duoc.reservasem.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.ui.components.BannerEstadoValidacion
import cl.duoc.reservasem.ui.components.CampoTextoTactico
import cl.duoc.reservasem.ui.components.EncabezadoSeccionTactico
import cl.duoc.reservasem.ui.components.ScaffoldSecundario
import cl.duoc.reservasem.ui.theme.MilitaryGoldGalon
import cl.duoc.reservasem.viewmodel.CadeteViewModel

@Composable
fun CrearEquipoScreen(
    viewModel: CadeteViewModel,
    onVolver: () -> Unit
) {
    val state by viewModel.crearEquipoState.collectAsState()

    val listaErrores = listOf(
        state.errores.nombreEquipoError,
        state.errores.companiaError,
        state.errores.capitanError,
        state.errores.disciplinaError,
        state.errores.integrantesError
    )

    ScaffoldSecundario(
        titulo = "INSCRIBIR ESCUADRA",
        subtitulo = "Registro de equipo representativo (IE 2.2.1)",
        onVolver = onVolver
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            EncabezadoSeccionTactico(
                titulo = "Ficha de Escuadra Deportiva",
                subtitulo = "Inscripción oficial para torneos intercompañías"
            )

            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.2.dp, MilitaryGoldGalon),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CampoTextoTactico(
                        valor = state.nombreEquipo,
                        onValorChange = viewModel::onNombreEquipoChange,
                        etiqueta = "Nombre de la Escuadra",
                        icono = Icons.Default.Shield,
                        errorMensaje = state.errores.nombreEquipoError,
                        textoAyudaValido = "Nombre de escuadra válido"
                    )

                    CampoTextoTactico(
                        valor = state.compania,
                        onValorChange = viewModel::onCompaniaEquipoChange,
                        etiqueta = "Compañía / Unidad Militar",
                        icono = Icons.Default.MilitaryTech,
                        errorMensaje = state.errores.companiaError
                    )

                    CampoTextoTactico(
                        valor = state.capitan,
                        onValorChange = viewModel::onCapitanEquipoChange,
                        etiqueta = "Grado y Nombre del Capitán Responsable",
                        icono = Icons.Default.Badge,
                        errorMensaje = state.errores.capitanError,
                        textoAyudaValido = "Capitán responsable verificado"
                    )

                    CampoTextoTactico(
                        valor = state.disciplina,
                        onValorChange = viewModel::onDisciplinaEquipoChange,
                        etiqueta = "Disciplina Deportiva",
                        icono = Icons.Default.SportsSoccer,
                        errorMensaje = state.errores.disciplinaError
                    )

                    CampoTextoTactico(
                        valor = state.integrantes,
                        onValorChange = viewModel::onIntegrantesEquipoChange,
                        etiqueta = "Dotación de Integrantes (2 a 50)",
                        icono = Icons.Default.Groups,
                        errorMensaje = state.errores.integrantesError,
                        textoAyudaValido = "Dotación dentro del rango reglamentario"
                    )

                    BannerEstadoValidacion(
                        erroresActivos = listaErrores,
                        mensajeListo = "Datos de escuadra verificados conforme al reglamento"
                    )

                    Button(
                        onClick = {
                            viewModel.validarYRegistrarEquipo(onExito = onVolver)
                        },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.2.dp, MilitaryGoldGalon),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                    ) {
                        Icon(Icons.Default.GroupAdd, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("REGISTRAR ESCUADRA OFICIAL", fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }
    }
}
