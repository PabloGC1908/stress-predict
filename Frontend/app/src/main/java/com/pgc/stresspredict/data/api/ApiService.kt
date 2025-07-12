package com.pgc.stresspredict.data.api

import com.pgc.stresspredict.data.model.request.*
import com.pgc.stresspredict.data.model.response.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("api/auth/sign-up")
    suspend fun registrarUsuario(
        @Body usuario: UsuarioFormRequest
    ): Response<AuthResponse>

    @POST("api/auth/log-in")
    suspend fun iniciarSesion(
        @Body loginRequest: AuthLoginRequest
    ): Response<AuthResponse>

    @GET("api/usuarios/perfil")
    suspend fun getPerfil(
        @Header("Authorization") token: String
    ): Response<PerfilUsuarioResponse>  // Añadido Response<>

    @PATCH("api/usuarios/perfil")
    suspend fun updatePerfilUsuario(
        @Header("Authorization") token: String,
        @Body updateRequest: PerfilUsuarioUpdateRequest
    ): Response<PerfilUsuarioResponse>  // Añadido tipo de retorno

    @POST("api/ml/predict")
    suspend fun predecirEstres(
        @Header("Authorization") token: String,
        @Body request: FormularioEstresRequest
    ): Response<PrediccionEstresResponse>
}