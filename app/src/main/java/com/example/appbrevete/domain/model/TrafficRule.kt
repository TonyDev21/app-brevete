package com.example.appbrevete.domain.model

/**
 * Tipos de vehículos para examen de reglas
 */
enum class VehicleCategory {
    MOTORCYCLE,
    CAR
}

/**
 * Categorías de licencias para automóviles
 */
enum class CarLicenseCategory {
    A_I,        // Categoría A-I
    A_IIA,      // Categoría A-IIa
    A_IIB,      // Categoría A-IIb
    A_IIC,      // Categoría A-IIc
    A_IIIA,     // Categoría A-IIIa
    A_IIIB,     // Categoría A-IIIb
    A_IIIC      // Categoría A-IIIc
}

/**
 * Categorías de licencias para motocicletas
 */
enum class MotorcycleLicenseCategory {
    B_IIA,      // Categoría B-IIA
    B_IIB,      // Categoría B-IIB  
    B_IIC       // Categoría B-IIC
}

/**
 * Modelo para una pregunta del examen de reglas
 */
data class TrafficRuleQuestion(
    val id: String,
    val category: CarLicenseCategory?,
    val vehicleType: VehicleCategory,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String? = null,
    val imageUrl: String? = null
)

/**
 * Resultado de un examen de reglas
 */
data class TrafficRuleExamResult(
    val id: String,
    val userId: String,
    val vehicleType: VehicleCategory,
    val category: CarLicenseCategory?,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val score: Float, // Porcentaje 0-100
    val passed: Boolean, // true si score >= 70
    val timeSpent: Long, // en milisegundos
    val completedAt: Long = System.currentTimeMillis()
)

/**
 * Resultado de un quiz individual
 */
data class QuizResult(
    val id: String,
    val vehicleType: VehicleCategory,
    val category: CarLicenseCategory?,
    val score: Float,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val timeSpent: Long,
    val passed: Boolean,
    val completedAt: Long
)

/**
 * Progreso del usuario en exámenes de reglas
 */
data class TrafficRuleProgress(
    val userId: String,
    val vehicleType: VehicleCategory,
    val category: CarLicenseCategory?,
    val totalAttempts: Int,
    val bestScore: Float,
    val lastAttemptDate: Long,
    val passed: Boolean
)