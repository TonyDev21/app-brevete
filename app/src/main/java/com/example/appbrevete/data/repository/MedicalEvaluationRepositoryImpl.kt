package com.example.appbrevete.data.repository

import com.example.appbrevete.data.local.dao.MedicalEvaluationDao
import com.example.appbrevete.data.local.entities.MedicalEvaluationEntity
import com.example.appbrevete.domain.model.MedicalEvaluation
import com.example.appbrevete.domain.repository.MedicalEvaluationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedicalEvaluationRepositoryImpl @Inject constructor(
    private val medicalEvaluationDao: MedicalEvaluationDao
) : MedicalEvaluationRepository {
    
    override suspend fun getEvaluationById(id: String): MedicalEvaluation? {
        return medicalEvaluationDao.getEvaluationById(id)?.toDomainModel()
    }
    
    override suspend fun getEvaluationByAppointmentId(appointmentId: String): MedicalEvaluation? {
        return medicalEvaluationDao.getEvaluationByAppointmentId(appointmentId)?.toDomainModel()
    }
    
    override fun getEvaluationsByUser(userId: String): Flow<List<MedicalEvaluation>> {
        return medicalEvaluationDao.getEvaluationsByUser(userId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getEvaluationsByResult(isApproved: Boolean): Flow<List<MedicalEvaluation>> {
        return medicalEvaluationDao.getEvaluationsByResult(isApproved).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getAllEvaluations(): Flow<List<MedicalEvaluation>> {
        return medicalEvaluationDao.getAllEvaluations().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override suspend fun insertEvaluation(evaluation: MedicalEvaluation) {
        medicalEvaluationDao.insertEvaluation(evaluation.toEntity())
    }
    
    override suspend fun updateEvaluation(evaluation: MedicalEvaluation) {
        medicalEvaluationDao.updateEvaluation(evaluation.toEntity())
    }
    
    override suspend fun deleteEvaluation(evaluation: MedicalEvaluation) {
        medicalEvaluationDao.deleteEvaluation(evaluation.toEntity())
    }
    
    override suspend fun deleteAllEvaluations() {
        medicalEvaluationDao.deleteAllEvaluations()
    }
}

// Extension functions
private fun MedicalEvaluationEntity.toDomainModel(): MedicalEvaluation {
    return MedicalEvaluation(
        id = id,
        appointmentId = appointmentId,
        userId = userId,
        medicinaGeneral = medicinaGeneral,
        vistaYOido = vistaYOido,
        grupoSanguineo = grupoSanguineo,
        evaluacionPsicologica = evaluacionPsicologica,
        examenRazonamiento = examenRazonamiento,
        observaciones = observaciones,
        resultadoFinal = resultadoFinal,
        evaluatorId = evaluatorId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

private fun MedicalEvaluation.toEntity(): MedicalEvaluationEntity {
    return MedicalEvaluationEntity(
        id = id,
        appointmentId = appointmentId,
        userId = userId,
        medicinaGeneral = medicinaGeneral,
        vistaYOido = vistaYOido,
        grupoSanguineo = grupoSanguineo,
        evaluacionPsicologica = evaluacionPsicologica,
        examenRazonamiento = examenRazonamiento,
        observaciones = observaciones,
        resultadoFinal = resultadoFinal,
        evaluatorId = evaluatorId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
