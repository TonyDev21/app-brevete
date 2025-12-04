package com.example.appbrevete.presentation.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.appbrevete.domain.model.*
import com.example.appbrevete.presentation.viewmodel.AdminManageAppointmentsViewModel
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminManageAppointmentsScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToEvaluation: (String) -> Unit = {},
    viewModel: AdminManageAppointmentsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedFilter by remember { mutableStateOf(AppointmentFilter.SCHEDULED) }
    
    LaunchedEffect(selectedFilter) {
        // Solo cargar citas médicas
        viewModel.loadAppointments(selectedFilter, AppointmentType.MEDICAL_EXAM)
        // Cargar estadísticas generales
        viewModel.loadAllStatistics()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver"
                )
            }
            Text(
                text = "Gestionar Citas Médicas",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Stats Card - Estadísticas Generales
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Estadísticas Generales de Citas Médicas",
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
                        value = uiState.totalAppointments.toString(),
                        icon = Icons.Default.Event,
                        color = MaterialTheme.colorScheme.primary
                    )
                    StatItem(
                        title = "Pendientes",
                        value = uiState.scheduledCount.toString(),
                        icon = Icons.Default.Schedule,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    StatItem(
                        title = "Completadas",
                        value = uiState.completedCount.toString(),
                        icon = Icons.Default.CheckCircle,
                        color = MaterialTheme.colorScheme.primary
                    )
                    StatItem(
                        title = "Canceladas",
                        value = uiState.cancelledCount.toString(),
                        icon = Icons.Default.Cancel,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Botones de categorías
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { selectedFilter = AppointmentFilter.SCHEDULED },
                modifier = Modifier.weight(1f),
                colors = if (selectedFilter == AppointmentFilter.SCHEDULED)
                    ButtonDefaults.buttonColors()
                else
                    ButtonDefaults.outlinedButtonColors()
            ) {
                Text(
                    text = "Pendientes",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
            
            Button(
                onClick = { selectedFilter = AppointmentFilter.COMPLETED },
                modifier = Modifier.weight(1f),
                colors = if (selectedFilter == AppointmentFilter.COMPLETED)
                    ButtonDefaults.buttonColors()
                else
                    ButtonDefaults.outlinedButtonColors()
            ) {
                Text(
                    text = "Completadas",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
            
            Button(
                onClick = { selectedFilter = AppointmentFilter.CANCELLED },
                modifier = Modifier.weight(1f),
                colors = if (selectedFilter == AppointmentFilter.CANCELLED)
                    ButtonDefaults.buttonColors()
                else
                    ButtonDefaults.outlinedButtonColors()
            ) {
                Text(
                    text = "Canceladas",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Appointments List
        Card(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Citas Médicas (${uiState.appointments.size})",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (uiState.appointments.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.EventBusy,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No hay citas con estos filtros",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    // Si es la vista de Completadas, mostrar subsecciones
                    if (selectedFilter == AppointmentFilter.COMPLETED) {
                        // Separar aprobadas y no aprobadas
                        val (approvedAppointments, notApprovedAppointments) = viewModel.getApprovedAndNotApprovedAppointments()
                        
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Sección de Aprobados
                            item {
                                Text(
                                    text = "✓ Aprobados (${approvedAppointments.size})",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            }
                            
                            if (approvedAppointments.isEmpty()) {
                                item {
                                    Text(
                                        text = "No hay citas aprobadas",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(start = 16.dp)
                                    )
                                }
                            } else {
                                items(approvedAppointments) { appointment ->
                                    AppointmentAdminCard(
                                        appointment = appointment,
                                        onStatusChange = { newStatus ->
                                            viewModel.updateAppointmentStatus(appointment.id, newStatus)
                                        },
                                        onClick = {
                                            // Ver evaluación completa
                                            onNavigateToEvaluation(appointment.id)
                                        },
                                        showApprovedBadge = true
                                    )
                                }
                            }
                            
                            // Sección de No Aprobados
                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "✗ No Aprobados (${notApprovedAppointments.size})",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            
                            if (notApprovedAppointments.isEmpty()) {
                                item {
                                    Text(
                                        text = "No hay citas desaprobadas",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(start = 16.dp)
                                    )
                                }
                            } else {
                                items(notApprovedAppointments) { appointment ->
                                    AppointmentAdminCard(
                                        appointment = appointment,
                                        onStatusChange = { newStatus ->
                                            viewModel.updateAppointmentStatus(appointment.id, newStatus)
                                        },
                                        onClick = {
                                            // Ver evaluación completa
                                            onNavigateToEvaluation(appointment.id)
                                        },
                                        showApprovedBadge = false
                                    )
                                }
                            }
                        }
                    } else {
                        // Vista normal para Pendientes y Canceladas
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(uiState.appointments) { appointment ->
                                AppointmentAdminCard(
                                    appointment = appointment,
                                    onStatusChange = { newStatus ->
                                        viewModel.updateAppointmentStatus(appointment.id, newStatus)
                                    },
                                    onClick = {
                                        // Navegar a la pantalla de evaluación solo si está pendiente
                                        if (appointment.status == AppointmentStatus.SCHEDULED) {
                                            onNavigateToEvaluation(appointment.id)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        
        if (uiState.errorMessage != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
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

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentAdminCard(
    appointment: Appointment,
    onStatusChange: (AppointmentStatus) -> Unit,
    onClick: () -> Unit = {},
    showApprovedBadge: Boolean? = null
) {
    // Detectar tipo de vehículo basado en la categoría de licencia
    val vehicleType = when {
        appointment.licenseTypeId == null -> "No especificado"
        appointment.licenseTypeId.contains("bii", ignoreCase = true) -> "Motocicleta"
        appointment.licenseTypeId.contains("a-i", ignoreCase = true) -> "Automóvil"
        appointment.licenseTypeId.contains("a-ii", ignoreCase = true) -> "Automóvil"
        appointment.licenseTypeId.contains("a-iii", ignoreCase = true) -> "Automóvil"
        else -> "No especificado"
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = when {
                showApprovedBadge == true -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
                showApprovedBadge == false -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                else -> MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    // ID de la cita
                    Text(
                        text = "ID: ${appointment.id.take(8).uppercase()}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Nombre del usuario
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = appointment.userName ?: "No disponible",
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // DNI
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Badge,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "DNI: ${appointment.userDni ?: "No disponible"}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Tipo de vehículo
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (vehicleType == "Motocicleta") Icons.Default.TwoWheeler else Icons.Default.DirectionsCar,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Tipo: $vehicleType",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Fecha
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${formatAppointmentDateTime(appointment.scheduledDate)} - ${appointment.scheduledTime}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                    }
                }
                
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Surface(
                        color = when (appointment.status) {
                            AppointmentStatus.SCHEDULED -> MaterialTheme.colorScheme.tertiaryContainer
                            AppointmentStatus.CONFIRMED -> MaterialTheme.colorScheme.primaryContainer
                            AppointmentStatus.COMPLETED -> MaterialTheme.colorScheme.primaryContainer
                            AppointmentStatus.CANCELLED -> MaterialTheme.colorScheme.errorContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        },
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = getAppointmentStatusDisplayName(appointment.status),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = when (appointment.status) {
                                AppointmentStatus.SCHEDULED -> MaterialTheme.colorScheme.onTertiaryContainer
                                AppointmentStatus.CONFIRMED -> MaterialTheme.colorScheme.onPrimaryContainer
                                AppointmentStatus.COMPLETED -> MaterialTheme.colorScheme.onPrimaryContainer
                                AppointmentStatus.CANCELLED -> MaterialTheme.colorScheme.onErrorContainer
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatItem(
    title: String,
    value: String,
    icon: ImageVector,
    color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = color
        )
        Text(
            text = title,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

enum class AppointmentFilter {
    ALL, SCHEDULED, COMPLETED, CANCELLED
}

private fun getAppointmentTypeDisplayName(type: AppointmentType): String {
    return when (type) {
        AppointmentType.MEDICAL_EXAM -> "Examen Médico"
        AppointmentType.THEORY_EXAM -> "Examen de Teoría"
        AppointmentType.PRACTICAL_EXAM -> "Examen Práctico"
        AppointmentType.DRIVING_CLASS -> "Clase de Manejo"
        AppointmentType.CONSULTATION -> "Consulta"
    }
}

private fun getAppointmentStatusDisplayName(status: AppointmentStatus): String {
    return when (status) {
        AppointmentStatus.SCHEDULED -> "Programada"
        AppointmentStatus.CONFIRMED -> "Confirmada"
        AppointmentStatus.IN_PROGRESS -> "En Progreso"
        AppointmentStatus.COMPLETED -> "Completada"
        AppointmentStatus.CANCELLED -> "Cancelada"
        AppointmentStatus.NO_SHOW -> "No se presentó"
        AppointmentStatus.RESCHEDULED -> "Reprogramada"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatAppointmentDateTime(dateTime: Long): String {
    val instant = Instant.ofEpochMilli(dateTime)
    val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    return localDateTime.format(formatter)
}