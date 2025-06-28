package com.pgc.stresspredict.data.model.request

data class UsuarioFormRequest(
    val nombre: String,
    val apellido: String,
    val dni: Int?,
    val fechaNacimiento: String?, // Usamos String en lugar de LocalDate
    val telefono: String?,
    val email: String,
    val contrasenia: String,
    val horasEstudioDia: Float?,
    val horasExtracurricularDia: Float?,
    val horasSuenoDia: Float?,
    val horasSocialDia: Float?,
    val horasActividadFisicaDia: Float?,
    val promedioCalificaciones: Float?
)