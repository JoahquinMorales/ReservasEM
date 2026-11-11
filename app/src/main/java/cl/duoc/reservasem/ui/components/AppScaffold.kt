package cl.duoc.reservasem.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.navigation.Rutas
import cl.duoc.reservasem.ui.theme.BadgeAprobadaCamuflaje
import cl.duoc.reservasem.ui.theme.BadgeRechazadaOxido
import cl.duoc.reservasem.ui.theme.MilitaryGoldGalon
import cl.duoc.reservasem.ui.theme.VibrantMilitaryGreen
import cl.duoc.reservasem.ui.theme.tacticalCommandBackgroundBrush
import kotlinx.coroutines.launch

data class ItemNavegacionInferior(
    val ruta: String,
    val etiqueta: String,
    val icono: ImageVector
)

data class ContextoNavegacionGlobal(
    val rutaActual: String = "",
    val esAdmin: Boolean = false,
    val nombreUsuario: String = "Usuario Institucional",
    val onNavegar: (String) -> Unit = {},
    val onCerrarSesion: (() -> Unit)? = null
)

val LocalNavegacionGlobal = staticCompositionLocalOf { ContextoNavegacionGlobal() }

private fun obtenerItemsBarraInferior(esAdmin: Boolean): List<ItemNavegacionInferior> {
    return if (esAdmin) {
        listOf(
            ItemNavegacionInferior(Rutas.HOME_ADMIN, "Mando", Icons.Default.Shield),
            ItemNavegacionInferior(Rutas.SOLICITUDES_ADMIN, "Solicitudes", Icons.Default.Assignment),
            ItemNavegacionInferior(Rutas.USUARIOS_ADMIN, "Usuarios", Icons.Default.Group),
            ItemNavegacionInferior(Rutas.INSTALACIONES_ADMIN, "Recintos", Icons.Default.Build),
            ItemNavegacionInferior(Rutas.PERFIL, "Perfil", Icons.Default.Person)
        )
    } else {
        listOf(
            ItemNavegacionInferior(Rutas.HOME_CADETE, "Recintos", Icons.Default.SportsSoccer),
            ItemNavegacionInferior(Rutas.CALENDARIO, "Horario", Icons.Default.CalendarMonth),
            ItemNavegacionInferior(Rutas.MIS_RESERVAS, "Reservas", Icons.Default.ListAlt),
            ItemNavegacionInferior(Rutas.EVENTOS, "Eventos", Icons.Default.EmojiEvents),
            ItemNavegacionInferior(Rutas.PERFIL, "Perfil", Icons.Default.Person)
        )
    }
}

private fun obtenerItemsMenuLateral(esAdmin: Boolean): List<ItemNavegacionInferior> {
    return if (esAdmin) {
        listOf(
            ItemNavegacionInferior(Rutas.HOME_ADMIN, "Puesto de Mando Principal", Icons.Default.Shield),
            ItemNavegacionInferior(Rutas.SOLICITUDES_ADMIN, "Gestión de Solicitudes", Icons.Default.Assignment),
            ItemNavegacionInferior(Rutas.USUARIOS_ADMIN, "Confirmar Nuevos Usuarios", Icons.Default.HowToReg),
            ItemNavegacionInferior(Rutas.INSTALACIONES_ADMIN, "Control y Mantención Recintos", Icons.Default.Build),
            ItemNavegacionInferior(Rutas.EVENTOS, "Torneos y Competencias EM", Icons.Default.EmojiEvents),
            ItemNavegacionInferior(Rutas.REPORTES, "Estadísticas y Reportes", Icons.Default.BarChart),
            ItemNavegacionInferior(Rutas.PERFIL, "Credencial y Perfil Oficial", Icons.Default.Badge),
            ItemNavegacionInferior(Rutas.CONFIGURACION, "Configuración y DataStore", Icons.Default.Settings),
            ItemNavegacionInferior(Rutas.AYUDA, "Reglamento y Soporte", Icons.Default.Info)
        )
    } else {
        listOf(
            ItemNavegacionInferior(Rutas.HOME_CADETE, "Catálogo de Recintos EM", Icons.Default.SportsSoccer),
            ItemNavegacionInferior(Rutas.NUEVA_RESERVA, "Solicitar Nueva Reserva", Icons.Default.AddCircleOutline),
            ItemNavegacionInferior(Rutas.CALENDARIO, "Disponibilidad por Bloques", Icons.Default.CalendarMonth),
            ItemNavegacionInferior(Rutas.MIS_RESERVAS, "Mis Reservas y Código QR/Barras", Icons.Default.QrCode2),
            ItemNavegacionInferior(Rutas.EVENTOS, "Torneos y Escuadras", Icons.Default.EmojiEvents),
            ItemNavegacionInferior(Rutas.PERFIL, "Mi Perfil y Credencial TUI", Icons.Default.Badge),
            ItemNavegacionInferior(Rutas.PREFERENCIAS, "Preferencias Visuales", Icons.Default.Tune),
            ItemNavegacionInferior(Rutas.CONFIGURACION, "Panel de Configuración", Icons.Default.Settings),
            ItemNavegacionInferior(Rutas.POLITICAS, "Normativa Deportiva EM", Icons.Default.Gavel)
        )
    }
}

