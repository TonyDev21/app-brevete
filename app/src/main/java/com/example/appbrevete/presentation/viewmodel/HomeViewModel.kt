package com.example.appbrevete.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbrevete.domain.model.Appointment
import com.example.appbrevete.domain.model.AppointmentStatus
import com.example.appbrevete.domain.model.User
import com.example.appbrevete.domain.repository.AppointmentRepository
import com.example.appbrevete.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val upcomingAppointments: List<Appointment> = emptyList(),
    val totalAppointments: Int = 0,
    val completedAppointments: Int = 0,
    val pendingAppointments: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    fun loadHomeData(userId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                
                // Cargar citas próximas
                appointmentRepository.getUpcomingAppointments(userId, System.currentTimeMillis())
                    .collect { appointments ->
                        _uiState.value = _uiState.value.copy(
                            upcomingAppointments = appointments,
                            isLoading = false
                        )
                    }
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al cargar datos: ${e.message}"
                )
            }
        }
    }
    
    fun loadUserData(userId: String) {
        loadHomeData(userId)
        loadAppointmentStats(userId)
    }
    
    fun loadAppointmentStats(userId: String) {
        viewModelScope.launch {
            try {
                // Cargar estadísticas de citas
                appointmentRepository.getAppointmentsByUser(userId).collect { appointments ->
                    val completed = appointments.count { it.status == AppointmentStatus.COMPLETED }
                    val pending = appointments.count { 
                        it.status == AppointmentStatus.SCHEDULED || it.status == AppointmentStatus.CONFIRMED 
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        totalAppointments = appointments.size,
                        completedAppointments = completed,
                        pendingAppointments = pending
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al cargar estadísticas: ${e.message}"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
