package cl.duoc.reservasem.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.model.Horarios
import cl.duoc.reservasem.ui.components.BannerEstadoValidacion
import cl.duoc.reservasem.ui.components.CampoTextoTactico
import cl.duoc.reservasem.ui.components.ScaffoldSecundario
import cl.duoc.reservasem.ui.components.SelectorTactico
import cl.duoc.reservasem.ui.theme.MilitaryGoldGalon
import cl.duoc.reservasem.viewmodel.CadeteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevaReservaScreen(
    viewModel: CadeteViewModel,
    onIrAConfirmar: () -> Unit,
    onVolver: () -> Unit
) {
    val state by viewModel.nuevaReservaState.collectAsState()
    val canchas by viewModel.canchas.collectAsState()

    var menuCanchasExpandido by remember { mutableStateOf(false) }

    val canchasOperativas = canchas.filter { !it.enMantencion }
    val listaErrores = listOf(
        state.errores.nombreSolicitanteError,
        state.errores.correoSolicitanteError,
        state.errores.motivoError,
        state.errores.conflictoHorarioError
    )

    ScaffoldSecundario(
        titulo = "Formulario de Solicitud de Reserva",
        subtitulo = state.nombreCanchaSeleccionada,
        onVolver = onVolver
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
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
                    Text(
                        text = "1. RECINTO Y PROGRAMACIÓN HORARIA",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MilitaryGoldGalon
                    )

                    ExposedDropdownMenuBox(
                        expanded = menuCanchasExpandido,
                        onExpandedChange = { menuCanchasExpandido = !menuCanchasExpandido }
                    ) {
                        OutlinedTextField(
                            value = state.nombreCanchaSeleccionada,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Instalación Deportiva", style = MaterialTheme.typography.bodySmall) },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuCanchasExpandido)
                            },
                            textStyle = MaterialTheme.typography.bodyMedium,
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MilitaryGoldGalon,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.55f)
                            ),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = menuCanchasExpandido,
                            onDismissRequest = { menuCanchasExpandido = false },
                            modifier = Modifier
                                .exposedDropdownSize()
                                .background(MaterialTheme.colorScheme.surface)
                        ) {
                            canchasOperativas.forEach { cancha ->
                                DropdownMenuItem(
                                    text = { Text(cancha.nombre, style = MaterialTheme.typography.bodySmall) },
                                    onClick = {
                                        viewModel.preseleccionarCancha(cancha)
                                        menuCanchasExpandido = false
                                    }
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SelectorTactico(
                            opciones = Horarios.diasHabiles,
                            seleccionActual = state.diaSemana,
                            onSeleccionChange = viewModel::onDiaSemanaChange,
                            etiqueta = "Día (Lun-Vie)",
                            modifier = Modifier.weight(1f)
                        )

                        SelectorTactico(
                            opciones = Horarios.bloquesOficiales,
                            seleccionActual = state.bloqueHorario,
                            onSeleccionChange = viewModel::onBloqueHorarioChange,
                            etiqueta = "Bloque (90m)",
                            modifier = Modifier.weight(1.2f)
                        )
                    }
                }
            }

            // 2. Formulario Validado Reactivamente de forma centralizada (IE 2.2.1)
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
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
                    Text(
                        text = "2. IDENTIFICACIÓN, INSTITUCIÓN Y VALIDACIÓN REACTIVA (IE 2.2.1)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MilitaryGoldGalon
                    )

                    val grupoSeleccionadoReserva = remember(state.tipoSolicitante) {
                        Horarios.detectarGrupoDeTipo(state.tipoSolicitante)
                    }

                    Text(
                        text = "Tipo de Institución / Sector Solicitante:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MilitaryGoldGalon
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Horarios.gruposInstitucionSolicitante.take(2).forEach { grupo ->
                            val activo = grupoSeleccionadoReserva.idGrupo == grupo.idGrupo
                            FilterChip(
                                selected = activo,
                                onClick = { viewModel.onTipoSolicitanteChange(grupo.tipos.first()) },
                                label = {
                                    Text(
                                        text = if (grupo.idGrupo == "MILITAR_EM") "Escuela Militar" else "Colegio / Univ.",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        maxLines = 1
                                    )
                                },
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Horarios.gruposInstitucionSolicitante.drop(2).forEach { grupo ->
                            val activo = grupoSeleccionadoReserva.idGrupo == grupo.idGrupo
                            FilterChip(
                                selected = activo,
                                onClick = { viewModel.onTipoSolicitanteChange(grupo.tipos.first()) },
                                label = {
                                    Text(
                                        text = if (grupo.idGrupo == "BENEFICENCIA_FUNDACION") "Beneficencia" else "Federación / Club",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        maxLines = 1
                                    )
                                },
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    SelectorTactico(
                        opciones = grupoSeleccionadoReserva.tipos,
                        seleccionActual = state.tipoSolicitante,
                        onSeleccionChange = viewModel::onTipoSolicitanteChange,
                        etiqueta = "Subtipo en ${grupoSeleccionadoReserva.tituloGrupo}"
                    )

                    CampoTextoTactico(
                        valor = state.nombreSolicitante,
                        onValorChange = viewModel::onNombreSolicitanteChange,
                        etiqueta = "Grado y Nombre del Solicitante",
                        icono = Icons.Default.Badge,
                        errorMensaje = state.errores.nombreSolicitanteError,
                        textoAyudaValido = "Identificación del solicitante verificada"
                    )

                    CampoTextoTactico(
                        valor = state.correoSolicitante,
                        onValorChange = viewModel::onCorreoSolicitanteChange,
                        etiqueta = "Correo Electrónico Institucional",
                        icono = Icons.Default.Email,
                        errorMensaje = state.errores.correoSolicitanteError,
                        textoAyudaValido = "Correo institucional válido"
                    )

                    CampoTextoTactico(
                        valor = state.motivoActividad,
                        onValorChange = viewModel::onMotivoChange,
                        etiqueta = "Motivo o Actividad Deportiva (ej. Entrenamiento Rama)",
                        icono = Icons.Default.Description,
                        errorMensaje = state.errores.motivoError,
                        textoAyudaValido = "Justificación deportiva registrada",
                        singleLine = false
                    )

                    BannerEstadoValidacion(
                        erroresActivos = listaErrores,
                        mensajeListo = "Campos validados para emisión de solicitud y credencial temporal"
                    )

                    AnimatedVisibility(visible = state.errores.conflictoHorarioError != null) {
                        Surface(
                            color = MaterialTheme.colorScheme.errorContainer,
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = state.errores.conflictoHorarioError ?: "",
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }

                    Button(
                        onClick = {
                            if (viewModel.validarFormularioReserva()) {
                                onIrAConfirmar()
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.2.dp, MilitaryGoldGalon),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                    ) {
                        Text(
                            text = "CONTINUAR A VALIDACIÓN DE CREDENCIAL",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}
