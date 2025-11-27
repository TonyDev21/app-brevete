package com.example.appbrevete.presentation.classes

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appbrevete.domain.model.DrivingClass
import com.example.appbrevete.presentation.classes.create.steps.PackageSelectionStep
import com.example.appbrevete.presentation.classes.create.steps.DateTimeSelectionStep
import com.example.appbrevete.presentation.classes.create.steps.ConfirmationStep
import com.example.appbrevete.presentation.viewmodel.DrivingClassViewModel

enum class EditClassStep {
    PACKAGE_SELECTION, DATE_TIME_SELECTION, CONFIRMATION
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDrivingClassScreen(
    userId: String,
    classId: String,
    onNavigateBack: () -> Unit,
    viewModel: DrivingClassViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var currentStep by remember { mutableStateOf(EditClassStep.PACKAGE_SELECTION) }
    var selectedPackage by remember { mutableStateOf<String?>(null) }
    var selectedDate by remember { mutableStateOf<String?>(null) }
    var selectedTime by remember { mutableStateOf<String?>(null) }
    var originalClass by remember { mutableStateOf<DrivingClass?>(null) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    // Cargar la clase a editar
    LaunchedEffect(classId) {
        viewModel.loadClassById(classId) { drivingClass ->
            originalClass = drivingClass
            selectedPackage = when (drivingClass.packageType) {
                com.example.appbrevete.domain.model.DrivingPackageType.BASIC_2H -> "2h"
                com.example.appbrevete.domain.model.DrivingPackageType.STANDARD_4H -> "4h"
                else -> "custom"
            }
            selectedDate = formatDateForInput(drivingClass.scheduledDate)
            selectedTime = drivingClass.scheduledTime
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = when (currentStep) {
                            EditClassStep.PACKAGE_SELECTION -> "Tipo de Paquete"
                            EditClassStep.DATE_TIME_SELECTION -> "Fecha y Hora"
                            EditClassStep.CONFIRMATION -> "Resumen de Cambios"
                        },
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { 
                        if (currentStep == EditClassStep.PACKAGE_SELECTION) {
                            onNavigateBack()
                        } else {
                            currentStep = when (currentStep) {
                                EditClassStep.DATE_TIME_SELECTION -> EditClassStep.PACKAGE_SELECTION
                                EditClassStep.CONFIRMATION -> EditClassStep.DATE_TIME_SELECTION
                                else -> EditClassStep.PACKAGE_SELECTION
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when (currentStep) {
            EditClassStep.PACKAGE_SELECTION -> {
                PackageSelectionStep(
                    selectedPackage = selectedPackage,
                    onPackageSelected = { packageType ->
                        selectedPackage = packageType
                    },
                    onNext = {
                        currentStep = EditClassStep.DATE_TIME_SELECTION
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                )
            }
            
            EditClassStep.DATE_TIME_SELECTION -> {
                DateTimeSelectionStep(
                    selectedPackage = selectedPackage ?: "",
                    selectedDate = selectedDate,
                    selectedTime = selectedTime,
                    onDateSelected = { date ->
                        selectedDate = date
                    },
                    onTimeSelected = { time ->
                        selectedTime = time
                    },
                    onNext = {
                        currentStep = EditClassStep.CONFIRMATION
                    },
                    onBack = {
                        currentStep = EditClassStep.PACKAGE_SELECTION
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                )
            }
            
            EditClassStep.CONFIRMATION -> {
                EditConfirmationStep(
                    originalClass = originalClass,
                    newPackageType = selectedPackage ?: "",
                    newDate = selectedDate ?: "",
                    newTime = selectedTime ?: "",
                    isLoading = uiState.isCreatingClass,
                    onConfirm = {
                        showConfirmDialog = true
                    },
                    onModify = {
                        currentStep = EditClassStep.DATE_TIME_SELECTION
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                )
            }
        }
    }
    
    // Diálogo de confirmación
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Confirmar cambios") },
            text = { Text("¿Estás seguro de que quieres actualizar esta clase con los nuevos datos?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmDialog = false
                        
                        viewModel.updateDrivingClass(
                            classId = classId,
                            packageType = selectedPackage ?: "",
                            date = selectedDate ?: "",
                            time = selectedTime ?: "",
                            onSuccess = {
                                Toast.makeText(
                                    context,
                                    "Clase actualizada exitosamente",
                                    Toast.LENGTH_SHORT
                                ).show()
                                // Recargar las clases para reflejar los cambios
                                viewModel.loadClasses(userId)
                                onNavigateBack()
                            },
                            onError = { error ->
                                // TODO: Mostrar error
                            }
                        )
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun EditConfirmationStep(
    originalClass: DrivingClass?,
    newPackageType: String,
    newDate: String,
    newTime: String,
    isLoading: Boolean,
    onConfirm: () -> Unit,
    onModify: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (originalClass == null) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val newPackageInfo = getPackageInfo(newPackageType)
    val originalPackageString = when (originalClass.packageType) {
        com.example.appbrevete.domain.model.DrivingPackageType.BASIC_2H -> "2h"
        com.example.appbrevete.domain.model.DrivingPackageType.STANDARD_4H -> "4h"
        else -> "custom"
    }
    val originalPackageInfo = getPackageInfo(originalPackageString)
    
    Column(modifier = modifier) {
        // Resumen de cambios
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Resumen de cambios",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                // Comparación de cambios
                ComparisonSection(
                    title = "Tipo de Licencia",
                    originalValue = originalPackageInfo.title,
                    newValue = newPackageInfo.title,
                    hasChanged = originalPackageString != newPackageType
                )
                
                ComparisonSection(
                    title = "Fecha",
                    originalValue = formatDateForDisplay(originalClass.scheduledDate),
                    newValue = formatDate(newDate),
                    hasChanged = formatDateForInput(originalClass.scheduledDate) != newDate
                )
                
                ComparisonSection(
                    title = "Hora",
                    originalValue = originalClass.scheduledTime,
                    newValue = newTime,
                    hasChanged = originalClass.scheduledTime != newTime
                )
                
                ComparisonSection(
                    title = "Ubicación",
                    originalValue = originalClass.location,
                    newValue = "Centro de Clases de Manejo - Lima",
                    hasChanged = false
                )
                
                Divider()
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Nuevo total:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = newPackageInfo.price,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Botones de acción
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Botón Atrás
            OutlinedButton(
                onClick = onModify,
                modifier = Modifier.weight(1f),
                enabled = !isLoading
            ) {
                Text("Atrás")
            }
            
            // Botón Confirmar Cambios
            Button(
                onClick = onConfirm,
                modifier = Modifier.weight(1f),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Actualizando...")
                } else {
                    Text("Confirmar Cambios")
                }
            }
        }
    }
}

@Composable
private fun ComparisonSection(
    title: String,
    originalValue: String,
    newValue: String,
    hasChanged: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = "Actual: $originalValue",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Text(
            text = if (hasChanged) "Nuevo: $newValue" else "Nueva: $newValue",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(12.dp))
    }
}

// Helper functions (reused from other files)
data class PackageInfo(
    val title: String,
    val description: String,
    val price: String
)

private fun getPackageInfo(packageType: String): PackageInfo {
    return when (packageType) {
        "2h" -> PackageInfo(
            title = "PAQUETE 2H",
            description = "2 horas de práctica de manejo con instructor profesional",
            price = "S/. 65.0"
        )
        "4h" -> PackageInfo(
            title = "PAQUETE 4H", 
            description = "4 horas de práctica de manejo con instructor profesional",
            price = "S/. 125.0"
        )
        else -> PackageInfo(
            title = "PAQUETE PERSONALIZADO",
            description = "Paquete personalizado de clases de manejo",
            price = "Consultar"
        )
    }
}

private fun formatDate(dateString: String): String {
    try {
        val parts = dateString.split("-")
        if (parts.size == 3) {
            val day = parts[2]
            val monthNumber = parts[1]
            val year = parts[0]
            return "$day/$monthNumber/$year"
        }
    } catch (e: Exception) {
        // Si hay error en el formato, devolver el string original
    }
    return dateString
}

private fun formatDateForDisplay(timestamp: Long): String {
    val dateFormat = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
    return dateFormat.format(java.util.Date(timestamp))
}

private fun formatDateForInput(timestamp: Long): String {
    val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
    return dateFormat.format(java.util.Date(timestamp))
}