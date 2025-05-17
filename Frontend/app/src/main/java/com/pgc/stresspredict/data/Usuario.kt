package com.pgc.stresspredict.data

data class Usuario(
    val correo: String,
    val contrasena: String,
    val nombres: String? = null,
    val apellidos: String? = null,
    val codigo: String? = null
)
