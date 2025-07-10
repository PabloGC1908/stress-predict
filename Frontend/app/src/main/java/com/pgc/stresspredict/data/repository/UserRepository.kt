package com.pgc.stresspredict.data.repository

import com.pgc.stresspredict.data.model.request.PerfilUsuarioUpdateRequest
import com.pgc.stresspredict.data.model.response.PerfilUsuarioResponse
import com.pgc.stresspredict.data.api.ApiService
import com.pgc.stresspredict.data.auth.SessionManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {

    suspend fun getUserProfile(): PerfilUsuarioResponse {
        return try {
            apiService.getPerfil(sessionManager.getAuthHeader())
        } catch (e: retrofit2.HttpException) {
            if (e.code() == 401) {
                sessionManager.logout()  // Limpia sesión si el token falla
            }
            throw Exception("Error de autenticación: ${e.message()}")
        }
    }

    suspend fun updateUserProfile(
        nombre: String,
        apellido: String,
        telefono: Int?,
        dni: Int?,
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
}