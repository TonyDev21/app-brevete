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

// Enum para categorías de motocicletas
enum class MotorcycleLicenseCategory {
    B_IIA,      // Categoría B-IIA
    B_IIB,      // Categoría B-IIB  
    B_IIC       // Categoría B-IIC
}

data class MotorcycleCategoryInfo(
    val category: MotorcycleLicenseCategory,
    val displayName: String,
    val description: String,
    val pdfFileName: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MotorcycleCategoriesScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToPdf: (MotorcycleLicenseCategory) -> Unit = {}
) {
    val categories = remember {
        listOf(
            MotorcycleCategoryInfo(
                MotorcycleLicenseCategory.B_IIA,
                "Categoría B-IIA",
                "Motocicletas hasta 125cc y mototaxis",
                "clase_b_categoria_iia.pdf"
            ),
            MotorcycleCategoryInfo(
                MotorcycleLicenseCategory.B_IIB,
                "Categoría B-IIB",
                "Motocicletas de 125cc a 250cc",
                "clase_b_categoria_iib.pdf"
            ),
            MotorcycleCategoryInfo(
                MotorcycleLicenseCategory.B_IIC,
                "Categoría B-IIC",
                "Motocicletas mayores a 250cc y triciclos",
                "clase_b_categoria_iic.pdf"
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
                text = "Categorías de Motocicletas",
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
                MotorcycleCategoryCard(
                    categoryInfo = categoryInfo,
                    onCategoryClick = { onNavigateToPdf(categoryInfo.category) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MotorcycleCategoryCard(
    categoryInfo: MotorcycleCategoryInfo,
    onCategoryClick: () -> Unit
) {
    Card(
        onClick = onCategoryClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícono de motocicleta
            Icon(
                imageVector = Icons.Default.TwoWheeler,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Información de la categoría
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = categoryInfo.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = categoryInfo.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Flecha indicadora
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}