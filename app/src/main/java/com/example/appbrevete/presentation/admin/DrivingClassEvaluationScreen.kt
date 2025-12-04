package com.example.appbrevete.presentation.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.appbrevete.domain.model.DrivingClassStatus
import com.example.appbrevete.presentation.viewmodel.AdminManageClassesViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrivingClassEvaluationScreen(
    classId: String,
    onNavigateBack: () -> Unit,
    viewModel: AdminManageClassesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    // Cargar clases si están vacías
    LaunchedEffect(Unit) {
        if (uiState.classes.isEmpty()) {
            viewModel.loadClasses()
        }
    }
    
    val drivingClass = remember(uiState.classes) {
        uiState.classes.find { it.id == classId }
    }
    
    var arranque by remember { mutableStateOf(false) }
    var cambios by remember { mutableStateOf(false) }
    var estacionamiento by remember { mutableStateOf(false) }
    var señales by remember { mutableStateOf(false) }
    var maniobras by remember { mutableStateOf(false) }
    var observations by remember { mutableStateOf("") }
    var showConfirmDialog by remember { mutableStateOf(false) }
    
    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (drivingClass == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Clase no encontrada")
                }
            } else {
                // Título
                Text(
                    text = "Evaluar Clase de Manejo",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            
            // Info básica
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Estudiante: ${drivingClass.userName ?: "No disponible"}")
                    Text("DNI: ${drivingClass.userDni ?: "No disponible"}")
                    Text("Fecha: ${formatDate(drivingClass.scheduledDate)} - ${drivingClass.scheduledTime}")
                }
            }
            
            // Criterios de evaluación
            Text(
                text = "Criterios de Evaluación",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    EvaluationCheckbox(
                        label = "Arranque y Frenado",
                        checked = arranque,
                        onCheckedChange = { arranque = it }
                    )
                    
                    EvaluationCheckbox(
                        label = "Cambios de Velocidad",
                        checked = cambios,
                        onCheckedChange = { cambios = it }
                    )
                    
                    EvaluationCheckbox(
                        label = "Estacionamiento",
                        checked = estacionamiento,
                        onCheckedChange = { estacionamiento = it }
                    )
                    
                    EvaluationCheckbox(
                        label = "Respeto de Señales",
                        checked = señales,
                        onCheckedChange = { señales = it }
                    )
                    
                    EvaluationCheckbox(
                        label = "Maniobras Básicas",
                        checked = maniobras,
                        onCheckedChange = { maniobras = it }
                    )
                }
            }
            
            // Observaciones
            OutlinedTextField(
                value = observations,
                onValueChange = { observations = it },
                label = { Text("Observaciones (opcional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 4
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Botón Completar
            Button(
                onClick = { showConfirmDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("COMPLETAR CLASE", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
    }
    
    // Diálogo de confirmación
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Confirmar") },
            text = { Text("¿Marcar esta clase como completada?") },
            confirmButton = {
                Button(
                    onClick = {
                        drivingClass?.let {
                            viewModel.completeDrivingClass(
                                classId = it.id,
                                completedHours = it.totalHours,
                                notes = buildEvaluationNotes(arranque, cambios, estacionamiento, señales, maniobras, observations)
                            )
                        }
                        showConfirmDialog = false
                        onNavigateBack()
                    }
                ) {
                    Text("Sí")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}

@Composable
private fun EvaluationCheckbox(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label)
    }
}

private fun buildEvaluationNotes(
    arranque: Boolean,
    cambios: Boolean,
    estacionamiento: Boolean,
    señales: Boolean,
    maniobras: Boolean,
    observations: String
): String {
    val criterios = mutableListOf<String>()
    if (arranque) criterios.add("Arranque y Frenado")
    if (cambios) criterios.add("Cambios de Velocidad")
    if (estacionamiento) criterios.add("Estacionamiento")
    if (señales) criterios.add("Respeto de Señales")
    if (maniobras) criterios.add("Maniobras Básicas")
    
    val result = StringBuilder()
    if (criterios.isNotEmpty()) {
        result.append("Criterios aprobados: ${criterios.joinToString(", ")}")
    }
    if (observations.isNotBlank()) {
        if (result.isNotEmpty()) result.append("\n\n")
        result.append("Observaciones: $observations")
    }
    return result.toString().takeIf { it.isNotBlank() } ?: "Clase completada"
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
