package com.example.appbrevete.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbrevete.domain.model.User
import com.example.appbrevete.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditProfileUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState = _uiState.asStateFlow()
    
    fun updateProfile(user: User, authViewModel: AuthViewModel) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, isSuccess = false) }
            
            try {
                // Actualizar en la base de datos
                userRepository.updateUser(user)
                
                // Verificar que se actualizó correctamente
                val updatedUser = userRepository.getUserById(user.id)
                if (updatedUser != null) {
                    // Actualizar el usuario actual en AuthViewModel
                    authViewModel.updateCurrentUser(updatedUser)
                    
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            isSuccess = true, 
                            errorMessage = null
                        ) 
                    }
                } else {
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            isSuccess = false, 
                            errorMessage = "Error: No se pudo verificar la actualización"
                        ) 
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        isSuccess = false, 
                        errorMessage = e.message ?: "Error al actualizar el perfil"
                    ) 
                }
            }
        }
    }
    
    fun clearState() {
        _uiState.update { EditProfileUiState() }
    }
}