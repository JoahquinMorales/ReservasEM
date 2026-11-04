package cl.duoc.reservasem.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migraciones {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `eventos` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `titulo` TEXT NOT NULL,
                    `disciplina` TEXT NOT NULL,
                    `recinto` TEXT NOT NULL,
                    `fecha` TEXT NOT NULL,
                    `organizador` TEXT NOT NULL,
                    `estado` TEXT NOT NULL
                )
                """.trimIndent()
            )
        }
    }
}
