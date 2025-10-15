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
    
    // Licencias de automóviles (Sistema Peruano)
    A_I,    // Vehículos particulares menores
    A_IIA,  // Vehículos particulares medianos
    A_IIB,  // Vehículos particulares medianos con remolque
    A_IIIA, // Vehículos particulares pesados
    A_IIIB, // Vehículos particulares pesados con remolque
    A_IIIC  // Categoría máxima - todos los vehículos
}