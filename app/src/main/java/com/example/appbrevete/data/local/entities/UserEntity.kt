package com.example.appbrevete.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import com.example.appbrevete.domain.model.UserRole

@Entity(
    tableName = "users",
    indices = [
        Index(value = ["email"], unique = true),
        Index(value = ["dni"], unique = true)
    ]
)
data class UserEntity(
    @PrimaryKey
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
