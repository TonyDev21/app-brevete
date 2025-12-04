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
import java.util.*
import javax.inject.Inject
import android.util.Log

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
                
                val normalizedEmail = email.trim().lowercase()
                val user = userRepository.authenticateUser(normalizedEmail, password)
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
                
                // Debug: Verificar si el email ya existe
                val existingUser = userRepository.getUserByEmail(email)
                if (existingUser != null) {
                    _authState.value = AuthState.Error("El email ya está registrado")
                    return@launch
                }
                
                // Debug: Verificar si el DNI ya existe
                val existingDni = userRepository.getUserByDni(dni)
                if (existingDni != null) {
                    _authState.value = AuthState.Error("El DNI ya está registrado")
                    return@launch
                }
                
                // Debug: Crear nuevo usuario
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
                    role = role
                )
                
                // Debug: Intentar insertar usuario
                userRepository.insertUser(newUser)
                
                // Debug: Verificar que el usuario se insertó correctamente
                val insertedUser = userRepository.getUserByEmail(email.trim().lowercase())
                if (insertedUser != null) {
                    _currentUser.value = insertedUser
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error("Error: Usuario no se guardó correctamente")
                }
                
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Error al registrar: ${e.message ?: "Error desconocido"}")
            }
        }
    }
    
    fun logout() {
        _currentUser.value = null
        _authState.value = AuthState.Unauthenticated
    }
    
    fun updateCurrentUser(user: User) {
        Log.d("AuthViewModel", "Updating current user: firstName=${user.firstName}, lastName=${user.lastName}")
        _currentUser.value = user
    }
    
    fun refreshCurrentUser() {
        viewModelScope.launch {
            _currentUser.value?.let { user ->
                try {
                    val refreshedUser = userRepository.getUserById(user.id)
                    if (refreshedUser != null) {
                        _currentUser.value = refreshedUser
                    }
                } catch (e: Exception) {
                    // Si hay error, mantener el usuario actual
                }
            }
        }
    }
    
    fun clearError() {
        if (_authState.value is AuthState.Error) {
            _authState.value = AuthState.Unauthenticated
        }
    }
}
