package cl.duoc.reservasem.model

import android.util.Patterns

object Validadores {

    fun validarNombre(nombre: String): String? {
        return when {
            nombre.trim().isEmpty() -> "El grado y nombre del solicitante es obligatorio"
            nombre.trim().length < 3 -> "Ingrese un nombre valido (minimo 3 caracteres)"
            else -> null
        }
    }

    fun validarCorreo(correo: String): String? {
        val limpio = correo.trim()
        return when {
            limpio.isEmpty() -> "El correo electronico institucional es obligatorio"
            !Patterns.EMAIL_ADDRESS.matcher(limpio).matches() ->
                "Formato de correo invalido (ej: cadete@escuelamilitar.cl)"
            else -> null
        }
    }

    fun validarClave(clave: String): String? {
        // Este es el "detector": Busca que exista al menos un numero o un simbolo especial (incluyendo el guion bajo)
        val contieneNumeroOSimbolo = ".*[0-9_!@#\$%^&*()\\-+=].*".toRegex()

        return when {
            clave.isEmpty() -> "Debe ingresar su contraseña de acceso"
            // Ahora exige ESTRICTAMENTE mas de 4 caracteres (minimo 5)
            clave.length <= 4 -> "La contraseña debe tener mas de 4 caracteres"
            // Si la clave NO hace match con el detector, tira error
            !clave.matches(contieneNumeroOSimbolo) -> "Debe incluir al menos un numero o caracter especial (ej: _)"
            else -> null
        }
    }

    fun validarMotivo(motivo: String): String? {
        return when {
            motivo.trim().isEmpty() -> "El motivo o actividad deportiva no puede quedar vacio"
            motivo.trim().length < 5 -> "Detalle brevemente la actividad (minimo 5 caracteres)"
            else -> null
        }
    }
}