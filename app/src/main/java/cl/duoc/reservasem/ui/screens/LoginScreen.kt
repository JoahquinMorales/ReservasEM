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
import cl.duoc.reservasem.ui.theme.MilitaryGoldGalon
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
    var menuPerfilesExpandido by remember { mutableStateOf(false) }

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
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = uiState.rolSeleccionado == "USUARIO",
                                onClick = { viewModel.seleccionarRol("USUARIO") },
                                label = {
                                    Text(
                                        text = "Cadete / Solicitante",
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
                                        text = "Administrador",
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
                                            "Demo Admin: admin@escuelamilitar.cl (clave: admin_2026)" // <-- Modificado
                                        } else {
                                            "Demo Cadete: cadete@escuelamilitar.cl (clave: cadete_1234)" // <-- Modificado
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
                            text = "INICIAR SESION",
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
                                "REGISTRO DE NUEVA CUENTA INSTITUCIONAL"
                            } else {
                                "INGRESO CON CREDENCIALES (${if (uiState.rolSeleccionado == "ADMIN") "OFICIAL ADMIN" else "SOLICITANTE"})"
                            },
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MilitaryGoldGalon
                        )

                        AnimatedVisibility(visible = uiState.modoRegistro) {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                CampoTextoTactico(
                                    valor = uiState.nombreRegistro,
                                    onValorChange = viewModel::onNombreRegistroChange,
                                    etiqueta = "Grado y Nombre completo o Entidad",
                                    icono = Icons.Default.Badge,
                                    errorMensaje = uiState.errores.nombreError
                                )

                                ExposedDropdownMenuBox(
                                    expanded = menuPerfilesExpandido,
                                    onExpandedChange = { menuPerfilesExpandido = !menuPerfilesExpandido }
                                ) {
                                    OutlinedTextField(
                                        value = uiState.tipoSolicitanteRegistro,
                                        onValueChange = {},
                                        readOnly = true,
                                        label = {
                                            Text(
                                                "Categoria de Solicitante (Estamento EM)",
                                                style = MaterialTheme.typography.bodySmall,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        },
                                        trailingIcon = {
                                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuPerfilesExpandido)
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
                                        expanded = menuPerfilesExpandido,
                                        onDismissRequest = { menuPerfilesExpandido = false }
                                    ) {
                                        Horarios.tiposSolicitanteOficiales.forEach { tipo ->
                                            DropdownMenuItem(
                                                text = { Text(tipo, style = MaterialTheme.typography.bodySmall) },
                                                onClick = {
                                                    viewModel.onTipoSolicitanteChange(tipo)
                                                    menuPerfilesExpandido = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        CampoTextoTactico(
                            valor = uiState.correo,
                            onValorChange = viewModel::onCorreoChange,
                            etiqueta = "Correo electronico institucional",
                            icono = Icons.Default.Email,
                            errorMensaje = uiState.errores.correoError
                        )

                        CampoTextoTactico(
                            valor = uiState.clave,
                            onValorChange = viewModel::onClaveChange,
                            etiqueta = "Contrasena (min. 5 caracteres)", // <-- Modificado
                            icono = Icons.Default.Lock,
                            errorMensaje = uiState.errores.claveError,
                            visualTransformation = PasswordVisualTransformation()
                        )

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
                                                text = "Crear cuenta nueva con este correo",
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
                                    text = if (uiState.modoRegistro) "REGISTRAR Y ACCEDER" else "INGRESAR AL SISTEMA",
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