package com.pgc.stresspredict.di

import com.google.gson.GsonBuilder
import com.pgc.stresspredict.data.api.ApiService
import com.pgc.stresspredict.data.auth.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton
import javax.inject.Named // Añade este import




// Qualifiers para Retrofit
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SpringBootRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FastApiRetrofit

// Qualifiers para ApiService
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainApiService

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MLApiService

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    const val URL = "https://f7e25dc80996.ngrok-free.app/"

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            // Reemplaza BuildConfig.DEBUG con una constante temporal
            level = HttpLoggingInterceptor.Level.BODY // Cambia manualmente en producción
        }
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(sessionManager: SessionManager): Interceptor {
        return Interceptor { chain ->
            val request = chain.request().newBuilder().apply {
                sessionManager.getAuthToken()?.let { token ->
                    addHeader("Authorization", "Bearer $token")
                }
            }.build()
            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    @Named("MainClient") // Usa @Named para distinguir los clientes
    fun provideMainOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("MLClient") // Usa @Named para distinguir los clientes
    fun provideMLOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @SpringBootRetrofit
    fun provideSpringBootRetrofit(
        @Named("MainClient") client: OkHttpClient // Usa el cliente correcto
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(
                GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .serializeNulls()
                    .create()
            ))
            .build()
    }

    @Provides
    @Singleton
    @FastApiRetrofit
    fun provideFastApiRetrofit(
        @Named("MLClient") client: OkHttpClient // Usa el cliente correcto
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl(URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @MainApiService
    @Provides
    @Singleton
    fun provideMainApiService(@SpringBootRetrofit retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @MLApiService
    @Provides
    @Singleton
    fun provideMLApiService(@FastApiRetrofit retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}