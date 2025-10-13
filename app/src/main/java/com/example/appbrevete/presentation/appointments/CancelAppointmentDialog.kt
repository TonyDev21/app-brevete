package com.example.appbrevete.presentation.appointments

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.appbrevete.presentation.viewmodel.AppointmentsViewModel
import com.example.appbrevete.domain.model.Appointment

@Composable
fun CancelAppointmentDialog(
    appointment: Appointment,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    viewModel: AppointmentsViewModel = hiltViewModel()
) {
    var showConfirmation by remember { mutableStateOf(false) }
    
    if (showConfirmation) {
        AlertDialog(
            onDismissRequest = onDismiss,
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = {
                Text(
                    text = "Eliminar Cita",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "¿Estás seguro de que quieres eliminar esta cita?\n\n" +
                            "Tipo: ${appointment.type}\n" +
                            "Fecha: ${java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(java.util.Date(appointment.scheduledDate))}\n" +
                            "Hora: ${appointment.scheduledTime}\n\n" +
                            "⚠️ Esta acción no se puede deshacer y la cita será eliminada permanentemente."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteAppointment(appointment)
                        onConfirm()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar Cita")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Mantener Cita")
                }
            }
        )
    } else {
        // Primer diálogo - información general
        AlertDialog(
            onDismissRequest = onDismiss,
            icon = {
                Icon(
                    imageVector = Icons.Default.Cancel,
                    contentDescription = null
                )
            },
            title = {
                Text(
                    text = "¿Cancelar esta cita?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Si cancelas esta cita:\n\n" +
                            "• Perderás la reserva de fecha y hora\n" +
                            "• Tendrás que reagendar si quieres otra cita\n" +
                            "• Es recomendable cancelar con al menos 24 horas de anticipación"
                )
            },
            confirmButton = {
                Button(
                    onClick = { showConfirmation = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Continuar")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Volver")
                }
            }
        )
    }
}
