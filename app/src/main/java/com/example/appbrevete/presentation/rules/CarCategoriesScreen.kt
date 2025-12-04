package com.example.appbrevete.presentation.rules

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
import com.example.appbrevete.domain.model.CarLicenseCategory

data class CategoryInfo(
    val category: CarLicenseCategory,
    val displayName: String,
    val description: String,
    val pdfFileName: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarCategoriesScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToPdf: (CarLicenseCategory) -> Unit = {}
) {
    val categories = remember {
        listOf(
            CategoryInfo(
                CarLicenseCategory.A_I,
                "Categoría A-I",
                "Vehículos menores (mototaxis, motocicletas hasta 125cc)",
                "categoria_a1.pdf"
            ),
            CategoryInfo(
                CarLicenseCategory.A_IIA,
                "Categoría A-IIa",
                "Automóviles particulares",
                "categoria_a2a.pdf"
            ),
            CategoryInfo(
                CarLicenseCategory.A_IIB,
                "Categoría A-IIb",
                "Taxis y vehículos de transporte liviano",
                "categoria_a2b.pdf"
            ),
            CategoryInfo(
                CarLicenseCategory.A_IIIA,
                "Categoría A-IIIa",
                "Vehículos de transporte público de pasajeros",
                "categoria_a3a.pdf"
            ),
            CategoryInfo(
                CarLicenseCategory.A_IIIB,
                "Categoría A-IIIb",
                "Vehículos de carga liviana",
                "categoria_a3b.pdf"
            ),
            CategoryInfo(
                CarLicenseCategory.A_IIIC,
                "Categoría A-IIIc",
                "Vehículos de carga pesada",
                "categoria_a3c.pdf"
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header con botón de regreso
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Regresar"
                )
            }
            Text(
                text = "Categorías de Automóviles",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Selecciona la categoría que deseas estudiar:",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { categoryInfo ->
                CategoryCard(
                    categoryInfo = categoryInfo,
                    onClick = { onNavigateToPdf(categoryInfo.category) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCard(
    categoryInfo: CategoryInfo,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DirectionsCar,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = categoryInfo.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = categoryInfo.description,
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