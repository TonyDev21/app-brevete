package com.example.appbrevete.data.local

import com.example.appbrevete.data.local.entities.UserEntity
import com.example.appbrevete.data.local.entities.LicenseTypeEntity
import com.example.appbrevete.domain.model.UserRole
import com.example.appbrevete.domain.model.LicenseCategory
import java.util.UUID

object DatabaseInitializer {
    
    fun getInitialUsers(): List<UserEntity> {
        return listOf(
            UserEntity(
                id = "admin-001",
                email = "admin@appbrevete.com",
                password = "admin123",
                firstName = "Administrador",
                lastName = "Sistema",
                dni = "12345678",
                phoneNumber = "999888777",
                address = "Av. Principal 123, Lima",
                birthDate = "01/01/1980",
                role = UserRole.ADMIN
            ),
            UserEntity(
                id = "student-001",
                email = "estudiante@test.com",
                password = "123456",
                firstName = "Juan",
                lastName = "Pérez García",
                dni = "87654321",
                phoneNumber = "987654321",
                address = "Jr. Los Álamos 456, Lima",
                birthDate = "15/05/1995",
                role = UserRole.STUDENT
            ),
            UserEntity(
                id = "instructor-001",
                email = "instructor@appbrevete.com",
                password = "instructor123",
                firstName = "Carlos",
                lastName = "Mendoza López",
                dni = "11223344",
                phoneNumber = "988777666",
                address = "Av. Los Pinos 789, Lima",
                birthDate = "20/08/1985",
                role = UserRole.INSTRUCTOR
            ),
            UserEntity(
                id = "examiner-001",
                email = "examinador@appbrevete.com",
                password = "examiner123",
                firstName = "María",
                lastName = "Rodríguez Silva",
                dni = "55667788",
                phoneNumber = "977666555",
                address = "Calle Las Flores 321, Lima",
                birthDate = "10/03/1978",
                role = UserRole.EXAMINER
            ),
            UserEntity(
                id = "doctor-001",
                email = "doctor@appbrevete.com",
                password = "doctor123",
                firstName = "Dr. Roberto",
                lastName = "Gómez Vargas",
                dni = "99887766",
                phoneNumber = "966555444",
                address = "Av. Salud 654, Lima",
                birthDate = "25/12/1970",
                role = UserRole.MEDICAL_DOCTOR
            )
        )
    }
    
    fun getInitialLicenseTypes(): List<LicenseTypeEntity> {
        return listOf(
            LicenseTypeEntity(
                id = "license-a1",
                name = "A1 - Motocicletas hasta 125cc",
                description = "Para conducir motocicletas de hasta 125cc de cilindrada",
                category = LicenseCategory.A1,
                ageRequirement = 18,
                medicalExamRequired = true,
                theoryExamRequired = true,
                practicalExamRequired = true,
                validityYears = 5,
                price = 45.0,
                isActive = true
            ),
            LicenseTypeEntity(
                id = "license-a2",
                name = "A2 - Motocicletas hasta 300cc",
                description = "Para conducir motocicletas de hasta 300cc de cilindrada",
                category = LicenseCategory.A2,
                ageRequirement = 18,
                medicalExamRequired = true,
                theoryExamRequired = true,
                practicalExamRequired = true,
                validityYears = 5,
                price = 55.0,
                isActive = true
            ),
            LicenseTypeEntity(
                id = "license-a3",
                name = "A3 - Motocicletas más de 300cc",
                description = "Para conducir motocicletas de más de 300cc de cilindrada",
                category = LicenseCategory.A3,
                ageRequirement = 18,
                medicalExamRequired = true,
                theoryExamRequired = true,
                practicalExamRequired = true,
                validityYears = 5,
                price = 65.0,
                isActive = true
            ),
            LicenseTypeEntity(
                id = "license-b1",
                name = "B1 - Vehículos particulares",
                description = "Para conducir automóviles particulares hasta 3500kg",
                category = LicenseCategory.B1,
                ageRequirement = 18,
                medicalExamRequired = true,
                theoryExamRequired = true,
                practicalExamRequired = true,
                validityYears = 5,
                price = 75.0,
                isActive = true
            ),
            LicenseTypeEntity(
                id = "license-b2",
                name = "B2 - Vehículos particulares con remolque",
                description = "Para conducir automóviles particulares hasta 3500kg con remolque",
                category = LicenseCategory.B2,
                ageRequirement = 21,
                medicalExamRequired = true,
                theoryExamRequired = true,
                practicalExamRequired = true,
                validityYears = 5,
                price = 85.0,
                isActive = true
            ),
            LicenseTypeEntity(
                id = "license-c1",
                name = "C1 - Vehículos de carga ligera",
                description = "Para conducir vehículos de carga hasta 7500kg",
                category = LicenseCategory.C1,
                ageRequirement = 21,
                medicalExamRequired = true,
                theoryExamRequired = true,
                practicalExamRequired = true,
                validityYears = 5,
                price = 95.0,
                isActive = true
            )
        )
    }
}
