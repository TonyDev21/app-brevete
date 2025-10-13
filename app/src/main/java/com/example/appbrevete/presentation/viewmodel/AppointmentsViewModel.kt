package com.example.appbrevete.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbrevete.domain.model.Appointment
import com.example.appbrevete.domain.model.AppointmentStatus
import com.example.appbrevete.domain.model.AppointmentType
import com.example.appbrevete.domain.repository.AppointmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class AppointmentsUiState(
    val appointments: List<Appointment> = emptyList(),
    val filteredAppointments: List<Appointment> = emptyList(),
    val selectedFilter: AppointmentStatus? = null,
    val selectedType: AppointmentType? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isCreatingAppointment: Boolean = false
)

@HiltViewModel
class AppointmentsViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AppointmentsUiState())
    val uiState: StateFlow<AppointmentsUiState> = _uiState.asStateFlow()
    
    fun loadAppointments(userId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                
                appointmentRepository.getAppointmentsByUser(userId).collect { appointments ->
                    _uiState.value = _uiState.value.copy(
                        appointments = appointments,
                        filteredAppointments = appointments,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al cargar citas: ${e.message}"
                )
            }
        }
    }
    
    fun filterByStatus(status: AppointmentStatus?) {
        val appointments = _uiState.value.appointments
        val filtered = if (status != null) {
            appointments.filter { it.status == status }
        } else {
            appointments
        }
        
        _uiState.value = _uiState.value.copy(
            selectedFilter = status,
            filteredAppointments = filtered
        )
    }
    
    fun filterByType(type: AppointmentType?) {
        val appointments = _uiState.value.appointments
        val filtered = if (type != null) {
            appointments.filter { it.type == type }
        } else {
            appointments
        }
        
        _uiState.value = _uiState.value.copy(
            selectedType = type,
            filteredAppointments = filtered
        )
    }
    
    fun createAppointment(
        userId: String,
        type: AppointmentType,
        scheduledDate: Long,
        scheduledTime: String,
        location: String,
        licenseTypeId: String? = null,
        notes: String? = null
    ) {
        viewModelScope.launch {
            try {
                println("AppointmentsViewModel: Starting appointment creation")
                _uiState.value = _uiState.value.copy(isCreatingAppointment = true, errorMessage = null)
                
                val newAppointment = Appointment(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    type = type,
                    licenseTypeId = licenseTypeId,
                    scheduledDate = scheduledDate,
                    scheduledTime = scheduledTime,
                    location = location,
                    status = AppointmentStatus.SCHEDULED,
                    notes = notes
                )
                
                println("AppointmentsViewModel: Inserting appointment: ${newAppointment.id}")
                appointmentRepository.insertAppointment(newAppointment)
                
                println("AppointmentsViewModel: Appointment created successfully")
                _uiState.value = _uiState.value.copy(
                    isCreatingAppointment = false,
                    errorMessage = null
                )
                
            } catch (e: Exception) {
                println("AppointmentsViewModel: Error creating appointment: ${e.message}")
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    isCreatingAppointment = false,
                    errorMessage = "Error al crear cita: ${e.message}"
                )
            }
        }
    }
    
    fun updateAppointmentStatus(appointmentId: String, status: AppointmentStatus) {
        viewModelScope.launch {
            try {
                appointmentRepository.updateAppointmentStatus(appointmentId, status)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al actualizar cita: ${e.message}"
                )
            }
        }
    }
    
    fun cancelAppointment(appointmentId: String) {
        viewModelScope.launch {
            try {
                appointmentRepository.updateAppointmentStatus(appointmentId, AppointmentStatus.CANCELLED)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al cancelar cita: ${e.message}"
                )
            }
        }
    }
    
    fun updateAppointment(appointment: Appointment) {
        viewModelScope.launch {
            try {
                println("AppointmentsViewModel: Updating appointment: ${appointment.id}")
                appointmentRepository.updateAppointment(appointment)
                println("AppointmentsViewModel: Appointment updated successfully")
                
                // CRUCIAL: Recargar las citas después de actualizar
                println("AppointmentsViewModel: Reloading appointments after update")
                loadAppointments(appointment.userId)
                
            } catch (e: Exception) {
                println("AppointmentsViewModel: Error updating appointment: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al actualizar cita: ${e.message}"
                )
            }
        }
    }
    
    fun deleteAppointment(appointment: Appointment) {
        viewModelScope.launch {
            try {
                println("AppointmentsViewModel: Deleting appointment: ${appointment.id}")
                appointmentRepository.deleteAppointment(appointment)
                println("AppointmentsViewModel: Appointment deleted successfully")
            } catch (e: Exception) {
                println("AppointmentsViewModel: Error deleting appointment: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al eliminar cita: ${e.message}"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
