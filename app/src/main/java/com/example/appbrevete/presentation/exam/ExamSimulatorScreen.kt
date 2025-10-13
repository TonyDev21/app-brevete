package com.example.appbrevete.presentation.exam

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
import androidx.compose.ui.unit.sp

@Composable
fun ExamSimulatorScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Simulador de Examen",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Practica para tu examen de reglas",
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Responde 20 preguntas en 30 minutos",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(getExamCategories()) { category ->
                ExamCategoryCard(category = category)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamCategoryCard(category: ExamCategory) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { /* Iniciar examen */ }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                category.icon,
                contentDescription = category.name,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = category.name,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Text(
                    text = "${category.questionCount} preguntas",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                Icons.Default.ArrowForward,
                contentDescription = "Iniciar",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

data class ExamCategory(
    val name: String,
    val questionCount: Int,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

fun getExamCategories(): List<ExamCategory> {
    return listOf(
        ExamCategory("Señales de Tránsito", 50, Icons.Default.Traffic),
        ExamCategory("Reglas de Tránsito", 40, Icons.Default.Rule),
        ExamCategory("Mecánica Básica", 30, Icons.Default.Build),
        ExamCategory("Seguridad Vial", 35, Icons.Default.Security),
        ExamCategory("Primeros Auxilios", 25, Icons.Default.MedicalServices),
        ExamCategory("Examen Completo", 20, Icons.Default.Quiz)
    )
}