private fun esRutaSeleccionada(rutaActual: String, rutaItem: String): Boolean {
    if (rutaActual == rutaItem) return true
    return when (rutaItem) {
        Rutas.HOME_CADETE -> rutaActual.startsWith("ficha_cancha") ||
            rutaActual == Rutas.NUEVA_RESERVA ||
            rutaActual == Rutas.CONFIRMAR_RESERVA ||
            rutaActual == Rutas.RESULTADOS_BUSQUEDA ||
            rutaActual == Rutas.INSTALACIONES_USUARIO
        Rutas.MIS_RESERVAS -> rutaActual.startsWith("detalle_reserva") ||
            rutaActual.startsWith("reprogramar_reserva")
        Rutas.EVENTOS -> rutaActual.startsWith("detalle_evento") ||
            rutaActual == Rutas.CREAR_EVENTO ||
            rutaActual == Rutas.CREAR_EQUIPO
        Rutas.INSTALACIONES_ADMIN -> rutaActual.startsWith("editar_cancha")
        Rutas.USUARIOS_ADMIN -> rutaActual.startsWith("detalle_usuario")
        Rutas.PERFIL -> rutaActual == Rutas.PREFERENCIAS ||
            rutaActual == Rutas.CONFIGURACION ||
            rutaActual == Rutas.POLITICAS ||
            rutaActual == Rutas.AYUDA
        else -> false
    }
}

