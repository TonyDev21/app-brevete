package com.example.appbrevete.data.mapper

import com.example.appbrevete.data.local.entities.UserEntity
import com.example.appbrevete.data.local.entities.LicenseTypeEntity
import com.example.appbrevete.data.local.entities.AppointmentEntity
import com.example.appbrevete.domain.model.User
import com.example.appbrevete.domain.model.LicenseType
import com.example.appbrevete.domain.model.Appointment

// User Mappers
fun UserEntity.toDomainModel(): User {
    return User(
        id = id,
        email = email,
        password = password,
        firstName = firstName,
        lastName = lastName,
        dni = dni,
        phoneNumber = phoneNumber,
        address = address,
        birthDate = birthDate,
        role = role,
        isActive = isActive,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        email = email,
        password = password,
        firstName = firstName,
        lastName = lastName,
        dni = dni,
        phoneNumber = phoneNumber,
        address = address,
        birthDate = birthDate,
        role = role,
        isActive = isActive,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

// LicenseType Mappers
fun LicenseTypeEntity.toDomainModel(): LicenseType {
    return LicenseType(
        id = id,
        name = name,
        description = description,
        category = category,
        ageRequirement = ageRequirement,
        medicalExamRequired = medicalExamRequired,
        theoryExamRequired = theoryExamRequired,
        practicalExamRequired = practicalExamRequired,
        validityYears = validityYears,
        price = price,
        isActive = isActive
    )
}

fun LicenseType.toEntity(): LicenseTypeEntity {
    return LicenseTypeEntity(
        id = id,
        name = name,
        description = description,
        category = category,
        ageRequirement = ageRequirement,
        medicalExamRequired = medicalExamRequired,
        theoryExamRequired = theoryExamRequired,
        practicalExamRequired = practicalExamRequired,
        validityYears = validityYears,
        price = price,
        isActive = isActive
    )
}

// Appointment Mappers
fun AppointmentEntity.toDomainModel(): Appointment {
    return Appointment(
        id = id,
        userId = userId,
        type = type,
        licenseTypeId = licenseTypeId,
        scheduledDate = scheduledDate,
        scheduledTime = scheduledTime,
        location = location,
        status = status,
        notes = notes,
        examinerId = examinerId,
        instructorId = instructorId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Appointment.toEntity(): AppointmentEntity {
    return AppointmentEntity(
        id = id,
        userId = userId,
        type = type,
        licenseTypeId = licenseTypeId,
        scheduledDate = scheduledDate,
        scheduledTime = scheduledTime,
        location = location,
        status = status,
        notes = notes,
        examinerId = examinerId,
        instructorId = instructorId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
