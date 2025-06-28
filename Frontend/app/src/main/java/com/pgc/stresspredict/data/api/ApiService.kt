package com.pgc.stresspredict.data.api

import com.pgc.stresspredict.data.model.request.AuthLoginRequest
import com.pgc.stresspredict.data.model.request.PerfilUsuarioUpdateRequest
import com.pgc.stresspredict.data.model.response.AuthResponse
import com.pgc.stresspredict.data.model.request.UsuarioFormRequest
import com.pgc.stresspredict.data.model.response.PerfilUsuarioResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface ApiService {
    /**
     * Endpoint para registro de usuarios
     * @param usuario Datos del usuario a registrar
     * @return Respuesta con token JWT y datos del usuario
     */
    @POST("auth/sign-up")
    suspend fun registrarUsuario(
        @Body usuario: UsuarioFormRequest
    ): Response<AuthResponse>

    /**
     * Endpoint para inicio de sesión
     * @param loginRequest Credenciales de acceso (email y contraseña)
     * @return Respuesta con token JWT y datos del usuario
     */
    @POST("auth/log-in")
    suspend fun iniciarSesion(
        @Body loginRequest: AuthLoginRequest
    ): Response<AuthResponse>

    @GET("api/usuarios/perfil")
    suspend fun getPerfil(@Header("Authorization") token: String): PerfilUsuarioResponse

    @PATCH("api/usuarios/perfil")
    suspend fun updatePerfilUsuario(
        @Header("Authorization") token: String,
        @Body updateRequest: PerfilUsuarioUpdateRequest
    )
}