package cl.duoc.reservasem.data

import cl.duoc.reservasem.model.CanchaModel
import cl.duoc.reservasem.model.EquipoModel
import cl.duoc.reservasem.model.EventoModel
import cl.duoc.reservasem.model.ReservaModel
import cl.duoc.reservasem.model.UsuarioModel

object DatosDemostracion {

    val usuariosIniciales = listOf(
        UsuarioModel(
            id = 1,
            correo = "admin@escuelamilitar.cl",
            contrasena = "admin_2026",
            nombre = "Mayor (R) Jefe de Deportes EM",
            rol = "ADMIN",
            tipoSolicitante = "Oficial"
        ),
        UsuarioModel(
            id = 2,
            correo = "cadete@escuelamilitar.cl",
            contrasena = "cadete_1234",
            nombre = "Cadete 3er Año Ignacio Rojas",
            rol = "USUARIO",
            tipoSolicitante = "Cadete"
        ),
        UsuarioModel(
            id = 3,
            correo = "profesor.atletismo@escuelamilitar.cl",
            contrasena = "profe_1234",
            nombre = "Prof. Carlos Valdivia (Rama Atletismo)",
            rol = "USUARIO",
            tipoSolicitante = "Profesor"
        )
    )

    // Las 14 Instalaciones Deportivas Oficiales de la Escuela Militar de Chile
    val canchasOficiales = listOf(
        CanchaModel(
            id = 1,
            nombre = "Cancha de Fútbol Principal (Estadio Militar)",
            categoria = "Cancha",
            capacidad = 44,
            enMantencion = false,
            motivoMantencion = ""
        ),
        CanchaModel(
            id = 2,
            nombre = "Cancha de Rugby Institucional",
            categoria = "Cancha",
            capacidad = 36,
            enMantencion = false,
            motivoMantencion = ""
        ),
        CanchaModel(
            id = 3,
            nombre = "Pista Atlética Sintética 400m",
            categoria = "Pista",
            capacidad = 80,
            enMantencion = false,
            motivoMantencion = ""
        ),
        CanchaModel(
            id = 4,
            nombre = "Piscina Olímpica Temperada",
            categoria = "Piscina",
            capacidad = 50,
            enMantencion = true,
            motivoMantencion = "Tratamiento químico programado y mantención de calderas"
        ),
        CanchaModel(
            id = 5,
            nombre = "Gimnasio Polideportivo Central",
            categoria = "Gimnasio",
            capacidad = 120,
            enMantencion = false,
            motivoMantencion = ""
        ),
        CanchaModel(
            id = 6,
            nombre = "Sala de Musculación y Fuerza Táctica",
            categoria = "Gimnasio",
            capacidad = 35,
            enMantencion = false,
            motivoMantencion = ""
        ),
        CanchaModel(
            id = 7,
            nombre = "Canchas de Tenis Arcilla (Sector Norte)",
            categoria = "Cancha",
            capacidad = 12,
            enMantencion = false,
            motivoMantencion = ""
        ),
        CanchaModel(
            id = 8,
            nombre = "Sala de Esgrima y Combate Cuerpo a Cuerpo",
            categoria = "Sala",
            capacidad = 28,
            enMantencion = false,
            motivoMantencion = ""
        ),
        CanchaModel(
            id = 9,
            nombre = "Polígono de Tiro Deportivo Cubierto",
            categoria = "Polígono",
            capacidad = 20,
            enMantencion = false,
            motivoMantencion = ""
        ),
        CanchaModel(
            id = 10,
            nombre = "Picadero y Cancha Ecuestre",
            categoria = "Cancha",
            capacidad = 30,
            enMantencion = false,
            motivoMantencion = ""
        ),
        CanchaModel(
            id = 11,
            nombre = "Cancha de Básquetbol y Vóleibol Exterior",
            categoria = "Cancha",
            capacidad = 24,
            enMantencion = false,
            motivoMantencion = ""
        ),
        CanchaModel(
            id = 12,
            nombre = "Pista de Obstáculos de Pentatlón Militar",
            categoria = "Pista",
            capacidad = 40,
            enMantencion = true,
            motivoMantencion = "Inspección estructural de obstáculos de cuerda y foso"
        ),
        CanchaModel(
            id = 13,
            nombre = "Cancha Sintética de Futbolito N° 1",
            categoria = "Cancha",
            capacidad = 18,
            enMantencion = false,
            motivoMantencion = ""
        ),
        CanchaModel(
            id = 14,
            nombre = "Cancha Sintética de Futbolito N° 2",
            categoria = "Cancha",
            capacidad = 18,
            enMantencion = false,
            motivoMantencion = ""
        )
    )

