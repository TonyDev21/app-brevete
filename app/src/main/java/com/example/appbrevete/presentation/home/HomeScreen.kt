package com.example.appbrevete.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appbrevete.domain.model.User
import com.example.appbrevete.domain.model.Appointment
import com.example.appbrevete.domain.model.DrivingClass
import com.example.appbrevete.domain.model.UserRole
import com.example.appbrevete.presentation.viewmodel.HomeViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import android.os.Build
import androidx.annotation.RequiresApi

// Data class para unificar citas y clases próximas
data class UpcomingEvent(
    val title: String,
    val date: String,
    val time: String,
    val status: String,
    val type: String // "appointment" o "class"
)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    currentUser: User,
    onNavigateToAppointments: () -> Unit = {},
    onNavigateToClasses: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToLicenses: () -> Unit = {},
    onNavigateToAdmin: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    // Cargar datos cuando se inicializa la pantalla
    LaunchedEffect(currentUser.id) {
        viewModel.loadUserData(currentUser.id)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            WelcomeCard(currentUser = currentUser)
        }
        
        if (uiState.isLoading) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        } else {
            item {
                QuickActionsCard(
                    currentUser = currentUser,
                    onNavigateToAppointments = onNavigateToAppointments,
                    onNavigateToClasses = onNavigateToClasses,
                    onNavigateToProfile = onNavigateToProfile,
                    onNavigateToLicenses = onNavigateToLicenses,
                    onNavigateToAdmin = onNavigateToAdmin
                )
            }
            
            item {
                StatsCard(
                    totalAppointments = uiState.totalAppointments,
                    completedAppointments = uiState.completedAppointments,
                    pendingAppointments = uiState.pendingAppointments,
                    totalClasses = uiState.totalClasses,
                    completedClasses = uiState.completedClasses,
                    pendingClasses = uiState.pendingClasses
                )
            }
            
            item {
                UpcomingAppointmentsCard(
                    appointments = uiState.upcomingAppointments,
                    drivingClasses = uiState.upcomingClasses
                )
            }
            
            item {
                RecentActivitiesCard()
            }
        }
        
        if (uiState.errorMessage != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = "Error: ${uiState.errorMessage}",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}

@Composable
fun WelcomeCard(currentUser: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "¡Hola, ${currentUser.firstName}!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Bienvenido a tu panel de control de App Brevete",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun QuickActionsCard(
    currentUser: User,
    onNavigateToAppointments: () -> Unit,
    onNavigateToClasses: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToLicenses: () -> Unit,
    onNavigateToAdmin: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Acciones Rápidas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                items(getQuickActions(
                    currentUser = currentUser,
                    onNavigateToAppointments = onNavigateToAppointments,
                    onNavigateToClasses = onNavigateToClasses,
                    onNavigateToProfile = onNavigateToProfile,
                    onNavigateToLicenses = onNavigateToLicenses,
                    onNavigateToAdmin = onNavigateToAdmin
                )) { action ->
                    QuickActionItem(
                        icon = action.icon,
                        title = action.title,
                        onClick = action.onClick
                    )
                }
            }
        }
    }
}

