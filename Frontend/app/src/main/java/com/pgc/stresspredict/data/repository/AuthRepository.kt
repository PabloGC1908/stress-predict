package com.pgc.stresspredict.data.repository

import com.pgc.stresspredict.data.api.ApiService
import com.pgc.stresspredict.data.auth.SessionManager
import com.pgc.stresspredict.data.model.request.AuthLoginRequest
import com.pgc.stresspredict.data.model.request.UsuarioFormRequest
import com.pgc.stresspredict.data.model.response.AuthResponse
import com.pgc.stresspredict.di.MainApiService
import com.pgc.stresspredict.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    @MainApiService private val apiService: ApiService,
    private val sessionManager: SessionManager
) {
    suspend fun registerUser(
        userData: UsuarioFormRequest
    ): NetworkResult<AuthResponse> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = apiService.registrarUsuario(userData)
            handleAuthResponse(response)
        } catch (e: Exception) {
            NetworkResult.Error(
                message = "Error de conexión: ${e.message}",
                code = null
            )
        }
    }

    suspend fun loginUser(
        loginRequest: AuthLoginRequest
    ): NetworkResult<AuthResponse> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = apiService.iniciarSesion(loginRequest)
            val result = handleAuthResponse(response)

            if (result is NetworkResult.Success) {
                sessionManager.saveAuthData(
                    token = result.data.jwt,
                    email = loginRequest.email
                )
            }

            result
        } catch (e: Exception) {
            NetworkResult.Error(
                message = "Error de conexión: ${e.message}",
                code = null
            )
        }
    }

    suspend fun isUserLoggedIn(): Boolean = withContext(Dispatchers.IO) {
        return@withContext sessionManager.isLoggedIn()
    }

    suspend fun logout() = withContext(Dispatchers.IO) {
        sessionManager.logout()
    }

    private fun handleAuthResponse(response: Response<AuthResponse>): NetworkResult<AuthResponse> {
        return when {
            response.isSuccessful && response.body() != null -> {
                NetworkResult.Success(response.body()!!)
            }
            response.errorBody() != null -> {
                NetworkResult.Error(
                    message = response.errorBody()!!.string(),
                    code = response.code()
                )
            }
            else -> {
                NetworkResult.Error(
                    message = "Respuesta inesperada del servidor",
                    code = response.code()
                )
            }
        }
    }
}