    val reservasIniciales = listOf(
        ReservaModel(
            id = 1,
            canchaId = 1,
            nombreCancha = "Cancha de Fútbol Principal (Estadio Militar)",
            correoUsuario = "cadete@escuelamilitar.cl",
            nombreSolicitante = "Cadete 3er Año Ignacio Rojas",
            tipoSolicitante = "Cadete",
            diaSemana = "Martes",
            bloqueHorario = "16:15 - 17:45",
            motivoActividad = "Entrenamiento Selección de Fútbol Inter-Escuelas",
            estado = "APROBADA",
            motivoRechazo = "",
            tieneCredencial = true
        ),
        ReservaModel(
            id = 2,
            canchaId = 5,
            nombreCancha = "Gimnasio Polideportivo Central",
            correoUsuario = "cadete@escuelamilitar.cl",
            nombreSolicitante = "Cadete 3er Año Ignacio Rojas",
            tipoSolicitante = "Rama Deportiva",
            diaSemana = "Jueves",
            bloqueHorario = "18:00 - 19:30",
            motivoActividad = "Preparación física de compañía y básquetbol",
            estado = "PENDIENTE",
            motivoRechazo = "",
            tieneCredencial = true
        ),
        ReservaModel(
            id = 3,
            canchaId = 9,
            nombreCancha = "Polígono de Tiro Deportivo Cubierto",
            correoUsuario = "profesor.atletismo@escuelamilitar.cl",
            nombreSolicitante = "Prof. Carlos Valdivia",
            tipoSolicitante = "Profesor",
            diaSemana = "Viernes",
            bloqueHorario = "09:45 - 11:15",
            motivoActividad = "Práctica selectiva de Pentatlón Militar",
            estado = "RECHAZADA",
            motivoRechazo = "Prioridad de instrucción reglamentaria de 4to año",
            tieneCredencial = false
        )
    )

    val eventosIniciales = listOf(
        EventoModel(
            id = 1,
            titulo = "Torneo Inter-Escuelas Matrices",
            disciplina = "Pentatlón Militar y Atletismo",
            recinto = "Pista Atlética Sintética 400m",
            fecha = "Viernes 15 Nov • 08:30 hrs",
            organizador = "Jefatura de Deportes EM",
            estado = "CONFIRMADO"
        ),
        EventoModel(
            id = 2,
            titulo = "Campeonato Interno Inter-Compañías",
            disciplina = "Fútbol y Rugby",
            recinto = "Cancha de Fútbol Principal (Estadio Militar)",
            fecha = "Miércoles 20 Nov • 16:15 hrs",
            organizador = "1ra Compañía de Cadetes",
            estado = "INSCRIPCIONES ABIERTAS"
        )
    )

    val equiposIniciales = listOf(
        EquipoModel(
            id = 1,
            nombreEquipo = "Escuadra Cóndores 1ra Cía",
            compania = "1ra Compañía",
            capitan = "Cadete Ignacio Rojas",
            disciplina = "Fútbol",
            integrantes = 16
        ),
        EquipoModel(
            id = 2,
            nombreEquipo = "Rama Pentatlón EM",
            compania = "Selección Institucional",
            capitan = "Subalférez Martín Soto",
            disciplina = "Pentatlón Militar",
            integrantes = 12
        )
    )
}