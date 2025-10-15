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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appbrevete.domain.model.LicenseType
import com.example.appbrevete.domain.model.LicenseCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicenseDetailScreen(
    licenseType: LicenseType,
    onNavigateBack: () -> Unit,
    onCreateAppointment: () -> Unit = {}
) {
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
            IconButton(
                onClick = onNavigateBack
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver"
                )
            }
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = licenseType.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Información completa y requisitos",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Información básica
            item {
                BasicInfoCard(licenseType = licenseType)
            }
            
            // Requisitos iniciales
            item {
                RequirementsCard(licenseType = licenseType)
            }
            
            // Costos por etapa
            item {
                CostsCard(licenseType = licenseType)
            }
            
            // Proceso paso a paso
            item {
                ProcessStepsCard(licenseType = licenseType)
            }
            
            // Botón de acción
            item {
                ActionButtonCard(
                    licenseType = licenseType,
                    onCreateAppointment = onCreateAppointment
                )
            }
        }
    }
}

@Composable
fun BasicInfoCard(licenseType: LicenseType) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Información General",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            InfoRow(
                icon = Icons.Default.Info,
                label = "Descripción",
                value = licenseType.description
            )
            
            InfoRow(
                icon = Icons.Default.Person,
                label = "Edad mínima",
                value = "${licenseType.ageRequirement} años"
            )
            
            InfoRow(
                icon = Icons.Default.Schedule,
                label = "Vigencia",
                value = "${licenseType.validityYears} años"
            )
            
            InfoRow(
                icon = Icons.Default.AttachMoney,
                label = "Costo total estimado",
                value = when (licenseType.category) {
                    LicenseCategory.BII_A, LicenseCategory.BII_B -> "S/ 250.00 - S/ 400.00"
                    LicenseCategory.BII_C -> "S/ 400.00 - S/ 650.00"
                    else -> "Consultar"
                }
            )
        }
    }
}

@Composable
fun RequirementsCard(licenseType: LicenseType) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "1. Requisitos Iniciales",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            RequirementItem(
                title = "Edad Mínima",
                detail = "18 años cumplidos"
            )
            
            RequirementItem(
                title = "Documento",
                detail = "DNI vigente, Carnet de Extranjería, PTP, u otros documentos válidos"
            )
            
            RequirementItem(
                title = "No Sanciones",
                detail = "Declaración Jurada de no estar privado del derecho de conducir"
            )
            
            RequirementItem(
                title = "Categoría",
                detail = when (licenseType.category) {
                    LicenseCategory.BII_A -> "B-IIa: Motocicletas de dos y tres ruedas para uso particular"
                    LicenseCategory.BII_B -> "B-IIb: Motocicletas lineales de cualquier cilindraje"
                    LicenseCategory.BII_C -> "B-IIc: Mototaxis/Trimotos de transporte de pasajeros"
                    else -> "Ver detalles específicos"
                }
            )
            
            if (licenseType.category == LicenseCategory.BII_C) {
                RequirementItem(
                    title = "Curso COFIPRO",
                    detail = "Obligatorio para categoría B-IIc (Mototaxis) si es tu primera licencia",
                    isHighlighted = true
                )
            }
        }
    }
}

