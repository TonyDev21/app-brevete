package com.example.appbrevete.presentation.appointments.edit.steps

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appbrevete.domain.model.LicenseType
import com.example.appbrevete.presentation.appointments.edit.components.LicenseTypeCard

/**
 * Paso 1: Selección de tipo de licencia
 */

@Composable
fun LicenseSelectionStep(
    licenseTypes: List<LicenseType>,
    selectedLicenseType: LicenseType?,
    originalLicenseTypeId: String?,
    onLicenseSelected: (LicenseType) -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Selecciona el nuevo tipo de licencia",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Mostrar licencia actual si existe
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
        
        // Lista de tipos de licencia disponibles
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
        
        // Botón para continuar
        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedLicenseType != null
        ) {
            Text("Continuar")
        }
    }
}