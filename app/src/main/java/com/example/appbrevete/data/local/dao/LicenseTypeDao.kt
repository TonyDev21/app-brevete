package com.example.appbrevete.data.local.dao

import androidx.room.*
import com.example.appbrevete.data.local.entities.LicenseTypeEntity
import com.example.appbrevete.domain.model.LicenseCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface LicenseTypeDao {
    
    @Query("SELECT * FROM license_types WHERE id = :id")
    suspend fun getLicenseTypeById(id: String): LicenseTypeEntity?
    
    @Query("SELECT * FROM license_types WHERE category = :category AND isActive = 1")
    fun getLicenseTypesByCategory(category: LicenseCategory): Flow<List<LicenseTypeEntity>>
    
    @Query("SELECT * FROM license_types WHERE isActive = 1 ORDER BY category")
    fun getAllActiveLicenseTypes(): Flow<List<LicenseTypeEntity>>
    
    @Query("SELECT * FROM license_types WHERE ageRequirement <= :age AND isActive = 1")
    fun getLicenseTypesForAge(age: Int): Flow<List<LicenseTypeEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLicenseType(licenseType: LicenseTypeEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLicenseTypes(licenseTypes: List<LicenseTypeEntity>)
    
    @Update
    suspend fun updateLicenseType(licenseType: LicenseTypeEntity)
    
    @Delete
    suspend fun deleteLicenseType(licenseType: LicenseTypeEntity)
    
    @Query("UPDATE license_types SET isActive = 0 WHERE id = :licenseTypeId")
    suspend fun deactivateLicenseType(licenseTypeId: String)
    
    @Query("DELETE FROM license_types")
    suspend fun deleteAllLicenseTypes()
}
