package cl.duoc.reservasem.ui.screens

import cl.duoc.reservasem.model.ReservaModel

data class EstadoBloqueHorario(
    val ocupado: Boolean,
    val descripcion: String
)

fun obtenerEstadoBloque(
    reservas: List<ReservaModel>,
    dia: String,
    bloque: String
): EstadoBloqueHorario {
    val coincidencia = reservas.firstOrNull {
        it.diaSemana.equals(dia, ignoreCase = true) &&
            it.bloqueHorario == bloque &&
            it.estado != "RECHAZADA"
    }
    return if (coincidencia != null) {
        EstadoBloqueHorario(
            ocupado = true,
            descripcion = "${coincidencia.nombreCancha} (${coincidencia.nombreSolicitante})"
        )
    } else {
        EstadoBloqueHorario(
            ocupado = false,
            descripcion = "Bloque disponible para solicitud de reserva"
        )
    }
}
