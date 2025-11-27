package com.example.appbrevete.presentation.classes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.appbrevete.presentation.viewmodel.DrivingClassViewModel
import com.example.appbrevete.domain.model.DrivingClass
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CancelDrivingClassDialog(
    drivingClass: DrivingClass,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    viewModel: DrivingClassViewModel = hiltViewModel()
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
                    text = "Eliminar Clase",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "¿Estás seguro de que quieres eliminar esta clase?\n\n" +
                            "Tipo: ${getDrivingClassTypeDisplayName(drivingClass.packageType)}\n" +
                            "Fecha: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(drivingClass.scheduledDate))}\n" +
                            "Hora: ${drivingClass.scheduledTime}\n\n" +
                            "⚠️ Esta acción no se puede deshacer y la clase será eliminada permanentemente."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteDrivingClass(drivingClass.id)
                        onConfirm()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar Clase")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Mantener Clase")
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
                    text = "¿Cancelar esta clase?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Si cancelas esta clase:\n\n" +
                            "• Perderás la reserva de fecha y hora\n" +
                            "• Tendrás que reagendar si quieres otra clase\n" +
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

private fun getDrivingClassTypeDisplayName(packageType: com.example.appbrevete.domain.model.DrivingPackageType): String {
    return when (packageType) {
        com.example.appbrevete.domain.model.DrivingPackageType.BASIC_2H -> "Paquete 2 Horas"
        com.example.appbrevete.domain.model.DrivingPackageType.STANDARD_4H -> "Paquete 4 Horas"
        com.example.appbrevete.domain.model.DrivingPackageType.CUSTOM -> "Paquete Personalizado"
    }
}