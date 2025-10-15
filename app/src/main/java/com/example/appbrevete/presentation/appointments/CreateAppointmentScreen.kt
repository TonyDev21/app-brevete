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
import com.example.appbrevete.presentation.viewmodel.LicenseTypesViewModel
import com.example.appbrevete.domain.model.LicenseType
import com.example.appbrevete.domain.model.LicenseCategory

enum class VehicleType {
    MOTORCYCLE, CAR
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAppointmentScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDateSelection: (LicenseType) -> Unit,
    viewModel: LicenseTypesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedVehicleType by remember { mutableStateOf<VehicleType?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadLicenseTypes()
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
            IconButton(onClick = {
                if (selectedVehicleType != null) {
                    selectedVehicleType = null // Regresar a selección de tipo de vehículo
                } else {
                    onNavigateBack() // Salir completamente
                }
            }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }
            
            Text(
                text = "Nueva Cita",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.width(48.dp)) // Para balancear el botón de atrás
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedVehicleType == null) {
            // Mostrar selección de tipo de vehículo
            VehicleTypeSelection(
                onVehicleTypeSelected = { vehicleType ->
                    selectedVehicleType = vehicleType
                }
            )
        } else {
            // Mostrar licencias específicas del tipo seleccionado
            LicenseTypeSelection(
                uiState = uiState,
                selectedVehicleType = selectedVehicleType!!,
                onLicenseTypeSelected = onNavigateToDateSelection
            )
        }
    }
}

@Composable
fun VehicleTypeSelection(
    onVehicleTypeSelected: (VehicleType) -> Unit
) {
    Column {
        Text(
            text = "Selecciona el tipo de vehículo",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium
        )
        
        Text(
            text = "Elige si necesitas una cita para licencia de moto o auto",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Tarjeta para Motocicletas
        VehicleTypeCard(
            title = "Motocicletas",
            description = "Licencias categoría B-II (Motos lineales, scooters, mototaxis)",
            icon = Icons.Default.TwoWheeler,
            onClick = { onVehicleTypeSelected(VehicleType.MOTORCYCLE) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tarjeta para Automóviles
        VehicleTypeCard(
            title = "Automóviles",
            description = "Licencias categoría A (Autos, camionetas, buses, camiones)",
            icon = Icons.Default.DirectionsCar,
            onClick = { onVehicleTypeSelected(VehicleType.CAR) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleTypeCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(64.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun LicenseTypeSelection(
    uiState: com.example.appbrevete.presentation.viewmodel.LicenseTypesUiState,
    selectedVehicleType: VehicleType,
    onLicenseTypeSelected: (LicenseType) -> Unit
) {
    val filteredLicenseTypes = when (selectedVehicleType) {
        VehicleType.MOTORCYCLE -> uiState.licenseTypes.filter { 
            it.category in listOf(LicenseCategory.BII_A, LicenseCategory.BII_B, LicenseCategory.BII_C)
        }
        VehicleType.CAR -> uiState.licenseTypes.filter { 
            it.category in listOf(LicenseCategory.A_I, LicenseCategory.A_IIA, LicenseCategory.A_IIB, 
                                  LicenseCategory.A_IIIA, LicenseCategory.A_IIIB, LicenseCategory.A_IIIC)
        }
    }

    Column {
        Text(
            text = when (selectedVehicleType) {
                VehicleType.MOTORCYCLE -> "Tipos de licencia para motocicletas"
                VehicleType.CAR -> "Tipos de licencia para automóviles"
            },
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium
        )
        
        Text(
            text = "Selecciona la categoría específica que necesitas",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (filteredLicenseTypes.isEmpty()) {
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
                        imageVector = Icons.Default.ErrorOutline,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No hay tipos de licencia disponibles",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredLicenseTypes) { licenseType ->
                    LicenseTypeSelectionCard(
                        licenseType = licenseType,
                        onClick = { onLicenseTypeSelected(licenseType) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicenseTypeSelectionCard(
    licenseType: LicenseType,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Ícono de categoría
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = when (licenseType.category) {
                                LicenseCategory.BII_A, LicenseCategory.BII_B, LicenseCategory.BII_C -> Icons.Default.TwoWheeler
                                LicenseCategory.A_I, LicenseCategory.A_IIA, LicenseCategory.A_IIB, LicenseCategory.A_IIIA, LicenseCategory.A_IIIB, LicenseCategory.A_IIIC -> Icons.Default.DirectionsCar
                                else -> Icons.Default.CreditCard
                            },
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column {
                    Text(
                        text = licenseType.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = licenseType.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
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
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Seleccionar",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}