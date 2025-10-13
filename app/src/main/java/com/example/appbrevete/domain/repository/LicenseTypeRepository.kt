package com.example.appbrevete.domain.repository

import com.example.appbrevete.domain.model.LicenseType
import com.example.appbrevete.domain.model.LicenseCategory
import kotlinx.coroutines.flow.Flow

interface LicenseTypeRepository {
    
    suspend fun getLicenseTypeById(id: String): LicenseType?
    
    fun getLicenseTypesByCategory(category: LicenseCategory): Flow<List<LicenseType>>
    
    fun getAllActiveLicenseTypes(): Flow<List<LicenseType>>
    
    fun getLicenseTypesForAge(age: Int): Flow<List<LicenseType>>
    
    suspend fun insertLicenseType(licenseType: LicenseType)
    
    suspend fun insertLicenseTypes(licenseTypes: List<LicenseType>)
    
    suspend fun updateLicenseType(licenseType: LicenseType)
    
    suspend fun deleteLicenseType(licenseType: LicenseType)
    
    suspend fun deactivateLicenseType(licenseTypeId: String)
}
