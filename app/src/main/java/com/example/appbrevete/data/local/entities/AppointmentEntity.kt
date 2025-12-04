package com.example.appbrevete.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index
import com.example.appbrevete.domain.model.AppointmentType
import com.example.appbrevete.domain.model.AppointmentStatus

@Entity(
    tableName = "appointments",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LicenseTypeEntity::class,
            parentColumns = ["id"],
            childColumns = ["licenseTypeId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["userId"]),
        Index(value = ["licenseTypeId"]),
        Index(value = ["examinerId"]),
        Index(value = ["instructorId"])
    ]
)
data class AppointmentEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val userName: String? = null,
    val userDni: String? = null,
    val type: AppointmentType,
    val licenseTypeId: String? = null,
    val scheduledDate: Long,
    val scheduledTime: String,
    val location: String,
    val status: AppointmentStatus,
    val notes: String? = null,
    val examinerId: String? = null,
    val instructorId: String? = null,
    val medicalEvaluationId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
