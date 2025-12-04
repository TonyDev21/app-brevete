package com.example.appbrevete.presentation.rules

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appbrevete.domain.model.CarLicenseCategory
import com.example.appbrevete.domain.model.MotorcycleLicenseCategory
import com.example.appbrevete.domain.model.VehicleCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

data class ContentSection(
    val title: String,
    val items: List<String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RulesPdfViewerScreen(
    vehicleType: VehicleCategory,
    category: CarLicenseCategory? = null,
    navController: NavController
) {
    val context = LocalContext.current
    
    val title = when (vehicleType) {
        VehicleCategory.MOTORCYCLE -> "Reglas para Motocicletas"
        VehicleCategory.CAR -> {
            when (category) {
                CarLicenseCategory.A_I -> "Categoría A-I"
                CarLicenseCategory.A_IIA -> "Categoría A-IIa"
                CarLicenseCategory.A_IIB -> "Categoría A-IIb"
                CarLicenseCategory.A_IIC -> "Categoría A-IIc"
                CarLicenseCategory.A_IIIA -> "Categoría A-IIIa"
                CarLicenseCategory.A_IIIB -> "Categoría A-IIIb"
                CarLicenseCategory.A_IIIC -> "Categoría A-IIIc"
                null -> "Reglas para Automóviles"
            }
        }
    }
    
    val sections = getContentForCategory(vehicleType, category)
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                // Información sobre descarga
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Info,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Información",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Esta es una guía básica. Para el manual completo y actualizado del MTC, presiona el botón de descarga.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
                
                // Contenido de las reglas
                items(sections) { section ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = section.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                            
                            section.items.forEach { item ->
                                Row(
                                    modifier = Modifier.padding(vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "• ",
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = item,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
                
                // Botones de acción
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Botón: Página Oficial
                        Button(
                            onClick = {
                                openOfficialPage(context, vehicleType, category)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Language,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Página Oficial",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                        
                        // Botón: Descargar PDF
                        Button(
                            onClick = {
                                downloadPdfFromAssets(context, vehicleType, category)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Download,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Descargar PDF",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                        
                        // Botón: Tomar Simulacro
                        Button(
                            onClick = {
                                openOfficialSimulator(context)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.tertiary
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Quiz,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Tomar Simulacro",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

fun getContentForCategory(vehicleType: VehicleCategory, category: CarLicenseCategory?): List<ContentSection> {
    return when (vehicleType) {
        VehicleCategory.MOTORCYCLE -> getMotorcycleContent()
        VehicleCategory.CAR -> getCarContent(category)
    }
}

fun getMotorcycleContent(): List<ContentSection> {
    return listOf(
        ContentSection(
            title = "Equipamiento Obligatorio",
            items = listOf(
                "Casco homologado para conductor y acompañante",
                "Chaleco reflectivo durante la noche",
                "Luces delanteras y posteriores funcionando",
                "Espejo retrovisor en buen estado",
                "Bocina operativa",
                "Sistema de frenos delantero y posterior"
            )
        ),
        ContentSection(
            title = "Velocidades Máximas",
            items = listOf(
                "Zona urbana: 40 km/h",
                "Zona escolar: 25 km/h", 
                "Zona residencial: 25 km/h",
                "Carreteras locales: 45 km/h",
                "Prohibido en autopistas y vías expresas"
            )
        ),
        ContentSection(
            title = "Reglas de Circulación",
            items = listOf(
                "Circular por el carril derecho",
                "No circular entre carriles (lane splitting)",
                "Máximo un acompañante",
                "Respetar semáforos y señales",
                "Mantener distancia segura (3 segundos)",
                "No circular en aceras o veredas"
            )
        ),
        ContentSection(
            title = "Infracciones Comunes",
            items = listOf(
                "Conducir sin casco: Multa grave",
                "Exceso de pasajeros: Multa y retención",
                "Circular en vías prohibidas: Inmovilización",
                "Sin documentos: Multa y retención temporal",
                "No respetar semáforos: Multa grave"
            )
        )
    )
}

fun getCarContent(category: CarLicenseCategory?): List<ContentSection> {
    val baseContent = listOf(
        ContentSection(
            title = "Velocidades Máximas",
            items = listOf(
                "Zona urbana: 50 km/h",
                "Zona escolar: 30 km/h",
                "Vías expresas urbanas: 80 km/h", 
                "Carreteras: 90 km/h",
                "Autopistas: 120 km/h"
            )
        ),
        ContentSection(
            title = "Equipamiento Obligatorio",
            items = listOf(
                "Cinturón de seguridad para todos los ocupantes",
                "Luces delanteras, posteriores y direccionales",
                "Espejos retrovisores (interior y exteriores)",
                "Bocina funcionando",
                "Sistema de frenos operativo",
                "Neumáticos en buen estado"
            )
        ),
        ContentSection(
            title = "Reglas Fundamentales",
            items = listOf(
                "Mantener distancia de seguimiento (regla de 3 segundos)",
                "Respetar límites de velocidad",
                "No usar teléfono celular mientras conduce",
                "Ceder el paso en intersecciones",
                "Detenerse completamente en señales de ALTO",
                "No conducir bajo efectos del alcohol"
            )
        )
    )
    
    // Agregar contenido específico por categoría
    val specificContent = when (category) {
        CarLicenseCategory.A_IIB -> listOf(
            ContentSection(
                title = "Especial para Microbuses (A-IIB)",
                items = listOf(
                    "Capacidad máxima: 24 asientos",
                    "Extintor de 4 kg mínimo",
                    "Botiquín de primeros auxilios",
                    "Cinturones para todos los asientos",
                    "Puertas de emergencia operativas",
                    "Velocidad reducida: 45 km/h en zona urbana"
                )
            )
        )
        CarLicenseCategory.A_IIC -> listOf(
            ContentSection(
                title = "Especial para Camiones (A-IIC)",
                items = listOf(
                    "Peso máximo: 7,000 - 12,000 kg",
                    "Extintor de 6 kg mínimo",
                    "Cuñas de seguridad (4 unidades)",
                    "Triángulos reflectivos (3 unidades)",
                    "Velocidades reducidas por peso",
                    "Revisión técnica cada 6 meses"
                )
            )
        )
        else -> emptyList()
    }
    
    return baseContent + specificContent
}

fun openOfficialSimulator(context: Context) {
    val simulatorUrl = "https://sierdgtt.mtc.gob.pe/"
    
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(simulatorUrl)
    }
    
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "No se pudo abrir el navegador", Toast.LENGTH_SHORT).show()
    }
}

fun openOfficialPage(context: Context, vehicleType: VehicleCategory, category: CarLicenseCategory?) {
    val officialUrl = getOfficialUrl(vehicleType, category)
    
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(officialUrl)
    }
    
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "No se pudo abrir el navegador", Toast.LENGTH_SHORT).show()
    }
}

fun getOfficialUrl(vehicleType: VehicleCategory, category: CarLicenseCategory?): String {
    return when (vehicleType) {
        VehicleCategory.MOTORCYCLE -> {
            when (category) {
                CarLicenseCategory.A_IIA -> "https://portal.mtc.gob.pe/transportes/terrestre/licencias/documentos/licencias/CLASE_B_CATEGOR%C3%8DA_IIA.pdf"
                CarLicenseCategory.A_IIB -> "https://portal.mtc.gob.pe/transportes/terrestre/licencias/documentos/licencias/CLASE_B_CATEGOR%C3%8DA_IIB.pdf"
                CarLicenseCategory.A_IIC -> "https://portal.mtc.gob.pe/transportes/terrestre/licencias/documentos/licencias/CLASE_B_CATEGOR%C3%8DA_IIC.pdf"
                else -> "https://portal.mtc.gob.pe/transportes/terrestre/licencias/documentos/licencias/CLASE_B_CATEGOR%C3%8DA_IIA.pdf" // Por defecto
            }
        }
        VehicleCategory.CAR -> {
            when (category) {
                CarLicenseCategory.A_I -> "https://portal.mtc.gob.pe/transportes/terrestre/licencias/documentos/licencias/CLASE_A_CATEGOR%C3%8DA_I%20-%20NUEVO.pdf"
                CarLicenseCategory.A_IIA -> "https://portal.mtc.gob.pe/transportes/terrestre/licencias/documentos/licencias/CLASE_A_CATEGOR%C3%8DA_IIA%20-%20NUEVO.pdf"
                CarLicenseCategory.A_IIB -> "https://portal.mtc.gob.pe/transportes/terrestre/licencias/documentos/licencias/CLASE_A_CATEGOR%C3%8DA_IIB%20-%20NUEVO.pdf"
                CarLicenseCategory.A_IIIA -> "https://portal.mtc.gob.pe/transportes/terrestre/licencias/documentos/licencias/CLASE_A_CATEGOR%C3%8DA_IIIA%20-%20NUEVO.pdf"
                CarLicenseCategory.A_IIIB -> "https://portal.mtc.gob.pe/transportes/terrestre/licencias/documentos/licencias/CLASE_A_CATEGOR%C3%8DA_IIIB%20-%20NUEVO.pdf"
                CarLicenseCategory.A_IIIC -> "https://portal.mtc.gob.pe/transportes/terrestre/licencias/documentos/licencias/CLASE_A_CATEGOR%C3%8DA_IIIC%20-%20NUEVO.pdf"
                CarLicenseCategory.A_IIC -> "https://portal.mtc.gob.pe/transportes/terrestre/licencias/documentos/licencias/CLASE_A_CATEGOR%C3%8DA_IIB%20-%20NUEVO.pdf" // No existe A-IIC para autos, usar A-IIB
                null -> "https://portal.mtc.gob.pe/transportes/terrestre/licencias/documentos/licencias/CLASE_A_CATEGOR%C3%8DA_I%20-%20NUEVO.pdf" // Por defecto
            }
        }
    }
}

fun downloadPdfFromAssets(context: Context, vehicleType: VehicleCategory, category: CarLicenseCategory?) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val assetPath = getPdfAssetPath(vehicleType, category)
            val fileName = getPdfFileName(vehicleType, category)
            
            // Copiar archivo desde assets a la carpeta de descargas
            val inputStream: InputStream = context.assets.open(assetPath)
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val outputFile = File(downloadsDir, fileName)
            
            FileOutputStream(outputFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            
            inputStream.close()
            
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "PDF descargado: ${outputFile.absolutePath}", Toast.LENGTH_LONG).show()
                
                // Abrir el PDF descargado
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(Uri.fromFile(outputFile), "application/pdf")
                    flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                }
                
                try {
                    context.startActivity(intent)
                } catch (e: Exception) {
                    // Si no puede abrir directamente, usar chooser
                    val chooser = Intent.createChooser(intent, "Abrir PDF")
                    context.startActivity(chooser)
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error al descargar PDF: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

fun getPdfAssetPath(vehicleType: VehicleCategory, category: CarLicenseCategory?): String {
    return when (vehicleType) {
        VehicleCategory.MOTORCYCLE -> {
            // Para motocicletas, usar uno de los PDFs de motos disponibles
            "rules/motos/CLASE_B_CATEGORÍA_IIA.pdf"
        }
        VehicleCategory.CAR -> {
            when (category) {
                CarLicenseCategory.A_I -> "rules/autos/CLASE_A_CATEGORÍA_I.pdf"
                CarLicenseCategory.A_IIA -> "rules/autos/CLASE_A_CATEGORÍA_IIA.pdf"
                CarLicenseCategory.A_IIB -> "rules/autos/CLASE_A_CATEGORÍA_IIB.pdf"
                CarLicenseCategory.A_IIC -> "rules/autos/CLASE_A_CATEGORÍA_IIC.pdf"  // Nota: el archivo dice IIC pero está en la carpeta motos
                CarLicenseCategory.A_IIIA -> "rules/autos/CLASE_A_CATEGORÍA_IIIA.pdf"
                CarLicenseCategory.A_IIIB -> "rules/autos/CLASE_A_CATEGORÍA_IIIB.pdf"
                CarLicenseCategory.A_IIIC -> "rules/autos/CLASE_A_CATEGORÍA_IIIC.pdf"
                null -> "rules/autos/CLASE_A_CATEGORÍA_I.pdf" // PDF por defecto
            }
        }
    }
}

fun getPdfFileName(vehicleType: VehicleCategory, category: CarLicenseCategory?): String {
    return when (vehicleType) {
        VehicleCategory.MOTORCYCLE -> "Reglas_Motocicletas.pdf"
        VehicleCategory.CAR -> {
            when (category) {
                CarLicenseCategory.A_I -> "Reglas_Categoria_A-I.pdf"
                CarLicenseCategory.A_IIA -> "Reglas_Categoria_A-IIA.pdf"
                CarLicenseCategory.A_IIB -> "Reglas_Categoria_A-IIB.pdf"
                CarLicenseCategory.A_IIC -> "Reglas_Categoria_A-IIC.pdf"
                CarLicenseCategory.A_IIIA -> "Reglas_Categoria_A-IIIA.pdf"
                CarLicenseCategory.A_IIIB -> "Reglas_Categoria_A-IIIB.pdf"
                CarLicenseCategory.A_IIIC -> "Reglas_Categoria_A-IIIC.pdf"
                null -> "Reglas_Automoviles.pdf"
            }
        }
    }
}