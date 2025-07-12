package com.pgc.stresspredict.data.repository

import com.pgc.stresspredict.data.model.request.PerfilUsuarioUpdateRequest
import com.pgc.stresspredict.data.model.response.PerfilUsuarioResponse
import com.pgc.stresspredict.data.api.ApiService
import com.pgc.stresspredict.data.auth.SessionManager
import com.pgc.stresspredict.di.MainApiService
import com.pgc.stresspredict.util.NetworkResult
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    @MainApiService private val apiService: ApiService,
    private val sessionManager: SessionManager
) {
    companion object {
        private const val AUTH_ERROR = "Error de autenticación"
        private const val NETWORK_ERROR = "Error de conexión"
        private const val PROFILE_ERROR = "Error al obtener perfil"
    }

    suspend fun getUserProfile(): NetworkResult<PerfilUsuarioResponse> {
        if (!sessionManager.isLoggedIn()) {
            return NetworkResult.Error("Usuario no autenticado", 401)
        }

        return try {
            val response = apiService.getPerfil(sessionManager.getAuthHeader())

            when {
                response.isSuccessful && response.body() != null -> {
                    NetworkResult.Success(response.body()!!)
                }
                response.code() == 401 -> {
                    sessionManager.logout()
                    NetworkResult.Error("Sesión expirada", 401)
                }
                else -> {
                    NetworkResult.Error(
                        response.errorBody()?.string() ?: PROFILE_ERROR,
                        response.code()
                    )
                }
            }
        } catch (e: Exception) {
            handleException(e)
        }
    }

    suspend fun updateUserProfile(
        nombre: String,
        apellido: String,
        telefono: Int?,
        dni: Int?,
        fechaNacimiento: String
    ): NetworkResult<Unit> {
        if (!sessionManager.isLoggedIn()) {
            return NetworkResult.Error("Usuario no autenticado", 401)
        }

        return try {
            val request = PerfilUsuarioUpdateRequest(
                nombre = nombre.trim(),
                apellido = apellido.trim(),
                telefono = telefono,
                dni = dni,
                fechaNacimiento = fechaNacimiento
            )

            val response = apiService.updatePerfilUsuario(
                sessionManager.getAuthHeader(),
                request
            )

            when {
                response.isSuccessful -> NetworkResult.Success(Unit)
                response.code() == 401 -> {
                    sessionManager.logout()
                    NetworkResult.Error("Sesión expirada", 401)
                }
                else -> NetworkResult.Error(
                    response.errorBody()?.string() ?: "Error al actualizar perfil",
                    response.code()
                )
            }
        } catch (e: Exception) {
            handleException(e)
        }
    }

    fun logout(): NetworkResult<Unit> {
        return try {
            sessionManager.logout()
            NetworkResult.Success(Unit)
        } catch (e: Exception) {
            NetworkResult.Error("Error al cerrar sesión: ${e.message}")
        }
    }

    fun getCurrentEmail(): String? = sessionManager.getUserEmail()

    private fun handleException(e: Exception): NetworkResult.Error {
        return when (e) {
            is HttpException -> {
                if (e.code() == 401) sessionManager.logout()
                NetworkResult.Error(
                    e.response()?.errorBody()?.string() ?: AUTH_ERROR,
                    e.code()
                )
            }
            is IOException -> NetworkResult.Error(NETWORK_ERROR, null)
            else -> NetworkResult.Error(e.message ?: "Error desconocido", null)
        }
    }
}