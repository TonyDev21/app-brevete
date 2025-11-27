package com.example.appbrevete.presentation.appointments.edit.steps

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appbrevete.domain.model.Appointment
import com.example.appbrevete.domain.model.LicenseType
import com.example.appbrevete.presentation.appointments.edit.components.ComparisonItem
import com.example.appbrevete.presentation.appointments.edit.formatTimestamp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Paso 3: Confirmación de cambios
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConfirmationStep(
    originalAppointment: Appointment?,
    newLicenseType: LicenseType?,
    newDate: LocalDate?,
    newTime: String?,
    licenseTypes: List<LicenseType>,
    onConfirm: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
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