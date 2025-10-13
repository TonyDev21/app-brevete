package com.example.appbrevete.data.repository

import com.example.appbrevete.data.local.dao.LicenseTypeDao
import com.example.appbrevete.data.mapper.toDomainModel
import com.example.appbrevete.data.mapper.toEntity
import com.example.appbrevete.domain.model.LicenseType
import com.example.appbrevete.domain.model.LicenseCategory
import com.example.appbrevete.domain.repository.LicenseTypeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LicenseTypeRepositoryImpl @Inject constructor(
    private val licenseTypeDao: LicenseTypeDao
) : LicenseTypeRepository {
    
    override suspend fun getLicenseTypeById(id: String): LicenseType? {
        return licenseTypeDao.getLicenseTypeById(id)?.toDomainModel()
    }
    
    override fun getLicenseTypesByCategory(category: LicenseCategory): Flow<List<LicenseType>> {
        return licenseTypeDao.getLicenseTypesByCategory(category).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getAllActiveLicenseTypes(): Flow<List<LicenseType>> {
        return licenseTypeDao.getAllActiveLicenseTypes().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getLicenseTypesForAge(age: Int): Flow<List<LicenseType>> {
        return licenseTypeDao.getLicenseTypesForAge(age).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override suspend fun insertLicenseType(licenseType: LicenseType) {
        licenseTypeDao.insertLicenseType(licenseType.toEntity())
    }
    
    override suspend fun insertLicenseTypes(licenseTypes: List<LicenseType>) {
        licenseTypeDao.insertLicenseTypes(licenseTypes.map { it.toEntity() })
    }
    
    override suspend fun updateLicenseType(licenseType: LicenseType) {
        licenseTypeDao.updateLicenseType(licenseType.toEntity())
    }
    
    override suspend fun deleteLicenseType(licenseType: LicenseType) {
        licenseTypeDao.deleteLicenseType(licenseType.toEntity())
    }
    
    override suspend fun deactivateLicenseType(licenseTypeId: String) {
        licenseTypeDao.deactivateLicenseType(licenseTypeId)
    }
}
