package com.example.appbrevete.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.appbrevete.domain.model.LicenseCategory

@Entity(tableName = "license_types")
data class LicenseTypeEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val category: LicenseCategory,
    val ageRequirement: Int,
    val medicalExamRequired: Boolean = true,
    val theoryExamRequired: Boolean = true,
    val practicalExamRequired: Boolean = true,
    val validityYears: Int,
    val price: Double,
    val isActive: Boolean = true
)
