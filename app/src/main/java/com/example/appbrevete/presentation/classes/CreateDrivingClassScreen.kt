package com.example.appbrevete.presentation.classes

import androidx.compose.foundation.layout.*
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
import com.example.appbrevete.presentation.viewmodel.DrivingClassViewModel
import com.example.appbrevete.presentation.classes.create.steps.PackageSelectionStep
import com.example.appbrevete.presentation.classes.create.steps.DateTimeSelectionStep
import com.example.appbrevete.presentation.classes.create.steps.ConfirmationStep

enum class CreateClassStep {
    PACKAGE_SELECTION, DATE_TIME_SELECTION, CONFIRMATION
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateDrivingClassScreen(
    userId: String,
    onNavigateBack: () -> Unit,
    viewModel: DrivingClassViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var currentStep by remember { mutableStateOf(CreateClassStep.PACKAGE_SELECTION) }
    var selectedPackage by remember { mutableStateOf<String?>(null) }
    var selectedDate by remember { mutableStateOf<String?>(null) }
    var selectedTime by remember { mutableStateOf<String?>(null) }

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
                when (currentStep) {
                    CreateClassStep.PACKAGE_SELECTION -> onNavigateBack()
                    CreateClassStep.DATE_TIME_SELECTION -> {
                        currentStep = CreateClassStep.PACKAGE_SELECTION
                    }
                    CreateClassStep.CONFIRMATION -> {
                        currentStep = CreateClassStep.DATE_TIME_SELECTION
                    }
                }
            }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }
            
            Text(
                text = when (currentStep) {
                    CreateClassStep.PACKAGE_SELECTION -> "Nueva Clase"
                    CreateClassStep.DATE_TIME_SELECTION -> "Fecha y Hora"
                    CreateClassStep.CONFIRMATION -> "Confirmar Clase"
                },
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.width(48.dp)) // Para balancear el botón de atrás
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (currentStep) {
            CreateClassStep.PACKAGE_SELECTION -> {
                PackageSelectionStep(
                    selectedPackage = selectedPackage,
                    onPackageSelected = { packageType ->
                        selectedPackage = packageType
                    },
                    onNext = {
                        currentStep = CreateClassStep.DATE_TIME_SELECTION
                    }
                )
            }
            
            CreateClassStep.DATE_TIME_SELECTION -> {
                DateTimeSelectionStep(
                    selectedPackage = selectedPackage ?: "",
                    selectedDate = selectedDate,
                    selectedTime = selectedTime,
                    onDateSelected = { date ->
                        selectedDate = date
                    },
                    onTimeSelected = { time ->
                        selectedTime = time
                    },
                    onNext = {
                        currentStep = CreateClassStep.CONFIRMATION
                    }
                )
            }
            
            CreateClassStep.CONFIRMATION -> {
                ConfirmationStep(
                    packageType = selectedPackage ?: "",
                    date = selectedDate ?: "",
                    time = selectedTime ?: "",
                    isLoading = uiState.isCreatingClass,
                    onConfirm = {
                        viewModel.createDrivingClass(
                            userId = userId,
                            packageType = selectedPackage ?: "",
                            date = selectedDate ?: "",
                            time = selectedTime ?: "",
                            onSuccess = {
                                onNavigateBack()
                            },
                            onError = { error ->
                                // TODO: Mostrar error
                            }
                        )
                    },
                    onModify = {
                        currentStep = CreateClassStep.DATE_TIME_SELECTION
                    }
                )
            }
        }
    }
}