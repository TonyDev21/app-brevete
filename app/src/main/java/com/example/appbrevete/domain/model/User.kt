package com.example.appbrevete.domain.model

data class User(
    val id: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val dni: String,
    val phoneNumber: String,
    val address: String,
    val birthDate: String,
    val role: UserRole,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class UserRole {
    STUDENT,        // Estudiante que busca obtener licencia
    INSTRUCTOR,     // Instructor de manejo
    EXAMINER,       // Examinador oficial
    ADMIN,          // Administrador del sistema
    MEDICAL_DOCTOR  // Doctor para exámenes médicos
}