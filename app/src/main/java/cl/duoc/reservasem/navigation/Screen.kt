package cl.duoc.reservasem.navigation

/**
 * Grafo de rutas selladas para navegación tipada con Compose Navigation.
 * Garantiza la consistencia de destinos en tiempo de compilación.
 */
//region Rutas de Navegación del Módulo
sealed class Screen(val route: String) {
    data object Registro : Screen("registro_screen")
    data object Resumen : Screen("resumen_screen")
    data object Perfil : Screen("perfil_screen")
    data object Configuracion : Screen("configuracion_screen")

    // TODO: Evaluar migración a Kotlin Serialization con Navigation 2.8+ para paso de argumentos tipados
}
//endregion
