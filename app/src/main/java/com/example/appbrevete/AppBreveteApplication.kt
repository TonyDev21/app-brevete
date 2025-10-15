package com.example.appbrevete

import android.app.Application
import com.example.appbrevete.data.local.AppBreveteDatabase
import com.example.appbrevete.data.local.DatabaseInitializer
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class AppBreveteApplication : Application() {
    
    @Inject
    lateinit var database: AppBreveteDatabase
    
    override fun onCreate() {
        super.onCreate()
        
        // Inicializar datos de prueba
        CoroutineScope(Dispatchers.IO).launch {
            initializeDatabase()
        }
    }
    
    private suspend fun initializeDatabase() {
        try {
            // Siempre forzar actualización de tipos de licencia para el sistema peruano
            println("DEBUG: Reinicializando tipos de licencia...")
            database.licenseTypeDao().deleteAllLicenseTypes()
            val initialLicenseTypes = DatabaseInitializer.getInitialLicenseTypes()
            database.licenseTypeDao().insertLicenseTypes(initialLicenseTypes)
            println("DEBUG: Insertados ${initialLicenseTypes.size} tipos de licencia")
            
            // Verificar si ya hay usuarios en la base de datos
            val userCount = database.userDao().getUserCountByRole(com.example.appbrevete.domain.model.UserRole.ADMIN)
            
            if (userCount == 0) {
                // Solo insertar usuarios si la base de datos está vacía
                val initialUsers = DatabaseInitializer.getInitialUsers()
                initialUsers.forEach { user ->
                    database.userDao().insertUser(user)
                }
                println("DEBUG: Insertados ${initialUsers.size} usuarios")
            }
            
        } catch (e: Exception) {
            // Log del error pero no crashear la app
            e.printStackTrace()
            println("DEBUG: Error al inicializar base de datos: ${e.message}")
        }
    }
}
