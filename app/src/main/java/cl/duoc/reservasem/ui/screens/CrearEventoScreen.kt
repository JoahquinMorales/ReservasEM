package cl.duoc.reservasem.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.SportsScore
import androidx.compose.material.icons.filled.Stadium
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
import cl.duoc.reservasem.viewmodel.AdminViewModel

@Composable
fun CrearEventoScreen(
    viewModel: AdminViewModel,
    onVolver: () -> Unit
) {
    val state by viewModel.crearEventoState.collectAsState()

    val listaErrores = listOf(
        state.errores.tituloError,
        state.errores.disciplinaError,
        state.errores.recintoError,
        state.errores.fechaError,
        state.errores.organizadorError
    )

    ScaffoldSecundario(
        titulo = "PROGRAMAR COMPETENCIA",
        subtitulo = "Publicación oficial de torneo intercompañías (IE 2.2.1)",
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
                titulo = "Nuevo Evento Institucional",
                subtitulo = "Complete los antecedentes validados para la orden del día"
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
                        valor = state.titulo,
                        onValorChange = viewModel::onTituloEventoChange,
                        etiqueta = "Título del Torneo / Competencia",
                        icono = Icons.Default.EmojiEvents,
                        errorMensaje = state.errores.tituloError,
                        textoAyudaValido = "Título oficial válido"
                    )

                    CampoTextoTactico(
                        valor = state.disciplina,
                        onValorChange = viewModel::onDisciplinaEventoChange,
                        etiqueta = "Disciplina Deportiva",
                        icono = Icons.Default.SportsScore,
                        errorMensaje = state.errores.disciplinaError,
                        textoAyudaValido = "Disciplina verificada"
                    )

                    CampoTextoTactico(
                        valor = state.recinto,
                        onValorChange = viewModel::onRecintoEventoChange,
                        etiqueta = "Recinto Asignado",
                        icono = Icons.Default.Stadium,
                        errorMensaje = state.errores.recintoError
                    )

                    CampoTextoTactico(
                        valor = state.fecha,
                        onValorChange = viewModel::onFechaEventoChange,
                        etiqueta = "Fecha y Hora Programada",
                        icono = Icons.Default.CalendarMonth,
                        errorMensaje = state.errores.fechaError
                    )

                    CampoTextoTactico(
                        valor = state.organizador,
                        onValorChange = viewModel::onOrganizadorEventoChange,
                        etiqueta = "Unidad Organizadora",
                        icono = Icons.Default.MilitaryTech,
                        errorMensaje = state.errores.organizadorError
                    )

                    BannerEstadoValidacion(
                        erroresActivos = listaErrores,
                        mensajeListo = "Antecedentes del evento validados para publicación"
                    )

                    Button(
                        onClick = {
                            viewModel.validarYPublicarEvento(onExito = onVolver)
                        },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.2.dp, MilitaryGoldGalon),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                    ) {
                        Icon(Icons.Default.EmojiEvents, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("PUBLICAR EVENTO OFICIAL", fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }
    }
}
