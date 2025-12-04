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
import com.example.appbrevete.presentation.viewmodel.AdminManageClassesViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminManageClassesScreenNew(
    onNavigateBack: () -> Unit = {},
    onClassClick: (String) -> Unit = {},
    viewModel: AdminManageClassesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedFilter by remember { mutableStateOf(ClassFilter.SCHEDULED) }
    
    LaunchedEffect(selectedFilter) {
        viewModel.loadClasses(selectedFilter, null)
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
                text = "Gestionar Clases de Manejo",
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
                    text = "Estadísticas Generales de Clases",
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
                        value = uiState.totalClasses.toString(),
                        icon = Icons.Default.DirectionsCar,
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
                onClick = { selectedFilter = ClassFilter.SCHEDULED },
                modifier = Modifier.weight(1f),
                colors = if (selectedFilter == ClassFilter.SCHEDULED)
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
                onClick = { selectedFilter = ClassFilter.COMPLETED },
                modifier = Modifier.weight(1f),
                colors = if (selectedFilter == ClassFilter.COMPLETED)
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
                onClick = { selectedFilter = ClassFilter.CANCELLED },
                modifier = Modifier.weight(1f),
                colors = if (selectedFilter == ClassFilter.CANCELLED)
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
        
        // Classes List
        Card(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Clases de Manejo (${uiState.classes.size})",
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
                } else if (uiState.classes.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.DirectionsCar,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No hay clases con estos filtros",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.classes) { drivingClass ->
                            DrivingClassAdminCard(
                                drivingClass = drivingClass,
                                onClick = { onClassClick(drivingClass.id) }
                            )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrivingClassAdminCard(
    drivingClass: DrivingClass,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
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
                    // ID de la clase
                    Text(
                        text = "ID: ${drivingClass.id.take(8).uppercase()}",
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
                            text = drivingClass.userName ?: "No disponible",
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
                            text = "DNI: ${drivingClass.userDni ?: "No disponible"}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Horas de Práctica
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Horas de Práctica: ${drivingClass.totalHours}h",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Fecha y Hora
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
                            text = "${formatDate(drivingClass.scheduledDate)} - ${drivingClass.scheduledTime}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                    }
                }
                
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Surface(
                        color = when (drivingClass.status) {
                            DrivingClassStatus.SCHEDULED -> MaterialTheme.colorScheme.tertiaryContainer
                            DrivingClassStatus.CONFIRMED -> MaterialTheme.colorScheme.primaryContainer
                            DrivingClassStatus.COMPLETED -> MaterialTheme.colorScheme.primaryContainer
                            DrivingClassStatus.CANCELLED -> MaterialTheme.colorScheme.errorContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        },
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = getDrivingClassStatusDisplayName(drivingClass.status),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = when (drivingClass.status) {
                                DrivingClassStatus.SCHEDULED -> MaterialTheme.colorScheme.onTertiaryContainer
                                DrivingClassStatus.CONFIRMED -> MaterialTheme.colorScheme.onPrimaryContainer
                                DrivingClassStatus.COMPLETED -> MaterialTheme.colorScheme.onPrimaryContainer
                                DrivingClassStatus.CANCELLED -> MaterialTheme.colorScheme.onErrorContainer
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }
            }
        }
    }
}

private fun getDrivingClassStatusDisplayName(status: DrivingClassStatus): String {
    return when (status) {
        DrivingClassStatus.SCHEDULED -> "Programada"
        DrivingClassStatus.CONFIRMED -> "Confirmada"
        DrivingClassStatus.IN_PROGRESS -> "En Progreso"
        DrivingClassStatus.COMPLETED -> "Completada"
        DrivingClassStatus.CANCELLED -> "Cancelada"
        DrivingClassStatus.RESCHEDULED -> "Reprogramada"
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
