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
                    LicenseCategory.A_I -> "S/ 150.00 - S/ 300.00"
                    LicenseCategory.A_IIA -> "S/ 400.00 - S/ 600.00"
                    LicenseCategory.A_IIB -> "S/ 450.00 - S/ 650.00"
                    LicenseCategory.A_IIIA -> "S/ 550.00 - S/ 750.00"
                    LicenseCategory.A_IIIB -> "S/ 580.00 - S/ 780.00"
                    LicenseCategory.A_IIIC -> "S/ 600.00 - S/ 800.00"
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
                    LicenseCategory.A_I -> "A-I: Automóviles, camionetas, microbuses hasta 2700kg"
                    LicenseCategory.A_IIA -> "A-IIa: Vehículos de categoría A-I para uso profesional"
                    LicenseCategory.A_IIB -> "A-IIb: Microbuses y ómnibus hasta 24 asientos"
                    LicenseCategory.A_IIIA -> "A-IIIa: Ómnibus de más de 24 asientos"
                    LicenseCategory.A_IIIB -> "A-IIIb: Camiones hasta 8000kg con remolque"
                    LicenseCategory.A_IIIC -> "A-IIIc: Camiones de más de 8000kg y tractocamiones"
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
            
            if (licenseType.category in listOf(LicenseCategory.A_IIA, LicenseCategory.A_IIB, LicenseCategory.A_IIIA, LicenseCategory.A_IIIB, LicenseCategory.A_IIIC)) {
                RequirementItem(
                    title = "Curso COFIPRO",
                    detail = "Obligatorio para todas las licencias profesionales de automóvil",
                    isHighlighted = true
                )
                RequirementItem(
                    title = "Examen Médico Reforzado",
                    detail = "Evaluación médica más rigurosa para conductores profesionales",
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
            
            when (licenseType.category) {
                // Costos para motocicletas
                LicenseCategory.BII_A, LicenseCategory.BII_B, LicenseCategory.BII_C -> {
                    CostItem(
                        step = "1. Examen Médico",
                        concept = "Certificado de Aptitud Psicosomática (Vigencia 6 meses)",
                        entity = "Centros Médicos Autorizados por MTC",
                        cost = "S/ 100.00 – S/ 200.00"
                    )
                    
                    if (licenseType.category == LicenseCategory.BII_C) {
                        CostItem(
                            step = "2. Curso COFIPRO",
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
                }
                
                // Costos para A-I (automóvil particular)
                LicenseCategory.A_I -> {
                    CostItem(
                        step = "1. Examen Médico",
                        concept = "Certificado de Aptitud Psicosomática (Vigencia 6 meses)",
                        entity = "Centros Médicos Autorizados por MTC",
                        cost = "S/ 80.00 – S/ 120.00"
                    )
                    
                    CostItem(
                        step = "2. Exámenes de Evaluación",
                        concept = "Derecho a Examen de Reglas y de Manejo",
                        entity = "Touring o Municipalidad Provincial",
                        cost = "S/ 50.00 – S/ 80.00 (Lima: S/ 72.76)"
                    )
                    
                    CostItem(
                        step = "2. Extra (Manejo)",
                        concept = "Alquiler de auto (opcional, si no llevas el tuyo)",
                        entity = "Centro de Evaluación",
                        cost = "S/ 40.00 – S/ 60.00"
                    )
                    
                    CostItem(
                        step = "3. Emisión de Brevete",
                        concept = "Derecho a Trámite e Impresión de Licencia",
                        entity = "Municipalidad Provincial",
                        cost = "S/ 6.70 (Digital) - S/ 14.70 (Físico)"
                    )
                }
                
                // Costos para licencias profesionales
                LicenseCategory.A_IIA, LicenseCategory.A_IIB, LicenseCategory.A_IIIA, 
                LicenseCategory.A_IIIB, LicenseCategory.A_IIIC -> {
                    CostItem(
                        step = "1. Examen Médico",
                        concept = "Certificado de Aptitud Psicosomática (Vigencia 6 meses)",
                        entity = "Centros Médicos Autorizados por MTC",
                        cost = "S/ 150.00 – S/ 250.00"
                    )
                    
                    CostItem(
                        step = "2. Exámenes de Evaluación",
                        concept = "Derecho a Examen de Reglas y de Manejo Especializado",
                        entity = "Touring o Municipalidad Provincial",
                        cost = "S/ 72.76 – S/ 100.00"
                    )
                    
                    CostItem(
                        step = "3. Curso COFIPRO",
                        concept = when (licenseType.category) {
                            LicenseCategory.A_IIA, LicenseCategory.A_IIB, LicenseCategory.A_IIIA, LicenseCategory.A_IIIB -> "Recategorización A-I a profesional (55 horas: 30 teóricas + 25 manejo)"
                            LicenseCategory.A_IIIC -> "Recategorización a A-IIIc (100 horas: 50 teóricas + 50 manejo)"
                            else -> "Curso de Formación de Conductores Profesionales"
                        },
                        entity = "Escuelas de Conductores autorizadas por MTC",
                        cost = "S/ 75.00 – S/ 100.00"
                    )
                    
                    CostItem(
                        step = "3. Extra (Manejo)",
                        concept = when (licenseType.category) {
                            LicenseCategory.A_IIA -> "Alquiler de auto (si no llevas el tuyo)"
                            LicenseCategory.A_IIB -> "Alquiler de microbus (si no llevas el tuyo)"
                            LicenseCategory.A_IIIA -> "Alquiler de ómnibus (si no llevas el tuyo)"
                            LicenseCategory.A_IIIB -> "Alquiler de camión con remolque (si no llevas el tuyo)"
                            LicenseCategory.A_IIIC -> "Alquiler de tractocamión/tráiler (si no llevas el tuyo)"
                            else -> "Alquiler de vehículo profesional"
                        },
                        entity = "Centro de Evaluación",
                        cost = when (licenseType.category) {
                            LicenseCategory.A_IIA -> "S/ 50.00 – S/ 80.00"
                            LicenseCategory.A_IIB -> "S/ 80.00 – S/ 120.00"
                            LicenseCategory.A_IIIA -> "S/ 100.00 – S/ 150.00"
                            LicenseCategory.A_IIIB -> "S/ 120.00 – S/ 180.00"
                            LicenseCategory.A_IIIC -> "S/ 150.00 – S/ 250.00"
                            else -> "S/ 70.00 – S/ 150.00"
                        }
                    )
                    
                    CostItem(
                        step = "4. Emisión de Brevete",
                        concept = "Derecho a Trámite e Impresión de Licencia Profesional",
                        entity = "Municipalidad Provincial",
                        cost = "S/ 14.70 – S/ 30.00"
                    )
                }
            }
            
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
                            LicenseCategory.BII_A, LicenseCategory.BII_B -> "S/ 250.00 – S/ 400.00"
                            LicenseCategory.BII_C -> "S/ 400.00 – S/ 650.00"
                            LicenseCategory.A_I -> "S/ 150.00 – S/ 300.00"
                            LicenseCategory.A_IIA -> "S/ 350.00 – S/ 500.00"
                            LicenseCategory.A_IIB -> "S/ 380.00 – S/ 550.00"
                            LicenseCategory.A_IIIA -> "S/ 400.00 – S/ 580.00"
                            LicenseCategory.A_IIIB -> "S/ 420.00 – S/ 610.00"
                            LicenseCategory.A_IIIC -> "S/ 450.00 – S/ 680.00"
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
            
            when (licenseType.category) {
                // Procesos para motocicletas
                LicenseCategory.BII_A, LicenseCategory.BII_B, LicenseCategory.BII_C -> {
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
                
                // Proceso para A-I (licencia particular de automóvil)
                LicenseCategory.A_I -> {
                    ProcessStep(
                        stepNumber = "1",
                        title = "Examen Médico Psicosomático",
                        description = "• Acude a un Centro Médico Autorizado por el MTC\n" +
                                "• Evaluación de vista, oído, grupo sanguíneo y coordinación motriz\n" +
                                "• Evaluación psicológica para determinar aptitud\n" +
                                "• El centro registra tu aptitud en el Sistema Nacional de Conductores (SNC)\n" +
                                "• Costo aproximado: S/ 80.00 - S/ 120.00"
                    )
                    
                    ProcessStep(
                        stepNumber = "2",
                        title = "Pago y Programación de Evaluaciones",
                        description = "• Realiza el pago por Derecho a Evaluación Teórica y Práctica\n" +
                                "• Costo en Lima (Touring): S/ 72.76\n" +
                                "• Costo en provincias: Varía entre S/ 50.00 - S/ 80.00\n" +
                                "• Programa tus citas para examen teórico y práctico\n" +
                                "• Pueden ser el mismo día o días diferentes"
                    )
                    
                    ProcessStep(
                        stepNumber = "3",
                        title = "Examen de Reglas de Tránsito (Teórico)",
                        description = "• Examen de 40 preguntas sobre reglamento de tránsito\n" +
                                "• Debes responder correctamente mínimo 35 preguntas (87.5%)\n" +
                                "• Tiempo límite: 45 minutos\n" +
                                "• Si desapruebas, puedes dar una segunda oportunidad\n" +
                                "• Temas: señales, infracciones, primeros auxilios, mecánica básica"
                    )
                    
                    ProcessStep(
                        stepNumber = "4",
                        title = "Examen de Manejo (Práctico)",
                        description = "• Evaluación práctica en circuito cerrado o vía pública\n" +
                                "• Debes llevar tu propio vehículo o alquilar uno\n" +
                                "• Evalúan: estacionamiento, arranque en pendiente, marcha atrás\n" +
                                "• También: señalización, uso de espejos, respeto de normas\n" +
                                "• Duración: 15-20 minutos aproximadamente"
                    )
                    
                    ProcessStep(
                        stepNumber = "5",
                        title = "Emisión y Entrega de Licencia",
                        description = "• Realiza el pago por emisión de la licencia\n" +
                                "• Licencia Electrónica: S/ 6.70\n" +
                                "• Licencia Física: S/ 14.70 (varía por municipalidad)\n" +
                                "• Presenta tu DNI y documentos requeridos\n" +
                                "• Recoge tu licencia con vigencia de 10 años\n" +
                                "• ¡Ya puedes conducir legalmente vehículos categoría A-I!"
                    )
                }
                
                // Proceso para licencias profesionales (A-IIa, A-IIb, A-IIIa, A-IIIb, A-IIIc)
                LicenseCategory.A_IIA, LicenseCategory.A_IIB, 
                LicenseCategory.A_IIIA, LicenseCategory.A_IIIB, LicenseCategory.A_IIIC -> {
                    ProcessStep(
                        stepNumber = "1",
                        title = "Licencia A-I Previa (Requisito)",
                        description = "• Debes tener licencia A-I vigente por mínimo 2 años\n" +
                                "• Sin infracciones graves o muy graves en el período\n" +
                                "• La licencia A-I debe estar en buen estado\n" +
                                "• Este es un proceso de RECATEGORIZACIÓN"
                    )
                    
                    ProcessStep(
                        stepNumber = "2",
                        title = "Curso COFIPRO Obligatorio",
                        description = "• Inscríbete en una Escuela de Conductores Profesionales autorizada\n" +
                                "• Curso específico para la categoría que solicitas\n" +
                                "• Incluye clases teóricas y prácticas especializadas\n" +
                                "• Duración: 140 horas académicas\n" +
                                "• Costo aproximado: S/ 800.00 - S/ 1,200.00\n" +
                                "• La escuela registra tu aprobación en el SNC"
                    )
                    
                    ProcessStep(
                        stepNumber = "3",
                        title = "Examen Médico Reforzado",
                        description = "• Examen médico más riguroso para conductores profesionales\n" +
                                "• Incluye evaluación cardiológica y neurológica adicional\n" +
                                "• Exámenes de laboratorio (sangre, orina)\n" +
                                "• Evaluación psicológica especializada\n" +
                                "• Costo aproximado: S/ 150.00 - S/ 250.00\n" +
                                "• Validez: 1 año"
                    )
                    
                    ProcessStep(
                        stepNumber = "4",
                        title = "Evaluación Teórica Especializada",
                        description = "• Examen específico para la categoría profesional\n" +
                                "• Incluye reglamento, carga, pasajeros, seguridad vial\n" +
                                "• 40 preguntas, mínimo 35 correctas\n" +
                                "• Conocimientos sobre el tipo de vehículo específico\n" +
                                "• Costo del derecho de evaluación: S/ 72.76 - S/ 100.00"
                    )
                    
                    ProcessStep(
                        stepNumber = "5",
                        title = "Evaluación Práctica Profesional",
                        description = when (licenseType.category) {
                            LicenseCategory.A_IIA -> "• Examen práctico con automóvil (categoría A-I)\n" +
                                    "• Debes llevar tu propio vehículo o alquilar uno (S/ 50-80)\n" +
                                    "• Evaluación en circuito y vía pública\n" +
                                    "• Maniobras de estacionamiento, reversas, pendientes\n" +
                                    "• Duración: 30-45 minutos"
                            
                            LicenseCategory.A_IIB -> "• Examen práctico con microbús (hasta 24 asientos)\n" +
                                    "• Debes llevar tu propio microbús o alquilar uno (S/ 80-120)\n" +
                                    "• Evaluación en circuito especializado y vía pública\n" +
                                    "• Maniobras de estacionamiento, reversas, manejo de pasajeros\n" +
                                    "• Duración: 30-45 minutos"
                            
                            LicenseCategory.A_IIIA -> "• Examen práctico con ómnibus (más de 24 asientos)\n" +
                                    "• Debes llevar tu propio ómnibus o alquilar uno (S/ 100-150)\n" +
                                    "• Evaluación en circuito especializado y vía pública\n" +
                                    "• Maniobras complejas de estacionamiento y manejo urbano\n" +
                                    "• Duración: 30-45 minutos"
                            
                            LicenseCategory.A_IIIB -> "• Examen práctico con camión y remolque\n" +
                                    "• Debes llevar tu propio vehículo o alquilar uno (S/ 120-180)\n" +
                                    "• Evaluación en circuito especializado y vía pública\n" +
                                    "• Maniobras de reversas con remolque, estacionamiento\n" +
                                    "• Duración: 30-45 minutos"
                            
                            LicenseCategory.A_IIIC -> "• Examen práctico con tractocamión/tráiler\n" +
                                    "• Debes llevar tu propio vehículo o alquilar uno (S/ 150-250)\n" +
                                    "• Evaluación en circuito especializado y vía pública\n" +
                                    "• Maniobras complejas con articulado, estacionamiento\n" +
                                    "• Duración: 30-45 minutos"
                            
                            else -> "• Examen práctico con vehículo de la categoría solicitada\n" +
                                    "• Evaluación en circuito especializado y vía pública\n" +
                                    "• Maniobras específicas según categoría\n" +
                                    "• Mayor exigencia que examen A-I\n" +
                                    "• Duración: 30-45 minutos"
                        }
                    )
                    
                    ProcessStep(
                        stepNumber = "6",
                        title = "Emisión de Licencia Profesional",
                        description = "• Pago por emisión de nueva licencia\n" +
                                "• Licencia Física obligatoria: S/ 14.70 - S/ 30.00\n" +
                                "• Presenta todos los documentos requeridos\n" +
                                "• Vigencia: 3 años (renovable)\n" +
                                "• Ya puedes trabajar como conductor profesional\n" +
                                "• Recuerda cumplir con revisiones técnicas del vehículo"
                    )
                }
            }
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