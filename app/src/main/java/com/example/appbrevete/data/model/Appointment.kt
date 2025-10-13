package com.example.appbrevete.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "appointments")
data class Appointment(
    @PrimaryKey
    val id: String,
    val userId: String,
    val type: String, // MEDICAL_EXAM, THEORETICAL_EXAM, PRACTICAL_EXAM, DOCUMENT_REVIEW
    val status: String, // SCHEDULED, COMPLETED, CANCELLED, IN_PROGRESS
    val scheduledDate: String,
    val scheduledTime: String,
    val description: String? = null,
    val notes: String? = null,
    val createdAt: String,
    val updatedAt: String
)

// Enum para tipos de cita
enum class AppointmentType {
    MEDICAL_EXAM,
    THEORETICAL_EXAM,
    PRACTICAL_EXAM,
    DOCUMENT_REVIEW
}

// Enum para estados de cita
enum class AppointmentStatus {
    SCHEDULED,
    COMPLETED,
    CANCELLED,
    IN_PROGRESS
}