@Composable
fun StatsCard(
    totalAppointments: Int,
    completedAppointments: Int,
    pendingAppointments: Int,
    totalClasses: Int,
    completedClasses: Int,
    pendingClasses: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Estadísticas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    title = "Total",
                    value = (totalAppointments + totalClasses).toString(),
                    icon = Icons.Filled.Event
                )
                StatItem(
                    title = "Completadas",
                    value = (completedAppointments + completedClasses).toString(),
                    icon = Icons.Filled.CheckCircle
                )
                StatItem(
                    title = "Pendientes",
                    value = (pendingAppointments + pendingClasses).toString(),
                    icon = Icons.Filled.Schedule
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UpcomingAppointmentsCard(appointments: List<Appointment>, drivingClasses: List<DrivingClass>) {
    // Combinar citas y clases próximas
    val upcomingEvents = mutableListOf<UpcomingEvent>()
    
    // Agregar citas médicas
    appointments.forEach { appointment ->
        upcomingEvents.add(
            UpcomingEvent(
                title = getAppointmentTypeDisplayName(appointment.type.name),
                date = formatDate(appointment.scheduledDate),
                time = appointment.scheduledTime,
                status = getAppointmentStatusDisplayName(appointment.status.name),
                type = "appointment"
            )
        )
    }
    
    // Agregar clases de manejo
    drivingClasses.forEach { drivingClass ->
        upcomingEvents.add(
            UpcomingEvent(
                title = getDrivingClassTypeDisplayName(drivingClass.packageType),
                date = formatDate(drivingClass.scheduledDate),
                time = drivingClass.scheduledTime,
                status = getDrivingClassStatusDisplayName(drivingClass.status),
                type = "class"
            )
        )
    }
    
    // Ordenar por fecha
    val sortedEvents = upcomingEvents.sortedBy { it.date }
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Próximas Citas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            if (sortedEvents.isEmpty()) {
                Text(
                    text = "No tienes citas próximas",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            } else {
                // Lista de citas próximas - mostrar todas
                sortedEvents.forEachIndexed { index, event ->
                    AppointmentItem(
                        title = event.title,
                        date = event.date,
                        time = event.time,
                        status = event.status
                    )
                    if (index < sortedEvents.size - 1) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun RecentActivitiesCard() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Actividades Recientes",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            // Placeholder para actividades recientes
            Text(
                text = "No hay actividades recientes",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun QuickActionItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(100.dp)
            .padding(vertical = 8.dp)
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(64.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            maxLines = 2,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun StatItem(
    title: String,
    value: String,
    icon: ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(100.dp)
            .padding(vertical = 8.dp)
    ) {
        Surface(
            modifier = Modifier.size(64.dp),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = title,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun AppointmentItem(
    title: String,
    date: String,
    time: String,
    status: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
            Text(
                text = "$date - $time",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Surface(
            color = when (status) {
                "Programada" -> MaterialTheme.colorScheme.primaryContainer
                "Completada" -> MaterialTheme.colorScheme.tertiaryContainer
                "Cancelada" -> MaterialTheme.colorScheme.errorContainer
                else -> MaterialTheme.colorScheme.surfaceVariant
            },
            shape = MaterialTheme.shapes.small
        ) {
            Text(
                text = status,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                fontSize = 12.sp,
                color = when (status) {
                    "Programada" -> MaterialTheme.colorScheme.onPrimaryContainer
                    "Completada" -> MaterialTheme.colorScheme.onTertiaryContainer
                    "Cancelada" -> MaterialTheme.colorScheme.onErrorContainer
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}

// Función auxiliar para formatear fechas
@RequiresApi(Build.VERSION_CODES.O)
private fun formatDate(timestamp: Long): String {
    return try {
        val instant = java.time.Instant.ofEpochMilli(timestamp)
        val localDate = instant.atZone(java.time.ZoneId.systemDefault()).toLocalDate()
        val formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM", Locale("es", "ES"))
        localDate.format(formatter)
    } catch (e: Exception) {
        "Fecha no disponible"
    }
}

// Función auxiliar para obtener el nombre del tipo de cita
private fun getAppointmentTypeDisplayName(type: String): String {
    return when (type) {
        "MEDICAL_EXAM" -> "Examen Médico"
        "THEORY_EXAM" -> "Examen Teórico"
        "PRACTICAL_EXAM" -> "Examen Práctico"
        "DRIVING_CLASS" -> "Clase de Manejo"
        "CONSULTATION" -> "Consulta"
        else -> type
    }
}

// Función auxiliar para obtener el estado de la cita
private fun getAppointmentStatusDisplayName(status: String): String {
    return when (status) {
        "SCHEDULED" -> "Programada"
        "CONFIRMED" -> "Confirmada"
        "COMPLETED" -> "Completada"
        "CANCELLED" -> "Cancelada"
        "IN_PROGRESS" -> "En Progreso"
        "NO_SHOW" -> "No se presentó"
        "RESCHEDULED" -> "Reprogramada"
        else -> status
    }
}

data class QuickAction(
    val icon: ImageVector,
    val title: String,
    val onClick: () -> Unit
)

private fun getQuickActions(
    currentUser: User,
    onNavigateToAppointments: () -> Unit,
    onNavigateToClasses: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToLicenses: () -> Unit,
    onNavigateToAdmin: () -> Unit
): List<QuickAction> {
    val actions = mutableListOf<QuickAction>()
    
    actions.add(
        QuickAction(
            icon = Icons.Filled.Event,
            title = "Citas",
            onClick = onNavigateToAppointments
        )
    )
    
    actions.add(
        QuickAction(
            icon = Icons.Filled.DirectionsCar,
            title = "Clases",
            onClick = onNavigateToClasses
        )
    )
    
    actions.add(
        QuickAction(
            icon = Icons.Filled.CreditCard,
            title = "Licencias",
            onClick = onNavigateToLicenses
        )
    )
    
    if (currentUser.role == UserRole.ADMIN) {
        actions.add(
            QuickAction(
                icon = Icons.Filled.AdminPanelSettings,
                title = "Admin",
                onClick = onNavigateToAdmin
            )
        )
    }
    
    return actions
}

// Función auxiliar para obtener el nombre del tipo de clase de manejo
private fun getDrivingClassTypeDisplayName(packageType: com.example.appbrevete.domain.model.DrivingPackageType): String {
    return when (packageType) {
        com.example.appbrevete.domain.model.DrivingPackageType.BASIC_2H -> "Clase 2 Horas"
        com.example.appbrevete.domain.model.DrivingPackageType.STANDARD_4H -> "Clase 4 Horas"
        com.example.appbrevete.domain.model.DrivingPackageType.CUSTOM -> "Clase Personalizada"
    }
}

// Función auxiliar para obtener el estado de la clase de manejo
private fun getDrivingClassStatusDisplayName(status: com.example.appbrevete.domain.model.DrivingClassStatus): String {
    return when (status) {
        com.example.appbrevete.domain.model.DrivingClassStatus.SCHEDULED -> "Programada"
        com.example.appbrevete.domain.model.DrivingClassStatus.CONFIRMED -> "Confirmada"
        com.example.appbrevete.domain.model.DrivingClassStatus.IN_PROGRESS -> "En Progreso"
        com.example.appbrevete.domain.model.DrivingClassStatus.COMPLETED -> "Completada"
        com.example.appbrevete.domain.model.DrivingClassStatus.CANCELLED -> "Cancelada"
        com.example.appbrevete.domain.model.DrivingClassStatus.RESCHEDULED -> "Reprogramada"
    }
}
