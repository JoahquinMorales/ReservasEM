package cl.duoc.reservasem.model

import android.util.Patterns

/**
 * IE 2.2.1: Centralización y desacoplamiento total de la lógica de validación de formularios.
 * Todas las pantallas y ViewModels consumen este objeto único para garantizar
 * consistencia en las reglas de negocio y en los mensajes de retroalimentación visual.
 */
object Validadores {

    fun validarNombre(nombre: String): String? {
        val limpio = nombre.trim()
        return when {
            limpio.isEmpty() -> "El grado y nombre del solicitante es obligatorio"
            limpio.length < 3 -> "Ingrese un nombre válido (mínimo 3 caracteres)"
            else -> null
        }
    }

    fun validarCorreo(correo: String): String? {
        val limpio = correo.trim()
        return when {
            limpio.isEmpty() -> "El correo electrónico institucional es obligatorio"
            !Patterns.EMAIL_ADDRESS.matcher(limpio).matches() ->
                "Formato de correo inválido (ej: cadete@escuelamilitar.cl)"
            else -> null
        }
    }

    fun validarClave(clave: String): String? {
        val contieneNumeroOSimbolo = ".*[0-9_!@#\$%^&*()\\-+=].*".toRegex()

        return when {
            clave.isEmpty() -> "Debe ingresar su contraseña de acceso"
            clave.length <= 4 -> "La contraseña debe tener más de 4 caracteres (mínimo 5)"
            !clave.matches(contieneNumeroOSimbolo) -> "Debe incluir al menos un número o carácter especial (ej: _)"
            else -> null
        }
    }

    fun validarMotivo(motivo: String): String? {
        val limpio = motivo.trim()
        return when {
            limpio.isEmpty() -> "El motivo o actividad deportiva no puede quedar vacío"
            limpio.length < 5 -> "Detalle brevemente la actividad (mínimo 5 caracteres)"
            else -> null
        }
    }

    fun validarMotivoRechazo(motivo: String): String? {
        val limpio = motivo.trim()
        return when {
            limpio.isEmpty() -> "Debe indicar el motivo oficial del rechazo"
            limpio.length < 5 -> "Especifique un motivo válido (mínimo 5 caracteres)"
            else -> null
        }
    }

    fun validarMotivoMantencion(enMantencion: Boolean, motivo: String): String? {
        if (!enMantencion) return null
        val limpio = motivo.trim()
        return when {
            limpio.isEmpty() -> "Debe especificar el motivo técnico para bloquear el recinto"
            limpio.length < 5 -> "Detalle la falla o trabajo técnico (mínimo 5 caracteres)"
            else -> null
        }
    }

    fun validarTituloEvento(titulo: String): String? {
        val limpio = titulo.trim()
        return when {
            limpio.isEmpty() -> "El título del torneo o competencia es obligatorio"
            limpio.length < 4 -> "El título debe tener al menos 4 caracteres"
            else -> null
        }
    }

    fun validarDisciplina(disciplina: String): String? {
        val limpio = disciplina.trim()
        return when {
            limpio.isEmpty() -> "La disciplina deportiva es obligatoria"
            limpio.length < 3 -> "Ingrese una disciplina válida (mínimo 3 caracteres)"
            else -> null
        }
    }

    fun validarRecinto(recinto: String): String? {
        val limpio = recinto.trim()
        return when {
            limpio.isEmpty() -> "Debe indicar el recinto deportivo o compañía asignada"
            limpio.length < 3 -> "Especifique un recinto válido (mínimo 3 caracteres)"
            else -> null
        }
    }

    fun validarFechaHora(fecha: String): String? {
        val limpio = fecha.trim()
        return when {
            limpio.isEmpty() -> "La fecha y hora programada son obligatorias"
            limpio.length < 5 -> "Indique día y hora (ej: Viernes 16:00 hrs)"
            else -> null
        }
    }

    fun validarOrganizador(organizador: String): String? {
        val limpio = organizador.trim()
        return when {
            limpio.isEmpty() -> "La unidad organizadora es obligatoria"
            limpio.length < 3 -> "Ingrese una unidad organizadora válida"
            else -> null
        }
    }

    fun validarNombreEquipo(nombre: String): String? {
        val limpio = nombre.trim()
        return when {
            limpio.isEmpty() -> "Ingrese el nombre oficial de la escuadra"
            limpio.length < 3 -> "El nombre de la escuadra debe tener al menos 3 caracteres"
            else -> null
        }
    }

    fun validarCompania(compania: String): String? {
        val limpio = compania.trim()
        return when {
            limpio.isEmpty() -> "La compañía o unidad es obligatoria"
            limpio.length < 3 -> "Ingrese una compañía o unidad válida"
            else -> null
        }
    }

    fun validarIntegrantes(cantidadTexto: String): String? {
        val limpio = cantidadTexto.trim()
        val numero = limpio.toIntOrNull()
        return when {
            limpio.isEmpty() -> "Indique la dotación de integrantes"
            numero == null -> "Ingrese un número entero válido"
            numero < 2 -> "La escuadra debe contar con al menos 2 integrantes"
            numero > 50 -> "La dotación máxima permitida por escuadra es de 50 integrantes"
            else -> null
        }
    }

    fun validarTerminos(acepta: Boolean): String? {
        return if (!acepta) "Debe aceptar el reglamento deportivo de la Escuela Militar" else null
    }

    fun validarEntidadInstitucion(entidad: String): String? {
        val limpio = entidad.trim()
        return when {
            limpio.isEmpty() -> "Indique el nombre de la Institución, Colegio, Fundación o Unidad"
            limpio.length < 3 -> "El nombre de la entidad debe tener al menos 3 caracteres"
            else -> null
        }
    }
}
