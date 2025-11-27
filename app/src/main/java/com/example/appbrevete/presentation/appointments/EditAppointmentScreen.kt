package com.example.appbrevete.presentation.appointments

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appbrevete.domain.model.Appointment
import com.example.appbrevete.domain.model.LicenseType
import com.example.appbrevete.presentation.appointments.edit.EditStep
import com.example.appbrevete.presentation.appointments.edit.components.StepIndicator
import com.example.appbrevete.presentation.appointments.edit.steps.ConfirmationStep
import com.example.appbrevete.presentation.appointments.edit.steps.DateTimeSelectionStep
import com.example.appbrevete.presentation.appointments.edit.steps.LicenseSelectionStep
import com.example.appbrevete.presentation.viewmodel.AppointmentsViewModel
import com.example.appbrevete.presentation.viewmodel.LicenseTypesViewModel
import java.time.LocalDate

/**
 * Pantalla principal para editar citas - Refactorizada en componentes modulares
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAppointmentScreen(
    appointmentId: String,
    userId: String,
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
    
    // Buscar la cita actual y preseleccionar los datos
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

        // Contenido de los pasos
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
                    },
                    modifier = Modifier.fillMaxSize()
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
                    },
                    modifier = Modifier.fillMaxSize()
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
                    },
                    modifier = Modifier.fillMaxSize()
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