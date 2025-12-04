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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appbrevete.domain.model.EvaluationResult
import com.example.appbrevete.presentation.viewmodel.MedicalEvaluationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalEvaluationScreen(
    appointmentId: String,
    onNavigateBack: () -> Unit,
    viewModel: MedicalEvaluationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    var medicinaGeneral by remember { mutableStateOf(EvaluationResult.PENDIENTE) }
    var vistaYOido by remember { mutableStateOf(EvaluationResult.PENDIENTE) }
    var grupoSanguineo by remember { mutableStateOf("") }
    var evaluacionPsicologica by remember { mutableStateOf(EvaluationResult.PENDIENTE) }
    var examenRazonamiento by remember { mutableStateOf(EvaluationResult.PENDIENTE) }
    var observaciones by remember { mutableStateOf("") }
    
    var showConfirmDialog by remember { mutableStateOf(false) }
    val isReadOnly = uiState.evaluation != null
    
    LaunchedEffect(appointmentId) {
        viewModel.loadAppointment(appointmentId)
    }
    
    // Cargar datos de la evaluación existente
    LaunchedEffect(uiState.evaluation) {
        uiState.evaluation?.let { eval ->
            medicinaGeneral = eval.medicinaGeneral
            vistaYOido = eval.vistaYOido
            grupoSanguineo = eval.grupoSanguineo
            evaluacionPsicologica = eval.evaluacionPsicologica
            examenRazonamiento = eval.examenRazonamiento
            observaciones = eval.observaciones ?: ""
        }
    }
    
    // Calcular resultado final automáticamente
    val resultadoFinal = medicinaGeneral == EvaluationResult.APROBADO &&
            vistaYOido == EvaluationResult.APROBADO &&
            grupoSanguineo.isNotBlank() &&
            evaluacionPsicologica == EvaluationResult.APROBADO &&
            examenRazonamiento == EvaluationResult.APROBADO
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        if (isReadOnly) "Ver Evaluación Médica" else "Evaluación Médica",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Información del paciente
            if (uiState.appointment != null) {
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
                            text = "Datos del Paciente",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Nombre: ${uiState.appointment?.userName ?: "N/A"}",
                            fontSize = 16.sp
                        )
                        Text(
                            text = "DNI: ${uiState.appointment?.userDni ?: "N/A"}",
                            fontSize = 16.sp
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Criterios de Evaluación",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 1. Medicina General
            EvaluationCriteriaCard(
                title = "Medicina General",
                result = medicinaGeneral,
                onResultChange = { medicinaGeneral = it },
                enabled = !isReadOnly
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 2. Vista y Oído
            EvaluationCriteriaCard(
                title = "Vista y Oído",
                result = vistaYOido,
                onResultChange = { vistaYOido = it },
                enabled = !isReadOnly
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 3. Grupo Sanguíneo
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Grupo Sanguíneo",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = grupoSanguineo,
                        onValueChange = { grupoSanguineo = it },
                        label = { Text("Ej: A+, O-, AB+") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !isReadOnly,
                        readOnly = isReadOnly
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 4. Evaluación Psicológica
            EvaluationCriteriaCard(
                title = "Evaluación Psicológica",
                result = evaluacionPsicologica,
                onResultChange = { evaluacionPsicologica = it },
                enabled = !isReadOnly
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 5. Examen de Razonamiento
            EvaluationCriteriaCard(
                title = "Examen de Razonamiento",
                result = examenRazonamiento,
                onResultChange = { examenRazonamiento = it },
                enabled = !isReadOnly
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Observaciones
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Observaciones",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = observaciones,
                        onValueChange = { observaciones = it },
                        label = { Text("Observaciones generales (opcional)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5,
                        enabled = !isReadOnly,
                        readOnly = isReadOnly
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Resultado Final
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (resultadoFinal) 
                        MaterialTheme.colorScheme.tertiaryContainer 
                    else 
                        MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Resultado Final",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (resultadoFinal) "APROBADO" else "NO APROBADO",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Icon(
                        imageVector = if (resultadoFinal) Icons.Default.CheckCircle else Icons.Default.Cancel,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = if (resultadoFinal) 
                            MaterialTheme.colorScheme.tertiary 
                        else 
                            MaterialTheme.colorScheme.error
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Botón Guardar (solo si no es modo lectura)
            if (!isReadOnly) {
                Button(
                    onClick = { showConfirmDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = grupoSanguineo.isNotBlank() && 
                             medicinaGeneral != EvaluationResult.PENDIENTE &&
                             vistaYOido != EvaluationResult.PENDIENTE &&
                             evaluacionPsicologica != EvaluationResult.PENDIENTE &&
                             examenRazonamiento != EvaluationResult.PENDIENTE
                ) {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Guardar Evaluación", fontSize = 16.sp)
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
    
    // Diálogo de confirmación
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Confirmar Evaluación") },
            text = { 
                Text("¿Está seguro de guardar esta evaluación médica?\n\nResultado: ${if (resultadoFinal) "APROBADO" else "NO APROBADO"}")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.saveEvaluation(
                            appointmentId = appointmentId,
                            medicinaGeneral = medicinaGeneral,
                            vistaYOido = vistaYOido,
                            grupoSanguineo = grupoSanguineo,
                            evaluacionPsicologica = evaluacionPsicologica,
                            examenRazonamiento = examenRazonamiento,
                            observaciones = observaciones,
                            resultadoFinal = resultadoFinal
                        )
                        showConfirmDialog = false
                        onNavigateBack()
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
    
    // Mostrar snackbar si hay error
    if (uiState.errorMessage != null) {
        LaunchedEffect(uiState.errorMessage) {
            // Aquí podrías mostrar un Snackbar
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluationCriteriaCard(
    title: String,
    result: EvaluationResult,
    onResultChange: (EvaluationResult) -> Unit,
    enabled: Boolean = true
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón Aprobado
                FilterChip(
                    selected = result == EvaluationResult.APROBADO,
                    onClick = { if (enabled) onResultChange(EvaluationResult.APROBADO) },
                    label = { Text("Aprobado") },
                    leadingIcon = if (result == EvaluationResult.APROBADO) {
                        { Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    } else null,
                    modifier = Modifier.weight(1f),
                    enabled = enabled,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                )
                
                // Botón No Aprobado
                FilterChip(
                    selected = result == EvaluationResult.NO_APROBADO,
                    onClick = { if (enabled) onResultChange(EvaluationResult.NO_APROBADO) },
                    label = { Text("No Aprobado") },
                    leadingIcon = if (result == EvaluationResult.NO_APROBADO) {
                        { Icon(Icons.Default.Cancel, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    } else null,
                    modifier = Modifier.weight(1f),
                    enabled = enabled,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.errorContainer
                    )
                )
            }
        }
    }
}
