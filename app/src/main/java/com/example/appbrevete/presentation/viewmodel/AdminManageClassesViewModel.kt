package com.example.appbrevete.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbrevete.domain.model.*
import com.example.appbrevete.domain.repository.DrivingClassRepository
import com.example.appbrevete.presentation.admin.ClassFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdminManageClassesUiState(
    val classes: List<DrivingClass> = emptyList(),
    val totalClasses: Int = 0,
    val scheduledCount: Int = 0,
    val completedCount: Int = 0,
    val cancelledCount: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val updateSuccess: Boolean = false
)

@HiltViewModel
class AdminManageClassesViewModel @Inject constructor(
    private val drivingClassRepository: DrivingClassRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AdminManageClassesUiState())
    val uiState: StateFlow<AdminManageClassesUiState> = _uiState.asStateFlow()
    
    fun loadClasses(filter: ClassFilter = ClassFilter.ALL, type: DrivingPackageType? = null) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                
                drivingClassRepository.getAllClasses().collect { classes ->
                    val filteredClasses = classes
                        .filter { drivingClass ->
                            // Filter by status
                            when (filter) {
                                ClassFilter.ALL -> true
                                ClassFilter.SCHEDULED -> drivingClass.status == DrivingClassStatus.SCHEDULED
                                ClassFilter.COMPLETED -> drivingClass.status == DrivingClassStatus.COMPLETED
                                ClassFilter.CANCELLED -> drivingClass.status == DrivingClassStatus.CANCELLED
                            }
                        }
                        .filter { drivingClass ->
                            // Filter by type
                            type?.let { drivingClass.packageType == it } ?: true
                        }
                        .sortedByDescending { it.scheduledDate } // Most recent first
                    
                    _uiState.value = _uiState.value.copy(
                        classes = filteredClasses,
                        isLoading = false
                    )
                }
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al cargar clases: ${e.message}"
                )
            }
        }
    }
    
    fun updateClassStatus(classId: String, newStatus: DrivingClassStatus) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(errorMessage = null)
                
                val drivingClass = _uiState.value.classes.find { it.id == classId }
                if (drivingClass != null) {
                    val updatedClass = drivingClass.copy(
                        status = newStatus,
                        updatedAt = System.currentTimeMillis()
                    )
                    
                    drivingClassRepository.updateClass(updatedClass)
                    
                    // Update local state immediately for better UX
                    val updatedClasses = _uiState.value.classes.map { 
                        if (it.id == classId) updatedClass else it 
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        classes = updatedClasses,
                        updateSuccess = true
                    )
                    
                    // Clear success flag after a moment
                    kotlinx.coroutines.delay(2000)
                    _uiState.value = _uiState.value.copy(updateSuccess = false)
                }
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al actualizar clase: ${e.message}"
                )
            }
        }
    }
    
    fun completeDrivingClass(
        classId: String,
        completedHours: Int,
        notes: String?
    ) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(errorMessage = null)
                
                val drivingClass = _uiState.value.classes.find { it.id == classId }
                if (drivingClass != null) {
                    val updatedClass = drivingClass.copy(
                        status = DrivingClassStatus.COMPLETED,
                        completedHours = completedHours,
                        notes = notes,
                        updatedAt = System.currentTimeMillis()
                    )
                    
                    drivingClassRepository.updateClass(updatedClass)
                    
                    // Update local state
                    val updatedClasses = _uiState.value.classes.map { 
                        if (it.id == classId) updatedClass else it 
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        classes = updatedClasses,
                        updateSuccess = true
                    )
                    
                    // Clear success flag
                    kotlinx.coroutines.delay(2000)
                    _uiState.value = _uiState.value.copy(updateSuccess = false)
                }
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al completar clase: ${e.message}"
                )
            }
        }
    }
    
    fun loadAllStatistics() {
        viewModelScope.launch {
            try {
                drivingClassRepository.getAllClasses().collect { allClasses ->
                    _uiState.value = _uiState.value.copy(
                        totalClasses = allClasses.size,
                        scheduledCount = allClasses.count { it.status == DrivingClassStatus.SCHEDULED },
                        completedCount = allClasses.count { it.status == DrivingClassStatus.COMPLETED },
                        cancelledCount = allClasses.count { it.status == DrivingClassStatus.CANCELLED }
                    )
                }
            } catch (e: Exception) {
                // Mantener estadísticas en 0 si hay error
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}