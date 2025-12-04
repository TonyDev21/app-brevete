package com.example.appbrevete.domain.model

data class MedicalEvaluation(
    val id: String,
    val appointmentId: String,
    val userId: String,
    val medicinaGeneral: EvaluationResult,
    val vistaYOido: EvaluationResult,
    val grupoSanguineo: String,
    val evaluacionPsicologica: EvaluationResult,
    val examenRazonamiento: EvaluationResult,
    val observaciones: String? = null,
    val resultadoFinal: Boolean, // true = Aprobado, false = No Aprobado
    val evaluatorId: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class EvaluationResult {
    PENDIENTE,      // Pendiente de evaluación
    APROBADO,       // Aprobado
    NO_APROBADO     // No Aprobado
}
