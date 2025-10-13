package com.example.appbrevete.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbrevete.domain.model.User
import com.example.appbrevete.domain.repository.UserRepository
import com.example.appbrevete.domain.repository.AppointmentRepository
import com.example.appbrevete.domain.model.AppointmentStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val user: User? = null,
    val totalAppointments: Int = 0,
    val completedAppointments: Int = 0,
    val isLoading: Boolean = false,
    val isUpdating: Boolean = false,
    val updateSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val appointmentRepository: AppointmentRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    
    fun loadUserProfile(userId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                
                val user = userRepository.getUserById(userId)
                
                // Cargar estadísticas de citas del usuario
                appointmentRepository.getAppointmentsByUser(userId).collect { appointments ->
                    val totalAppointments = appointments.size
                    val completedAppointments = appointments.count { it.status == AppointmentStatus.COMPLETED }
                    
                    _uiState.value = _uiState.value.copy(
                        user = user,
                        totalAppointments = totalAppointments,
                        completedAppointments = completedAppointments,
                        isLoading = false
                    )
                }
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al cargar perfil: ${e.message}"
                )
            }
        }
    }
    
    fun updateProfile(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        address: String
    ) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isUpdating = true, errorMessage = null, updateSuccess = false)
                
                val currentUser = _uiState.value.user
                if (currentUser != null) {
                    val updatedUser = currentUser.copy(
                        firstName = firstName,
                        lastName = lastName,
                        phoneNumber = phoneNumber,
                        address = address,
                        updatedAt = System.currentTimeMillis()
                    )
                    
                    userRepository.updateUser(updatedUser)
                    
                    _uiState.value = _uiState.value.copy(
                        user = updatedUser,
                        isUpdating = false,
                        updateSuccess = true
                    )
                }
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isUpdating = false,
                    errorMessage = "Error al actualizar perfil: ${e.message}"
                )
            }
        }
    }
    
    fun changePassword(currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isUpdating = true, errorMessage = null, updateSuccess = false)
                
                val currentUser = _uiState.value.user
                if (currentUser != null) {
                    // Verificar contraseña actual
                    if (currentUser.password != currentPassword) {
                        _uiState.value = _uiState.value.copy(
                            isUpdating = false,
                            errorMessage = "Contraseña actual incorrecta"
                        )
                        return@launch
                    }
                    
                    val updatedUser = currentUser.copy(
                        password = newPassword, // En producción, esto debería ser hasheado
                        updatedAt = System.currentTimeMillis()
                    )
                    
                    userRepository.updateUser(updatedUser)
                    
                    _uiState.value = _uiState.value.copy(
                        user = updatedUser,
                        isUpdating = false,
                        updateSuccess = true
                    )
                }
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isUpdating = false,
                    errorMessage = "Error al cambiar contraseña: ${e.message}"
                )
            }
        }
    }
    
    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            updateSuccess = false
        )
    }
}
