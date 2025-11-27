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
            println("DEBUG: Reinicializando tipos de licencia...")
            database.licenseTypeDao().deleteAllLicenseTypes()
            val initialLicenseTypes = DatabaseInitializer.getInitialLicenseTypes()
            database.licenseTypeDao().insertLicenseTypes(initialLicenseTypes)
            println("DEBUG: Insertados ${initialLicenseTypes.size} tipos de licencia")

            val userCount = database.userDao().getUserCountByRole(com.example.appbrevete.domain.model.UserRole.ADMIN)
            
            if (userCount == 0) {
                val initialUsers = DatabaseInitializer.getInitialUsers()
                initialUsers.forEach { user ->
                    database.userDao().insertUser(user)
                }
                println("DEBUG: Insertados ${initialUsers.size} usuarios")
            }
            
        } catch (e: Exception) {
            e.printStackTrace()
            println("DEBUG: Error al inicializar base de datos: ${e.message}")
        }
    }
}
