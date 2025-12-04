package com.example.appbrevete.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbrevete.domain.model.Appointment
import com.example.appbrevete.domain.model.EvaluationResult
import com.example.appbrevete.domain.model.MedicalEvaluation
import com.example.appbrevete.domain.repository.AppointmentRepository
import com.example.appbrevete.domain.repository.MedicalEvaluationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class MedicalEvaluationUiState(
    val appointment: Appointment? = null,
    val evaluation: MedicalEvaluation? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class MedicalEvaluationViewModel @Inject constructor(
    private val medicalEvaluationRepository: MedicalEvaluationRepository,
    private val appointmentRepository: AppointmentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MedicalEvaluationUiState())
    val uiState: StateFlow<MedicalEvaluationUiState> = _uiState.asStateFlow()

    fun loadAppointment(appointmentId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                appointmentRepository.getAllAppointments().collect { appointments ->
                    val appointment = appointments.find { it.id == appointmentId }
                    
                    // Si la cita tiene evaluación, cargarla
                    var evaluation: MedicalEvaluation? = null
                    if (appointment?.medicalEvaluationId != null) {
                        evaluation = medicalEvaluationRepository.getEvaluationById(appointment.medicalEvaluationId)
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        appointment = appointment,
                        evaluation = evaluation,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al cargar cita: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun saveEvaluation(
        appointmentId: String,
        medicinaGeneral: EvaluationResult,
        vistaYOido: EvaluationResult,
        grupoSanguineo: String,
        evaluacionPsicologica: EvaluationResult,
        examenRazonamiento: EvaluationResult,
        observaciones: String,
        resultadoFinal: Boolean
    ) {
        viewModelScope.launch {
            try {
                // Obtener userId del appointment
                val userId = _uiState.value.appointment?.userId ?: ""
                
                // Crear la evaluación médica
                val evaluation = MedicalEvaluation(
                    id = UUID.randomUUID().toString(),
                    appointmentId = appointmentId,
                    userId = userId,
                    medicinaGeneral = medicinaGeneral,
                    vistaYOido = vistaYOido,
                    grupoSanguineo = grupoSanguineo,
                    evaluacionPsicologica = evaluacionPsicologica,
                    examenRazonamiento = examenRazonamiento,
                    observaciones = observaciones,
                    resultadoFinal = resultadoFinal,
                    evaluatorId = "admin", // TODO: Obtener el ID del admin actual
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )

                // Guardar la evaluación
                medicalEvaluationRepository.insertEvaluation(evaluation)

                // Actualizar el estado de la cita a COMPLETED
                _uiState.value.appointment?.let { appointment ->
                    val updatedAppointment = appointment.copy(
                        status = com.example.appbrevete.domain.model.AppointmentStatus.COMPLETED,
                        medicalEvaluationId = evaluation.id
                    )
                    appointmentRepository.updateAppointment(updatedAppointment)
                }

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al guardar evaluación: ${e.message}"
                )
            }
        }
    }
}
