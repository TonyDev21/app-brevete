package com.example.appbrevete.domain.model

/**
 * Modelo para una clase de manejo individual
 */
data class DrivingClass(
    val id: String,
    val studentId: String,
    val userName: String? = null,
    val userDni: String? = null,
    val instructorId: String,
    val instructorName: String,
    val packageType: DrivingPackageType,
    val totalHours: Int,
    val completedHours: Int,
    val scheduledDate: Long,
    val scheduledTime: String,
    val status: DrivingClassStatus,
    val location: String,
    val vehicleType: VehicleType,
    val notes: String? = null,
    val evaluations: List<SkillEvaluation> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * Tipos de paquetes de clases disponibles
 */
enum class DrivingPackageType {
    BASIC_2H,      // 2 horas - S/ 65
    STANDARD_4H,   // 4 horas - S/ 125  
    CUSTOM         // Paquete personalizado
}

/**
 * Estados de una clase de manejo
 */
enum class DrivingClassStatus {
    SCHEDULED,     // Programada
    CONFIRMED,     // Confirmada
    IN_PROGRESS,   // En progreso
    COMPLETED,     // Completada
    CANCELLED,     // Cancelada
    RESCHEDULED    // Reprogramada
}

/**
 * Tipos de vehículos para las clases
 */
enum class VehicleType {
    MOTORCYCLE_125CC,   // Motocicleta hasta 125cc
    MOTORCYCLE_250CC,   // Motocicleta hasta 250cc
    MOTORCYCLE_PLUS,    // Motocicleta más de 250cc
    CAR_MANUAL,         // Automóvil manual
    CAR_AUTOMATIC,      // Automóvil automático
    MICROBUS,           // Microbús
    TRUCK,              // Camión
    BUS                 // Ómnibus
}

/**
 * Evaluación de habilidades específicas
 */
data class SkillEvaluation(
    val id: String,
    val classId: String,
    val skill: DrivingSkill,
    val rating: Int, // 1-5 estrellas
    val comments: String? = null,
    val evaluatedAt: Long = System.currentTimeMillis()
)

/**
 * Habilidades de manejo evaluadas
 */
enum class DrivingSkill {
    PARKING,           // Estacionamiento
    HILL_START,        // Arranque en pendiente
    REVERSE,           // Marcha atrás
    LANE_CHANGE,       // Cambio de carril
    TRAFFIC_RULES,     // Respeto de normas
    SIGNALING,         // Señalización
    SPEED_CONTROL,     // Control de velocidad
    DEFENSIVE_DRIVING, // Manejo defensivo
    NIGHT_DRIVING,     // Manejo nocturno
    CITY_DRIVING       // Manejo urbano
}

/**
 * Información del instructor
 */
data class Instructor(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val licenseNumber: String,
    val specialties: List<VehicleType>,
    val rating: Float = 0f,
    val totalClasses: Int = 0,
    val yearsExperience: Int,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
) {
    val fullName: String
        get() = "$firstName $lastName"
}