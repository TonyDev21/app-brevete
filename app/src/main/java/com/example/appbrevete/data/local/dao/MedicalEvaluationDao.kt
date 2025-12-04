package com.example.appbrevete.data.local.dao

import androidx.room.*
import com.example.appbrevete.data.local.entities.MedicalEvaluationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicalEvaluationDao {
    
    @Query("SELECT * FROM medical_evaluations WHERE id = :id")
    suspend fun getEvaluationById(id: String): MedicalEvaluationEntity?
    
    @Query("SELECT * FROM medical_evaluations WHERE appointmentId = :appointmentId")
    suspend fun getEvaluationByAppointmentId(appointmentId: String): MedicalEvaluationEntity?
    
    @Query("SELECT * FROM medical_evaluations WHERE userId = :userId ORDER BY createdAt DESC")
    fun getEvaluationsByUser(userId: String): Flow<List<MedicalEvaluationEntity>>
    
    @Query("SELECT * FROM medical_evaluations WHERE resultadoFinal = :isApproved ORDER BY createdAt DESC")
    fun getEvaluationsByResult(isApproved: Boolean): Flow<List<MedicalEvaluationEntity>>
    
    @Query("SELECT * FROM medical_evaluations ORDER BY createdAt DESC")
    fun getAllEvaluations(): Flow<List<MedicalEvaluationEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvaluation(evaluation: MedicalEvaluationEntity)
    
    @Update
    suspend fun updateEvaluation(evaluation: MedicalEvaluationEntity)
    
    @Delete
    suspend fun deleteEvaluation(evaluation: MedicalEvaluationEntity)
    
    @Query("DELETE FROM medical_evaluations")
    suspend fun deleteAllEvaluations()
}
