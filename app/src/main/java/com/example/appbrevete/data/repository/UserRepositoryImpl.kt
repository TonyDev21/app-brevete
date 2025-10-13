package com.example.appbrevete.data.repository

import com.example.appbrevete.data.local.dao.UserDao
import com.example.appbrevete.data.mapper.toDomainModel
import com.example.appbrevete.data.mapper.toEntity
import com.example.appbrevete.domain.model.User
import com.example.appbrevete.domain.model.UserRole
import com.example.appbrevete.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {
    
    override suspend fun getUserById(id: String): User? {
        return userDao.getUserById(id)?.toDomainModel()
    }
    
    override suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)?.toDomainModel()
    }
    
    override suspend fun getUserByDni(dni: String): User? {
        return userDao.getUserByDni(dni)?.toDomainModel()
    }
    
    override suspend fun authenticateUser(email: String, password: String): User? {
        return userDao.getUserByEmailAndPassword(email, password)?.toDomainModel()
    }
    
    override fun getUsersByRole(role: UserRole): Flow<List<User>> {
        return userDao.getUsersByRole(role).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getAllActiveUsers(): Flow<List<User>> {
        return userDao.getAllActiveUsers().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override suspend fun insertUser(user: User) {
        userDao.insertUser(user.toEntity())
    }
    
    override suspend fun updateUser(user: User) {
        userDao.updateUser(user.toEntity())
    }
    
    override suspend fun deleteUser(user: User) {
        userDao.deleteUser(user.toEntity())
    }
    
    override suspend fun deactivateUser(userId: String) {
        userDao.deactivateUser(userId)
    }
    
    override suspend fun getUserCountByRole(role: UserRole): Int {
        return userDao.getUserCountByRole(role)
    }
}
