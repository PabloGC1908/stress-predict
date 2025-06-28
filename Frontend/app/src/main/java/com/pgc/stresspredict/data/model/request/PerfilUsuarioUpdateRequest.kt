package com.pgc.stresspredict.data.model.request

data class PerfilUsuarioUpdateRequest(
    val nombre: String,
    val apellido: String,
    val telefono: Int,
    val dni: Int,
    val fechaNacimiento: String
)