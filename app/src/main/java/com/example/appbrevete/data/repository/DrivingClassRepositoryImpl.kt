package com.example.appbrevete.data.repository

import com.example.appbrevete.data.local.dao.DrivingClassDao
import com.example.appbrevete.data.local.entities.DrivingClassEntity
import com.example.appbrevete.domain.model.DrivingClass
import com.example.appbrevete.domain.model.DrivingClassStatus
import com.example.appbrevete.domain.model.DrivingPackageType
import com.example.appbrevete.domain.model.VehicleType
import com.example.appbrevete.domain.repository.DrivingClassRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DrivingClassRepositoryImpl @Inject constructor(
    private val drivingClassDao: DrivingClassDao
) : DrivingClassRepository {
    
    override fun getAllClasses(): Flow<List<DrivingClass>> {
        return drivingClassDao.getAllClasses().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override suspend fun getClassById(id: String): DrivingClass? {
        return drivingClassDao.getClassById(id)?.toDomainModel()
    }
    
    override fun getClassesByStudent(studentId: String): Flow<List<DrivingClass>> {
        return drivingClassDao.getClassesByStudent(studentId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getAllClassesByStudent(studentId: String): Flow<List<DrivingClass>> {
        return drivingClassDao.getAllClassesByStudent(studentId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getClassesByStatus(status: String): Flow<List<DrivingClass>> {
        return drivingClassDao.getClassesByStatus(status).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override suspend fun insertClass(drivingClass: DrivingClass) {
        drivingClassDao.insertClass(drivingClass.toEntity())
    }
    
    override suspend fun updateClass(drivingClass: DrivingClass) {
        drivingClassDao.updateClass(drivingClass.toEntity())
    }
    
    override suspend fun updateClass(
        classId: String,
        packageType: String,
        totalHours: Int,
        scheduledDate: Long,
        scheduledTime: String
    ) {
        drivingClassDao.updateClassDetails(
            id = classId,
            packageType = packageType,
            totalHours = totalHours,
            scheduledDate = scheduledDate,
            scheduledTime = scheduledTime,
            updatedAt = System.currentTimeMillis()
        )
    }
    
    override suspend fun deleteClass(drivingClass: DrivingClass) {
        drivingClassDao.deleteClass(drivingClass.toEntity())
    }
    
    override suspend fun deleteClassById(id: String) {
        drivingClassDao.deleteClassById(id)
    }
    
    override suspend fun updateClassStatus(id: String, status: String) {
        drivingClassDao.updateClassStatus(id, status)
    }
}

// Extension functions para convertir entre Entity y Domain Model
private fun DrivingClassEntity.toDomainModel(): DrivingClass {
    return DrivingClass(
        id = id,
        studentId = studentId,
        instructorId = instructorId,
        instructorName = instructorName,
        packageType = when (packageType) {
            "2h" -> DrivingPackageType.BASIC_2H
            "4h" -> DrivingPackageType.STANDARD_4H
            else -> DrivingPackageType.CUSTOM
        },
        totalHours = totalHours,
        completedHours = completedHours,
        scheduledDate = scheduledDate,
        scheduledTime = scheduledTime,
        status = when (status) {
            "scheduled" -> DrivingClassStatus.SCHEDULED
            "confirmed" -> DrivingClassStatus.CONFIRMED
            "in_progress" -> DrivingClassStatus.IN_PROGRESS
            "completed" -> DrivingClassStatus.COMPLETED
            "cancelled" -> DrivingClassStatus.CANCELLED
            "rescheduled" -> DrivingClassStatus.RESCHEDULED
            else -> DrivingClassStatus.SCHEDULED
        },
        location = location,
        vehicleType = when (vehicleType) {
            "motorcycle_125cc" -> VehicleType.MOTORCYCLE_125CC
            "motorcycle_250cc" -> VehicleType.MOTORCYCLE_250CC
            "motorcycle_plus" -> VehicleType.MOTORCYCLE_PLUS
            "car_automatic" -> VehicleType.CAR_AUTOMATIC
            "microbus" -> VehicleType.MICROBUS
            "truck" -> VehicleType.TRUCK
            "bus" -> VehicleType.BUS
            else -> VehicleType.CAR_MANUAL
        },
        notes = notes,
        userName = userName,
        userDni = userDni,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

private fun DrivingClass.toEntity(): DrivingClassEntity {
    return DrivingClassEntity(
        id = id,
        studentId = studentId,
        instructorId = instructorId,
        instructorName = instructorName,
        packageType = when (packageType) {
            DrivingPackageType.BASIC_2H -> "2h"
            DrivingPackageType.STANDARD_4H -> "4h"
            DrivingPackageType.CUSTOM -> "custom"
        },
        totalHours = totalHours,
        completedHours = completedHours,
        scheduledDate = scheduledDate,
        scheduledTime = scheduledTime,
        status = when (status) {
            DrivingClassStatus.SCHEDULED -> "scheduled"
            DrivingClassStatus.CONFIRMED -> "confirmed"
            DrivingClassStatus.IN_PROGRESS -> "in_progress"
            DrivingClassStatus.COMPLETED -> "completed"
            DrivingClassStatus.CANCELLED -> "cancelled"
            DrivingClassStatus.RESCHEDULED -> "rescheduled"
        },
        location = location,
        vehicleType = when (vehicleType) {
            VehicleType.MOTORCYCLE_125CC -> "motorcycle_125cc"
            VehicleType.MOTORCYCLE_250CC -> "motorcycle_250cc"
            VehicleType.MOTORCYCLE_PLUS -> "motorcycle_plus"
            VehicleType.CAR_MANUAL -> "car_manual"
            VehicleType.CAR_AUTOMATIC -> "car_automatic"
            VehicleType.MICROBUS -> "microbus"
            VehicleType.TRUCK -> "truck"
            VehicleType.BUS -> "bus"
        },
        notes = notes,
        userName = userName,
        userDni = userDni,
        price = when (packageType) {
            DrivingPackageType.BASIC_2H -> 65.0
            DrivingPackageType.STANDARD_4H -> 125.0
            DrivingPackageType.CUSTOM -> 0.0
        },
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}