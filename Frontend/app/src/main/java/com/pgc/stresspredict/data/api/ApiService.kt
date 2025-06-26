package com.pgc.stresspredict.data.api

import com.pgc.stresspredict.data.model.AuthLoginRequest
import com.pgc.stresspredict.data.model.AuthResponse
import com.pgc.stresspredict.data.model.UsuarioFormRequest
import retrofit2.Response
import retrofit2.http.Body
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

    // Puedes añadir otros endpoints aquí según necesites
}