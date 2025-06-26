package com.pgc.stresspredict.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pgc.stresspredict.data.api.ApiService
import com.pgc.stresspredict.data.RetrofitClient
import com.pgc.stresspredict.data.model.AuthLoginRequest
import com.pgc.stresspredict.data.model.AuthResponse
import com.pgc.stresspredict.data.model.UsuarioFormRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val apiService = RetrofitClient.instance.create(ApiService::class.java)

    // Estados para Registro
    private val _registrationState = MutableStateFlow<AuthState>(AuthState.Idle)
    val registrationState: StateFlow<AuthState> = _registrationState

    // Estados para Login
    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)
    val loginState: StateFlow<AuthState> = _loginState

    // Token de autenticación
    var authToken by mutableStateOf<String?>(null)
        private set

    /**
     * Maneja el registro de un nuevo usuario
     */
    fun registerUser(userData: UsuarioFormRequest) {
        viewModelScope.launch {
            _registrationState.value = AuthState.Loading
            try {
                val response = apiService.registrarUsuario(userData)
                if (response.isSuccessful && response.body()?.status == true) {
                    response.body()?.let { authResponse ->
                        authToken = authResponse.jwt
                        _registrationState.value = AuthState.Success(authResponse)
                    } ?: run {
                        _registrationState.value = AuthState.Error("Respuesta inválida del servidor")
                    }
                } else {
                    _registrationState.value = AuthState.Error(
                        response.body()?.message ?: "Error en el registro (Código ${response.code()})"
                    )
                }
            } catch (e: Exception) {
                _registrationState.value = AuthState.Error("Error de conexión: ${e.message ?: "Desconocido"}")
            }
        }
    }

    /**
     * Maneja el inicio de sesión de un usuario
     */
    fun loginUser(loginRequest: AuthLoginRequest) {
        viewModelScope.launch {
            _loginState.value = AuthState.Loading
            try {
                val response = apiService.iniciarSesion(loginRequest)
                if (response.isSuccessful && response.body()?.status == true) {
                    response.body()?.let { authResponse ->
                        authToken = authResponse.jwt
                        _loginState.value = AuthState.Success(authResponse)
                    } ?: run {
                        _loginState.value = AuthState.Error("Respuesta inválida del servidor")
                    }
                } else {
                    _loginState.value = AuthState.Error(
                        response.body()?.message ?: "Error en el login (Código ${response.code()})"
                    )
                }
            } catch (e: Exception) {
                _loginState.value = AuthState.Error("Error de conexión: ${e.message ?: "Desconocido"}")
            }
        }
    }

    /**
     * Limpia los estados de autenticación
     */
    fun resetAuthStates() {
        _registrationState.value = AuthState.Idle
        _loginState.value = AuthState.Idle
    }

    /**
     * Estados posibles para las operaciones de autenticación
     */
    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Success(val authResponse: AuthResponse) : AuthState()
        data class Error(val message: String) : AuthState()
    }
}