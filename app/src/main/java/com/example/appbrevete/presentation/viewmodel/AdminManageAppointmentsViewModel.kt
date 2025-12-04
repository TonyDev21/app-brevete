package com.example.appbrevete.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbrevete.domain.model.*
import com.example.appbrevete.domain.repository.AppointmentRepository
import com.example.appbrevete.domain.repository.MedicalEvaluationRepository
import com.example.appbrevete.presentation.admin.AppointmentFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdminManageAppointmentsUiState(
    val appointments: List<Appointment> = emptyList(),
    val approvedAppointments: List<Appointment> = emptyList(),
    val notApprovedAppointments: List<Appointment> = emptyList(),
    val totalAppointments: Int = 0,
    val scheduledCount: Int = 0,
    val completedCount: Int = 0,
    val cancelledCount: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val updateSuccess: Boolean = false
)

@HiltViewModel
class AdminManageAppointmentsViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    private val medicalEvaluationRepository: MedicalEvaluationRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AdminManageAppointmentsUiState())
    val uiState: StateFlow<AdminManageAppointmentsUiState> = _uiState.asStateFlow()
    
    fun loadAppointments(filter: AppointmentFilter = AppointmentFilter.ALL, type: AppointmentType? = null) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                
                appointmentRepository.getAllAppointments().collect { appointments ->
                    val filteredAppointments = appointments
                        .filter { appointment ->
                            // Filter by status
                            when (filter) {
                                AppointmentFilter.ALL -> true
                                AppointmentFilter.SCHEDULED -> appointment.status == AppointmentStatus.SCHEDULED
                                AppointmentFilter.COMPLETED -> appointment.status == AppointmentStatus.COMPLETED
                                AppointmentFilter.CANCELLED -> appointment.status == AppointmentStatus.CANCELLED
                            }
                        }
                        .filter { appointment ->
                            // Filter by type
                            type?.let { appointment.type == it } ?: true
                        }
                        .sortedByDescending { it.scheduledDate } // Most recent first
                    
                    _uiState.value = _uiState.value.copy(
                        appointments = filteredAppointments,
                        isLoading = false
                    )
                    
                    // Si el filtro es COMPLETED, cargar evaluaciones para separar aprobados y no aprobados
                    if (filter == AppointmentFilter.COMPLETED) {
                        loadApprovedAndNotApprovedAppointments(filteredAppointments)
                    }
                }
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al cargar citas: ${e.message}"
                )
            }
        }
    }
    
    fun updateAppointmentStatus(appointmentId: String, newStatus: AppointmentStatus) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(errorMessage = null)
                
                val appointment = _uiState.value.appointments.find { it.id == appointmentId }
                if (appointment != null) {
                    val updatedAppointment = appointment.copy(
                        status = newStatus,
                        updatedAt = System.currentTimeMillis()
                    )
                    
                    appointmentRepository.updateAppointment(updatedAppointment)
                    
                    // Update local state immediately for better UX
                    val updatedAppointments = _uiState.value.appointments.map { 
                        if (it.id == appointmentId) updatedAppointment else it 
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        appointments = updatedAppointments,
                        updateSuccess = true
                    )
                    
                    // Clear success flag after a moment
                    kotlinx.coroutines.delay(2000)
                    _uiState.value = _uiState.value.copy(updateSuccess = false)
                }
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al actualizar cita: ${e.message}"
                )
            }
        }
    }
    
    private suspend fun loadApprovedAndNotApprovedAppointments(appointments: List<Appointment>) {
        val approved = mutableListOf<Appointment>()
        val notApproved = mutableListOf<Appointment>()
        
        try {
            for (appointment in appointments) {
                if (appointment.medicalEvaluationId != null) {
                    val evaluation = medicalEvaluationRepository
                        .getEvaluationById(appointment.medicalEvaluationId)
                    
                    if (evaluation != null) {
                        if (evaluation.resultadoFinal) {
                            approved.add(appointment)
                        } else {
                            notApproved.add(appointment)
                        }
                    }
                }
            }
            
            _uiState.value = _uiState.value.copy(
                approvedAppointments = approved,
                notApprovedAppointments = notApproved
            )
        } catch (e: Exception) {
            // En caso de error, mantener listas vacías
        }
    }
    
    fun getApprovedAndNotApprovedAppointments(): Pair<List<Appointment>, List<Appointment>> {
        return Pair(_uiState.value.approvedAppointments, _uiState.value.notApprovedAppointments)
    }
    
    fun loadAllStatistics() {
        viewModelScope.launch {
            try {
                appointmentRepository.getAllAppointments().collect { allAppointments ->
                    val medicalAppointments = allAppointments.filter { it.type == AppointmentType.MEDICAL_EXAM }
                    _uiState.value = _uiState.value.copy(
                        totalAppointments = medicalAppointments.size,
                        scheduledCount = medicalAppointments.count { it.status == AppointmentStatus.SCHEDULED },
                        completedCount = medicalAppointments.count { it.status == AppointmentStatus.COMPLETED },
                        cancelledCount = medicalAppointments.count { it.status == AppointmentStatus.CANCELLED }
                    )
                }
            } catch (e: Exception) {
                // Mantener estadísticas en 0 si hay error
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}