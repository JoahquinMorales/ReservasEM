package cl.duoc.reservasem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.reservasem.navigation.AppNavigation
import cl.duoc.reservasem.ui.theme.EscuelaMilitarTheme
import cl.duoc.reservasem.viewmodel.AppViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appViewModel: AppViewModel = viewModel(factory = AppViewModel.Factory)
            val sesion by appViewModel.sesionState.collectAsState()
            val estadoModoOscuro = sesion.modoOscuro

            EscuelaMilitarTheme(darkTheme = estadoModoOscuro) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(appViewModel = appViewModel)
                }
            }
        }
    }
}
