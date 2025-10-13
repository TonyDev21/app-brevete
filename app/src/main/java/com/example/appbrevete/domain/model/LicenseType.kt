package com.example.appbrevete.domain.model

data class LicenseType(
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

enum class LicenseCategory {
    A1,  // Motocicletas hasta 125cc
    A2,  // Motocicletas hasta 300cc
    A3,  // Motocicletas de más de 300cc
    B1,  // Vehículos particulares hasta 3500kg
    B2,  // Vehículos particulares hasta 3500kg con remolque
    C1,  // Vehículos de carga hasta 7500kg
    C2,  // Vehículos de carga hasta 12000kg
    C3,  // Vehículos de carga de más de 12000kg
    D1,  // Vehículos de transporte público hasta 16 pasajeros
    D2,  // Vehículos de transporte público de más de 16 pasajeros
    E1,  // Vehículos especiales
    E2   // Vehículos de emergencia
}