@Composable
fun CostsCard(licenseType: LicenseType) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "2. Costos Aproximados por Etapa",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            CostItem(
                step = "1. Examen Médico",
                concept = "Certificado de Aptitud Psicosomática (Vigencia 6 meses)",
                entity = "Centros Médicos Autorizados por MTC",
                cost = "S/ 100.00 – S/ 200.00"
            )
            
            if (licenseType.category == LicenseCategory.BII_C) {
                CostItem(
                    step = "2. Examen COFIPRO",
                    concept = "Curso de Formación de Conductores (Solo para B-IIc)",
                    entity = "Escuelas autorizadas (COFIPRO)",
                    cost = "S/ 150.00 – S/ 250.00"
                )
            }
            
            CostItem(
                step = "3. Exámenes de Evaluación",
                concept = "Derecho a Examen de Reglas y de Manejo (2-3 oportunidades)",
                entity = "Touring o Municipalidad Provincial",
                cost = "S/ 65.00 – S/ 73.00 (Lima: S/ 72.76)"
            )
            
            CostItem(
                step = "3. Extra (Manejo)",
                concept = "Alquiler de moto (opcional, si no llevas la tuya)",
                entity = "Centro de Evaluación (Ej. Touring)",
                cost = "S/ 35.00 (aprox.)"
            )
            
            CostItem(
                step = "4. Emisión de Brevete",
                concept = "Derecho a Trámite e Impresión de Licencia",
                entity = "Municipalidad Provincial",
                cost = "S/ 14.70 (Físico) - S/ 27.80 (Lima)"
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = "COSTO TOTAL ESTIMADO",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = when (licenseType.category) {
                            LicenseCategory.BII_A, LicenseCategory.BII_B -> "S/ 250.00 – S/ 400.00 (Moto Lineal)"
                            LicenseCategory.BII_C -> "S/ 400.00 – S/ 650.00 (Mototaxi)"
                            else -> "Consultar costos específicos"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun ProcessStepsCard(licenseType: LicenseType) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "3. Proceso Paso a Paso",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            ProcessStep(
                stepNumber = "1",
                title = "Certificado de Salud Psicosomático",
                description = "• Acude a un Centro Médico Autorizado por el MTC\n" +
                        "• Aprueba exámenes de vista, oído, grupo sanguíneo, psicología y coordinación motriz\n" +
                        "• El centro registra tu aptitud en el Sistema Nacional de Conductores (SNC)"
            )
            
            if (licenseType.category == LicenseCategory.BII_C) {
                ProcessStep(
                    stepNumber = "2",
                    title = "Curso COFIPRO (Solo B-IIc)",
                    description = "• Inscríbete en una escuela autorizada para el curso de Formación de Conductores\n" +
                            "• Aprueba el curso COFIPRO\n" +
                            "• La escuela registra la culminación en el sistema"
                )
            }
            
            ProcessStep(
                stepNumber = "3",
                title = "Pago y Programación de Evaluaciones",
                description = "• Realiza el pago por Derecho a Evaluación (Reglas y Manejo)\n" +
                        "• En Lima: Pago de S/ 72.76 al Touring\n" +
                        "• Programa tus citas para ambos exámenes"
            )
            
            ProcessStep(
                stepNumber = "4",
                title = "Examen de Reglas (Teórico)",
                description = "• Rinde el examen teórico (40 preguntas)\n" +
                        "• Debes aprobar con mínimo 35 correctas\n" +
                        "• Si desapruebas, tienes otra oportunidad"
            )
            
            ProcessStep(
                stepNumber = "5",
                title = "Examen de Manejo (Práctico)",
                description = "• Rinde el examen práctico en el circuito\n" +
                        "• Debes llevar tu moto y casco, o alquilar una\n" +
                        "• Si apruebas, se registra tu aptitud en el SNC"
            )
            
            ProcessStep(
                stepNumber = "6",
                title = "Emisión y Recojo de la Licencia",
                description = "• Realiza el pago por emisión de licencia\n" +
                        "• Licencia Electrónica: S/ 6.70\n" +
                        "• Licencia Física: S/ 14.70\n" +
                        "• Presenta documentos y recoge tu licencia (vigencia 5 años)"
            )
        }
    }
}

@Composable
fun ActionButtonCard(
    licenseType: LicenseType,
    onCreateAppointment: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "¿Listo para comenzar?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Programa tu cita para el examen médico y comienza el proceso",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onCreateAppointment,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Event,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Programar Cita")
            }
        }
    }
}

@Composable
fun InfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun RequirementItem(
    title: String,
    detail: String,
    isHighlighted: Boolean = false
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        color = if (isHighlighted) MaterialTheme.colorScheme.secondaryContainer 
               else MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.small
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = if (isHighlighted) MaterialTheme.colorScheme.onSecondaryContainer
                       else MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = detail,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isHighlighted) MaterialTheme.colorScheme.onSecondaryContainer
                       else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun CostItem(
    step: String,
    concept: String,
    entity: String,
    cost: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.small
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = step,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = concept,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Entidad: $entity",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = cost,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun ProcessStep(
    stepNumber: String,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Surface(
            modifier = Modifier.size(32.dp),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.primary
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stepNumber,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }
    }
}