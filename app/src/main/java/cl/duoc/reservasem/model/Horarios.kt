package cl.duoc.reservasem.model

data class GrupoTipoSolicitante(
    val idGrupo: String,
    val tituloGrupo: String,
    val descripcion: String,
    val tipos: List<String>
)

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

    /**
     * Clasificación separada por tipo de institución, estamento militar,
     * colegios/educación, fundaciones de beneficencia y convenios deportivos.
     */
    val gruposInstitucionSolicitante = listOf(
        GrupoTipoSolicitante(
            idGrupo = "MILITAR_EM",
            tituloGrupo = "Dotación Escuela Militar (Interno)",
            descripcion = "Cadetes, Oficiales, Suboficiales, Profesores y Ramas EM",
            tipos = listOf(
                "Cadete Escuela Militar",
                "Oficial Escuela Militar",
                "Profesor / Instructor Militar",
                "Suboficial / Cuadro Permanente",
                "Rama Deportiva / Seleccionado EM",
                "Personal Civil de Planta EM"
            )
        ),
        GrupoTipoSolicitante(
            idGrupo = "COLEGIO_EDUCACION",
            tituloGrupo = "Colegios, Universidades e Instituciones Educativas",
            descripcion = "Establecimientos escolares, liceos, Duoc UC y universidades en convenio",
            tipos = listOf(
                "Colegio / Establecimiento Escolar",
                "Universidad / Educación Superior (Duoc UC)",
                "Escuela Matriz FF.AA. Invitada",
                "Delegación Deportiva Estudiantil"
            )
        ),
        GrupoTipoSolicitante(
            idGrupo = "BENEFICENCIA_FUNDACION",
            tituloGrupo = "Beneficencia, Fundaciones y Corporaciones",
            descripcion = "Instituciones sin fines de lucro, fundaciones sociales y rehabilitación",
            tipos = listOf(
                "Fundación / Institución de Beneficencia",
                "Corporación de Rehabilitación / Inclusión",
                "Fundación Señoras del Ejército / Acción Social",
                "Agrupación Social / Comunitaria sin Fines de Lucro"
            )
        ),
        GrupoTipoSolicitante(
            idGrupo = "FEDERACION_EXTERNA",
            tituloGrupo = "Federaciones, Municipalidades y Entidades Públicas",
            descripcion = "Federaciones deportivas nacionales, clubes y convenios municipales",
            tipos = listOf(
                "Federación / Asociación Deportiva Nacional",
                "Municipalidad / Corporación Municipal de Deportes",
                "Club Deportivo en Convenio Institucional",
                "Comisión Organizadora de Torneo Oficial"
            )
        )
    )

    val tiposSolicitanteOficiales: List<String> =
        gruposInstitucionSolicitante.flatMap { it.tipos }

    fun obtenerGrupoPorId(idGrupo: String): GrupoTipoSolicitante {
        return gruposInstitucionSolicitante.find { it.idGrupo == idGrupo }
            ?: gruposInstitucionSolicitante.first()
    }

    fun detectarGrupoDeTipo(tipoSolicitante: String): GrupoTipoSolicitante {
        return gruposInstitucionSolicitante.find { grupo ->
            grupo.tipos.any { it.equals(tipoSolicitante, ignoreCase = true) || tipoSolicitante.contains(it, ignoreCase = true) }
        } ?: when {
            tipoSolicitante.contains("Colegio", ignoreCase = true) ||
                tipoSolicitante.contains("Universidad", ignoreCase = true) ||
                tipoSolicitante.contains("Escolar", ignoreCase = true) -> gruposInstitucionSolicitante[1]
            tipoSolicitante.contains("Beneficencia", ignoreCase = true) ||
                tipoSolicitante.contains("Fundación", ignoreCase = true) ||
                tipoSolicitante.contains("Fundacion", ignoreCase = true) -> gruposInstitucionSolicitante[2]
            tipoSolicitante.contains("Federación", ignoreCase = true) ||
                tipoSolicitante.contains("Municipal", ignoreCase = true) ||
                tipoSolicitante.contains("Club", ignoreCase = true) -> gruposInstitucionSolicitante[3]
            else -> gruposInstitucionSolicitante[0]
        }
    }

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
