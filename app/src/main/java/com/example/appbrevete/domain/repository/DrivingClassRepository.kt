package com.example.appbrevete.domain.repository

import com.example.appbrevete.domain.model.DrivingClass
import kotlinx.coroutines.flow.Flow

interface DrivingClassRepository {
    
    fun getAllClasses(): Flow<List<DrivingClass>>
    
    suspend fun getClassById(id: String): DrivingClass?
    
    fun getClassesByStudent(studentId: String): Flow<List<DrivingClass>>
    
    fun getAllClassesByStudent(studentId: String): Flow<List<DrivingClass>>
    
    fun getClassesByStatus(status: String): Flow<List<DrivingClass>>
    
    suspend fun insertClass(drivingClass: DrivingClass)
    
    suspend fun updateClass(drivingClass: DrivingClass)
    
    suspend fun updateClass(
        classId: String,
        packageType: String,
        totalHours: Int,
        scheduledDate: Long,
        scheduledTime: String
    )
    
    suspend fun deleteClass(drivingClass: DrivingClass)
    
    suspend fun deleteClassById(id: String)
    
    suspend fun updateClassStatus(id: String, status: String)
}