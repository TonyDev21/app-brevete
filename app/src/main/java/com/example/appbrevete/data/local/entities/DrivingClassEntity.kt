package com.example.appbrevete.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "driving_classes")
data class DrivingClassEntity(
    @PrimaryKey
    val id: String,
    val studentId: String,
    val userName: String? = null,
    val userDni: String? = null,
    val instructorId: String,
    val instructorName: String,
    val packageType: String, // "2h", "4h", "custom"
    val totalHours: Int,
    val completedHours: Int = 0,
    val scheduledDate: Long,
    val scheduledTime: String,
    val status: String, // "scheduled", "confirmed", "completed", "cancelled"
    val location: String,
    val vehicleType: String = "car_manual",
    val notes: String? = null,
    val price: Double,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)