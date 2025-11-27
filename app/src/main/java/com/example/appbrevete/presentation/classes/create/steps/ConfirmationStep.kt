package com.example.appbrevete.presentation.classes.create.steps

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Paso 3: Confirmación de la clase de manejo
 */

@Composable
fun ConfirmationStep(
    packageType: String,
    date: String,
    time: String,
    isLoading: Boolean,
    onConfirm: () -> Unit,
    onModify: () -> Unit,
    modifier: Modifier = Modifier
) {
    val packageInfo = getPackageInfo(packageType)
    
    Column(modifier = modifier) {
        
        // Resumen de la clase
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Assignment,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Resumen de tu clase",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                // Tipo de Paquete
                DetailRow(
                    icon = Icons.Default.DriveEta,
                    label = "Tipo de Paquete",
                    value = packageInfo.title
                )
                
                // Descripción
                DetailRow(
                    icon = Icons.Default.Description,
                    label = "Descripción", 
                    value = packageInfo.description
                )
                
                // Fecha
                DetailRow(
                    icon = Icons.Default.CalendarToday,
                    label = "Fecha",
                    value = formatDate(date)
                )
                
                // Hora
                DetailRow(
                    icon = Icons.Default.Schedule,
                    label = "Hora",
                    value = time
                )
                
                // Ubicación
                DetailRow(
                    icon = Icons.Default.LocationOn,
                    label = "Ubicación",
                    value = "Centro de Clases de Manejo - Lima"
                )
                
                Divider(
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                
                // Total a pagar dentro del resumen
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total a pagar:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = packageInfo.price,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Información importante
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Información importante",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "• Llega 15 minutos antes de tu clase\n• Trae tu DNI y documentos requeridos\n• El pago se realiza el día de la clase",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
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
            // Botón Modificar
            OutlinedButton(
                onClick = onModify,
                modifier = Modifier.weight(1f),
                enabled = !isLoading
            ) {
                Text("Modificar")
            }
            
            // Botón Confirmar
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
                    Text("Confirmando...")
                } else {
                    Text("Confirmar Clase")
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

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