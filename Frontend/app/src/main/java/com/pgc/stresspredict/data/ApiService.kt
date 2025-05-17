package com.pgc.stresspredict.data

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("auth/registro")
    fun registrar(@Body usuario: Usuario): Call<String>

    @POST("auth/login")
    fun login(@Body usuario: Usuario): Call<String>

    @POST("prediccion")
    fun predecir(@Body datos: Map<String, Any>): Call<String>
}
