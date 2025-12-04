package com.example.appbrevete.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.appbrevete.data.local.AppBreveteDatabase
import com.example.appbrevete.data.local.dao.UserDao
import com.example.appbrevete.data.local.dao.LicenseTypeDao
import com.example.appbrevete.data.local.dao.AppointmentDao
import com.example.appbrevete.data.local.dao.DrivingClassDao
import com.example.appbrevete.data.local.dao.MedicalEvaluationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }
    
    @Provides
    @Singleton
    fun provideAppBreveteDatabase(@ApplicationContext context: Context): AppBreveteDatabase {
        return AppBreveteDatabase.buildDatabase(context)
    }
    
    @Provides
    fun provideUserDao(database: AppBreveteDatabase): UserDao {
        return database.userDao()
    }
    
    @Provides
    fun provideLicenseTypeDao(database: AppBreveteDatabase): LicenseTypeDao {
        return database.licenseTypeDao()
    }
    
    @Provides
    fun provideAppointmentDao(database: AppBreveteDatabase): AppointmentDao {
        return database.appointmentDao()
    }
    
    @Provides
    fun provideDrivingClassDao(database: AppBreveteDatabase): DrivingClassDao {
        return database.drivingClassDao()
    }
    
    @Provides
    fun provideMedicalEvaluationDao(database: AppBreveteDatabase): MedicalEvaluationDao {
        return database.medicalEvaluationDao()
    }
}
