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
    companion object {
        private const val AUTH_ERROR = "Authentication required"
        private const val SERVER_ERROR = "Server error"
        private const val NETWORK_ERROR = "Network error"
        private const val UNKNOWN_ERROR = "Unknown error"
    }

    suspend fun predictStress(
        formData: FormularioEstresRequest
    ): NetworkResult<PrediccionEstresResponse> {
        return withContext(Dispatchers.IO) {
            try {
                // 1. Validate session and get token
                val token = sessionManager.getAuthToken()
                    ?: return@withContext NetworkResult.Error(
                        message = AUTH_ERROR,
                        code = 401
                    )

                // 2. Validate input data
                validateInputData(formData)

                // 3. Make API call
                val response = mlApiService.predecirEstres(
                    token = sessionManager.getAuthHeader(),
                    request = formData
                )

                // 4. Validate response
                if (response.isSuccessful) {
                    response.body()?.let { prediction ->
                        NetworkResult.Success(prediction)
                    } ?: NetworkResult.Error(
                        message = "Empty response body",
                        code = response.code()
                    )
                } else {
                    handleErrorResponse(response.code(), response.errorBody()?.toString())
                }
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    private fun validateInputData(formData: FormularioEstresRequest) {
        // Add your validation logic here
        // Example: require(formData.horasSueno > 0) { "Sleep hours must be positive" }
    }

    private fun handleErrorResponse(code: Int, errorBody: String?): NetworkResult.Error {
        return when (code) {
            401 -> {
                sessionManager.logout()
                NetworkResult.Error("Session expired", 401)
            }
            400 -> NetworkResult.Error("Invalid request: $errorBody", 400)
            500 -> NetworkResult.Error(SERVER_ERROR, 500)
            else -> NetworkResult.Error("Error $code: $errorBody", code)
        }
    }

    private fun handleException(e: Exception): NetworkResult.Error {
        return when (e) {
            is HttpException -> handleErrorResponse(e.code(), e.message())
            is IOException -> NetworkResult.Error(NETWORK_ERROR, null)
            is IllegalArgumentException -> NetworkResult.Error(e.message ?: "Invalid data", 400)
            else -> NetworkResult.Error(e.message ?: UNKNOWN_ERROR, null)
        }
    }
}