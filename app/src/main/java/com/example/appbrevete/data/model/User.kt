package com.example.appbrevete.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val documentNumber: String,
    val role: String = "USER", // USER, ADMIN
    val isActive: Boolean = true,
    val createdAt: String,
    val updatedAt: String
)

// Extensión para verificar si es administrador
fun User.isAdmin(): Boolean = role == "ADMIN"

// Extensión para verificar si es usuario regular
fun User.isUser(): Boolean = role == "USER"
