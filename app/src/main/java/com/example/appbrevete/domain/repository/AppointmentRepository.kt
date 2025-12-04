package com.example.appbrevete.domain.repository

import com.example.appbrevete.domain.model.Appointment
import com.example.appbrevete.domain.model.AppointmentType
import com.example.appbrevete.domain.model.AppointmentStatus
import kotlinx.coroutines.flow.Flow

interface AppointmentRepository {
    
    suspend fun getAppointmentById(id: String): Appointment?
    
    fun getAllAppointments(): Flow<List<Appointment>>
    
    fun getAppointmentsByUser(userId: String): Flow<List<Appointment>>
    
    fun getAppointmentsByUserAndStatus(userId: String, status: AppointmentStatus): Flow<List<Appointment>>
    
    fun getAppointmentsByType(type: AppointmentType): Flow<List<Appointment>>
    
    fun getAppointmentsByExaminer(examinerId: String): Flow<List<Appointment>>
    
    fun getAppointmentsByInstructor(instructorId: String): Flow<List<Appointment>>
    
    fun getAppointmentsByDateRange(startDate: Long, endDate: Long): Flow<List<Appointment>>
    
    fun getUpcomingAppointments(userId: String, currentDate: Long): Flow<List<Appointment>>
    
    suspend fun insertAppointment(appointment: Appointment)
    
    suspend fun updateAppointment(appointment: Appointment)
    
    suspend fun deleteAppointment(appointment: Appointment)
    
    suspend fun updateAppointmentStatus(appointmentId: String, status: AppointmentStatus)
    
    suspend fun getAppointmentCountByStatus(status: AppointmentStatus): Int
}
