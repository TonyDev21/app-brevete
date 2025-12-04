package com.example.appbrevete.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbrevete.domain.model.DrivingClass
import com.example.appbrevete.domain.model.DrivingClassStatus
import com.example.appbrevete.domain.model.DrivingPackageType
import com.example.appbrevete.domain.model.VehicleType
import com.example.appbrevete.domain.repository.DrivingClassRepository
import com.example.appbrevete.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class DrivingClassUiState(
    val classes: List<DrivingClass> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isCreatingClass: Boolean = false
)

@HiltViewModel
class DrivingClassViewModel @Inject constructor(
    private val drivingClassRepository: DrivingClassRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(DrivingClassUiState())
    val uiState: StateFlow<DrivingClassUiState> = _uiState.asStateFlow()
    
    // Job para manejar la carga de clases
    private var currentLoadJob: Job? = null
    
    private val _selectedClass = MutableStateFlow<DrivingClass?>(null)
    val selectedClass: StateFlow<DrivingClass?> = _selectedClass.asStateFlow()
    
    fun loadClasses(userId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                
                // Cancelar el job anterior si existe
                currentLoadJob?.cancel()
                
                currentLoadJob = viewModelScope.launch {
                    drivingClassRepository.getClassesByStudent(userId).collect { classes ->
                        _uiState.value = _uiState.value.copy(
                            classes = classes,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al cargar las clases: ${e.message}"
                )
            }
        }
    }
    
    fun createDrivingClass(
        userId: String,
        packageType: String,
        date: String,
        time: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isCreatingClass = true, errorMessage = null)
                
                // Obtener datos del usuario
                val user = userRepository.getUserById(userId)
                val userName = user?.let { "${it.firstName} ${it.lastName}" }
                val userDni = user?.dni
                
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val parsedDate = dateFormat.parse(date)
                val scheduledDate = parsedDate?.time ?: System.currentTimeMillis()
                
                val drivingPackageType = when (packageType) {
                    "2h" -> DrivingPackageType.BASIC_2H
                    "4h" -> DrivingPackageType.STANDARD_4H
                    else -> DrivingPackageType.CUSTOM
                }
                
                val totalHours = when (packageType) {
                    "2h" -> 2
                    "4h" -> 4
                    else -> 1
                }
                
                val newClass = DrivingClass(
                    id = UUID.randomUUID().toString(),
                    studentId = userId,
                    instructorId = "instructor_1",
                    instructorName = "Instructor Profesional",
                    packageType = drivingPackageType,
                    totalHours = totalHours,
                    completedHours = 0,
                    scheduledDate = scheduledDate,
                    scheduledTime = time,
                    status = DrivingClassStatus.SCHEDULED,
                    location = "Centro de Clases de Manejo - Lima",
                    vehicleType = VehicleType.CAR_MANUAL,
                    notes = null,
                    userName = userName,
                    userDni = userDni
                )
                
                drivingClassRepository.insertClass(newClass)
                _uiState.value = _uiState.value.copy(isCreatingClass = false)
                onSuccess()
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isCreatingClass = false,
                    errorMessage = "Error al crear la clase: ${e.message}"
                )
                onError(e.message ?: "Error desconocido")
            }
        }
    }
    
    fun deleteClass(classId: String) {
        viewModelScope.launch {
            try {
                drivingClassRepository.deleteClassById(classId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al eliminar la clase: ${e.message}"
                )
            }
        }
    }
    
    fun updateClassStatus(classId: String, status: DrivingClassStatus) {
        viewModelScope.launch {
            try {
                val statusString = when (status) {
                    DrivingClassStatus.SCHEDULED -> "scheduled"
                    DrivingClassStatus.CONFIRMED -> "confirmed"
                    DrivingClassStatus.IN_PROGRESS -> "in_progress"
                    DrivingClassStatus.COMPLETED -> "completed"
                    DrivingClassStatus.CANCELLED -> "cancelled"
                    DrivingClassStatus.RESCHEDULED -> "rescheduled"
                }
                drivingClassRepository.updateClassStatus(classId, statusString)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al actualizar el estado: ${e.message}"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
    
    fun loadClassById(classId: String, onLoaded: (DrivingClass) -> Unit) {
        viewModelScope.launch {
            try {
                val drivingClass = drivingClassRepository.getClassById(classId)
                drivingClass?.let { 
                    _selectedClass.value = it
                    onLoaded(it) 
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al cargar la clase: ${e.message}"
                )
            }
        }
    }
    
    fun updateDrivingClass(
        classId: String,
        packageType: String,
        date: String,
        time: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isCreatingClass = true, errorMessage = null)
                
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val parsedDate = dateFormat.parse(date)
                val scheduledDate = parsedDate?.time ?: System.currentTimeMillis()
                
                val totalHours = when (packageType) {
                    "2h" -> 2
                    "4h" -> 4
                    else -> 1
                }
                
                // Actualizar la clase existente
                drivingClassRepository.updateClass(
                    classId = classId,
                    packageType = packageType,
                    totalHours = totalHours,
                    scheduledDate = scheduledDate,
                    scheduledTime = time
                )
                
                // Recargar las clases para reflejar los cambios
                val currentUserId = _uiState.value.classes.firstOrNull()?.studentId
                if (currentUserId != null) {
                    loadClasses(currentUserId)
                }
                
                _uiState.value = _uiState.value.copy(isCreatingClass = false)
                onSuccess()
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isCreatingClass = false,
                    errorMessage = e.message
                )
                onError(e.message ?: "Error desconocido")
            }
        }
    }
    
    fun updateDrivingClassStatus(classId: String, status: DrivingClassStatus) {
        viewModelScope.launch {
            try {
                val statusString = when (status) {
                    DrivingClassStatus.SCHEDULED -> "scheduled"
                    DrivingClassStatus.CONFIRMED -> "confirmed"
                    DrivingClassStatus.IN_PROGRESS -> "in_progress"
                    DrivingClassStatus.COMPLETED -> "completed"
                    DrivingClassStatus.CANCELLED -> "cancelled"
                    DrivingClassStatus.RESCHEDULED -> "rescheduled"
                }
                drivingClassRepository.updateClassStatus(classId, statusString)
                
                // Recargar las clases para reflejar el cambio
                _uiState.value.classes.firstOrNull()?.studentId?.let { userId ->
                    loadClasses(userId)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al actualizar el estado: ${e.message}"
                )
            }
        }
    }
    
    fun deleteDrivingClass(classId: String) {
        viewModelScope.launch {
            try {
                drivingClassRepository.deleteClassById(classId)
                
                // Recargar las clases para reflejar los cambios
                val currentUserId = _uiState.value.classes.firstOrNull()?.studentId
                if (currentUserId != null) {
                    loadClasses(currentUserId)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al eliminar la clase: ${e.message}"
                )
            }
        }
    }
}