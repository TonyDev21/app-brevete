package com.example.appbrevete.presentation.appointments

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
import com.example.appbrevete.presentation.viewmodel.AppointmentsViewModel
import com.example.appbrevete.domain.model.Appointment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(
    userId: String,
    onCreateAppointment: () -> Unit = {},
    onEditAppointment: (String) -> Unit = {}, // Agregar callback para editar
    viewModel: AppointmentsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var appointmentToCancel by remember { mutableStateOf<Appointment?>(null) }

    LaunchedEffect(userId) {
        viewModel.loadAppointments(userId)
    }
    
    // Recargar citas cada vez que se entra a la pantalla
    LaunchedEffect(Unit) {
        viewModel.loadAppointments(userId)
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
                text = "Mis Citas",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            FloatingActionButton(
                onClick = onCreateAppointment,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva cita")
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
        } else if (uiState.appointments.isEmpty()) {
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
                        imageVector = Icons.Default.EventBusy,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No tienes citas programadas",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Programa tu primera cita para comenzar",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            // Lista de citas
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.appointments) { appointment ->
                    AppointmentCard(
                        appointment = appointment,
                        onEdit = { 
                            onEditAppointment(appointment.id)
                        },
                        onCancel = { 
                            appointmentToCancel = appointment
                        }
                    )
                }
            }
        }
    }
    
    // Diálogo de cancelación
    appointmentToCancel?.let { appointment ->
        CancelAppointmentDialog(
            appointment = appointment,
            onDismiss = { appointmentToCancel = null },
            onConfirm = {
                appointmentToCancel = null
                viewModel.loadAppointments(userId) // Recargar lista
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentCard(
    appointment: Appointment,
    onEdit: () -> Unit,
    onCancel: () -> Unit
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
                    Text(
                        text = getAppointmentTypeDisplayName(appointment.type),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formatDate(appointment.scheduledDate),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = appointment.scheduledTime,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Badge de estado
                AssistChip(
                    onClick = { },
                    label = { 
                        Text(getAppointmentStatusDisplayName(appointment.status))
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = when (appointment.status) {
                            com.example.appbrevete.domain.model.AppointmentStatus.SCHEDULED -> MaterialTheme.colorScheme.primaryContainer
                            com.example.appbrevete.domain.model.AppointmentStatus.COMPLETED -> MaterialTheme.colorScheme.tertiaryContainer
                            com.example.appbrevete.domain.model.AppointmentStatus.CANCELLED -> MaterialTheme.colorScheme.errorContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
                )
            }
            
            if (appointment.notes?.isNotEmpty() == true) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = appointment.notes,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Botones de acción (solo si está programada)
            if (appointment.status == com.example.appbrevete.domain.model.AppointmentStatus.SCHEDULED) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onEdit,
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
                    
                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
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

private fun getAppointmentTypeDisplayName(type: com.example.appbrevete.domain.model.AppointmentType): String {
    return when (type) {
        com.example.appbrevete.domain.model.AppointmentType.MEDICAL_EXAM -> "Examen Médico"
        com.example.appbrevete.domain.model.AppointmentType.THEORY_EXAM -> "Examen Teórico"
        com.example.appbrevete.domain.model.AppointmentType.PRACTICAL_EXAM -> "Examen Práctico"
        com.example.appbrevete.domain.model.AppointmentType.DRIVING_CLASS -> "Clase de Manejo"
        com.example.appbrevete.domain.model.AppointmentType.CONSULTATION -> "Consulta"
    }
}

private fun getAppointmentStatusDisplayName(status: com.example.appbrevete.domain.model.AppointmentStatus): String {
    return when (status) {
        com.example.appbrevete.domain.model.AppointmentStatus.SCHEDULED -> "Programada"
        com.example.appbrevete.domain.model.AppointmentStatus.CONFIRMED -> "Confirmada"
        com.example.appbrevete.domain.model.AppointmentStatus.IN_PROGRESS -> "En Progreso"
        com.example.appbrevete.domain.model.AppointmentStatus.COMPLETED -> "Completada"
        com.example.appbrevete.domain.model.AppointmentStatus.CANCELLED -> "Cancelada"
        com.example.appbrevete.domain.model.AppointmentStatus.NO_SHOW -> "No se presentó"
        com.example.appbrevete.domain.model.AppointmentStatus.RESCHEDULED -> "Reprogramada"
    }
}

private fun formatDate(timestamp: Long): String {
    val date = java.util.Date(timestamp)
    val format = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
    return format.format(date)
}
