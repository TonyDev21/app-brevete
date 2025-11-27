package com.example.appbrevete.presentation.classes.create.steps

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appbrevete.presentation.classes.create.components.DrivingPackageCard

/**
 * Paso 1: Selección de paquete de clases
 */

data class DrivingPackage(
    val id: String,
    val name: String,
    val description: String,
    val hours: Int,
    val price: Double
)

@Composable
fun PackageSelectionStep(
    selectedPackage: String?,
    onPackageSelected: (String) -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    val availablePackages = listOf(
        DrivingPackage(
            id = "2h",
            name = "PAQUETE 2H",
            description = "2 horas de práctica de manejo con instructor profesional",
            hours = 2,
            price = 65.0
        ),
        DrivingPackage(
            id = "4h",
            name = "PAQUETE 4H", 
            description = "4 horas de práctica de manejo con instructor profesional",
            hours = 4,
            price = 125.0
        )
    )

    Column(modifier = modifier) {
        Text(
            text = "Tipos de paquete para clases de manejo",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Selecciona el paquete específico que necesitas",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Lista de paquetes disponibles
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(availablePackages) { drivingPackage ->
                DrivingPackageCard(
                    drivingPackage = drivingPackage,
                    isSelected = selectedPackage == drivingPackage.id,
                    onClick = { onPackageSelected(drivingPackage.id) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Botón para continuar
        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth(),
            enabled = !selectedPackage.isNullOrEmpty()
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Continuar")
        }
    }
}