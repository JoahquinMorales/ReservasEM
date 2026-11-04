package cl.duoc.reservasem.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.navigation.Rutas
import cl.duoc.reservasem.ui.theme.MilitaryGoldGalon
import cl.duoc.reservasem.ui.theme.tacticalCommandBackgroundBrush

data class ItemNavegacionInferior(
    val ruta: String,
    val etiqueta: String,
    val icono: ImageVector
)

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
    val itemsNav = if (esAdmin) {
        listOf(
            ItemNavegacionInferior(Rutas.HOME_ADMIN, "Mando", Icons.Default.Shield),
            ItemNavegacionInferior(Rutas.SOLICITUDES_ADMIN, "Solicitudes", Icons.Default.Assignment),
            ItemNavegacionInferior(Rutas.INSTALACIONES_ADMIN, "Recintos", Icons.Default.Build),
            ItemNavegacionInferior(Rutas.REPORTES, "Reportes", Icons.Default.BarChart),
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
                        actions = {
                            accionesExtra()
                            IconButton(onClick = { onNavegar(Rutas.CONFIGURACION) }) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Configuración y Mando",
                                    tint = MilitaryGoldGalon
                                )
                            }
                            IconButton(onClick = { onNavegar(Rutas.NOTIFICACIONES) }) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notificaciones",
                                    tint = MilitaryGoldGalon
                                )
                            }
                            if (onCerrarSesion != null) {
                                IconButton(onClick = onCerrarSesion) {
                                    Icon(
                                        imageVector = Icons.Default.ExitToApp,
                                        contentDescription = "Cerrar Sesión"
                                    )
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f),
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
                Column {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MilitaryGoldGalon.copy(alpha = 0.45f)
                    )
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
                        tonalElevation = 6.dp
                    ) {
                        itemsNav.forEach { item ->
                            val seleccionado = rutaActual == item.ruta
                            NavigationBarItem(
                                selected = seleccionado,
                                onClick = {
                                    if (!seleccionado) {
                                        onNavegar(item.ruta)
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = item.icono,
                                        contentDescription = item.etiqueta
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
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                }
            },
            floatingActionButton = floatingActionButton,
            content = content
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldSecundario(
    titulo: String,
    subtitulo: String? = null,
    onVolver: () -> Unit,
    accionesExtra: @Composable RowScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
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
                            IconButton(onClick = onVolver) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Volver",
                                    tint = MilitaryGoldGalon
                                )
                            }
                        },
                        actions = accionesExtra,
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f),
                            titleContentColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MilitaryGoldGalon.copy(alpha = 0.55f)
                    )
                }
            },
            content = content
        )
    }
}
