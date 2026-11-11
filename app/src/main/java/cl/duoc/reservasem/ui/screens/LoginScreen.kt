package cl.duoc.reservasem.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.model.Horarios
import cl.duoc.reservasem.ui.components.CampoTextoTactico
import cl.duoc.reservasem.ui.components.SelectorTactico
import cl.duoc.reservasem.ui.theme.BadgeAprobadaCamuflaje
import cl.duoc.reservasem.ui.theme.MilitaryGoldGalon
import cl.duoc.reservasem.ui.theme.VibrantMilitaryGreen
import cl.duoc.reservasem.ui.theme.tacticalCommandBackgroundBrush
import cl.duoc.reservasem.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    modoOscuro: Boolean,
    onCambiarModoOscuro: (Boolean) -> Unit,
    onLoginExitoso: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val grupoActual = remember(uiState.grupoInstitucionId) {
        Horarios.obtenerGrupoPorId(uiState.grupoInstitucionId)
    }

    LaunchedEffect(uiState.loginExitoso) {
        if (uiState.loginExitoso) {
            onLoginExitoso()
        }
    }

    if (uiState.mostrarDialogoEvaluadorGoogle) {
        DialogoEvaluadorGoogle(
            rolActual = uiState.rolSeleccionado,
            onDismiss = { viewModel.mostrarDialogoEvaluadorGoogle(false) },
            onConfirmar = { correo, nombre ->
                viewModel.autenticarConGoogleEnRoom(correo, nombre)
            }
        )
    }

    val commandGradient = tacticalCommandBackgroundBrush()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = commandGradient)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Column {
                    TopAppBar(
                        title = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = MilitaryGoldGalon,
                                    modifier = Modifier.size(24.dp)
                                )
                                Column {
                                    Text(
                                        text = "ESCUELA MILITAR • DEPORTES",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.ExtraBold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "Centro de Mando de Recintos Deportivos",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MilitaryGoldGalon,
                                        maxLines = 1
                                    )
                                }
                            }
                        },
                        actions = {
                            IconButton(onClick = { onCambiarModoOscuro(!modoOscuro) }) {
                                Icon(
                                    imageVector = if (modoOscuro) Icons.Default.LightMode else Icons.Default.DarkMode,
                                    contentDescription = "Alternar Tema",
                                    tint = MilitaryGoldGalon
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f)
                        )
                    )
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MilitaryGoldGalon.copy(alpha = 0.55f)
                    )
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val cardShape = RoundedCornerShape(10.dp)

                // 1. Selección de Perfil de Acceso
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 4.dp,
                            shape = cardShape,
                            ambientColor = MilitaryGoldGalon.copy(alpha = 0.25f)
                        )
                        .animateContentSize(),
                    shape = cardShape,
                    border = BorderStroke(1.1.dp, MilitaryGoldGalon.copy(alpha = 0.7f)),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "1. SELECCIONE PERFIL DE ACCESO",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MilitaryGoldGalon
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = uiState.rolSeleccionado == "USUARIO",
                                onClick = { viewModel.seleccionarRol("USUARIO") },
                                label = {
                                    Text(
                                        text = "Cadete / Institución",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1
                                    )
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp))
                                },
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.weight(1f)
                            )
                            FilterChip(
                                selected = uiState.rolSeleccionado == "ADMIN",
                                onClick = { viewModel.seleccionarRol("ADMIN") },
                                label = {
                                    Text(
                                        text = "Oficial Admin",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1
                                    )
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.AdminPanelSettings, contentDescription = null, modifier = Modifier.size(16.dp))
                                },
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f),
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(0.8.dp, MilitaryGoldGalon.copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (uiState.rolSeleccionado == "ADMIN") {
                                            "Demo Admin: admin@escuelamilitar.cl (clave: admin_2026)"
                                        } else {
                                            "Demo Cadete: cadete@escuelamilitar.cl (clave: cadete_1234)"
                                        },
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                TextButton(
                                    onClick = {
                                        viewModel.autocompletarCredencialesDemo(
                                            esAdmin = uiState.rolSeleccionado == "ADMIN"
                                        )
                                    },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                                    modifier = Modifier.height(28.dp)
                                ) {
                                    Text(
                                        text = "CARGAR",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MilitaryGoldGalon
                                    )
                                }
                            }
                        }
                    }
                }

                // 2. NUEVO APARTADO: Clasificación por Tipo de Institución (Militar, Colegio, Beneficencia, Federación)
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 4.dp,
                            shape = cardShape,
                            ambientColor = MilitaryGoldGalon.copy(alpha = 0.25f)
                        )
                        .animateContentSize(),
                    shape = cardShape,
                    border = BorderStroke(1.1.dp, MilitaryGoldGalon.copy(alpha = 0.75f)),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "2. TIPO DE INSTITUCIÓN / CONVENIO SOLICITANTE",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MilitaryGoldGalon
                        )
                        Text(
                            text = "Seleccione el sector institucional al que pertenece para ingresar o crear cuenta:",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        // Fila 1 de Sectores: Escuela Militar + Colegios / Educación
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = uiState.grupoInstitucionId == "MILITAR_EM",
                                onClick = {
                                    if (!uiState.modoRegistro) {
                                        viewModel.autocompletarDemoPorSector("MILITAR_EM")
                                    } else {
                                        viewModel.onGrupoInstitucionChange("MILITAR_EM")
                                    }
                                },
                                label = {
                                    Text(
                                        text = "Escuela Militar",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        maxLines = 1
                                    )
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Shield, contentDescription = null, modifier = Modifier.size(15.dp))
                                },
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.weight(1f)
                            )

                            FilterChip(
                                selected = uiState.grupoInstitucionId == "COLEGIO_EDUCACION",
                                onClick = {
                                    if (!uiState.modoRegistro) {
                                        viewModel.autocompletarDemoPorSector("COLEGIO_EDUCACION")
                                    } else {
                                        viewModel.onGrupoInstitucionChange("COLEGIO_EDUCACION")
                                    }
                                },
                                label = {
                                    Text(
                                        text = "Colegio / Univ.",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        maxLines = 1
                                    )
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.size(15.dp))
                                },
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // Fila 2 de Sectores: Beneficencia / Fundación + Federaciones / Clubes
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = uiState.grupoInstitucionId == "BENEFICENCIA_FUNDACION",
                                onClick = {
                                    if (!uiState.modoRegistro) {
                                        viewModel.autocompletarDemoPorSector("BENEFICENCIA_FUNDACION")
                                    } else {
                                        viewModel.onGrupoInstitucionChange("BENEFICENCIA_FUNDACION")
                                    }
                                },
                                label = {
                                    Text(
                                        text = "Beneficencia",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        maxLines = 1
                                    )
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.VolunteerActivism, contentDescription = null, modifier = Modifier.size(15.dp))
                                },
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.weight(1f)
                            )

                            FilterChip(
                                selected = uiState.grupoInstitucionId == "FEDERACION_EXTERNA",
                                onClick = {
                                    if (!uiState.modoRegistro) {
                                        viewModel.autocompletarDemoPorSector("FEDERACION_EXTERNA")
                                    } else {
                                        viewModel.onGrupoInstitucionChange("FEDERACION_EXTERNA")
                                    }
                                },
                                label = {
                                    Text(
                                        text = "Federación / Club",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        maxLines = 1
                                    )
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.EmojiEvents, contentDescription = null, modifier = Modifier.size(15.dp))
                                },
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // Ficha descriptiva del sector seleccionado
                        Surface(
                            color = MilitaryGoldGalon.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(0.8.dp, MilitaryGoldGalon.copy(alpha = 0.55f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                                Text(
                                    text = "Sector activo: ${grupoActual.tituloGrupo}",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MilitaryGoldGalon
                                )
                                Text(
                                    text = grupoActual.descripcion,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // 3. Selector Iniciar Sesión vs Crear Cuenta
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.alternarModoRegistro(false) },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(
                            1.dp,
                            if (!uiState.modoRegistro) MilitaryGoldGalon else MilitaryGoldGalon.copy(alpha = 0.35f)
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!uiState.modoRegistro) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                            contentColor = if (!uiState.modoRegistro) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "INICIAR SESIÓN",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Button(
                        onClick = { viewModel.alternarModoRegistro(true) },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(
                            1.dp,
                            if (uiState.modoRegistro) MilitaryGoldGalon else MilitaryGoldGalon.copy(alpha = 0.35f)
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (uiState.modoRegistro) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                            contentColor = if (uiState.modoRegistro) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "CREAR CUENTA",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                // 4. Formulario de Ingreso / Registro con separación de tipos de institución
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 5.dp,
                            shape = cardShape,
                            ambientColor = MilitaryGoldGalon.copy(alpha = 0.25f)
                        )
                        .animateContentSize(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessMediumLow
                            )
                        ),
                    shape = cardShape,
                    border = BorderStroke(1.2.dp, MilitaryGoldGalon.copy(alpha = 0.75f)),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = if (uiState.modoRegistro) {
                                "3. REGISTRO DE CUENTA • ${grupoActual.tituloGrupo.uppercase()}"
                            } else {
                                "3. INGRESO CON CREDENCIALES (${if (uiState.rolSeleccionado == "ADMIN") "OFICIAL ADMIN" else grupoActual.tituloGrupo.uppercase()})"
                            },
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MilitaryGoldGalon
                        )

                        AnimatedVisibility(visible = uiState.modoRegistro) {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                SelectorTactico(
                                    opciones = grupoActual.tipos,
                                    seleccionActual = uiState.tipoSolicitanteRegistro,
                                    onSeleccionChange = viewModel::onTipoSolicitanteChange,
                                    etiqueta = "Subtipo de Solicitante (${grupoActual.tituloGrupo})"
                                )

                                CampoTextoTactico(
                                    valor = uiState.nombreEntidadInstitucion,
                                    onValorChange = viewModel::onNombreEntidadChange,
                                    etiqueta = when (uiState.grupoInstitucionId) {
                                        "COLEGIO_EDUCACION" -> "Nombre del Colegio, Liceo o Universidad"
                                        "BENEFICENCIA_FUNDACION" -> "Nombre de la Fundación o Institución de Beneficencia"
                                        "FEDERACION_EXTERNA" -> "Nombre de la Federación, Club o Municipalidad"
                                        else -> "Compañía, Unidad o Rama de la Escuela Militar"
                                    },
                                    icono = Icons.Default.AccountBalance,
                                    errorMensaje = uiState.errores.entidadError,
                                    textoAyudaValido = "Entidad / Institución registrada correctamente"
                                )

                                CampoTextoTactico(
                                    valor = uiState.nombreRegistro,
                                    onValorChange = viewModel::onNombreRegistroChange,
                                    etiqueta = "Grado y Nombre del Responsable / Solicitante",
                                    icono = Icons.Default.Badge,
                                    errorMensaje = uiState.errores.nombreError,
                                    textoAyudaValido = "Responsable verificado"
                                )
                            }
                        }

                        CampoTextoTactico(
                            valor = uiState.correo,
                            onValorChange = viewModel::onCorreoChange,
                            etiqueta = "Correo electrónico institucional o de convenio",
                            icono = Icons.Default.Email,
                            errorMensaje = uiState.errores.correoError,
                            textoAyudaValido = "Formato de correo institucional válido"
                        )

                        CampoTextoTactico(
                            valor = uiState.clave,
                            onValorChange = viewModel::onClaveChange,
                            etiqueta = "Contraseña (mín. 5 caracteres + número o símbolo)",
                            icono = Icons.Default.Lock,
                            errorMensaje = uiState.errores.claveError,
                            textoAyudaValido = "Contraseña cumple requisitos de seguridad",
                            visualTransformation = PasswordVisualTransformation()
                        )

                        AnimatedVisibility(visible = uiState.mensajeExito != null) {
                            Surface(
                                color = BadgeAprobadaCamuflaje.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp),
                                border = BorderStroke(1.dp, VibrantMilitaryGreen),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = VibrantMilitaryGreen
                                    )
                                    Text(
                                        text = uiState.mensajeExito ?: "",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        AnimatedVisibility(visible = uiState.errores.generalError != null) {
                            Surface(
                                color = MaterialTheme.colorScheme.errorContainer,
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = uiState.errores.generalError ?: "",
                                        color = MaterialTheme.colorScheme.onErrorContainer,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (uiState.sugerirCrearCuenta && !uiState.modoRegistro) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        OutlinedButton(
                                            onClick = { viewModel.alternarModoRegistro(true) },
                                            shape = RoundedCornerShape(6.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Crear cuenta nueva en este sector institucional",
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = FontWeight.ExtraBold
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Button(
                            onClick = {
                                if (uiState.modoRegistro) {
                                    viewModel.registrarNuevoUsuario()
                                } else {
                                    viewModel.iniciarSesion()
                                }
                            },
                            enabled = !uiState.cargando,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.2.dp, MilitaryGoldGalon),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                        ) {
                            if (uiState.cargando) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            } else {
                                Text(
                                    text = if (uiState.modoRegistro) "REGISTRAR CUENTA EN SECTOR" else "INGRESAR AL SISTEMA",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }

                OutlinedButton(
                    onClick = { viewModel.iniciarSesionGoogle(context) },
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.2.dp, MilitaryGoldGalon),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Google",
                        modifier = Modifier.size(18.dp),
                        tint = MilitaryGoldGalon
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "CONTINUAR CON GOOGLE",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun DialogoEvaluadorGoogle(
    rolActual: String,
    onDismiss: () -> Unit,
    onConfirmar: (correo: String, nombre: String) -> Unit
) {
    var correo by remember {
        mutableStateOf(
            if (rolActual == "ADMIN") "evaluador.admin.em@gmail.com"
            else "evaluador.docente@escuelamilitar.cl"
        )
    }
    var nombre by remember {
        mutableStateOf(
            if (rolActual == "ADMIN") "Evaluador Docente (Admin Google)"
            else "Evaluador Docente Duoc UC"
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(10.dp),
        title = {
            Text(
                text = "Ingresar como Evaluador Google",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Simulador de Evaluacion Academica (Credential Manager fallback). Confirme la cuenta Google para autenticar en Room:",
                    style = MaterialTheme.typography.bodySmall
                )
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre", style = MaterialTheme.typography.labelSmall) },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo Google", style = MaterialTheme.typography.labelSmall) },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirmar(
                        correo.trim().ifBlank { "evaluador@escuelamilitar.cl" },
                        nombre.trim().ifBlank { "Evaluador Google EM" }
                    )
                },
                shape = RoundedCornerShape(6.dp)
            ) {
                Text("Autenticar", fontWeight = FontWeight.ExtraBold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
