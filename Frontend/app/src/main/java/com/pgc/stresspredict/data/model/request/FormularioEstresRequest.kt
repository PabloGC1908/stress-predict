package com.pgc.stresspredict.data.model.request
import com.google.gson.annotations.SerializedName

data class FormularioEstresRequest(
    @SerializedName("study_hours_per_day") val horasEstudio: Double,
    @SerializedName("extracurricular_hours_per_day") val horasExtracurriculares: Double,
    @SerializedName("sleep_hours_per_day") val horasSueno: Double,
    @SerializedName("social_hours_per_day") val horasSociales: Double,
    @SerializedName("physical_activity_hours_per_day") val horasActividadFisica: Double,
    @SerializedName("gpa") val promedioCalificaciones: Double
)
