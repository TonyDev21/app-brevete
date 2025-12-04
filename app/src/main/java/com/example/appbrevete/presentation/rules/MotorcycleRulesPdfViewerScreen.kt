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
import com.example.appbrevete.domain.model.MotorcycleLicenseCategory
import com.example.appbrevete.domain.model.VehicleCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

data class MotorcycleContentSection(
    val title: String,
    val items: List<String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MotorcycleRulesPdfViewerScreen(
    category: MotorcycleLicenseCategory,
    navController: NavController
) {
    val context = LocalContext.current
    
    val title = when (category) {
        MotorcycleLicenseCategory.B_IIA -> "Categoría B-IIA"
        MotorcycleLicenseCategory.B_IIB -> "Categoría B-IIB"
        MotorcycleLicenseCategory.B_IIC -> "Categoría B-IIC"
    }
    
    val sections = getMotorcycleContentForCategory(category)
    
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
                                openOfficialMotorcyclePage(context, category)
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
                                downloadMotorcyclePdfFromAssets(context, category)
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
                                openOfficialMotorcycleSimulator(context)
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

fun openOfficialMotorcyclePage(context: Context, category: MotorcycleLicenseCategory) {
    val officialUrl = when (category) {
        MotorcycleLicenseCategory.B_IIA -> "https://portal.mtc.gob.pe/transportes/terrestre/licencias/documentos/licencias/CLASE_B_CATEGOR%C3%8DA_IIA.pdf"
        MotorcycleLicenseCategory.B_IIB -> "https://portal.mtc.gob.pe/transportes/terrestre/licencias/documentos/licencias/CLASE_B_CATEGOR%C3%8DA_IIB.pdf"
        MotorcycleLicenseCategory.B_IIC -> "https://portal.mtc.gob.pe/transportes/terrestre/licencias/documentos/licencias/CLASE_B_CATEGOR%C3%8DA_IIC.pdf"
    }
    
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(officialUrl)
    }
    
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "No se pudo abrir el navegador", Toast.LENGTH_SHORT).show()
    }
}

fun downloadMotorcyclePdfFromAssets(context: Context, category: MotorcycleLicenseCategory) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val assetPath = getMotorcyclePdfAssetPath(category)
            val fileName = getMotorcyclePdfFileName(category)
            
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

fun getMotorcyclePdfAssetPath(category: MotorcycleLicenseCategory): String {
    return when (category) {
        MotorcycleLicenseCategory.B_IIA -> "rules/motos/CLASE_B_CATEGORÍA_IIA.pdf"
        MotorcycleLicenseCategory.B_IIB -> "rules/motos/CLASE_B_CATEGORÍA_IIB.pdf"
        MotorcycleLicenseCategory.B_IIC -> "rules/motos/CLASE_B_CATEGORÍA_IIC.pdf"
    }
}

fun getMotorcyclePdfFileName(category: MotorcycleLicenseCategory): String {
    return when (category) {
        MotorcycleLicenseCategory.B_IIA -> "Reglas_Moto_B-IIA.pdf"
        MotorcycleLicenseCategory.B_IIB -> "Reglas_Moto_B-IIB.pdf"
        MotorcycleLicenseCategory.B_IIC -> "Reglas_Moto_B-IIC.pdf"
    }
}

fun getMotorcycleContentForCategory(category: MotorcycleLicenseCategory): List<MotorcycleContentSection> {
    val baseContent = listOf(
        MotorcycleContentSection(
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
        MotorcycleContentSection(
            title = "Velocidades Máximas",
            items = listOf(
                "Zona urbana: 40 km/h",
                "Zona escolar: 25 km/h", 
                "Zona residencial: 25 km/h",
                "Carreteras locales: 45 km/h",
                "Prohibido en autopistas y vías expresas"
            )
        ),
        MotorcycleContentSection(
            title = "Reglas de Circulación",
            items = listOf(
                "Circular por el carril derecho",
                "No circular entre carriles (lane splitting)",
                "Máximo un acompañante",
                "Respetar semáforos y señales",
                "Mantener distancia segura (3 segundos)",
                "No circular en aceras o veredas"
            )
        )
    )
    
    // Agregar contenido específico por categoría
    val specificContent = when (category) {
        MotorcycleLicenseCategory.B_IIA -> listOf(
            MotorcycleContentSection(
                title = "Especial para B-IIA",
                items = listOf(
                    "Motocicletas hasta 125cc",
                    "Mototaxis de pasajeros",
                    "Velocidad máxima reducida en zona urbana",
                    "Prohibido transportar carga pesada",
                    "Licencia válida por 5 años"
                )
            )
        )
        MotorcycleLicenseCategory.B_IIB -> listOf(
            MotorcycleContentSection(
                title = "Especial para B-IIB",
                items = listOf(
                    "Motocicletas de 125cc a 250cc",
                    "Mayor potencia y velocidad permitida",
                    "Puede circular en más vías urbanas",
                    "Requiere mayor experiencia de manejo",
                    "Licencia válida por 5 años"
                )
            )
        )
        MotorcycleLicenseCategory.B_IIC -> listOf(
            MotorcycleContentSection(
                title = "Especial para B-IIC",
                items = listOf(
                    "Motocicletas mayores a 250cc",
                    "Triciclos motorizados",
                    "Pueden circular en carreteras",
                    "Mayor responsabilidad de manejo",
                    "Licencia válida por 5 años"
                )
            )
        )
    }
    
    return baseContent + specificContent
}

fun openOfficialMotorcycleSimulator(context: Context) {
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