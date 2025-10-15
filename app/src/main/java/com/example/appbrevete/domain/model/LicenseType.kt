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
    // Licencias de motocicletas (Sistema Peruano)
    BII_A,  // Motocicletas hasta 125cc
    BII_B,  // Motocicletas hasta 250cc  
    BII_C,  // Motocicletas de más de 250cc
    
    // Licencias de automóviles
    A1,  // Vehículos particulares hasta 3500kg
    A2,  // Vehículos particulares hasta 3500kg con remolque
    B1,  // Vehículos de carga hasta 7500kg
    B2,  // Vehículos de carga hasta 12000kg
    B3,  // Vehículos de carga de más de 12000kg
    C1,  // Vehículos de transporte público hasta 16 pasajeros
    C2,  // Vehículos de transporte público de más de 16 pasajeros
    D1,  // Vehículos especiales
    D2   // Vehículos de emergencia
}