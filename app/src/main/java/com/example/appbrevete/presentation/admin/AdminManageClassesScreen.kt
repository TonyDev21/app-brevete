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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appbrevete.domain.model.DrivingClass
import com.example.appbrevete.domain.model.DrivingClassStatus
import com.example.appbrevete.domain.model.DrivingPackageType
import com.example.appbrevete.presentation.viewmodel.AdminManageClassesViewModel
import java.text.SimpleDateFormat
import java.util.*

enum class ClassFilter {
    ALL, SCHEDULED, COMPLETED, CANCELLED
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminManageClassesScreen(
    viewModel: AdminManageClassesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedFilter by remember { mutableStateOf(ClassFilter.ALL) }
    var selectedType by remember { mutableStateOf<DrivingPackageType?>(null) }
    
    LaunchedEffect(selectedFilter, selectedType) {
        viewModel.loadClasses(selectedFilter, selectedType)
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Gestión de Clases",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Stats Card
        ClassesStatsCard(classes = uiState.classes)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Filters
        ClassFiltersCard(
            selectedFilter = selectedFilter,
            selectedType = selectedType,
            onFilterChange = { selectedFilter = it },
            onTypeChange = { selectedType = it }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.classes.isEmpty()) {
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
                        text = "No hay clases con los filtros aplicados",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            // Classes List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.classes) { drivingClass ->
                    ClassAdminCard(
                        drivingClass = drivingClass,
                        onStatusChange = { newStatus ->
                            viewModel.updateClassStatus(drivingClass.id, newStatus)
                        }
                    )
                }
            }
        }
        
        uiState.errorMessage?.let { error ->
            LaunchedEffect(error) {
                // Handle error - could show snackbar
            }
        }
    }
}

@Composable
fun ClassesStatsCard(classes: List<DrivingClass>) {
    val totalClasses = classes.size
    val scheduledCount = classes.count { it.status == DrivingClassStatus.SCHEDULED }
    val completedCount = classes.count { it.status == DrivingClassStatus.COMPLETED }
    val cancelledCount = classes.count { it.status == DrivingClassStatus.CANCELLED }
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Resumen de Clases",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    label = "Total",
                    value = totalClasses.toString(),
                    color = MaterialTheme.colorScheme.primary
                )
                StatItem(
                    label = "Programadas",
                    value = scheduledCount.toString(),
                    color = MaterialTheme.colorScheme.secondary
                )
                StatItem(
                    label = "Completadas",
                    value = completedCount.toString(),
                    color = MaterialTheme.colorScheme.tertiary
                )
                StatItem(
                    label = "Canceladas",
                    value = cancelledCount.toString(),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, color: androidx.compose.ui.graphics.Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassFiltersCard(
    selectedFilter: ClassFilter,
    selectedType: DrivingPackageType?,
    onFilterChange: (ClassFilter) -> Unit,
    onTypeChange: (DrivingPackageType?) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Filtros",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    // Status filter
                    var statusDropdownExpanded by remember { mutableStateOf(false) }
                    
                    ExposedDropdownMenuBox(
                        expanded = statusDropdownExpanded,
                        onExpandedChange = { statusDropdownExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = when (selectedFilter) {
                                ClassFilter.ALL -> "Todos"
                                ClassFilter.SCHEDULED -> "Programadas"
                                ClassFilter.COMPLETED -> "Completadas"
                                ClassFilter.CANCELLED -> "Canceladas"
                            },
                            onValueChange = { },
                            readOnly = true,
                            label = { Text("Estado") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = statusDropdownExpanded
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        
                        ExposedDropdownMenu(
                            expanded = statusDropdownExpanded,
                            onDismissRequest = { statusDropdownExpanded = false }
                        ) {
                            val filters = listOf(
                                ClassFilter.ALL to "Todos",
                                ClassFilter.SCHEDULED to "Programadas",
                                ClassFilter.COMPLETED to "Completadas",
                                ClassFilter.CANCELLED to "Canceladas"
                            )
                            
                            filters.forEach { (filter, label) ->
                                DropdownMenuItem(
                                    text = { Text(label) },
                                    onClick = {
                                        onFilterChange(filter)
                                        statusDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                
                Column(modifier = Modifier.weight(1f)) {
                    // Type filter
                    var typeDropdownExpanded by remember { mutableStateOf(false) }
                    
                    ExposedDropdownMenuBox(
                        expanded = typeDropdownExpanded,
                        onExpandedChange = { typeDropdownExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedType?.let { 
                                when (it) {
                                    DrivingPackageType.BASIC_2H -> "Básico (2h)"
                                    DrivingPackageType.STANDARD_4H -> "Estándar (4h)"
                                    DrivingPackageType.CUSTOM -> "Personalizado"
                                }
                            } ?: "Todos los tipos",
                            onValueChange = { },
                            readOnly = true,
                            label = { Text("Tipo") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = typeDropdownExpanded
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        
                        ExposedDropdownMenu(
                            expanded = typeDropdownExpanded,
                            onDismissRequest = { typeDropdownExpanded = false }
                        ) {
                            val types = listOf(
                                null to "Todos los tipos",
                                DrivingPackageType.BASIC_2H to "Básico (2h)",
                                DrivingPackageType.STANDARD_4H to "Estándar (4h)",
                                DrivingPackageType.CUSTOM to "Personalizado"
                            )
                            
                            types.forEach { (type, label) ->
                                DropdownMenuItem(
                                    text = { Text(label) },
                                    onClick = {
                                        onTypeChange(type)
                                        typeDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ClassAdminCard(
    drivingClass: DrivingClass,
    onStatusChange: (DrivingClassStatus) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Clase #${drivingClass.id.take(8)}",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "Estudiante: ${drivingClass.studentId}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                    
                    Text(
                        text = "Tipo: ${getDrivingClassTypeDisplayName(drivingClass.packageType)}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                    
                    Text(
                        text = "Fecha: ${formatClassDateTime(drivingClass.scheduledDate)}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                    
                    if (drivingClass.instructorId != null) {
                        Text(
                            text = "Instructor: ${drivingClass.instructorId}",
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
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            
            // Action buttons for admins
            if (drivingClass.status == DrivingClassStatus.SCHEDULED) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { onStatusChange(DrivingClassStatus.CONFIRMED) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Confirmar")
                    }
                    
                    OutlinedButton(
                        onClick = { onStatusChange(DrivingClassStatus.CANCELLED) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Cancelar")
                    }
                }
            } else if (drivingClass.status == DrivingClassStatus.CONFIRMED) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onStatusChange(DrivingClassStatus.COMPLETED) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Marcar Completada")
                    }
                    
                    OutlinedButton(
                        onClick = { onStatusChange(DrivingClassStatus.CANCELLED) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Cancelar")
                    }
                }
            }
        }
    }
}

private fun getDrivingClassTypeDisplayName(packageType: DrivingPackageType): String {
    return when (packageType) {
        DrivingPackageType.BASIC_2H -> "Paquete 2 Horas"
        DrivingPackageType.STANDARD_4H -> "Paquete 4 Horas"
        DrivingPackageType.CUSTOM -> "Paquete Personalizado"
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

private fun formatClassDateTime(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return format.format(date)
}