package com.example.appbrevete.presentation.appointments

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import com.example.appbrevete.domain.model.LicenseType
import com.example.appbrevete.domain.model.AppointmentType
import com.example.appbrevete.presentation.viewmodel.AppointmentsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentSummaryScreen(
    userId: String,
    licenseType: LicenseType,
    selectedDate: String,
    selectedTime: String,
    onNavigateBack: () -> Unit,
    onAppointmentCreated: () -> Unit,
    viewModel: AppointmentsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var isAppointmentCreated by remember { mutableStateOf(false) }
    
    // Mapa de descripciones por ID de licencia
    val licenseDescriptions = mapOf(
        "license-bii-a" to "Motocicletas de dos y tres ruedas (con sidecar) para uso particular.",
        "license-bii-b" to "Los mismos vehículos que la licencia BII-A y para motocicletas de cualquier cilindraje.",
        "license-bii-c" to "Para mototaxis y trimotos destinadas al transporte de pasajeros.",
        "license-a-i" to "Vehículos particulares (Sedán, Coupé, Pick-up, Station Wagon, etc.). Máx. 9 asientos y 3.5 toneladas.",
        "license-a-iia" to "Vehículos de transporte de personas y mercancías hasta 3.5 toneladas (Taxis, ambulancias, furgonetas, y todos los de la cat. A-I).",
        "license-a-iib" to "Minibuses (máx. 16 asientos, 4 toneladas), furgonetas (máx. 7 toneladas), y todos los de A-I y A-IIa.",
        "license-a-iiia" to "Ómnibus, buses y vehículos de transporte de personas (más de 6 toneladas).",
        "license-a-iiib" to "Vehículos de transporte de carga (camiones rígidos de más de 12 toneladas) y todos los de A-I y A-IIa.",
        "license-a-iiic" to "Todos los vehículos de las categorías A-I, A-IIa, A-IIb, A-IIIa y A-IIIb."
    )
    
    val licenseDescription = licenseDescriptions[licenseType.id] ?: "Descripción no disponible"
    
    // Convertir fecha y hora de formato URL-safe a formato de visualización
    val displayDate = if (selectedDate.contains("-")) {
        selectedDate.replace("-", "/")
    } else {
        selectedDate
    }
    
    val displayTime = if (selectedTime.contains("-")) {
        selectedTime.replace("-", ":")
    } else {
        selectedTime
    }
    
    // Log para depuración
    println("AppointmentSummary: Received date=$selectedDate, time=$selectedTime")
    println("AppointmentSummary: Display date=$displayDate, time=$displayTime")
    
    // Calcular fecha en timestamp
    val scheduledTimestamp = remember {
        try {
            val dateParts = if (selectedDate.contains("-")) {
                selectedDate.split("-")
            } else {
                selectedDate.split("/")
            }
            val day = dateParts[0].toInt()
            val month = dateParts[1].toInt()
            val year = dateParts[2].toInt()
            
            // Crear fecha
            val calendar = java.util.Calendar.getInstance()
            calendar.set(year, month - 1, day) // month es 0-based
            calendar.timeInMillis
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }

    LaunchedEffect(uiState.isCreatingAppointment, uiState.errorMessage, isAppointmentCreated) {
        println("AppointmentSummary: State changed - isCreating: ${uiState.isCreatingAppointment}, error: ${uiState.errorMessage}, shouldNavigate: $isAppointmentCreated")
        
        // Si ya no está creando la cita, no hay error, y se marcó para crear
        if (!uiState.isCreatingAppointment && uiState.errorMessage == null && isAppointmentCreated) {
            println("AppointmentSummary: Appointment created successfully, navigating back")
            // Navegar inmediatamente
            onAppointmentCreated()
        }
        
        // Si hay error, resetear el flag
        if (uiState.errorMessage != null && isAppointmentCreated) {
            println("AppointmentSummary: Error occurred, resetting flag")
            isAppointmentCreated = false
        }
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
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }
            
            Text(
                text = "Confirmar Cita",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Resumen de la cita
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Assignment,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Resumen de tu cita",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Detalles de la licencia
                SummaryItem(
                    icon = Icons.Default.CreditCard,
                    label = "Tipo de Licencia",
                    value = licenseType.name
                )
                
                SummaryItem(
                    icon = Icons.Default.Description,
                    label = "Descripción",
                    value = licenseDescription
                )
                
                SummaryItem(
                    icon = Icons.Default.CalendarToday,
                    label = "Fecha",
                    value = displayDate
                )
                
                SummaryItem(
                    icon = Icons.Default.Schedule,
                    label = "Hora",
                    value = displayTime
                )
                
                SummaryItem(
                    icon = Icons.Default.LocationOn,
                    label = "Ubicación",
                    value = "Centro de Licencias - Lima"
                )
                
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                
                // Total
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total a pagar:",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "S/. ${licenseType.price}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Información adicional
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Información importante",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = "• Llega 15 minutos antes de tu cita\n• Trae tu DNI y documentos requeridos\n• El pago se realiza el día de la cita",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Mostrar mensaje de éxito si la cita se está creando
        if (uiState.isCreatingAppointment) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Creando tu cita...",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Mostrar mensaje de éxito cuando la cita fue creada
        if (!uiState.isCreatingAppointment && isAppointmentCreated && uiState.errorMessage == null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "¡Cita creada exitosamente! Redirigiendo...",
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Mostrar error si existe
        if (uiState.errorMessage != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = "Error: ${uiState.errorMessage}",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Botones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onNavigateBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("Modificar")
            }
            
            Button(
                onClick = { showConfirmationDialog = true },
                modifier = Modifier.weight(1f),
                enabled = !uiState.isCreatingAppointment
            ) {
                if (uiState.isCreatingAppointment) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Confirmar Cita")
                }
            }
        }
    }
    
    // Diálogo de confirmación
    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = { Text("Confirmar reservación") },
            text = { Text("¿Estás seguro de que quieres reservar esta cita? El pago se realizará el día de la cita.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        println("AppointmentSummary: User confirmed appointment creation")
                        showConfirmationDialog = false
                        
                        // Crear la cita y navegar inmediatamente para simplificar
                        viewModel.createAppointment(
                            userId = userId,
                            type = AppointmentType.MEDICAL_EXAM,
                            scheduledDate = scheduledTimestamp,
                            scheduledTime = displayTime,
                            location = "Centro de Licencias - Lima",
                            licenseTypeId = licenseType.id,
                            notes = "Trámite para ${licenseType.name}"
                        )
                        
                        // Navegar inmediatamente sin esperar estado
                        println("AppointmentSummary: Navigating immediately after creation")
                        onAppointmentCreated()
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmationDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun SummaryItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .size(20.dp)
                .padding(top = 2.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}
