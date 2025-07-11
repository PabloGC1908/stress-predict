package com.pgc.stresspredict.data.api

import com.pgc.stresspredict.data.model.request.AuthLoginRequest
import com.pgc.stresspredict.data.model.request.PerfilUsuarioUpdateRequest
import com.pgc.stresspredict.data.model.response.AuthResponse
import com.pgc.stresspredict.data.model.request.UsuarioFormRequest
import com.pgc.stresspredict.data.model.response.PerfilUsuarioResponse
import com.pgc.stresspredict.data.model.request.FormularioEstresRequest
import com.pgc.stresspredict.data.model.response.PrediccionEstresResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

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
    suspend fun getPerfil(@Header("Authorization") token: String): PerfilUsuarioResponse

    @PATCH("api/usuarios/perfil")
    suspend fun updatePerfilUsuario(
        @Header("Authorization") token: String,
        @Body updateRequest: PerfilUsuarioUpdateRequest
    )

    @POST("api/ml/predict")
    suspend fun predecirEstres(
        @Header("Authorization") token: String,
        @Body request: FormularioEstresRequest
    ): Response<PrediccionEstresResponse>

}