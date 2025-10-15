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
            // Licencias de Motocicletas (Sistema Peruano)
            LicenseTypeEntity(
                id = "license-bii-a",
                name = "CATEGORÍA BII-A",
                description = "Motocicletas de dos y tres ruedas (con sidecar) para uso particular.",
                category = LicenseCategory.BII_A,
                ageRequirement = 18,
                medicalExamRequired = true,
                theoryExamRequired = true,
                practicalExamRequired = true,
                validityYears = 5,
                price = 400.0,
                isActive = true
            ),
            LicenseTypeEntity(
                id = "license-bii-b",
                name = "CATEGORÍA BII-B",
                description = "Los mismos vehículos que la licencia BII-A y para motocicletas de cualquier cilindraje.",
                category = LicenseCategory.BII_B,
                ageRequirement = 18,
                medicalExamRequired = true,
                theoryExamRequired = true,
                practicalExamRequired = true,
                validityYears = 5,
                price = 400.0,
                isActive = true
            ),
            LicenseTypeEntity(
                id = "license-bii-c",
                name = "CATEGORÍA BII-C",
                description = "Para mototaxis y trimotos destinadas al transporte de pasajeros.",
                category = LicenseCategory.BII_C,
                ageRequirement = 21,
                medicalExamRequired = true,
                theoryExamRequired = true,
                practicalExamRequired = true,
                validityYears = 3,
                price = 650.0,
                isActive = true
            ),
            // Licencias de Automóviles (Sistema Peruano)
            LicenseTypeEntity(
                id = "license-a-i",
                name = "CATEGORÍA A-I",
                description = "Vehículos particulares (Sedán, Coupé, Pick-up, Station Wagon, etc.). Máx. 9 asientos y 3.5 toneladas.",
                category = LicenseCategory.A_I,
                ageRequirement = 18,
                medicalExamRequired = true,
                theoryExamRequired = true,
                practicalExamRequired = true,
                validityYears = 5,
                price = 300.0,
                isActive = true
            ),
            LicenseTypeEntity(
                id = "license-a-iia",
                name = "CATEGORÍA A-IIa",
                description = "Vehículos de transporte de personas y mercancías hasta 3.5 toneladas (Taxis, ambulancias, furgonetas, y todos los de la cat. A-I).",
                category = LicenseCategory.A_IIA,
                ageRequirement = 21,
                medicalExamRequired = true,
                theoryExamRequired = true,
                practicalExamRequired = true,
                validityYears = 5,
                price = 350.0,
                isActive = true
            ),
            LicenseTypeEntity(
                id = "license-a-iib",
                name = "CATEGORÍA A-IIb",
                description = "Minibuses (máx. 16 asientos, 4 toneladas), furgonetas (máx. 7 toneladas), y todos los de A-I y A-IIa.",
                category = LicenseCategory.A_IIB,
                ageRequirement = 21,
                medicalExamRequired = true,
                theoryExamRequired = true,
                practicalExamRequired = true,
                validityYears = 5,
                price = 380.0,
                isActive = true
            ),
            LicenseTypeEntity(
                id = "license-a-iiia",
                name = "CATEGORÍA A-IIIa",
                description = "Ómnibus, buses y vehículos de transporte de personas (más de 6 toneladas).",
                category = LicenseCategory.A_IIIA,
                ageRequirement = 24,
                medicalExamRequired = true,
                theoryExamRequired = true,
                practicalExamRequired = true,
                validityYears = 3,
                price = 450.0,
                isActive = true
            ),
            LicenseTypeEntity(
                id = "license-a-iiib",
                name = "CATEGORÍA A-IIIb",
                description = "Vehículos de transporte de carga (camiones rígidos de más de 12 toneladas) y todos los de A-I y A-IIa.",
                category = LicenseCategory.A_IIIB,
                ageRequirement = 24,
                medicalExamRequired = true,
                theoryExamRequired = true,
                practicalExamRequired = true,
                validityYears = 3,
                price = 480.0,
                isActive = true
            ),
            LicenseTypeEntity(
                id = "license-a-iiic",
                name = "CATEGORÍA A-IIIc",
                description = "Todos los vehículos de las categorías A-I, A-IIa, A-IIb, A-IIIa y A-IIIb.",
                category = LicenseCategory.A_IIIC,
                ageRequirement = 27,
                medicalExamRequired = true,
                theoryExamRequired = true,
                practicalExamRequired = true,
                validityYears = 3,
                price = 520.0,
                isActive = true
            )
        )
    }
}
