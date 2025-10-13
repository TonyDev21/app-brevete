package com.example.appbrevete.domain.repository

import com.example.appbrevete.domain.model.User
import com.example.appbrevete.domain.model.UserRole
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    
    suspend fun getUserById(id: String): User?
    
    suspend fun getUserByEmail(email: String): User?
    
    suspend fun getUserByDni(dni: String): User?
    
    suspend fun authenticateUser(email: String, password: String): User?
    
    fun getUsersByRole(role: UserRole): Flow<List<User>>
    
    fun getAllActiveUsers(): Flow<List<User>>
    
    suspend fun insertUser(user: User)
    
    suspend fun updateUser(user: User)
    
    suspend fun deleteUser(user: User)
    
    suspend fun deactivateUser(userId: String)
    
    suspend fun getUserCountByRole(role: UserRole): Int
}
