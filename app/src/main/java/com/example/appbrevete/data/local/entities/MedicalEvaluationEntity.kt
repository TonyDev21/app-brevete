package com.example.appbrevete.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index
import com.example.appbrevete.domain.model.EvaluationResult

@Entity(
    tableName = "medical_evaluations",
    foreignKeys = [
        ForeignKey(
            entity = AppointmentEntity::class,
            parentColumns = ["id"],
            childColumns = ["appointmentId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["appointmentId"]),
        Index(value = ["userId"]),
        Index(value = ["evaluatorId"])
    ]
)
data class MedicalEvaluationEntity(
    @PrimaryKey
    val id: String,
    val appointmentId: String,
    val userId: String,
    val medicinaGeneral: EvaluationResult,
    val vistaYOido: EvaluationResult,
    val grupoSanguineo: String,
    val evaluacionPsicologica: EvaluationResult,
    val examenRazonamiento: EvaluationResult,
    val observaciones: String? = null,
    val resultadoFinal: Boolean,
    val evaluatorId: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
