package com.pgc.stresspredict.data

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://192.168.18.7:8081/api/"
    private const val CONNECT_TIMEOUT = 15L
    private const val READ_TIMEOUT = 15L

    // Interceptor para logs (solo en debug)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (/* BuildConfig.DEBUG */ true) { // Cambia a false en producción
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    // Cliente HTTP con configuración
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        .build()

    // Conversor Gson con configuración personalizada
    private val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") // Formato de fecha compatible
        .create()

    // Instancia de Retrofit
    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // Función helper para obtener el servicio API
    inline fun <reified T> createService(): T = instance.create(T::class.java)
}