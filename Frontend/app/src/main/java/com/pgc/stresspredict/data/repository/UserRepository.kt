package com.pgc.stresspredict.data.repository

import android.content.Context
import com.pgc.stresspredict.data.model.request.PerfilUsuarioUpdateRequest
import com.pgc.stresspredict.data.model.response.PerfilUsuarioResponse
import com.pgc.stresspredict.data.api.ApiService
import com.pgc.stresspredict.data.auth.SessionManager

class UserRepository(
    private val apiService: ApiService,
    private val context: Context
) {
    private val sessionManager by lazy { SessionManager(context) }

    suspend fun getUserProfile(): PerfilUsuarioResponse {
        val token = sessionManager.getAuthToken()
            ?: throw Exception("Usuario no autenticado")
        return apiService.getPerfil("Bearer $token")
    }

    suspend fun updateUserProfile(
        nombre: String,
        apellido: String,
        telefono: Int,
        dni: Int,
        fechaNacimiento: String
    ): Boolean {
        return try {
            val token = sessionManager.getAuthToken()
                ?: throw Exception("Usuario no autenticado")

            val request = PerfilUsuarioUpdateRequest(
                nombre = nombre,
                apellido = apellido,
                telefono = telefono,
                dni = dni,
                fechaNacimiento = fechaNacimiento
            )

            apiService.updatePerfilUsuario("Bearer $token", request)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun logout() {
        sessionManager.logout()
    }

    fun getCurrentEmail(): String? {
        return sessionManager.getUserEmail()
    }

    companion object
}