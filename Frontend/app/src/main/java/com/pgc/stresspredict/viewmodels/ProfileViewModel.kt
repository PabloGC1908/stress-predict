package com.pgc.stresspredict.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pgc.stresspredict.data.model.response.PerfilUsuarioResponse
import com.pgc.stresspredict.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class ProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    internal val _profileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val profileState: StateFlow<ProfileState> = _profileState

    internal val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    init {
        loadProfile()
        loadEmail()
    }

    private fun loadEmail() {
        _email.value = userRepository.getCurrentEmail() ?: ""
    }

    open fun loadProfile() {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            try {
                val profile = userRepository.getUserProfile()
                _profileState.value = ProfileState.Success(profile)
            } catch (e: Exception) {
                _profileState.value = ProfileState.Error(
                    message = e.message ?: "Error al cargar el perfil"
                )
            }
        }
    }

    fun updateProfile(
        nombre: String,
        apellido: String,
        telefono: Int,
        dni: Int,
        fechaNacimiento: String
    ) {
        viewModelScope.launch {
            try {
                val success = userRepository.updateUserProfile(
                    nombre = nombre,
                    apellido = apellido,
                    telefono = telefono,
                    dni = dni,
                    fechaNacimiento = fechaNacimiento
                )

                if (success) {
                    loadProfile() // Recargar los datos actualizados
                }
            } catch (e: Exception) {
                _profileState.value = ProfileState.Error(
                    message = "Error al actualizar: ${e.message}"
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

}

sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val profile: PerfilUsuarioResponse) : ProfileState()
    data class Error(val message: String) : ProfileState()
}