package com.example.appbrevete.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import com.example.appbrevete.presentation.admin.AdminScreen
import com.example.appbrevete.presentation.admin.AdminManageAppointmentsScreen
import com.example.appbrevete.presentation.admin.AdminManageClassesScreenNew
import com.example.appbrevete.presentation.admin.MedicalEvaluationScreen
import com.example.appbrevete.presentation.admin.DrivingClassEvaluationScreen
import com.example.appbrevete.presentation.admin.CreateAdminUserScreen
import com.example.appbrevete.presentation.appointments.AppointmentsScreen
import com.example.appbrevete.presentation.appointments.CreateAppointmentScreen
import com.example.appbrevete.presentation.appointments.DateTimeSelectionScreen
import com.example.appbrevete.presentation.appointments.AppointmentSummaryScreen
import com.example.appbrevete.presentation.appointments.EditAppointmentScreen
import com.example.appbrevete.presentation.classes.ClassesScreen
import com.example.appbrevete.presentation.classes.CreateDrivingClassScreen
import com.example.appbrevete.presentation.classes.EditDrivingClassScreen
import com.example.appbrevete.presentation.exam.ExamSimulatorScreen
import com.example.appbrevete.presentation.home.HomeScreen
import com.example.appbrevete.presentation.license.LicenseTypesScreen
import com.example.appbrevete.presentation.rules.TrafficRulesScreen
import com.example.appbrevete.presentation.rules.CarCategoriesScreen
import com.example.appbrevete.presentation.rules.MotorcycleCategoriesScreen
import com.example.appbrevete.presentation.rules.RulesPdfViewerScreen
import com.example.appbrevete.presentation.rules.MotorcycleRulesPdfViewerScreen
import com.example.appbrevete.presentation.rules.TrafficRuleQuizScreen
import com.example.appbrevete.domain.model.VehicleCategory
import com.example.appbrevete.domain.model.CarLicenseCategory
import com.example.appbrevete.domain.model.MotorcycleLicenseCategory
import com.example.appbrevete.presentation.license.LicenseDetailScreen
import com.example.appbrevete.presentation.profile.ProfileScreen
import com.example.appbrevete.presentation.profile.EditProfileScreen
import com.example.appbrevete.presentation.auth.LoginScreen
import com.example.appbrevete.presentation.auth.RegisterScreen
import com.example.appbrevete.presentation.viewmodel.AuthViewModel
import com.example.appbrevete.presentation.viewmodel.AuthState
import com.example.appbrevete.domain.model.User
import com.example.appbrevete.domain.model.UserRole

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()
    
    when (authState) {
        is AuthState.Loading -> {
            // Pantalla de carga
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is AuthState.Authenticated -> {
            // Usuario autenticado - mostrar app principal
            currentUser?.let { user ->
                MainAppNavigation(
                    currentUser = user,
                    onLogout = { authViewModel.logout() }
                )
            }
        }
        is AuthState.Unauthenticated, is AuthState.Error -> {
            // Usuario no autenticado - mostrar pantallas de auth
            AuthNavigation(authViewModel = authViewModel)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppNavigation(
    currentUser: User,
    onLogout: () -> Unit
) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val currentUserFromAuth by authViewModel.currentUser.collectAsStateWithLifecycle()
    
    // Always use the most current user data
    val userToUse = currentUserFromAuth ?: currentUser
    
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(280.dp)
            ) {
                DrawerContent(
                    currentUser = userToUse,
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    onLogout = {
                        onLogout()
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { 
                        Text(
                            text = getScreenTitle(currentRoute),
                            fontWeight = FontWeight.Bold
                        ) 
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { 
                                scope.launch {
                                    if (drawerState.isClosed) {
                                        drawerState.open()
                                    } else {
                                        drawerState.close()
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    }
                )
            }
        ) { paddingValues ->
            // Determinar la pantalla de inicio según el rol
            val startDestination = if (userToUse.role == UserRole.ADMIN) {
                Screen.Admin.route
            } else {
                Screen.Home.route
            }
            
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        currentUser = userToUse,
                        onNavigateToAppointments = {
                            navController.navigate(Screen.Appointments.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onNavigateToClasses = {
                            navController.navigate(Screen.Classes.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onNavigateToProfile = {
                            navController.navigate(Screen.Profile.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onNavigateToLicenses = {
                            navController.navigate(Screen.LicenseTypes.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onNavigateToAdmin = {
                            navController.navigate(Screen.Admin.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
                composable(Screen.Appointments.route) {
                    AppointmentsScreen(
                        userId = currentUser.id,
                        onCreateAppointment = {
                            navController.navigate(Screen.CreateAppointment.route)
                        },
                        onEditAppointment = { appointmentId ->
                            navController.navigate("${Screen.EditAppointment.route}/$appointmentId/${currentUser.id}")
                        }
                    )
                }
                composable(Screen.CreateAppointment.route) {
                    CreateAppointmentScreen(
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToDateSelection = { licenseType ->
                            navController.navigate("${Screen.DateTimeSelection.route}/${licenseType.id}/${licenseType.category}/${licenseType.name}/${licenseType.price}/${licenseType.ageRequirement}/${licenseType.validityYears}")
                        }
                    )
                }
                composable("${Screen.DateTimeSelection.route}/{licenseTypeId}/{category}/{name}/{price}/{ageRequirement}/{validityYears}") { backStackEntry ->
                    val licenseTypeId = backStackEntry.arguments?.getString("licenseTypeId") ?: ""
                    val categoryString = backStackEntry.arguments?.getString("category") ?: ""
                    val name = backStackEntry.arguments?.getString("name") ?: ""
                    val price = backStackEntry.arguments?.getString("price")?.toDoubleOrNull() ?: 0.0
                    val ageRequirement = backStackEntry.arguments?.getString("ageRequirement")?.toIntOrNull() ?: 18
                    val validityYears = backStackEntry.arguments?.getString("validityYears")?.toIntOrNull() ?: 5
                    
                    val category = try {
                        com.example.appbrevete.domain.model.LicenseCategory.valueOf(categoryString)
                    } catch (e: Exception) {
                        com.example.appbrevete.domain.model.LicenseCategory.BII_A
                    }
                    
                    val licenseType = com.example.appbrevete.domain.model.LicenseType(
                        id = licenseTypeId,
                        category = category,
                        name = name,
                        description = "",
                        ageRequirement = ageRequirement,
                        validityYears = validityYears,
                        price = price,
                        isActive = true
                    )
                    
                    DateTimeSelectionScreen(
                        licenseType = licenseType,
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToSummary = { selectedLicenseType, selectedDate, selectedTime ->
                            // Codificar los parámetros para URL segura
                            val encodedDate = java.net.URLEncoder.encode(selectedDate, "UTF-8")
                            val encodedTime = java.net.URLEncoder.encode(selectedTime, "UTF-8")
                            navController.navigate("${Screen.AppointmentSummary.route}/${selectedLicenseType.id}/${selectedLicenseType.category}/${selectedLicenseType.name}/${selectedLicenseType.price}/${encodedDate}/${encodedTime}")
                        }
                    )
                }
                composable("${Screen.AppointmentSummary.route}/{licenseTypeId}/{category}/{name}/{price}/{selectedDate}/{selectedTime}") { backStackEntry ->
                    val licenseTypeId = backStackEntry.arguments?.getString("licenseTypeId") ?: ""
                    val categoryString = backStackEntry.arguments?.getString("category") ?: ""
                    val name = backStackEntry.arguments?.getString("name") ?: ""
                    val price = backStackEntry.arguments?.getString("price")?.toDoubleOrNull() ?: 0.0
                    val encodedDate = backStackEntry.arguments?.getString("selectedDate") ?: ""
                    val encodedTime = backStackEntry.arguments?.getString("selectedTime") ?: ""
                    
                    // Decodificar los parámetros
                    val selectedDate = try {
                        java.net.URLDecoder.decode(encodedDate, "UTF-8")
                    } catch (e: Exception) {
                        encodedDate
                    }
                    
                    val selectedTime = try {
                        java.net.URLDecoder.decode(encodedTime, "UTF-8")
                    } catch (e: Exception) {
                        encodedTime
                    }
                    
                    val category = try {
                        com.example.appbrevete.domain.model.LicenseCategory.valueOf(categoryString)
                    } catch (e: Exception) {
                        com.example.appbrevete.domain.model.LicenseCategory.BII_A
                    }
                    
                    val licenseType = com.example.appbrevete.domain.model.LicenseType(
                        id = licenseTypeId,
                        category = category,
                        name = name,
                        description = "",
                        ageRequirement = 18,
                        validityYears = 5,
                        price = price,
                        isActive = true
                    )
                    
                    AppointmentSummaryScreen(
                        userId = currentUser.id,
                        licenseType = licenseType,
                        selectedDate = selectedDate,
                        selectedTime = selectedTime,
                        onNavigateBack = { navController.popBackStack() },
                        onAppointmentCreated = {
                            println("AppNavigation: onAppointmentCreated called, navigating to appointments")
                            navController.navigate(Screen.Appointments.route) {
                                popUpTo(Screen.CreateAppointment.route) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    )
                }
                composable("${Screen.EditAppointment.route}/{appointmentId}/{userId}") { backStackEntry ->
                    val appointmentId = backStackEntry.arguments?.getString("appointmentId") ?: ""
                    val userId = backStackEntry.arguments?.getString("userId") ?: ""
                    
                    EditAppointmentScreen(
                        appointmentId = appointmentId,
                        userId = userId,
                        onNavigateBack = { 
                            println("AppNavigation: onNavigateBack called")
                            navController.popBackStack() 
                        },
                        onAppointmentUpdated = {
                            println("AppNavigation: onAppointmentUpdated called, navigating to appointments")
                            navController.navigate(Screen.Appointments.route) {
                                popUpTo(Screen.EditAppointment.route) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    )
                }
                composable(Screen.Classes.route) {
                    ClassesScreen(
                        userId = currentUser.id,
                        onNavigateToBooking = {
                            navController.navigate(Screen.ClassBooking.route)
                        },
                        onNavigateToEdit = { classId ->
                            navController.navigate("${Screen.EditDrivingClass.route}/$classId")
                        }
                    )
                }
                composable(Screen.ClassBooking.route) {
                    CreateDrivingClassScreen(
                        userId = currentUser.id,
                        onNavigateBack = {
                            navController.popBackStack()
                        }
                    )
                }
                composable("${Screen.EditDrivingClass.route}/{classId}") { backStackEntry ->
                    val classId = backStackEntry.arguments?.getString("classId") ?: ""
                    
                    EditDrivingClassScreen(
                        userId = currentUser.id,
                        classId = classId,
                        onNavigateBack = {
                            navController.popBackStack()
                        }
                    )
                }
                composable(Screen.TrafficRules.route) {
                    TrafficRulesScreen(
                        onNavigateToMotorcycleCategories = {
                            navController.navigate(Screen.MotorcycleCategories.route)
                        },
                        onNavigateToCarCategories = {
                            navController.navigate(Screen.CarCategories.route)
                        }
                    )
                }
                composable(Screen.CarCategories.route) {
                    CarCategoriesScreen(
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToPdf = { category ->
                            navController.navigate("${Screen.RulesPdfViewer.route}/CAR/${category.name}")
                        }
                    )
                }
                composable(Screen.MotorcycleCategories.route) {
                    MotorcycleCategoriesScreen(
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToPdf = { category ->
                            navController.navigate("${Screen.MotorcycleRulesPdfViewer.route}/${category.name}")
                        }
                    )
                }
                composable("${Screen.RulesPdfViewer.route}/{vehicleType}/{category}") { backStackEntry ->
                    val vehicleTypeString = backStackEntry.arguments?.getString("vehicleType") ?: "CAR"
                    val categoryString = backStackEntry.arguments?.getString("category")
                    
                    val vehicleType = try {
                        VehicleCategory.valueOf(vehicleTypeString)
                    } catch (e: Exception) {
                        VehicleCategory.CAR
                    }
                    
                    val category = if (categoryString == "null" || categoryString == null) {
                        null
                    } else {
                        try {
                            CarLicenseCategory.valueOf(categoryString)
                        } catch (e: Exception) {
                            null
                        }
                    }
                    
                    RulesPdfViewerScreen(
                        vehicleType = vehicleType,
                        category = category,
                        navController = navController
                    )
                }
                composable("${Screen.MotorcycleRulesPdfViewer.route}/{category}") { backStackEntry ->
                    val categoryString = backStackEntry.arguments?.getString("category") ?: "B_IIA"
                    
                    val category = try {
                        MotorcycleLicenseCategory.valueOf(categoryString)
                    } catch (e: Exception) {
                        MotorcycleLicenseCategory.B_IIA
                    }
                    
                    MotorcycleRulesPdfViewerScreen(
                        category = category,
                        navController = navController
                    )
                }
                composable("${Screen.TrafficRuleQuiz.route}/{vehicleType}/{category}") { backStackEntry ->
                    val vehicleTypeString = backStackEntry.arguments?.getString("vehicleType") ?: "CAR"
                    val categoryString = backStackEntry.arguments?.getString("category")
                    
                    val vehicleType = try {
                        VehicleCategory.valueOf(vehicleTypeString)
                    } catch (e: Exception) {
                        VehicleCategory.CAR
                    }
                    
                    val category = if (categoryString == "null" || categoryString == null) {
                        null
                    } else {
                        try {
                            CarLicenseCategory.valueOf(categoryString)
                        } catch (e: Exception) {
                            null
                        }
                    }
                    
                    TrafficRuleQuizScreen(
                        vehicleType = vehicleType,
                        category = category,
                        onNavigateBack = { navController.popBackStack() },
                        onQuizCompleted = { score ->
                            // Navegar de vuelta con el resultado
                            navController.popBackStack()
                        }
                    )
                }
                composable(Screen.LicenseTypes.route) {
                    LicenseTypesScreen(
                        onNavigateToLicenseDetail = { licenseType ->
                            val encodedId = java.net.URLEncoder.encode(licenseType.id, "UTF-8")
                            val encodedName = java.net.URLEncoder.encode(licenseType.name, "UTF-8")
                            val encodedDescription = java.net.URLEncoder.encode(licenseType.description, "UTF-8")
                            navController.navigate("${Screen.LicenseDetail.route}/$encodedId/${licenseType.category}/$encodedName/$encodedDescription/${licenseType.ageRequirement}/${licenseType.validityYears}/${licenseType.price}")
                        }
                    )
                }
                composable("${Screen.LicenseDetail.route}/{licenseId}/{category}/{name}/{description}/{ageRequirement}/{validityYears}/{price}") { backStackEntry ->
                    val licenseId = backStackEntry.arguments?.getString("licenseId") ?: ""
                    val categoryString = backStackEntry.arguments?.getString("category") ?: ""
                    val encodedName = backStackEntry.arguments?.getString("name") ?: ""
                    val encodedDescription = backStackEntry.arguments?.getString("description") ?: ""
                    val ageRequirement = backStackEntry.arguments?.getString("ageRequirement")?.toIntOrNull() ?: 18
                    val validityYears = backStackEntry.arguments?.getString("validityYears")?.toIntOrNull() ?: 5
                    val price = backStackEntry.arguments?.getString("price")?.toDoubleOrNull() ?: 0.0
                    
                    // Decodificar los parámetros
                    val decodedId = try {
                        java.net.URLDecoder.decode(licenseId, "UTF-8")
                    } catch (e: Exception) {
                        licenseId
                    }
                    
                    val decodedName = try {
                        java.net.URLDecoder.decode(encodedName, "UTF-8")
                    } catch (e: Exception) {
                        encodedName
                    }
                    
                    val decodedDescription = try {
                        java.net.URLDecoder.decode(encodedDescription, "UTF-8")
                    } catch (e: Exception) {
                        encodedDescription
                    }
                    
                    val category = try {
                        com.example.appbrevete.domain.model.LicenseCategory.valueOf(categoryString)
                    } catch (e: Exception) {
                        com.example.appbrevete.domain.model.LicenseCategory.BII_A
                    }
                    
                    val licenseType = com.example.appbrevete.domain.model.LicenseType(
                        id = decodedId,
                        name = decodedName,
                        description = decodedDescription,
                        category = category,
                        ageRequirement = ageRequirement,
                        validityYears = validityYears,
                        price = price,
                        isActive = true
                    )
                    
                    LicenseDetailScreen(
                        licenseType = licenseType,
                        onNavigateBack = { navController.popBackStack() },
                        onCreateAppointment = {
                            // Navegar a crear cita con el tipo de licencia seleccionado
                            navController.navigate(Screen.CreateAppointment.route)
                        }
                    )
                }
                composable(Screen.Profile.route) {
                    ProfileScreen(
                        currentUser = userToUse,
                        onLogout = onLogout,
                        onNavigateToEditProfile = {
                            navController.navigate(Screen.EditProfile.route)
                        }
                    )
                }
                composable(Screen.EditProfile.route) {
                    EditProfileScreen(
                        currentUser = userToUse,
                        onNavigateBack = { navController.popBackStack() },
                        onProfileUpdated = {
                            navController.popBackStack()
                        }
                    )
                }
                if (userToUse.role == com.example.appbrevete.domain.model.UserRole.ADMIN) {
                    composable(Screen.Admin.route) {
                        AdminScreen(
                            onNavigateToManageAppointments = {
                                navController.navigate(Screen.AdminManageAppointments.route)
                            },
                            onNavigateToManageClasses = {
                                navController.navigate(Screen.AdminManageClasses.route)
                            },
                            onNavigateToCreateUser = {
                                navController.navigate(Screen.CreateAdminUser.route)
                            }
                        )
                    }
                    composable(Screen.AdminManageAppointments.route) {
                        AdminManageAppointmentsScreen(
                            onNavigateBack = { navController.popBackStack() },
                            onNavigateToEvaluation = { appointmentId ->
                                navController.navigate(Screen.MedicalEvaluation.createRoute(appointmentId))
                            }
                        )
                    }
                    composable(Screen.MedicalEvaluation.route) { backStackEntry ->
                        val appointmentId = backStackEntry.arguments?.getString("appointmentId") ?: ""
                        MedicalEvaluationScreen(
                            appointmentId = appointmentId,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable(Screen.AdminManageClasses.route) {
                        AdminManageClassesScreenNew(
                            onNavigateBack = { navController.popBackStack() },
                            onClassClick = { classId ->
                                navController.navigate(Screen.DrivingClassEvaluation.createRoute(classId))
                            }
                        )
                    }
                    composable(Screen.DrivingClassEvaluation.route) { backStackEntry ->
                        val classId = backStackEntry.arguments?.getString("classId") ?: ""
                        DrivingClassEvaluationScreen(
                            classId = classId,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable(Screen.CreateAdminUser.route) {
                        CreateAdminUserScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerContent(
    currentUser: User,
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header del drawer con información del usuario
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "¡Hola, ${currentUser.firstName}!",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = getRoleDisplayName(currentUser.role),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        
        Divider()
        Spacer(modifier = Modifier.height(8.dp))
        
        // Items del menú
        val menuItems = getMenuItemsForRole(currentUser.role)
        menuItems.forEach { item ->
            NavigationDrawerItem(
                icon = { 
                    Icon(
                        imageVector = item.icon, 
                        contentDescription = item.title
                    ) 
                },
                label = { 
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.bodyLarge
                    ) 
                },
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Botón de cerrar sesión al final
        Divider()
        Spacer(modifier = Modifier.height(8.dp))
        
        NavigationDrawerItem(
            icon = { 
                Icon(
                    imageVector = Icons.Default.ExitToApp, 
                    contentDescription = "Cerrar sesión"
                ) 
            },
            label = { 
                Text(
                    text = "Cerrar sesión",
                    style = MaterialTheme.typography.bodyLarge
                ) 
            },
            selected = false,
            onClick = onLogout,
            colors = NavigationDrawerItemDefaults.colors(
                unselectedIconColor = MaterialTheme.colorScheme.error,
                unselectedTextColor = MaterialTheme.colorScheme.error
            )
        )
    }
}

@Composable
fun AuthNavigation(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToHome = {
                    // La navegación al home se maneja automáticamente por el estado de auth
                },
                authViewModel = authViewModel
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    // Esta navegación debe ser manejada por el estado de auth a nivel superior
                    // No hacemos nada aquí, el cambio de estado manejará la navegación
                },
                authViewModel = authViewModel
            )
        }
    }
}

data class MenuItem(
    val route: String,
    val title: String,
    val icon: ImageVector
)

fun getMenuItemsForRole(role: UserRole): List<MenuItem> {
    return when (role) {
        UserRole.ADMIN -> listOf(
            MenuItem(Screen.Admin.route, "Inicio", Icons.Default.Home),
            MenuItem(Screen.AdminManageAppointments.route, "Citas Médicas", Icons.Default.Event),
            MenuItem(Screen.AdminManageClasses.route, "Clases de Manejo", Icons.Default.DirectionsCar),
            MenuItem(Screen.Profile.route, "Perfil", Icons.Default.Person)
        )
        else -> {
            val baseItems = listOf(
                MenuItem(Screen.Home.route, "Inicio", Icons.Default.Home),
                MenuItem(Screen.Appointments.route, "Citas Médicas", Icons.Default.Event),
                MenuItem(Screen.TrafficRules.route, "Examen de Reglas", Icons.Default.Quiz),
                MenuItem(Screen.Classes.route, "Clases de Manejo", Icons.Default.DirectionsCar),
                MenuItem(Screen.LicenseTypes.route, "Licencias", Icons.Default.CreditCard),
                MenuItem(Screen.Profile.route, "Perfil", Icons.Default.Person)
            )
            baseItems
        }
    }
}

fun getScreenTitle(route: String?): String {
    return when (route) {
        Screen.Home.route -> "Inicio"
        Screen.Appointments.route -> "Citas Médicas"
        Screen.Classes.route -> "Clases de Manejo"
        Screen.ExamSimulator.route -> "Simulador de Examen"
        Screen.TrafficRules.route -> "Examen de Reglas"
        Screen.CarCategories.route -> "Categorías de Automóvil"
        Screen.RulesPdfViewer.route -> "Manual de Reglas"
        Screen.TrafficRuleQuiz.route -> "Examen de Reglas"
        Screen.LicenseTypes.route -> "Tipos de Licencia"
        Screen.Profile.route -> "Perfil"
        Screen.EditProfile.route -> "Editar Perfil"
        Screen.Admin.route -> "Inicio"
        Screen.AdminManageAppointments.route -> "Gestionar Citas Médicas"
        Screen.AdminManageClasses.route -> "Gestionar Clases de Manejo"
        Screen.Login.route -> "Iniciar Sesión"
        Screen.Register.route -> "Registro"
        else -> "App Brevete"
    }
}

fun getRoleDisplayName(role: UserRole): String {
    return when (role) {
        UserRole.STUDENT -> "Estudiante"
        UserRole.INSTRUCTOR -> "Instructor"
        UserRole.EXAMINER -> "Examinador"
        UserRole.ADMIN -> "Administrador"
        UserRole.MEDICAL_DOCTOR -> "Doctor Médico"
    }
}

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Appointments : Screen("appointments")
    object CreateAppointment : Screen("create_appointment")
    object EditAppointment : Screen("edit_appointment")
    object DateTimeSelection : Screen("datetime_selection")
    object AppointmentSummary : Screen("appointment_summary")
    object Classes : Screen("classes")
    object ClassBooking : Screen("class_booking")
    object EditDrivingClass : Screen("edit_driving_class")
    object ExamSimulator : Screen("exam_simulator")
    object TrafficRules : Screen("traffic_rules")
    object CarCategories : Screen("car_categories")
    object MotorcycleCategories : Screen("motorcycle_categories")
    object RulesPdfViewer : Screen("rules_pdf_viewer")
    object MotorcycleRulesPdfViewer : Screen("motorcycle_rules_pdf_viewer")
    object TrafficRuleQuiz : Screen("traffic_rule_quiz")
    object LicenseTypes : Screen("license_types")
    object LicenseDetail : Screen("license_detail")
    object Profile : Screen("profile")
    object EditProfile : Screen("edit_profile")
    object Admin : Screen("admin")
    object AdminManageAppointments : Screen("admin_manage_appointments")
    object AdminManageClasses : Screen("admin_manage_classes")
    object MedicalEvaluation : Screen("medical_evaluation/{appointmentId}") {
        fun createRoute(appointmentId: String) = "medical_evaluation/$appointmentId"
    }
    object DrivingClassEvaluation : Screen("driving_class_evaluation/{classId}") {
        fun createRoute(classId: String) = "driving_class_evaluation/$classId"
    }
    object CreateAdminUser : Screen("create_admin_user")
    object Login : Screen("login")
    object Register : Screen("register")
}
