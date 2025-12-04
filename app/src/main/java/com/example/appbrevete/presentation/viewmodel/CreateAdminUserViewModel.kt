package com.example.appbrevete.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbrevete.domain.model.User
import com.example.appbrevete.domain.model.UserRole
import com.example.appbrevete.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class CreateAdminUserUiState(
    val isLoading: Boolean = false,
    val isUserCreated: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class CreateAdminUserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CreateAdminUserUiState())
    val uiState: StateFlow<CreateAdminUserUiState> = _uiState.asStateFlow()
    
    fun createAdminUser(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        dni: String,
        phoneNumber: String,
        address: String,
        birthDate: String
    ) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                
                // Verificar si el email ya existe
                val existingUser = userRepository.getUserByEmail(email)
                if (existingUser != null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "El email ya está registrado"
                    )
                    return@launch
                }
                
                // Verificar si el DNI ya existe
                val existingDni = userRepository.getUserByDni(dni)
                if (existingDni != null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "El DNI ya está registrado"
                    )
                    return@launch
                }
                
                // Crear nuevo usuario con rol ADMIN
                val newUser = User(
                    id = UUID.randomUUID().toString(),
                    email = email.trim().lowercase(),
                    password = password,
                    firstName = firstName.trim(),
                    lastName = lastName.trim(),
                    dni = dni.trim(),
                    phoneNumber = phoneNumber.trim(),
                    address = address.trim(),
                    birthDate = birthDate.trim(),
                    role = UserRole.ADMIN
                )
                
                // Insertar usuario
                userRepository.insertUser(newUser)
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isUserCreated = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al crear usuario: ${e.message}"
                )
            }
        }
    }
}
