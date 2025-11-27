package com.example.appbrevete.data.local.dao

import androidx.room.*
import com.example.appbrevete.data.local.entities.DrivingClassEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DrivingClassDao {
    
    @Query("SELECT * FROM driving_classes WHERE status != 'cancelled' ORDER BY scheduledDate ASC")
    fun getAllClasses(): Flow<List<DrivingClassEntity>>
    
    @Query("SELECT * FROM driving_classes WHERE id = :id")
    suspend fun getClassById(id: String): DrivingClassEntity?
    
    @Query("SELECT * FROM driving_classes WHERE studentId = :studentId AND status != 'cancelled' ORDER BY scheduledDate ASC")
    fun getClassesByStudent(studentId: String): Flow<List<DrivingClassEntity>>
    
    @Query("SELECT * FROM driving_classes WHERE studentId = :studentId ORDER BY scheduledDate ASC")
    fun getAllClassesByStudent(studentId: String): Flow<List<DrivingClassEntity>>
    
    @Query("SELECT * FROM driving_classes WHERE status = :status ORDER BY scheduledDate ASC")
    fun getClassesByStatus(status: String): Flow<List<DrivingClassEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClass(drivingClass: DrivingClassEntity)
    
    @Update
    suspend fun updateClass(drivingClass: DrivingClassEntity)
    
    @Delete
    suspend fun deleteClass(drivingClass: DrivingClassEntity)
    
    @Query("DELETE FROM driving_classes WHERE id = :id")
    suspend fun deleteClassById(id: String)
    
    @Query("UPDATE driving_classes SET status = :status WHERE id = :id")
    suspend fun updateClassStatus(id: String, status: String)
    
    @Query("UPDATE driving_classes SET packageType = :packageType, totalHours = :totalHours, scheduledDate = :scheduledDate, scheduledTime = :scheduledTime, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateClassDetails(
        id: String,
        packageType: String,
        totalHours: Int,
        scheduledDate: Long,
        scheduledTime: String,
        updatedAt: Long
    )
}