package com.example.appbrevete.presentation.license

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appbrevete.presentation.viewmodel.LicenseTypesViewModel
import com.example.appbrevete.domain.model.LicenseType

enum class VehicleCategory(val displayName: String) {
    MOTORCYCLES("Motos"),
    CARS("Autos")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicenseTypesScreen(
    viewModel: LicenseTypesViewModel = hiltViewModel(),
    onNavigateToLicenseDetail: (LicenseType) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedCategory by remember { mutableStateOf<VehicleCategory?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadLicenseTypes()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header con navegación
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (selectedCategory != null) {
                IconButton(
                    onClick = { selectedCategory = null }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver"
                    )
                }
            }
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (selectedCategory == null) "Tipos de Licencia" else "Licencias de ${selectedCategory?.displayName ?: ""}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = if (selectedCategory == null) 
                        "Selecciona una categoría de vehículo" 
                    else 
                        "Conoce los tipos de licencia disponibles",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
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
        } else if (selectedCategory == null) {
            // Mostrar categorías principales
            VehicleCategoriesGrid(
                onCategorySelected = { category ->
                    selectedCategory = category
                }
            )
        } else {
            // Mostrar tipos de licencia filtrados por categoría
            val currentCategory = selectedCategory // Copia local para smart cast
            if (currentCategory != null) {
                val filteredLicenseTypes = uiState.licenseTypes.filter { licenseType ->
                    when (currentCategory) {
                        VehicleCategory.MOTORCYCLES -> licenseType.category in listOf(
                            com.example.appbrevete.domain.model.LicenseCategory.BII_A,
                            com.example.appbrevete.domain.model.LicenseCategory.BII_B,
                            com.example.appbrevete.domain.model.LicenseCategory.BII_C
                        )
                        VehicleCategory.CARS -> licenseType.category in listOf(
                            com.example.appbrevete.domain.model.LicenseCategory.A_I,
                            com.example.appbrevete.domain.model.LicenseCategory.A_IIA,
                            com.example.appbrevete.domain.model.LicenseCategory.A_IIB,
                            com.example.appbrevete.domain.model.LicenseCategory.A_IIIA,
                            com.example.appbrevete.domain.model.LicenseCategory.A_IIIB,
                            com.example.appbrevete.domain.model.LicenseCategory.A_IIIC
                        )
                    }
                }
                
                // Debug temporal - agregar logs
                println("DEBUG: Total license types: ${uiState.licenseTypes.size}")
                uiState.licenseTypes.forEach { license ->
                    println("DEBUG: License - ID: ${license.id}, Category: ${license.category}, Name: ${license.name}")
                }
                println("DEBUG: Filtered license types for ${currentCategory}: ${filteredLicenseTypes.size}")
                filteredLicenseTypes.forEach { license ->
                    println("DEBUG: Filtered License - ID: ${license.id}, Category: ${license.category}, Name: ${license.name}")
                }
            
            if (filteredLicenseTypes.isEmpty()) {
                // Estado vacío para categoría específica
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
                            imageVector = Icons.Default.CreditCard,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No hay licencias disponibles para ${currentCategory.displayName}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                } else {
                    // Lista de tipos de licencia filtrados
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredLicenseTypes) { licenseType ->
                            LicenseTypeCard(
                                licenseType = licenseType,
                                onClick = { onNavigateToLicenseDetail(licenseType) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicenseTypeCard(
    licenseType: LicenseType,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                        text = licenseType.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
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
                                com.example.appbrevete.domain.model.LicenseCategory.BII_A,
                                com.example.appbrevete.domain.model.LicenseCategory.BII_B,
                                com.example.appbrevete.domain.model.LicenseCategory.BII_C -> Icons.Default.TwoWheeler
                                com.example.appbrevete.domain.model.LicenseCategory.A_I,
                                com.example.appbrevete.domain.model.LicenseCategory.A_IIA,
                                com.example.appbrevete.domain.model.LicenseCategory.A_IIB,
                                com.example.appbrevete.domain.model.LicenseCategory.A_IIIA,
                                com.example.appbrevete.domain.model.LicenseCategory.A_IIIB,
                                com.example.appbrevete.domain.model.LicenseCategory.A_IIIC -> Icons.Default.DirectionsCar
                                else -> Icons.Default.CreditCard
                            },
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = licenseType.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Información adicional
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Edad mínima",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${licenseType.ageRequirement} años",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Column {
                    Text(
                        text = "Vigencia",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${licenseType.validityYears} años",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Column {
                    Text(
                        text = "Costo",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "S/. ${licenseType.price}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Botón de acción
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ver requisitos y procesos")
            }
        }
    }
}

@Composable
fun VehicleCategoriesGrid(
    onCategorySelected: (VehicleCategory) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Categoría Motos
        VehicleCategoryCard(
            category = VehicleCategory.MOTORCYCLES,
            icon = Icons.Default.TwoWheeler,
            description = "Licencias para motocicletas y vehículos de dos ruedas",
            onClick = { onCategorySelected(VehicleCategory.MOTORCYCLES) }
        )
        
        // Categoría Autos
        VehicleCategoryCard(
            category = VehicleCategory.CARS,
            icon = Icons.Default.DirectionsCar,
            description = "Licencias para automóviles, camionetas y vehículos comerciales",
            onClick = { onCategorySelected(VehicleCategory.CARS) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleCategoryCard(
    category: VehicleCategory,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Ícono grande
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.primary,
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
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            
            // Contenido de texto
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = category.displayName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Flecha
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Entrar",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
