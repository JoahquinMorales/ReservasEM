package cl.duoc.reservasem.model

object Horarios {
    val diasHabiles = listOf(
        "Lunes",
        "Martes",
        "Miércoles",
        "Jueves",
        "Viernes"
    )

    val bloquesOficiales = listOf(
        "08:00 - 09:30",
        "09:45 - 11:15",
        "11:30 - 13:00",
        "14:30 - 16:00",
        "16:15 - 17:45",
        "18:00 - 19:30",
        "19:00 - 20:30"
    )

    val tiposSolicitanteOficiales = listOf(
        "Cadete",
        "Profesor",
        "Oficial",
        "Rama Deportiva",
        "Seleccionado Institucional",
        "Instructor Militar",
        "Suboficial",
        "Personal Civil Planta",
        "Comisión Organizadora"
    )

    val categoriasRecintos = listOf(
        "Todas",
        "Cancha",
        "Gimnasio",
        "Piscina",
        "Pista",
        "Polígono",
        "Sala"
    )
}
