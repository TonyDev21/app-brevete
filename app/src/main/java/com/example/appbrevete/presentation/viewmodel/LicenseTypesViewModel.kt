package com.example.appbrevete.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbrevete.domain.model.LicenseType
import com.example.appbrevete.domain.model.LicenseCategory
import com.example.appbrevete.domain.repository.LicenseTypeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LicenseTypesUiState(
    val licenseTypes: List<LicenseType> = emptyList(),
    val filteredLicenseTypes: List<LicenseType> = emptyList(),
    val selectedCategory: LicenseCategory? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class LicenseTypesViewModel @Inject constructor(
    private val licenseTypeRepository: LicenseTypeRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(LicenseTypesUiState())
    val uiState: StateFlow<LicenseTypesUiState> = _uiState.asStateFlow()
    
    fun loadLicenseTypes() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                
                licenseTypeRepository.getAllActiveLicenseTypes().collect { licenseTypes ->
                    _uiState.value = _uiState.value.copy(
                        licenseTypes = licenseTypes,
                        filteredLicenseTypes = licenseTypes,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al cargar tipos de licencia: ${e.message}"
                )
            }
        }
    }
    
    fun filterByCategory(category: LicenseCategory?) {
        val licenseTypes = _uiState.value.licenseTypes
        val filtered = if (category != null) {
            licenseTypes.filter { it.category == category }
        } else {
            licenseTypes
        }
        
        _uiState.value = _uiState.value.copy(
            selectedCategory = category,
            filteredLicenseTypes = filtered
        )
    }
    
    fun filterByAge(age: Int) {
        val licenseTypes = _uiState.value.licenseTypes
        val filtered = licenseTypes.filter { it.ageRequirement <= age }
        
        _uiState.value = _uiState.value.copy(
            filteredLicenseTypes = filtered
        )
    }
    
    fun getLicenseTypeById(id: String): LicenseType? {
        return _uiState.value.licenseTypes.find { it.id == id }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
