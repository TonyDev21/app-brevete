package com.example.appbrevete.presentation.appointments

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import com.example.appbrevete.domain.model.LicenseCategory
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appbrevete.domain.model.LicenseType
import com.example.appbrevete.domain.model.Appointment
import com.example.appbrevete.presentation.viewmodel.AppointmentsViewModel
import com.example.appbrevete.presentation.viewmodel.LicenseTypesViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAppointmentScreen(
    appointmentId: String,
    userId: String, // AGREGAR userId como parámetro
    onNavigateBack: () -> Unit,
    onAppointmentUpdated: () -> Unit,
    appointmentsViewModel: AppointmentsViewModel = hiltViewModel(),
    licenseTypesViewModel: LicenseTypesViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val appointmentsUiState by appointmentsViewModel.uiState.collectAsStateWithLifecycle()
    val licenseTypesUiState by licenseTypesViewModel.uiState.collectAsStateWithLifecycle()
    
    var currentStep by remember { mutableStateOf(EditStep.LICENSE_SELECTION) }
    var selectedLicenseType by remember { mutableStateOf<LicenseType?>(null) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedTime by remember { mutableStateOf<String?>(null) }
    var originalAppointment by remember { mutableStateOf<Appointment?>(null) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    // Cargar datos iniciales
    LaunchedEffect(Unit) {
        licenseTypesViewModel.loadLicenseTypes()
        appointmentsViewModel.loadAppointments(userId) 
    }
    
    // Buscar la cita actual y preseleccionar la licencia
    LaunchedEffect(appointmentsUiState.appointments, licenseTypesUiState.licenseTypes) {
        val appointment = appointmentsUiState.appointments.find { it.id == appointmentId }
        if (appointment != null && originalAppointment == null) {
            originalAppointment = appointment
            
            // Pre-seleccionar la licencia actual si aún no se ha seleccionado una diferente
            if (selectedLicenseType == null) {
                selectedLicenseType = licenseTypesUiState.licenseTypes.find { it.id == appointment.licenseTypeId }
            }
            
            // Convertir timestamp a LocalDate
            try {
                val calendar = java.util.Calendar.getInstance()
                calendar.timeInMillis = appointment.scheduledDate
                selectedDate = LocalDate.of(
                    calendar.get(java.util.Calendar.YEAR),
                    calendar.get(java.util.Calendar.MONTH) + 1,
                    calendar.get(java.util.Calendar.DAY_OF_MONTH)
                )
            } catch (e: Exception) {
                selectedDate = LocalDate.now().plusDays(1)
            }
            
            selectedTime = appointment.scheduledTime
        }
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
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }
            
            Text(
                text = "Editar Cita",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Indicador de pasos
        StepIndicator(
            currentStep = currentStep,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        when (currentStep) {
            EditStep.LICENSE_SELECTION -> {
                LicenseSelectionStep(
                    licenseTypes = licenseTypesUiState.licenseTypes,
                    selectedLicenseType = selectedLicenseType,
                    originalLicenseTypeId = originalAppointment?.licenseTypeId,
                    onLicenseSelected = { licenseType ->
                        selectedLicenseType = licenseType
                    },
                    onNext = {
                        if (selectedLicenseType != null) {
                            currentStep = EditStep.DATE_TIME_SELECTION
                        }
                    }
                )
            }
            
            EditStep.DATE_TIME_SELECTION -> {
                DateTimeSelectionStep(
                    selectedDate = selectedDate,
                    selectedTime = selectedTime,
                    onDateSelected = { date -> selectedDate = date },
                    onTimeSelected = { time -> selectedTime = time },
                    onNext = {
                        if (selectedDate != null && selectedTime != null) {
                            currentStep = EditStep.CONFIRMATION
                        }
                    },
                    onBack = {
                        currentStep = EditStep.LICENSE_SELECTION
                    }
                )
            }
            
            EditStep.CONFIRMATION -> {
                ConfirmationStep(
                    originalAppointment = originalAppointment,
                    newLicenseType = selectedLicenseType,
                    newDate = selectedDate,
                    newTime = selectedTime,
                    licenseTypes = licenseTypesUiState.licenseTypes,
                    onConfirm = { showConfirmDialog = true },
                    onBack = {
                        currentStep = EditStep.DATE_TIME_SELECTION
                    }
                )
            }
        }
    }

    // Diálogo de confirmación
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Confirmar cambios") },
            text = { Text("¿Estás seguro de que quieres actualizar esta cita con los nuevos datos?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmDialog = false
                        
                        originalAppointment?.let { appointment ->
                            selectedLicenseType?.let { licenseType ->
                                selectedDate?.let { date ->
                                    selectedTime?.let { time ->
                                        // Convertir fecha a timestamp
                                        val calendar = java.util.Calendar.getInstance()
                                        calendar.set(date.year, date.monthValue - 1, date.dayOfMonth)
                                        
                                        val updatedAppointment = appointment.copy(
                                            licenseTypeId = licenseType.id,
                                            scheduledDate = calendar.timeInMillis,
                                            scheduledTime = time,
                                            notes = "Trámite para ${licenseType.name} (Actualizado)"
                                        )
                                        
                                        appointmentsViewModel.updateAppointment(updatedAppointment)
                                        Toast.makeText(context, "Cita actualizada exitosamente", Toast.LENGTH_SHORT).show()
                                        onAppointmentUpdated()
                                    }
                                }
                            }
                        }
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

enum class EditStep {
    LICENSE_SELECTION,
    DATE_TIME_SELECTION,
    CONFIRMATION
}

@Composable
fun StepIndicator(
    currentStep: EditStep,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StepItem(
            stepNumber = 1,
            title = "Licencia",
            isActive = currentStep == EditStep.LICENSE_SELECTION,
            isCompleted = currentStep.ordinal > EditStep.LICENSE_SELECTION.ordinal
        )
        
        Divider(
            modifier = Modifier
                .weight(1f)
                .height(2.dp),
            color = if (currentStep.ordinal > EditStep.LICENSE_SELECTION.ordinal) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outline
            }
        )
        
        StepItem(
            stepNumber = 2,
            title = "Fecha/Hora",
            isActive = currentStep == EditStep.DATE_TIME_SELECTION,
            isCompleted = currentStep.ordinal > EditStep.DATE_TIME_SELECTION.ordinal
        )
        
        Divider(
            modifier = Modifier
                .weight(1f)
                .height(2.dp),
            color = if (currentStep.ordinal > EditStep.DATE_TIME_SELECTION.ordinal) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outline
            }
        )
        
        StepItem(
            stepNumber = 3,
            title = "Confirmar",
            isActive = currentStep == EditStep.CONFIRMATION,
            isCompleted = false
        )
    }
}

@Composable
fun StepItem(
    stepNumber: Int,
    title: String,
    isActive: Boolean,
    isCompleted: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(32.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = androidx.compose.foundation.shape.CircleShape,
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            } else {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = androidx.compose.foundation.shape.CircleShape,
                    color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                    border = if (isActive) null else androidx.compose.foundation.BorderStroke(
                        2.dp,
                        MaterialTheme.colorScheme.outline
                    )
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stepNumber.toString(),
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = if (isActive || isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun LicenseSelectionStep(
    licenseTypes: List<LicenseType>,
    selectedLicenseType: LicenseType?,
    originalLicenseTypeId: String?,
    onLicenseSelected: (LicenseType) -> Unit,
    onNext: () -> Unit
) {
    Column {
        Text(
            text = "Selecciona el nuevo tipo de licencia",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        if (originalLicenseTypeId != null) {
            val originalLicense = licenseTypes.find { it.id == originalLicenseTypeId }
            if (originalLicense != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Licencia actual: ${originalLicense.name}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(licenseTypes) { licenseType ->
                LicenseTypeCard(
                    licenseType = licenseType,
                    isSelected = selectedLicenseType?.id == licenseType.id || 
                               (selectedLicenseType == null && originalLicenseTypeId == licenseType.id),
                    onClick = { onLicenseSelected(licenseType) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedLicenseType != null
        ) {
            Text("Continuar")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateTimeSelectionStep(
    selectedDate: LocalDate?,
    selectedTime: String?,
    onDateSelected: (LocalDate) -> Unit,
    onTimeSelected: (String) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val availableDates = remember { generateAvailableDates() }
    val availableTimes = remember { generateAvailableTimes() }
    
    Column {
        Text(
            text = "Selecciona nueva fecha y hora",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Selección de fecha
        Text(
            text = "Fecha:",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(availableDates) { date ->
                DateCard(
                    date = date,
                    isSelected = selectedDate == date,
                    onClick = { onDateSelected(date) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Selección de hora
        Text(
            text = "Hora:",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(availableTimes.chunked(3)) { timeChunk ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    timeChunk.forEach { time ->
                        Box(modifier = Modifier.weight(1f)) {
                            TimeCard(
                                time = time,
                                isSelected = selectedTime == time,
                                onClick = { onTimeSelected(time) }
                            )
                        }
                    }
                    // Llenar espacios vacíos
                    repeat(3 - timeChunk.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("Atrás")
            }
            
            Button(
                onClick = onNext,
                modifier = Modifier.weight(1f),
                enabled = selectedDate != null && selectedTime != null
            ) {
                Text("Continuar")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConfirmationStep(
    originalAppointment: Appointment?,
    newLicenseType: LicenseType?,
    newDate: LocalDate?,
    newTime: String?,
    licenseTypes: List<LicenseType>,
    onConfirm: () -> Unit,
    onBack: () -> Unit
) {
    Column {
        Text(
            text = "Confirmar cambios",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Resumen de cambios",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                if (newLicenseType != null) {
                    val originalLicenseName = licenseTypes.find { it.id == originalAppointment?.licenseTypeId }?.name ?: "N/A"
                    ComparisonItem(
                        label = "Tipo de Licencia",
                        oldValue = "Actual: $originalLicenseName",
                        newValue = "Nuevo: ${newLicenseType.name}"
                    )
                }
                
                if (newDate != null) {
                    ComparisonItem(
                        label = "Fecha",
                        oldValue = "Actual: ${formatTimestamp(originalAppointment?.scheduledDate ?: 0L)}",
                        newValue = "Nueva: ${newDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}"
                    )
                }
                
                if (newTime != null) {
                    ComparisonItem(
                        label = "Hora",
                        oldValue = "Actual: ${originalAppointment?.scheduledTime ?: "N/A"}",
                        newValue = "Nueva: $newTime"
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                if (newLicenseType != null) {
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
                            text = "S/. ${newLicenseType.price}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("Atrás")
            }
            
            Button(
                onClick = onConfirm,
                modifier = Modifier.weight(1f)
            ) {
                Text("Confirmar Cambios")
            }
        }
    }
}

@Composable
fun ComparisonItem(
    label: String,
    oldValue: String,
    newValue: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = oldValue,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Text(
            text = newValue,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(12.dp))
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val date = java.util.Date(timestamp)
    val format = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
    return format.format(date)
}

@RequiresApi(Build.VERSION_CODES.O)
private fun generateAvailableDates(): List<LocalDate> {
    val today = LocalDate.now()
    val dates = mutableListOf<LocalDate>()
    
    for (i in 1..30) {
        val date = today.plusDays(i.toLong())
        // Excluir domingos (SUNDAY = 7)
        if (date.dayOfWeek.value != 7) {
            dates.add(date)
        }
    }
    
    return dates.take(14) // Mostrar solo las próximas 2 semanas
}

private fun generateAvailableTimes(): List<String> {
    return listOf(
        "08:00", "08:30", "09:00", "09:30", "10:00", "10:30",
        "11:00", "11:30", "14:00", "14:30", "15:00", "15:30",
        "16:00", "16:30", "17:00", "17:30"
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicenseTypeCard(
    licenseType: LicenseType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(
                2.dp,
                MaterialTheme.colorScheme.primary
            )
        } else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = androidx.compose.foundation.shape.CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (licenseType.category) {
                                    LicenseCategory.BII_A, LicenseCategory.BII_B, LicenseCategory.BII_C -> Icons.Default.TwoWheeler
                                    LicenseCategory.A_I, LicenseCategory.A_IIA, LicenseCategory.A_IIB, LicenseCategory.A_IIIA, LicenseCategory.A_IIIB, LicenseCategory.A_IIIC -> Icons.Default.DirectionsCar
                                    else -> Icons.Default.CreditCard
                                },
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column {
                        Text(
                            text = licenseType.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        )
                        Text(
                            text = licenseType.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isSelected) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(12.dp))
            }
            
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "S/. ${licenseType.price}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Seleccionado",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
