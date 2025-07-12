package com.pgc.stresspredict.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pgc.stresspredict.data.model.request.AuthLoginRequest
import com.pgc.stresspredict.data.model.request.UsuarioFormRequest
import com.pgc.stresspredict.data.model.response.AuthResponse
import com.pgc.stresspredict.data.repository.AuthRepository
import com.pgc.stresspredict.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    // Registration State
    private val _registrationState = MutableStateFlow<AuthState>(AuthState.Idle)
    val registrationState: StateFlow<AuthState> = _registrationState

    // Login State
    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)
    val loginState: StateFlow<AuthState> = _loginState

    fun registerUser(userData: UsuarioFormRequest) {
        viewModelScope.launch {
            _registrationState.value = AuthState.Loading
            when (val result = authRepository.registerUser(userData)) {
                is NetworkResult.Success -> {
                    _registrationState.value = AuthState.Success(result.data)
                }
                is NetworkResult.Error -> {
                    _registrationState.value = AuthState.Error(
                        message = result.message,
                        code = result.code
                    )
                }
            }
        }
    }

    fun loginUser(loginRequest: AuthLoginRequest) {
        viewModelScope.launch {
            _loginState.value = AuthState.Loading
            when (val result = authRepository.loginUser(loginRequest)) {
                is NetworkResult.Success -> {
                    _loginState.value = AuthState.Success(result.data)
                }
                is NetworkResult.Error -> {
                    _loginState.value = AuthState.Error(
                        message = result.message,
                        code = result.code
                    )
                }
            }
        }
    }

    suspend fun isLoggedIn(): Boolean = authRepository.isUserLoggedIn()

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Success(val authResponse: AuthResponse) : AuthState()
        data class Error(val message: String, val code: Int? = null) : AuthState()
    }
}