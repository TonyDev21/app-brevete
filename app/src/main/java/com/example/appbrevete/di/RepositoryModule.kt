package com.example.appbrevete.di

import com.example.appbrevete.data.repository.UserRepositoryImpl
import com.example.appbrevete.data.repository.LicenseTypeRepositoryImpl
import com.example.appbrevete.data.repository.AppointmentRepositoryImpl
import com.example.appbrevete.domain.repository.UserRepository
import com.example.appbrevete.domain.repository.LicenseTypeRepository
import com.example.appbrevete.domain.repository.AppointmentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
    
    @Binds
    @Singleton
    abstract fun bindLicenseTypeRepository(
        licenseTypeRepositoryImpl: LicenseTypeRepositoryImpl
    ): LicenseTypeRepository
    
    @Binds
    @Singleton
    abstract fun bindAppointmentRepository(
        appointmentRepositoryImpl: AppointmentRepositoryImpl
    ): AppointmentRepository
}
