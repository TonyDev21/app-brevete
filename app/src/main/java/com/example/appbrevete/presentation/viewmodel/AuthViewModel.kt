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

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                
                val user = userRepository.authenticateUser(email, password)
                if (user != null) {
                    _currentUser.value = user
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error("Email o contraseña incorrectos")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Error de conexión: ${e.message}")
            }
        }
    }
    
    fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        dni: String,
        phoneNumber: String,
        address: String,
        birthDate: String,
        role: UserRole = UserRole.STUDENT
    ) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                
                // Verificar si el email ya existe
                val existingUser = userRepository.getUserByEmail(email)
                if (existingUser != null) {
                    _authState.value = AuthState.Error("El email ya está registrado")
                    return@launch
                }
                
                // Verificar si el DNI ya existe
                val existingDni = userRepository.getUserByDni(dni)
                if (existingDni != null) {
                    _authState.value = AuthState.Error("El DNI ya está registrado")
                    return@launch
                }
                
                // Crear nuevo usuario
                val newUser = User(
                    id = UUID.randomUUID().toString(),
                    email = email,
                    password = password, // En producción, esto debería ser hasheado
                    firstName = firstName,
                    lastName = lastName,
                    dni = dni,
                    phoneNumber = phoneNumber,
                    address = address,
                    birthDate = birthDate,
                    role = role
                )
                
                userRepository.insertUser(newUser)
                _currentUser.value = newUser
                _authState.value = AuthState.Authenticated
                
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Error al registrar: ${e.message}")
            }
        }
    }
    
    fun logout() {
        _currentUser.value = null
        _authState.value = AuthState.Unauthenticated
    }
    
    fun clearError() {
        if (_authState.value is AuthState.Error) {
            _authState.value = AuthState.Unauthenticated
        }
    }
}
