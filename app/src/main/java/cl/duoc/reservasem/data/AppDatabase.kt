package cl.duoc.reservasem.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cl.duoc.reservasem.model.CanchaModel
import cl.duoc.reservasem.model.EquipoModel
import cl.duoc.reservasem.model.EventoModel
import cl.duoc.reservasem.model.ReservaModel
import cl.duoc.reservasem.model.UsuarioModel

@Database(
    entities = [
        UsuarioModel::class,
        CanchaModel::class,
        ReservaModel::class,
        EventoModel::class,
        EquipoModel::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun obtenerInstancia(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "escuela_militar_deportes_db"
                )
                    .addMigrations(Migraciones.MIGRATION_1_2)
                    .fallbackToDestructiveMigration(true)
                    .build()
                    .also { INSTANCE = it }
            }
        }

        fun obtenerBaseDatos(context: Context): AppDatabase = obtenerInstancia(context)
    }
}
