package com.pgc.stresspredict.data.model.response

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class PerfilUsuarioResponse(
    val nombre: String,
    val apellido: String,
    val telefono: Int,
    val dni: Int,
    val fechaNacimiento: String, // Formato esperado: "yyyy-MM-dd"
    val promedioHorasEstudioDia: Float,
    val promedioHorasExtracurricularDia: Float,
    val promedioHorasSuenoDia: Float,
    val promedioHorasSocialDia: Float,
    val promedioHorasActividadFisica: Float,
    val promedioCalificaciones: Float
) {
    fun calcularEdad(): Int {
        return try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val birthDate = LocalDate.parse(fechaNacimiento, formatter)
            val currentDate = LocalDate.now()
            java.time.Period.between(birthDate, currentDate).years
        } catch (e: Exception) {
            0 // Retorna 0 si hay error en el parseo
        }
    }

    fun nombreCompleto() = "$nombre $apellido"
}