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
import cl.duoc.reservasem.ui.components.ScaffoldSecundario
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
    var menuPerfilesExpandido by remember { mutableStateOf(false) }
    var menuDiasExpandido by remember { mutableStateOf(false) }
    var menuBloquesExpandido by remember { mutableStateOf(false) }

    val canchasOperativas = canchas.filter { !it.enMantencion }

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

                    // Selector de Recinto Operativo
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

                    // Día Hábil y Bloque Horario (90 min)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = menuDiasExpandido,
                            onExpandedChange = { menuDiasExpandido = !menuDiasExpandido },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = state.diaSemana,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Día (Lun-Vie)", style = MaterialTheme.typography.bodySmall) },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuDiasExpandido)
                                },
                                textStyle = MaterialTheme.typography.bodyMedium,
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true,
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = menuDiasExpandido,
                                onDismissRequest = { menuDiasExpandido = false },
                                modifier = Modifier
                                    .exposedDropdownSize()
                                    .background(MaterialTheme.colorScheme.surface)
                            ) {
                                Horarios.diasHabiles.forEach { dia ->
                                    DropdownMenuItem(
                                        text = { Text(dia, style = MaterialTheme.typography.bodySmall) },
                                        onClick = {
                                            viewModel.onDiaSemanaChange(dia)
                                            menuDiasExpandido = false
                                        }
                                    )
                                }
                            }
                        }

                        ExposedDropdownMenuBox(
                            expanded = menuBloquesExpandido,
                            onExpandedChange = { menuBloquesExpandido = !menuBloquesExpandido },
                            modifier = Modifier.weight(1.2f)
                        ) {
                            OutlinedTextField(
                                value = state.bloqueHorario,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Bloque (90m)", style = MaterialTheme.typography.bodySmall) },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuBloquesExpandido)
                                },
                                textStyle = MaterialTheme.typography.bodyMedium,
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true,
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = menuBloquesExpandido,
                                onDismissRequest = { menuBloquesExpandido = false },
                                modifier = Modifier
                                    .exposedDropdownSize()
                                    .background(MaterialTheme.colorScheme.surface)
                            ) {
                                Horarios.bloquesOficiales.forEach { bloque ->
                                    DropdownMenuItem(
                                        text = { Text(bloque, style = MaterialTheme.typography.bodySmall) },
                                        onClick = {
                                            viewModel.onBloqueHorarioChange(bloque)
                                            menuBloquesExpandido = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 2. Formulario Validado Reactivamente con isError y supportingText (Rúbrica Duoc UC)
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
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "2. IDENTIFICACIÓN Y VALIDACIÓN REACTIVA",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MilitaryGoldGalon
                    )

                    // Selector compacto de los 9 perfiles oficiales
                    ExposedDropdownMenuBox(
                        expanded = menuPerfilesExpandido,
                        onExpandedChange = { menuPerfilesExpandido = !menuPerfilesExpandido }
                    ) {
                        OutlinedTextField(
                            value = state.tipoSolicitante,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Estamento / Tipo de Solicitante", style = MaterialTheme.typography.bodySmall) },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuPerfilesExpandido)
                            },
                            textStyle = MaterialTheme.typography.bodyMedium,
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true,
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = menuPerfilesExpandido,
                            onDismissRequest = { menuPerfilesExpandido = false },
                            modifier = Modifier
                                .exposedDropdownSize()
                                .background(MaterialTheme.colorScheme.surface)
                        ) {
                            Horarios.tiposSolicitanteOficiales.forEach { perfil ->
                                DropdownMenuItem(
                                    text = { Text(perfil, style = MaterialTheme.typography.bodySmall) },
                                    onClick = {
                                        viewModel.onTipoSolicitanteChange(perfil)
                                        menuPerfilesExpandido = false
                                    }
                                )
                            }
                        }
                    }

                    // Campo 1 Validado en Tiempo Real con isError + supportingText
                    OutlinedTextField(
                        value = state.nombreSolicitante,
                        onValueChange = viewModel::onNombreSolicitanteChange,
                        label = { Text("Grado y Nombre del Solicitante") },
                        leadingIcon = {
                            Icon(Icons.Default.Badge, contentDescription = null, tint = MilitaryGoldGalon)
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        isError = state.errores.nombreSolicitanteError != null,
                        supportingText = {
                            state.errores.nombreSolicitanteError?.let { msg ->
                                Text(
                                    text = msg,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Campo 2 Validado en Tiempo Real con isError + supportingText
                    OutlinedTextField(
                        value = state.correoSolicitante,
                        onValueChange = viewModel::onCorreoSolicitanteChange,
                        label = { Text("Correo Electrónico Institucional") },
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null, tint = MilitaryGoldGalon)
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        isError = state.errores.correoSolicitanteError != null,
                        supportingText = {
                            state.errores.correoSolicitanteError?.let { msg ->
                                Text(
                                    text = msg,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Campo 3 Validado en Tiempo Real con isError + supportingText
                    OutlinedTextField(
                        value = state.motivoActividad,
                        onValueChange = viewModel::onMotivoChange,
                        label = { Text("Motivo o Actividad Deportiva (ej. Entrenamiento Rama)") },
                        leadingIcon = {
                            Icon(Icons.Default.Description, contentDescription = null, tint = MilitaryGoldGalon)
                        },
                        singleLine = false,
                        shape = RoundedCornerShape(8.dp),
                        isError = state.errores.motivoError != null,
                        supportingText = {
                            state.errores.motivoError?.let { msg ->
                                Text(
                                    text = msg,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
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