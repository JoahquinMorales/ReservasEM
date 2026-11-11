package cl.duoc.reservasem.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.duoc.reservasem.ui.components.ContextoNavegacionGlobal
import cl.duoc.reservasem.ui.components.LocalNavegacionGlobal
import cl.duoc.reservasem.ui.screens.*
import cl.duoc.reservasem.viewmodel.AdminViewModel
import cl.duoc.reservasem.viewmodel.AppViewModel
import cl.duoc.reservasem.viewmodel.CadeteViewModel
import cl.duoc.reservasem.viewmodel.LoginViewModel

@Composable
fun AppNavigation(
    appViewModel: AppViewModel = viewModel(factory = AppViewModel.Factory)
) {
    val navController = rememberNavController()
    val sesion by appViewModel.sesionState.collectAsState()

    val cadeteViewModel: CadeteViewModel = viewModel(factory = CadeteViewModel.Factory)
    val adminViewModel: AdminViewModel = viewModel(factory = AdminViewModel.Factory)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = navBackStackEntry?.destination?.route ?: ""

    val startDestination = when {
        !sesion.sesionIniciada -> Rutas.LOGIN
        sesion.rol == "ADMIN" -> Rutas.HOME_ADMIN
        else -> Rutas.HOME_CADETE
    }

    val navegarRapido: (String) -> Unit = { rutaDestino ->
        navController.navigate(rutaDestino) {
            launchSingleTop = true
        }
    }

    val cerrarSesionGlobal: () -> Unit = {
        appViewModel.cerrarSesion()
        navController.navigate(Rutas.LOGIN) {
            popUpTo(0) { inclusive = true }
        }
    }

    val contextoGlobal = ContextoNavegacionGlobal(
        rutaActual = rutaActual,
        esAdmin = sesion.rol == "ADMIN",
        nombreUsuario = sesion.nombre.ifBlank { sesion.correo.ifBlank { "Usuario Escuela Militar" } },
        onNavegar = navegarRapido,
        onCerrarSesion = cerrarSesionGlobal
    )

    CompositionLocalProvider(LocalNavegacionGlobal provides contextoGlobal) {
        Scaffold(
            containerColor = androidx.compose.ui.graphics.Color.Transparent
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                composable(Rutas.LOGIN) {
                    val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory)
                    LoginScreen(
                        viewModel = loginViewModel,
                        modoOscuro = sesion.modoOscuro,
                        onCambiarModoOscuro = appViewModel::cambiarModoOscuro,
                        onLoginExitoso = {
                            val destino = if (loginViewModel.uiState.value.rolSeleccionado == "ADMIN") {
                                Rutas.HOME_ADMIN
                            } else {
                                Rutas.HOME_CADETE
                            }
                            navController.navigate(destino) {
                                popUpTo(Rutas.LOGIN) { inclusive = true }
                            }
                        }
                    )
                }

                composable(Rutas.HOME_CADETE) {
                    CadeteScreen(
                        cadeteViewModel = cadeteViewModel,
                        appViewModel = appViewModel,
                        onNavegar = navegarRapido,
                        onSeleccionarCancha = { cancha ->
                            cadeteViewModel.preseleccionarCancha(cancha)
                            navController.navigate(Rutas.fichaCancha(cancha.id))
                        },
                        onReservarDirecto = { cancha ->
                            cadeteViewModel.preseleccionarCancha(cancha)
                            navController.navigate(Rutas.NUEVA_RESERVA)
                        },
                        onCerrarSesion = cerrarSesionGlobal
                    )
                }

                composable(Rutas.HOME_ADMIN) {
                    AdminScreen(
                        adminViewModel = adminViewModel,
                        appViewModel = appViewModel,
                        onNavegar = navegarRapido,
                        onCerrarSesion = cerrarSesionGlobal
                    )
                }

                composable(Rutas.NUEVA_RESERVA) {
                    NuevaReservaScreen(
                        viewModel = cadeteViewModel,
                        onIrAConfirmar = { navController.navigate(Rutas.CONFIRMAR_RESERVA) },
                        onVolver = { navController.popBackStack() }
                    )
                }

                composable(Rutas.RESULTADOS_BUSQUEDA) {
                    ResultadosScreen(
                        viewModel = cadeteViewModel,
                        onSeleccionarCancha = { cancha ->
                            cadeteViewModel.preseleccionarCancha(cancha)
                            navController.navigate(Rutas.NUEVA_RESERVA)
                        },
                        onVolver = { navController.popBackStack() }
                    )
                }

                composable(
                    route = Rutas.FICHA_CANCHA,
                    arguments = listOf(navArgument("canchaId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val canchaId = backStackEntry.arguments?.getInt("canchaId") ?: 1
                    FichaCanchaScreen(
                        canchaId = canchaId,
                        viewModel = cadeteViewModel,
                        onSolicitarReserva = { cancha ->
                            cadeteViewModel.preseleccionarCancha(cancha)
                            navController.navigate(Rutas.NUEVA_RESERVA)
                        },
                        onVolver = { navController.popBackStack() }
                    )
                }

                composable(Rutas.CONFIRMAR_RESERVA) {
                    ConfirmarReservaScreen(
                        viewModel = cadeteViewModel,
                        onReservaConfirmada = {
                            navController.navigate(Rutas.MIS_RESERVAS) {
                                popUpTo(Rutas.HOME_CADETE)
                            }
                        },
                        onVolver = { navController.popBackStack() }
                    )
                }

                composable(Rutas.MIS_RESERVAS) {
                    MisReservasScreen(
                        viewModel = cadeteViewModel,
                        onVerDetalle = { id -> navController.navigate(Rutas.detalleReserva(id)) },
                        onReprogramar = { id -> navController.navigate(Rutas.reprogramarReserva(id)) },
                        onVolver = { navController.popBackStack() }
                    )
                }

                composable(
                    route = Rutas.DETALLE_RESERVA,
                    arguments = listOf(navArgument("reservaId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val reservaId = backStackEntry.arguments?.getInt("reservaId") ?: 1
                    DetalleReservaScreen(
                        reservaId = reservaId,
                        viewModel = cadeteViewModel,
                        esAdmin = sesion.rol == "ADMIN",
                        onReprogramar = { id -> navController.navigate(Rutas.reprogramarReserva(id)) },
                        onVolver = { navController.popBackStack() }
                    )
                }

                composable(
                    route = Rutas.REPROGRAMAR_RESERVA,
                    arguments = listOf(navArgument("reservaId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val reservaId = backStackEntry.arguments?.getInt("reservaId") ?: 1
                    ReprogramarReservaScreen(
                        reservaId = reservaId,
                        viewModel = cadeteViewModel,
                        onVolver = { navController.popBackStack() }
                    )
                }

                composable(Rutas.SOLICITUDES_ADMIN) {
                    SolicitudesScreen(
                        viewModel = adminViewModel,
                        onVerDetalle = { id -> navController.navigate(Rutas.detalleReserva(id)) },
                        onVolver = { navController.popBackStack() }
                    )
                }

                composable(Rutas.CALENDARIO) {
                    CalendarioScreen(
                        cadeteViewModel = cadeteViewModel,
                        onSeleccionarBloque = { dia, bloque ->
                            cadeteViewModel.onDiaSemanaChange(dia)
                            cadeteViewModel.onBloqueHorarioChange(bloque)
                            navController.navigate(Rutas.NUEVA_RESERVA)
                        },
                        onVolver = { navController.popBackStack() }
                    )
                }

                composable(Rutas.INSTALACIONES_USUARIO) {
                    InstalacionUsuarioScreen(
                        viewModel = cadeteViewModel,
                        onReservarCancha = { cancha ->
                            cadeteViewModel.preseleccionarCancha(cancha)
                            navController.navigate(Rutas.NUEVA_RESERVA)
                        },
                        onVolver = { navController.popBackStack() }
                    )
                }

                composable(Rutas.INSTALACIONES_ADMIN) {
                    InstalacionesScreen(
                        viewModel = adminViewModel,
                        onEditarCancha = { id -> navController.navigate(Rutas.editarCancha(id)) },
                        onVolver = { navController.popBackStack() }
                    )
                }

                composable(
                    route = Rutas.EDITAR_CANCHA,
                    arguments = listOf(navArgument("canchaId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val canchaId = backStackEntry.arguments?.getInt("canchaId") ?: 1
                    EditarCanchaScreen(
                        canchaId = canchaId,
                        viewModel = adminViewModel,
                        onVolver = { navController.popBackStack() }
                    )
                }

                composable(Rutas.EVENTOS) {
                    EventosScreen(
                        cadeteViewModel = cadeteViewModel,
                        esAdmin = sesion.rol == "ADMIN",
                        onCrearEvento = { navController.navigate(Rutas.CREAR_EVENTO) },
                        onCrearEquipo = { navController.navigate(Rutas.CREAR_EQUIPO) },
                        onVerDetalleEvento = { id -> navController.navigate(Rutas.detalleEvento(id)) },
                        onVolver = { navController.popBackStack() }
                    )
                }

                composable(
                    route = Rutas.DETALLE_EVENTO,
                    arguments = listOf(navArgument("eventoId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val eventoId = backStackEntry.arguments?.getInt("eventoId") ?: 1
                    DetalleEventoScreen(
                        eventoId = eventoId,
                        viewModel = cadeteViewModel,
                        onVolver = { navController.popBackStack() }
                    )
                }

                composable(Rutas.CREAR_EVENTO) {
                    CrearEventoScreen(
                        viewModel = adminViewModel,
                        onVolver = { navController.popBackStack() }
                    )
                }

                composable(Rutas.CREAR_EQUIPO) {
                    CrearEquipoScreen(
                        viewModel = cadeteViewModel,
                        onVolver = { navController.popBackStack() }
                    )
                }

                composable(Rutas.USUARIOS_ADMIN) {
                    UsuariosScreen(
                        viewModel = adminViewModel,
                        onVerDetalleUsuario = { id -> navController.navigate(Rutas.detalleUsuario(id)) },
                        onVolver = { navController.popBackStack() }
                    )
                }

                composable(
                    route = Rutas.DETALLE_USUARIO,
                    arguments = listOf(navArgument("usuarioId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val usuarioId = backStackEntry.arguments?.getInt("usuarioId") ?: 1
                    DetalleUsuarioScreen(
                        usuarioId = usuarioId,
                        viewModel = adminViewModel,
                        onVolver = { navController.popBackStack() }
                    )
                }

                composable(Rutas.REPORTES) {
                    ReportesScreen(
                        viewModel = adminViewModel,
                        onVolver = { navController.popBackStack() }
                    )
                }

                composable(Rutas.NOTIFICACIONES) {
                    NotificacionesScreen(
                        viewModel = cadeteViewModel,
                        onVolver = { navController.popBackStack() }
                    )
                }

                composable(Rutas.PERFIL) {
                    PerfilScreen(
                        appViewModel = appViewModel,
                        cadeteViewModel = cadeteViewModel,
                        onIrAPreferencias = { navController.navigate(Rutas.PREFERENCIAS) },
                        onIrAConfiguracion = { navController.navigate(Rutas.CONFIGURACION) },
                        onCerrarSesion = cerrarSesionGlobal,
                        onVolver = { navController.popBackStack() }
                    )
                }

                composable(Rutas.PREFERENCIAS) {
                    PreferenciasScreen(
                        appViewModel = appViewModel,
                        onIrAConfiguracion = { navController.navigate(Rutas.CONFIGURACION) },
                        onVolver = { navController.popBackStack() }
                    )
                }

                composable(Rutas.CONFIGURACION) {
                    ConfiguracionScreen(
                        appViewModel = appViewModel,
                        onIrAPoliticas = { navController.navigate(Rutas.POLITICAS) },
                        onIrAAyuda = { navController.navigate(Rutas.AYUDA) },
                        onVolver = { navController.popBackStack() }
                    )
                }

                composable(Rutas.POLITICAS) {
                    PoliticasScreen(onVolver = { navController.popBackStack() })
                }

                composable(Rutas.AYUDA) {
                    AyudaScreen(onVolver = { navController.popBackStack() })
                }
            }
        }
    }
}
