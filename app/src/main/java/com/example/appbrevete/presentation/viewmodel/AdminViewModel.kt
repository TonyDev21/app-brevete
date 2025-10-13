package com.example.appbrevete.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbrevete.domain.model.User
import com.example.appbrevete.domain.model.UserRole
import com.example.appbrevete.domain.model.AppointmentStatus
import com.example.appbrevete.domain.repository.UserRepository
import com.example.appbrevete.domain.repository.AppointmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdminStats(
    val totalUsers: Int = 0,
    val studentCount: Int = 0,
    val instructorCount: Int = 0,
    val examinerCount: Int = 0,
    val doctorCount: Int = 0,
    val totalAppointments: Int = 0,
    val scheduledAppointments: Int = 0,
    val completedAppointments: Int = 0,
    val cancelledAppointments: Int = 0
)

data class AdminUiState(
    val stats: AdminStats = AdminStats(),
    val allUsers: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val appointmentRepository: AppointmentRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()
    
    fun loadAdminData() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                
                // Cargar estadísticas de usuarios
                val studentCount = userRepository.getUserCountByRole(UserRole.STUDENT)
                val instructorCount = userRepository.getUserCountByRole(UserRole.INSTRUCTOR)
                val examinerCount = userRepository.getUserCountByRole(UserRole.EXAMINER)
                val doctorCount = userRepository.getUserCountByRole(UserRole.MEDICAL_DOCTOR)
                
                // Cargar estadísticas de citas
                val scheduledCount = appointmentRepository.getAppointmentCountByStatus(AppointmentStatus.SCHEDULED)
                val completedCount = appointmentRepository.getAppointmentCountByStatus(AppointmentStatus.COMPLETED)
                val cancelledCount = appointmentRepository.getAppointmentCountByStatus(AppointmentStatus.CANCELLED)
                
                val stats = AdminStats(
                    totalUsers = studentCount + instructorCount + examinerCount + doctorCount,
                    studentCount = studentCount,
                    instructorCount = instructorCount,
                    examinerCount = examinerCount,
                    doctorCount = doctorCount,
                    totalAppointments = scheduledCount + completedCount + cancelledCount,
                    scheduledAppointments = scheduledCount,
                    completedAppointments = completedCount,
                    cancelledAppointments = cancelledCount
                )
                
                _uiState.value = _uiState.value.copy(
                    stats = stats,
                    isLoading = false
                )
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al cargar datos administrativos: ${e.message}"
                )
            }
        }
    }
    
    fun loadAllUsers() {
        viewModelScope.launch {
            try {
                userRepository.getAllActiveUsers().collect { users ->
                    _uiState.value = _uiState.value.copy(allUsers = users)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al cargar usuarios: ${e.message}"
                )
            }
        }
    }
    
    fun getUsersByRole(role: UserRole) {
        viewModelScope.launch {
            try {
                userRepository.getUsersByRole(role).collect { users ->
                    _uiState.value = _uiState.value.copy(allUsers = users)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al cargar usuarios por rol: ${e.message}"
                )
            }
        }
    }
    
    fun deactivateUser(userId: String) {
        viewModelScope.launch {
            try {
                userRepository.deactivateUser(userId)
                // Recargar datos después de la operación
                loadAdminData()
                loadAllUsers()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al desactivar usuario: ${e.message}"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
