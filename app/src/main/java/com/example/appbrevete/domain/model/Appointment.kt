package com.example.appbrevete.domain.model

data class Appointment(
    val id: String,
    val userId: String,
    val type: AppointmentType,
    val licenseTypeId: String? = null,
    val scheduledDate: Long,
    val scheduledTime: String,
    val location: String,
    val status: AppointmentStatus,
    val notes: String? = null,
    val examinerId: String? = null,
    val instructorId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class AppointmentType {
    MEDICAL_EXAM,      // Examen médico
    THEORY_EXAM,       // Examen de reglas/teoría
    PRACTICAL_EXAM,    // Examen de manejo práctico
    DRIVING_CLASS,     // Clase de manejo
    CONSULTATION       // Consulta general
}

enum class AppointmentStatus {
    SCHEDULED,         // Programada
    CONFIRMED,         // Confirmada
    IN_PROGRESS,       // En progreso
    COMPLETED,         // Completada
    CANCELLED,         // Cancelada
    NO_SHOW,           // No se presentó
    RESCHEDULED        // Reprogramada
}