package com.example.appbrevete.data.local.dao

import androidx.room.*
import com.example.appbrevete.data.local.entities.UserEntity
import com.example.appbrevete.domain.model.UserRole
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: String): UserEntity?
    
    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?
    
    @Query("SELECT * FROM users WHERE dni = :dni")
    suspend fun getUserByDni(dni: String): UserEntity?
    
    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun getUserByEmailAndPassword(email: String, password: String): UserEntity?
    
    @Query("SELECT * FROM users WHERE role = :role AND isActive = 1")
    fun getUsersByRole(role: UserRole): Flow<List<UserEntity>>
    
    @Query("SELECT * FROM users WHERE isActive = 1")
    fun getAllActiveUsers(): Flow<List<UserEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)
    
    @Update
    suspend fun updateUser(user: UserEntity)
    
    @Delete
    suspend fun deleteUser(user: UserEntity)
    
    @Query("UPDATE users SET isActive = 0 WHERE id = :userId")
    suspend fun deactivateUser(userId: String)
    
    @Query("SELECT COUNT(*) FROM users WHERE role = :role AND isActive = 1")
    suspend fun getUserCountByRole(role: UserRole): Int
}
