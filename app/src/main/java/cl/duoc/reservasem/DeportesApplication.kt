package cl.duoc.reservasem

import android.app.Application
import cl.duoc.reservasem.data.AppDatabase
import cl.duoc.reservasem.repository.DeportesRepository
import cl.duoc.reservasem.repository.PreferenciasRepository

class DeportesApplication : Application() {
    val database: AppDatabase by lazy {
        AppDatabase.obtenerInstancia(this)
    }

    val deportesRepository: DeportesRepository by lazy {
        DeportesRepository(database.appDao())
    }

    val preferenciasRepository: PreferenciasRepository by lazy {
        PreferenciasRepository(this)
    }
}
