package com.example.appbrevete.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbrevete.domain.model.Appointment
import com.example.appbrevete.domain.model.AppointmentStatus
import com.example.appbrevete.domain.model.User
import com.example.appbrevete.domain.model.DrivingClass
import com.example.appbrevete.domain.model.DrivingClassStatus
import com.example.appbrevete.domain.repository.AppointmentRepository
import com.example.appbrevete.domain.repository.UserRepository
import com.example.appbrevete.domain.repository.DrivingClassRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val upcomingAppointments: List<Appointment> = emptyList(),
    val upcomingClasses: List<DrivingClass> = emptyList(),
    val totalAppointments: Int = 0,
    val completedAppointments: Int = 0,
    val pendingAppointments: Int = 0,
    val totalClasses: Int = 0,
    val completedClasses: Int = 0,
    val pendingClasses: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    private val userRepository: UserRepository,
    private val drivingClassRepository: DrivingClassRepository
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
        // Limpiar datos antes de cargar
        _uiState.value = HomeUiState()
        
        loadHomeData(userId)
        loadDrivingClasses(userId)
        loadAppointmentStats(userId)
        loadClassStats(userId)
    }
    
    fun loadDrivingClasses(userId: String) {
        viewModelScope.launch {
            try {
                // Cargar clases próximas (programadas y confirmadas)
                drivingClassRepository.getClassesByStudent(userId).collect { classes ->
                    val upcomingClasses = classes.filter { 
                        it.status == DrivingClassStatus.SCHEDULED || 
                        it.status == DrivingClassStatus.CONFIRMED 
                    }.sortedBy { it.scheduledDate }
                    
                    _uiState.value = _uiState.value.copy(
                        upcomingClasses = upcomingClasses
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al cargar clases: ${e.message}"
                )
            }
        }
    }
    
    fun loadAppointmentStats(userId: String) {
        viewModelScope.launch {
            try {
                // Cargar estadísticas de citas (excluyendo canceladas)
                appointmentRepository.getAppointmentsByUser(userId).collect { appointments ->
                    val activeAppointments = appointments.filter { it.status != AppointmentStatus.CANCELLED }
                    val completed = activeAppointments.count { it.status == AppointmentStatus.COMPLETED }
                    val pending = activeAppointments.count { 
                        it.status == AppointmentStatus.SCHEDULED || it.status == AppointmentStatus.CONFIRMED 
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        totalAppointments = activeAppointments.size,
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
    
    fun loadClassStats(userId: String) {
        viewModelScope.launch {
            try {
                // Cargar estadísticas de clases de manejo - solo clases activas
                drivingClassRepository.getClassesByStudent(userId).collect { classes ->
                    val completed = classes.count { it.status == DrivingClassStatus.COMPLETED }
                    val pending = classes.count { 
                        it.status == DrivingClassStatus.SCHEDULED || 
                        it.status == DrivingClassStatus.CONFIRMED ||
                        it.status == DrivingClassStatus.IN_PROGRESS
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        totalClasses = classes.size,
                        completedClasses = completed,
                        pendingClasses = pending
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al cargar estadísticas de clases: ${e.message}"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
