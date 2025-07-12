package com.pgc.stresspredict.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pgc.stresspredict.data.model.response.PerfilUsuarioResponse
import com.pgc.stresspredict.data.repository.UserRepository
import com.pgc.stresspredict.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            when (val result = userRepository.getUserProfile()) {
                is NetworkResult.Success -> {
                    _uiState.update {
                        ProfileUiState.Success(
                            profile = result.data,
                            email = userRepository.getCurrentEmail() ?: ""
                        )
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.value = ProfileUiState.Error(
                        message = result.message,
                        shouldLogout = result.code == 401
                    )
                    if (result.code == 401) {
                        logout(silent = true)
                    }
                }
            }
        }
    }

    fun updateProfile(
        nombre: String,
        apellido: String,
        telefono: Int?,
        dni: Int?,
        fechaNacimiento: String
    ) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                when (currentState) {
                    is ProfileUiState.Success -> currentState.copy(isUpdating = true)
                    else -> currentState
                }
            }

            when (val result = userRepository.updateUserProfile(
                nombre = nombre,
                apellido = apellido,
                telefono = telefono,
                dni = dni,
                fechaNacimiento = fechaNacimiento
            )) {
                is NetworkResult.Success -> {
                    loadProfile() // Refresh data after update
                }
                is NetworkResult.Error -> {
                    _uiState.update {
                        when (it) {
                            is ProfileUiState.Success -> it.copy(
                                isUpdating = false,
                                updateError = result.message
                            )
                            else -> ProfileUiState.Error(
                                message = result.message,
                                shouldLogout = result.code == 401
                            )
                        }
                    }
                    if (result.code == 401) {
                        logout(silent = true)
                    }
                }
            }
        }
    }

    fun logout(silent: Boolean = false) {
        viewModelScope.launch {
            userRepository.logout()
            if (!silent) {
                _uiState.value = ProfileUiState.LoggedOut
            } else {
                _uiState.value = ProfileUiState.Error(
                    message = "Sesión expirada",
                    shouldLogout = false
                )
            }
        }
    }
}

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(
        val profile: PerfilUsuarioResponse,
        val email: String,
        val isUpdating: Boolean = false,
        val updateError: String? = null
    ) : ProfileUiState()

    data class Error(
        val message: String,
        val shouldLogout: Boolean
    ) : ProfileUiState()

    object LoggedOut : ProfileUiState()
}