@Composable
fun BarraNavegacionInferiorTactica(
    rutaActual: String,
    esAdmin: Boolean,
    onNavegar: (String) -> Unit
) {
    val itemsNav = obtenerItemsBarraInferior(esAdmin)

    Column {
        HorizontalDivider(
            thickness = 1.dp,
            color = MilitaryGoldGalon.copy(alpha = 0.5f)
        )
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.97f),
            tonalElevation = 8.dp
        ) {
            itemsNav.forEach { item ->
                val seleccionado = esRutaSeleccionada(rutaActual, item.ruta)
                NavigationBarItem(
                    selected = seleccionado,
                    onClick = {
                        if (rutaActual != item.ruta) {
                            onNavegar(item.ruta)
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = item.icono,
                            contentDescription = item.etiqueta,
                            modifier = Modifier.size(if (seleccionado) 22.dp else 20.dp)
                        )
                    },
                    label = {
                        Text(
                            text = item.etiqueta,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (seleccionado) FontWeight.ExtraBold else FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MilitaryGoldGalon,
                        selectedTextColor = MilitaryGoldGalon,
                        indicatorColor = MilitaryGoldGalon.copy(alpha = 0.22f),
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}

@Composable
private fun ContenidoMenuLateralTactico(
    esAdmin: Boolean,
    subtitulo: String?,
    rutaActual: String,
    onSeleccionarRuta: (String) -> Unit,
    onCerrarSesion: (() -> Unit)?
) {
    val itemsMenuLateral = obtenerItemsMenuLateral(esAdmin)

    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.surface,
        drawerShape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
        modifier = Modifier.width(305.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                MilitaryGoldGalon.copy(alpha = 0.22f),
                                BadgeAprobadaCamuflaje.copy(alpha = 0.14f)
                            )
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            color = MilitaryGoldGalon.copy(alpha = 0.25f),
                            shape = CircleShape,
                            border = BorderStroke(1.5.dp, MilitaryGoldGalon),
                            modifier = Modifier.size(46.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (esAdmin) Icons.Default.AdminPanelSettings else Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = MilitaryGoldGalon,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "ESCUELA MILITAR",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = MilitaryGoldGalon
                            )
                            Text(
                                text = subtitulo ?: "Usuario Institucional",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Surface(
                        color = if (esAdmin) MilitaryGoldGalon.copy(alpha = 0.2f) else VibrantMilitaryGreen.copy(alpha = 0.18f),
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(0.8.dp, MilitaryGoldGalon)
                    ) {
                        Text(
                            text = if (esAdmin) "PERFIL: OFICIAL ADMINISTRADOR" else "PERFIL: SOLICITANTE / CADETE",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MilitaryGoldGalon,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            HorizontalDivider(thickness = 1.2.dp, color = MilitaryGoldGalon.copy(alpha = 0.6f))
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "MENÚ TÁCTICO DE NAVEGACIÓN RÁPIDA",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MilitaryGoldGalon,
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp)
            )

            itemsMenuLateral.forEach { item ->
                val seleccionado = rutaActual == item.ruta
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = item.etiqueta,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = if (seleccionado) FontWeight.ExtraBold else FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    icon = {
                        Icon(
                            imageVector = item.icono,
                            contentDescription = item.etiqueta,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    selected = seleccionado,
                    onClick = { onSeleccionarRuta(item.ruta) },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = MilitaryGoldGalon.copy(alpha = 0.20f),
                        selectedIconColor = MilitaryGoldGalon,
                        selectedTextColor = MilitaryGoldGalon,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            HorizontalDivider(
                thickness = 0.8.dp,
                color = MilitaryGoldGalon.copy(alpha = 0.35f),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            if (onCerrarSesion != null) {
                OutlinedButton(
                    onClick = onCerrarSesion,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, BadgeRechazadaOxido),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = BadgeRechazadaOxido
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Cerrar Sesión",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "CERRAR SESIÓN INSTITUCIONAL",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    titulo: String,
    subtitulo: String? = null,
    rutaActual: String,
    esAdmin: Boolean,
    onNavegar: (String) -> Unit,
    onCerrarSesion: (() -> Unit)? = null,
    accionesExtra: @Composable RowScope.() -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val commandGradient = tacticalCommandBackgroundBrush()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ContenidoMenuLateralTactico(
                esAdmin = esAdmin,
                subtitulo = subtitulo,
                rutaActual = rutaActual,
                onSeleccionarRuta = { destino ->
                    scope.launch { drawerState.close() }
                    if (rutaActual != destino) {
                        onNavegar(destino)
                    }
                },
                onCerrarSesion = if (onCerrarSesion != null) {
                    {
                        scope.launch { drawerState.close() }
                        onCerrarSesion()
                    }
                } else null
            )
        }
    ) {
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
                            navigationIcon = {
                                IconButton(
                                    onClick = {
                                        scope.launch {
                                            if (drawerState.isClosed) drawerState.open() else drawerState.close()
                                        }
                                    }
                                ) {
                                    Surface(
                                        color = MilitaryGoldGalon.copy(alpha = 0.16f),
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(1.dp, MilitaryGoldGalon.copy(alpha = 0.65f)),
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Default.Menu,
                                                contentDescription = "Abrir Menú Táctico",
                                                tint = MilitaryGoldGalon,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                            },
                            title = {
                                Column {
                                    Text(
                                        text = titulo.uppercase(),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.ExtraBold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    if (!subtitulo.isNullOrBlank()) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(6.dp)
                                                    .background(VibrantMilitaryGreen, CircleShape)
                                            )
                                            Text(
                                                text = subtitulo,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.SemiBold,
                                                color = MilitaryGoldGalon,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                            },
                            actions = {
                                accionesExtra()
                                IconButton(onClick = { onNavegar(Rutas.NOTIFICACIONES) }) {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = "Notificaciones",
                                        tint = MilitaryGoldGalon
                                    )
                                }
                                IconButton(onClick = { onNavegar(Rutas.CONFIGURACION) }) {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = "Configuración y Mando",
                                        tint = MilitaryGoldGalon
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                                titleContentColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        HorizontalDivider(
                            thickness = 1.2.dp,
                            color = MilitaryGoldGalon.copy(alpha = 0.6f)
                        )
                    }
                },
                bottomBar = {
                    BarraNavegacionInferiorTactica(
                        rutaActual = rutaActual,
                        esAdmin = esAdmin,
                        onNavegar = onNavegar
                    )
                },
                floatingActionButton = floatingActionButton,
                content = content
            )
        }
    }
}

/**
 * ScaffoldSecundario ahora conserva SIEMPRE visible la barra inferior de selección rápida (NavigationBar)
 * y el acceso al menú táctico lateral, permitiendo cambiar de sección instantáneamente desde cualquier pantalla
 * sin perder el botón de retroceso ("Volver").
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldSecundario(
    titulo: String,
    subtitulo: String? = null,
    onVolver: () -> Unit,
    accionesExtra: @Composable RowScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val navGlobal = LocalNavegacionGlobal.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val commandGradient = tacticalCommandBackgroundBrush()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ContenidoMenuLateralTactico(
                esAdmin = navGlobal.esAdmin,
                subtitulo = navGlobal.nombreUsuario,
                rutaActual = navGlobal.rutaActual,
                onSeleccionarRuta = { destino ->
                    scope.launch { drawerState.close() }
                    navGlobal.onNavegar(destino)
                },
                onCerrarSesion = navGlobal.onCerrarSesion?.let { cerrar ->
                    {
                        scope.launch { drawerState.close() }
                        cerrar()
                    }
                }
            )
        }
    ) {
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
                                Column {
                                    Text(
                                        text = titulo.uppercase(),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.ExtraBold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    if (!subtitulo.isNullOrBlank()) {
                                        Text(
                                            text = subtitulo,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MilitaryGoldGalon,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            },
                            navigationIcon = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = onVolver) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowBack,
                                            contentDescription = "Volver",
                                            tint = MilitaryGoldGalon
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            scope.launch {
                                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Menu,
                                            contentDescription = "Menú Táctico",
                                            tint = MilitaryGoldGalon
                                        )
                                    }
                                }
                            },
                            actions = accionesExtra,
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                                titleContentColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = MilitaryGoldGalon.copy(alpha = 0.55f)
                        )
                    }
                },
                bottomBar = {
                    BarraNavegacionInferiorTactica(
                        rutaActual = navGlobal.rutaActual,
                        esAdmin = navGlobal.esAdmin,
                        onNavegar = navGlobal.onNavegar
                    )
                },
                content = content
            )
        }
    }
}
