package com.example.appbrevete.domain.repository

import com.example.appbrevete.domain.model.MedicalEvaluation
import kotlinx.coroutines.flow.Flow

interface MedicalEvaluationRepository {
    
    suspend fun getEvaluationById(id: String): MedicalEvaluation?
    
    suspend fun getEvaluationByAppointmentId(appointmentId: String): MedicalEvaluation?
    
    fun getEvaluationsByUser(userId: String): Flow<List<MedicalEvaluation>>
    
    fun getEvaluationsByResult(isApproved: Boolean): Flow<List<MedicalEvaluation>>
    
    fun getAllEvaluations(): Flow<List<MedicalEvaluation>>
    
    suspend fun insertEvaluation(evaluation: MedicalEvaluation)
    
    suspend fun updateEvaluation(evaluation: MedicalEvaluation)
    
    suspend fun deleteEvaluation(evaluation: MedicalEvaluation)
    
    suspend fun deleteAllEvaluations()
}
