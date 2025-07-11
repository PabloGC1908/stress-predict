package com.pgc.stresspredict.data.repository

import com.pgc.stresspredict.data.api.ApiService
import com.pgc.stresspredict.data.auth.SessionManager
import com.pgc.stresspredict.data.model.request.FormularioEstresRequest
import com.pgc.stresspredict.data.model.response.PrediccionEstresResponse
import com.pgc.stresspredict.di.MLApiService
import com.pgc.stresspredict.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StressPredictionRepository @Inject constructor(
    @MLApiService private val mlApiService: ApiService,
    private val sessionManager: SessionManager
) {

    suspend fun predictStress(
        formData: FormularioEstresRequest
    ): NetworkResult<PrediccionEstresResponse> {
        return withContext(Dispatchers.IO) {
            try {
                // 1. Obtener token seguro
                val token = sessionManager.getAuthToken()
                    ?: return@withContext NetworkResult.Error(
                        message = "Usuario no autenticado",
                        code = 401
                    )

                // 2. Llamar al modelo de IA
                val prediction = mlApiService.predecirEstres(
                    token = "Bearer $token",
                    request = formData
                )

                // 3. Retornar resultado exitoso
                NetworkResult.Success(prediction)

            } catch (e: HttpException) {
                // Errores HTTP (4xx/5xx)
                NetworkResult.Error(
                    message = "Error en el servidor: ${e.message()}",
                    code = e.code()
                )
            } catch (e: IOException) {
                // Fallos de red
                NetworkResult.Error(
                    message = "Error de conexión. Verifica tu internet",
                    code = null
                )
            } catch (e: Exception) {
                // Otros errores inesperados
                NetworkResult.Error(
                    message = "Error inesperado: ${e.message ?: "Intenta nuevamente"}",
                    code = null
                )
            } as NetworkResult.Error
        }
    }
}