package com.example.appbrevete.data.local.dao

import androidx.room.*
import com.example.appbrevete.data.local.entities.AppointmentEntity
import com.example.appbrevete.domain.model.AppointmentType
import com.example.appbrevete.domain.model.AppointmentStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface AppointmentDao {
    
    @Query("SELECT * FROM appointments WHERE id = :id")
    suspend fun getAppointmentById(id: String): AppointmentEntity?
    
    @Query("SELECT * FROM appointments ORDER BY scheduledDate DESC")
    fun getAllAppointments(): Flow<List<AppointmentEntity>>
    
    @Query("SELECT * FROM appointments WHERE userId = :userId ORDER BY scheduledDate ASC")
    fun getAppointmentsByUser(userId: String): Flow<List<AppointmentEntity>>
    
    @Query("SELECT * FROM appointments WHERE userId = :userId AND status = :status ORDER BY scheduledDate ASC")
    fun getAppointmentsByUserAndStatus(userId: String, status: AppointmentStatus): Flow<List<AppointmentEntity>>
    
    @Query("SELECT * FROM appointments WHERE type = :type ORDER BY scheduledDate ASC")
    fun getAppointmentsByType(type: AppointmentType): Flow<List<AppointmentEntity>>
    
    @Query("SELECT * FROM appointments WHERE examinerId = :examinerId ORDER BY scheduledDate ASC")
    fun getAppointmentsByExaminer(examinerId: String): Flow<List<AppointmentEntity>>
    
    @Query("SELECT * FROM appointments WHERE instructorId = :instructorId ORDER BY scheduledDate ASC")
    fun getAppointmentsByInstructor(instructorId: String): Flow<List<AppointmentEntity>>
    
    @Query("SELECT * FROM appointments WHERE scheduledDate >= :startDate AND scheduledDate <= :endDate ORDER BY scheduledDate ASC")
    fun getAppointmentsByDateRange(startDate: Long, endDate: Long): Flow<List<AppointmentEntity>>
    
    @Query("SELECT * FROM appointments WHERE userId = :userId AND scheduledDate >= :currentDate AND status IN ('SCHEDULED', 'CONFIRMED') ORDER BY scheduledDate ASC LIMIT 5")
    fun getUpcomingAppointments(userId: String, currentDate: Long): Flow<List<AppointmentEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: AppointmentEntity)
    
    @Update
    suspend fun updateAppointment(appointment: AppointmentEntity)
    
    @Delete
    suspend fun deleteAppointment(appointment: AppointmentEntity)
    
    @Query("UPDATE appointments SET status = :status, updatedAt = :updatedAt WHERE id = :appointmentId")
    suspend fun updateAppointmentStatus(appointmentId: String, status: AppointmentStatus, updatedAt: Long = System.currentTimeMillis())
    
    @Query("SELECT COUNT(*) FROM appointments WHERE status = :status")
    suspend fun getAppointmentCountByStatus(status: AppointmentStatus): Int
}
