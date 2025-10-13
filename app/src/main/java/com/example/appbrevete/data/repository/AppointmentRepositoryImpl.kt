package com.example.appbrevete.data.repository

import com.example.appbrevete.data.local.dao.AppointmentDao
import com.example.appbrevete.data.mapper.toDomainModel
import com.example.appbrevete.data.mapper.toEntity
import com.example.appbrevete.domain.model.Appointment
import com.example.appbrevete.domain.model.AppointmentType
import com.example.appbrevete.domain.model.AppointmentStatus
import com.example.appbrevete.domain.repository.AppointmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppointmentRepositoryImpl @Inject constructor(
    private val appointmentDao: AppointmentDao
) : AppointmentRepository {
    
    override suspend fun getAppointmentById(id: String): Appointment? {
        return appointmentDao.getAppointmentById(id)?.toDomainModel()
    }
    
    override fun getAppointmentsByUser(userId: String): Flow<List<Appointment>> {
        return appointmentDao.getAppointmentsByUser(userId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getAppointmentsByUserAndStatus(userId: String, status: AppointmentStatus): Flow<List<Appointment>> {
        return appointmentDao.getAppointmentsByUserAndStatus(userId, status).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getAppointmentsByType(type: AppointmentType): Flow<List<Appointment>> {
        return appointmentDao.getAppointmentsByType(type).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getAppointmentsByExaminer(examinerId: String): Flow<List<Appointment>> {
        return appointmentDao.getAppointmentsByExaminer(examinerId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getAppointmentsByInstructor(instructorId: String): Flow<List<Appointment>> {
        return appointmentDao.getAppointmentsByInstructor(instructorId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getAppointmentsByDateRange(startDate: Long, endDate: Long): Flow<List<Appointment>> {
        return appointmentDao.getAppointmentsByDateRange(startDate, endDate).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getUpcomingAppointments(userId: String, currentDate: Long): Flow<List<Appointment>> {
        return appointmentDao.getUpcomingAppointments(userId, currentDate).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override suspend fun insertAppointment(appointment: Appointment) {
        appointmentDao.insertAppointment(appointment.toEntity())
    }
    
    override suspend fun updateAppointment(appointment: Appointment) {
        println("AppointmentRepositoryImpl: Updating appointment ${appointment.id}")
        appointmentDao.updateAppointment(appointment.toEntity())
        println("AppointmentRepositoryImpl: Appointment ${appointment.id} updated successfully")
    }
    
    override suspend fun deleteAppointment(appointment: Appointment) {
        appointmentDao.deleteAppointment(appointment.toEntity())
    }
    
    override suspend fun updateAppointmentStatus(appointmentId: String, status: AppointmentStatus) {
        appointmentDao.updateAppointmentStatus(appointmentId, status, System.currentTimeMillis())
    }
    
    override suspend fun getAppointmentCountByStatus(status: AppointmentStatus): Int {
        return appointmentDao.getAppointmentCountByStatus(status)
    }
}
