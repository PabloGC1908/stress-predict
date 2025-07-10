package com.pgc.stresspredict.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pgc.stresspredict.data.model.response.PerfilUsuarioResponse
import com.pgc.stresspredict.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _updateState = MutableStateFlow<UpdateState>(UpdateState.Idle)
    val updateState: StateFlow<UpdateState> = _updateState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        loadEmail()
        loadProfile()
    }

    private fun loadEmail() {
        _email.value = userRepository.getCurrentEmail() ?: ""
    }

    fun loadProfile() {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            try {
                val profile = userRepository.getUserProfile()
                _profileState.value = ProfileState.Success(profile)
            } catch (e: Exception) {
                _profileState.value = ProfileState.Error(
                    message = e.message ?: "Error al cargar el perfil"
                )
                // Si es error de autenticación, podrías limpiar el estado aquí
                if (e.message?.contains("autenticado", ignoreCase = true) == true) {
                    logout()
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
            _updateState.value = UpdateState.Loading
            try {
                val success = userRepository.updateUserProfile(
                    nombre = nombre,
                    apellido = apellido,
                    telefono = telefono,
                    dni = dni,
                    fechaNacimiento = fechaNacimiento
                )

                _updateState.value = if (success) {
                    loadProfile() // Recargar los datos actualizados
                    UpdateState.Success
                } else {
                    UpdateState.Error("Error desconocido al actualizar")
                }
            } catch (e: Exception) {
                _updateState.value = UpdateState.Error(
                    message = "Error al actualizar: ${e.message}"
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
            // Resetear estados después del logout
            _profileState.value = ProfileState.Loading
            _email.value = ""
            _updateState.value = UpdateState.Idle
        }
    }
}

sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val profile: PerfilUsuarioResponse) : ProfileState()
    data class Error(val message: String) : ProfileState()
}

sealed class UpdateState {
    object Idle : UpdateState()
    object Loading : UpdateState()
    object Success : UpdateState()
    data class Error(val message: String) : UpdateState()
}