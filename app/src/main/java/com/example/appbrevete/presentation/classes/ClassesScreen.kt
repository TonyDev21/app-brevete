package com.example.appbrevete.presentation.classes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appbrevete.presentation.viewmodel.DrivingClassViewModel
import com.example.appbrevete.domain.model.DrivingClass
import com.example.appbrevete.domain.model.DrivingClassStatus
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassesScreen(
    userId: String,
    onNavigateToBooking: () -> Unit = {},
    onNavigateToEdit: (String) -> Unit = {},
    viewModel: DrivingClassViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var classToCancel by remember { mutableStateOf<DrivingClass?>(null) }

    LaunchedEffect(userId) {
        viewModel.loadClasses(userId)
    }
    
    // Recargar clases cada vez que se entra a la pantalla
    LaunchedEffect(Unit) {
        viewModel.loadClasses(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mis Clases",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            FloatingActionButton(
                onClick = onNavigateToBooking,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva clase")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.classes.isEmpty()) {
            // Estado vacío
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No tienes clases programadas",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Programa tu primera clase para comenzar",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            // Lista de clases
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.classes) { drivingClass ->
                    DrivingClassCard(
                        drivingClass = drivingClass,
                        onStatusChange = { status ->
                            viewModel.updateClassStatus(drivingClass.id, status)
                        },
                        onEdit = { classToEdit ->
                            onNavigateToEdit(classToEdit.id)
                        },
                        onCancel = { drivingClassToCancel ->
                            classToCancel = drivingClassToCancel
                        }
                    )
                }
            }
        }
    }
    
    // Diálogo de cancelación
    classToCancel?.let { drivingClass ->
        CancelDrivingClassDialog(
            drivingClass = drivingClass,
            onDismiss = { classToCancel = null },
            onConfirm = { classToCancel = null }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrivingClassCard(
    drivingClass: DrivingClass,
    onStatusChange: (DrivingClassStatus) -> Unit,
    onEdit: (DrivingClass) -> Unit,
    onCancel: (DrivingClass) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { /* TODO: Ver detalles */ }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = getDrivingClassTypeDisplayName(drivingClass.packageType),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        // Mostrar "(Actualizado)" si la clase ha sido modificada recientemente
                        if (wasRecentlyUpdated(drivingClass.updatedAt, drivingClass.createdAt)) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "(Actualizado)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formatDate(drivingClass.scheduledDate),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = drivingClass.scheduledTime,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Badge de estado
                AssistChip(
                    onClick = { },
                    label = { 
                        Text(getDrivingClassStatusDisplayName(drivingClass.status))
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = when (drivingClass.status) {
                            DrivingClassStatus.SCHEDULED -> MaterialTheme.colorScheme.primaryContainer
                            DrivingClassStatus.COMPLETED -> MaterialTheme.colorScheme.tertiaryContainer
                            DrivingClassStatus.CANCELLED -> MaterialTheme.colorScheme.errorContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
                )
            }
            
            if (drivingClass.notes?.isNotEmpty() == true) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = drivingClass.notes,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Información adicional
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${drivingClass.totalHours} horas • ${drivingClass.location}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            // Botones de acción (solo para clases programadas)
            if (drivingClass.status == DrivingClassStatus.SCHEDULED) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Botón Editar
                    OutlinedButton(
                        onClick = { onEdit(drivingClass) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Editar")
                    }
                    
                    // Botón Cancelar
                    OutlinedButton(
                        onClick = { onCancel(drivingClass) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Cancelar")
                    }
                }
            }
        }
    }
}

private fun getDrivingClassTypeDisplayName(packageType: com.example.appbrevete.domain.model.DrivingPackageType): String {
    return when (packageType) {
        com.example.appbrevete.domain.model.DrivingPackageType.BASIC_2H -> "Paquete 2 Horas"
        com.example.appbrevete.domain.model.DrivingPackageType.STANDARD_4H -> "Paquete 4 Horas"
        com.example.appbrevete.domain.model.DrivingPackageType.CUSTOM -> "Paquete Personalizado"
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
    val date = Date(timestamp)
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return format.format(date)
}

private fun wasRecentlyUpdated(updatedAt: Long, createdAt: Long): Boolean {
    // Mostrar "Actualizado" si:
    // 1. La fecha de actualización es diferente a la de creación (ha sido modificado)
    // 2. Y la actualización fue en las últimas 24 horas
    val wasModified = updatedAt != createdAt
    val isRecent = System.currentTimeMillis() - updatedAt < 24 * 60 * 60 * 1000 // 24 horas
    return wasModified && isRecent
}
