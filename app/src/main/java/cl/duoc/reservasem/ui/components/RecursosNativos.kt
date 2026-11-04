package cl.duoc.reservasem.ui.components

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import cl.duoc.reservasem.model.ReservaModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import java.util.Calendar

fun agendarEnGoogleCalendar(
    context: Context,
    reserva: ReservaModel,
    esAdmin: Boolean
) {
    val (inicioMillis, finMillis) = calcularRangoHorarioReservaMillis(
        diaSemana = reserva.diaSemana,
        bloqueHorario = reserva.bloqueHorario
    )

    val prefijoTitulo = if (esAdmin) "[CONTROL EM]" else "[RESERVA EM]"
    val tituloEvento = "$prefijoTitulo ${reserva.nombreCancha} - ${reserva.motivoActividad}"

    val descripcionCompleta = buildString {
        appendLine("• Recinto Deportivo: ${reserva.nombreCancha}")
        appendLine("• Solicitante: ${reserva.nombreSolicitante} (${reserva.correoUsuario})")
        appendLine("• Estamento / Perfil: ${reserva.tipoSolicitante}")
        appendLine("• Día y Bloque: ${reserva.diaSemana} | ${reserva.bloqueHorario}")
        appendLine("• Motivo / Actividad: ${reserva.motivoActividad}")
        appendLine("• Estado Oficial: ${reserva.estado}")
        appendLine("• Credencial Verificada: ${if (reserva.tieneCredencial) "Sí" else "Pendiente"}")
        if (reserva.motivoRechazo.isNotBlank()) {
            appendLine("• Observación Mando: ${reserva.motivoRechazo}")
        }
    }

    val intent = Intent(Intent.ACTION_INSERT).apply {
        data = CalendarContract.Events.CONTENT_URI
        putExtra(CalendarContract.Events.TITLE, tituloEvento)
        putExtra(CalendarContract.Events.DESCRIPTION, descripcionCompleta)
        putExtra(
            CalendarContract.Events.EVENT_LOCATION,
            "Escuela Militar del Libertador Bernardo O'Higgins - ${reserva.nombreCancha}"
        )
        putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, inicioMillis)
        putExtra(CalendarContract.EXTRA_EVENT_END_TIME, finMillis)
    }

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(
            context,
            "Abriendo calendario para: $tituloEvento",
            Toast.LENGTH_LONG
        ).show()
    }
}

private fun calcularRangoHorarioReservaMillis(
    diaSemana: String,
    bloqueHorario: String
): Pair<Long, Long> {
    val calendar = Calendar.getInstance()
    val targetDayOfWeek = when (diaSemana.lowercase()) {
        "lunes" -> Calendar.MONDAY
        "martes" -> Calendar.TUESDAY
        "miércoles", "miercoles" -> Calendar.WEDNESDAY
        "jueves" -> Calendar.THURSDAY
        "viernes" -> Calendar.FRIDAY
        else -> Calendar.MONDAY
    }

    while (calendar.get(Calendar.DAY_OF_WEEK) != targetDayOfWeek) {
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    val partes = bloqueHorario.split("-").map { it.trim() }
    val inicioPartes = partes.getOrNull(0)?.split(":") ?: listOf("08", "00")
    val finPartes = partes.getOrNull(1)?.split(":") ?: listOf("09", "30")

    val calInicio = (calendar.clone() as Calendar).apply {
        set(Calendar.HOUR_OF_DAY, inicioPartes.getOrNull(0)?.toIntOrNull() ?: 8)
        set(Calendar.MINUTE, inicioPartes.getOrNull(1)?.toIntOrNull() ?: 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    val calFin = (calendar.clone() as Calendar).apply {
        set(Calendar.HOUR_OF_DAY, finPartes.getOrNull(0)?.toIntOrNull() ?: 9)
        set(Calendar.MINUTE, finPartes.getOrNull(1)?.toIntOrNull() ?: 30)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    return calInicio.timeInMillis to calFin.timeInMillis
}

suspend fun iniciarFlujoGoogleSignIn(
    context: Context,
    onCredencialObtenida: (correo: String, nombre: String) -> Unit,
    onRequiereFallbackEvaluador: () -> Unit
) {
    try {
        val credentialManager = CredentialManager.create(context)
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("100000000000-escuelamilitardemo.apps.googleusercontent.com")
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = credentialManager.getCredential(
            request = request,
            context = context
        )
        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
        val email = googleIdTokenCredential.id
        val displayName = googleIdTokenCredential.displayName ?: email.substringBefore("@")
        onCredencialObtenida(email, displayName)
    } catch (e: Exception) {
        onRequiereFallbackEvaluador()
    }
}
