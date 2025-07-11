package com.pgc.stresspredict.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pgc.stresspredict.data.model.request.FormularioEstresRequest
import com.pgc.stresspredict.data.model.response.PrediccionEstresResponse
import com.pgc.stresspredict.data.repository.StressPredictionRepository
import com.pgc.stresspredict.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StressViewModel @Inject constructor(
    private val repository: StressPredictionRepository
) : ViewModel() {

    // Estados de la UI
    private val _predictionState = MutableStateFlow<PredictionState>(PredictionState.Idle)
    val predictionState: StateFlow<PredictionState> = _predictionState.asStateFlow()

    // Estado de validación del formulario
    private val _formValidation = MutableStateFlow<Map<StressFormField, Boolean>>(emptyMap())
    val formValidation: StateFlow<Map<StressFormField, Boolean>> = _formValidation.asStateFlow()

    // Datos del formulario
    private val _formData = MutableStateFlow(
        FormularioEstresRequest(
            horasEstudio = 0.0,
            horasExtracurriculares = 0.0,
            horasSueno = 0.0,
            horasSociales = 0.0,
            horasActividadFisica = 0.0,
            promedioCalificaciones = 0.0
        )
    )
    val formData: StateFlow<FormularioEstresRequest> = _formData.asStateFlow()

    // Actualización segura de campos con validación
    fun updateFormField(field: StressFormField, value: String) {
        val doubleValue = value.toDoubleOrNull() ?: 0.0

        _formData.value = _formData.value.copy(
            horasEstudio = if (field == StressFormField.STUDY_HOURS) doubleValue else _formData.value.horasEstudio,
            horasExtracurriculares = if (field == StressFormField.EXTRACURRICULAR_HOURS) doubleValue else _formData.value.horasExtracurriculares,
            horasSueno = if (field == StressFormField.SLEEP_HOURS) doubleValue else _formData.value.horasSueno,
            horasSociales = if (field == StressFormField.SOCIAL_HOURS) doubleValue else _formData.value.horasSociales,
            horasActividadFisica = if (field == StressFormField.PHYSICAL_ACTIVITY) doubleValue else _formData.value.horasActividadFisica,
            promedioCalificaciones = if (field == StressFormField.GPA) doubleValue else _formData.value.promedioCalificaciones
        )

        // Actualizar estado de validación
        _formValidation.value = _formValidation.value.toMutableMap().apply {
            this[field] = when (field) {
                StressFormField.STUDY_HOURS -> doubleValue > 0
                StressFormField.SLEEP_HOURS -> doubleValue > 0
                StressFormField.GPA -> doubleValue > 0
                else -> true // Los demás campos son opcionales
            }
        }
    }

    // Validar formulario completo
    private fun validateForm(): Boolean {
        return _formData.value.run {
            horasEstudio > 0 &&
                    horasSueno > 0 &&
                    promedioCalificaciones > 0
        }
    }

    // Disparar la predicción con validación
    fun predictStress() {
        if (!validateForm()) {
            _predictionState.value = PredictionState.Error(
                message = "Por favor complete todos los campos requeridos",
                code = 400
            )
            return
        }

        viewModelScope.launch {
            _predictionState.value = PredictionState.Loading
            when (val result = repository.predictStress(_formData.value)) {
                is NetworkResult.Success -> {
                    _predictionState.value = PredictionState.Success(result.data)
                }
                is NetworkResult.Error -> {
                    _predictionState.value = PredictionState.Error(
                        message = result.message ?: "Error desconocido",
                        code = result.code
                    )
                }
            }
        }
    }

    fun isFormValid(): Boolean {
        return _formData.value.run {
            horasEstudio > 0 &&
                    horasSueno > 0 &&
                    promedioCalificaciones in 0.0..20.0
        }
    }

    fun resetState() {
        _predictionState.value = PredictionState.Idle
    }
}

// Estados posibles
sealed class PredictionState {
    object Idle : PredictionState()
    object Loading : PredictionState()
    data class Success(val data: PrediccionEstresResponse) : PredictionState()
    data class Error(val message: String, val code: Int? = null) : PredictionState()
}

// Campos del formulario
enum class StressFormField {
    STUDY_HOURS,
    EXTRACURRICULAR_HOURS,
    SLEEP_HOURS,
    SOCIAL_HOURS,
    PHYSICAL_ACTIVITY,
    GPA
}