package com.example.appbrevete.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.example.appbrevete.data.local.entities.UserEntity
import com.example.appbrevete.data.local.entities.LicenseTypeEntity
import com.example.appbrevete.data.local.entities.AppointmentEntity
import com.example.appbrevete.data.local.dao.UserDao
import com.example.appbrevete.data.local.dao.LicenseTypeDao
import com.example.appbrevete.data.local.dao.AppointmentDao

@Database(
    entities = [
        UserEntity::class,
        LicenseTypeEntity::class,
        AppointmentEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppBreveteDatabase : RoomDatabase() {
    
    abstract fun userDao(): UserDao
    abstract fun licenseTypeDao(): LicenseTypeDao
    abstract fun appointmentDao(): AppointmentDao
    
    companion object {
        const val DATABASE_NAME = "appbrevete_database"
        
        fun buildDatabase(context: Context): AppBreveteDatabase {
            return Room.databaseBuilder(
                context,
                AppBreveteDatabase::class.java,
                DATABASE_NAME
            ).fallbackToDestructiveMigration()
             .build()
        }
    }
}
