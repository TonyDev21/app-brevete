package com.example.appbrevete.data.local

import androidx.room.TypeConverter
import com.example.appbrevete.domain.model.UserRole
import com.example.appbrevete.domain.model.LicenseCategory
import com.example.appbrevete.domain.model.AppointmentType
import com.example.appbrevete.domain.model.AppointmentStatus

class Converters {
    
    @TypeConverter
    fun fromUserRole(role: UserRole): String = role.name
    
    @TypeConverter
    fun toUserRole(role: String): UserRole = UserRole.valueOf(role)
    
    @TypeConverter
    fun fromLicenseCategory(category: LicenseCategory): String = category.name
    
    @TypeConverter
    fun toLicenseCategory(category: String): LicenseCategory = LicenseCategory.valueOf(category)
    
    @TypeConverter
    fun fromAppointmentType(type: AppointmentType): String = type.name
    
    @TypeConverter
    fun toAppointmentType(type: String): AppointmentType = AppointmentType.valueOf(type)
    
    @TypeConverter
    fun fromAppointmentStatus(status: AppointmentStatus): String = status.name
    
    @TypeConverter
    fun toAppointmentStatus(status: String): AppointmentStatus = AppointmentStatus.valueOf(status)
}
