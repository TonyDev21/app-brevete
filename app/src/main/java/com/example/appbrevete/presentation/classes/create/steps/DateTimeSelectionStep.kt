package com.example.appbrevete.presentation.classes.create.steps

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appbrevete.presentation.classes.create.components.DateCard
import com.example.appbrevete.presentation.classes.create.components.TimeCard
import java.text.SimpleDateFormat
import java.util.*

/**
 * Paso 2: Selección de fecha y hora
 */

data class AvailableDate(
    val dayName: String,
    val dayNumber: String,
    val monthName: String,
    val fullDate: String
)

@Composable
fun DateTimeSelectionStep(
    selectedPackage: String,
    selectedDate: String?,
    selectedTime: String?,
    onDateSelected: (String) -> Unit,
    onTimeSelected: (String) -> Unit,
    onNext: () -> Unit,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val availableDates = getAvailableDates()
    val availableTimes = getAvailableTimes()
    
    Column(modifier = modifier) {
        // Header con paquete seleccionado
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = if (selectedPackage == "2h") "PAQUETE 2H" else "PAQUETE 4H",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Selección de fecha
        Text(
            text = "Selecciona una fecha",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(availableDates) { date ->
                DateCard(
                    date = date,
                    isSelected = selectedDate == date.fullDate,
                    onClick = { onDateSelected(date.fullDate) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Selección de horario (solo si se seleccionó fecha)
        if (selectedDate != null) {
            Text(
                text = "Selecciona un horario",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(availableTimes) { time ->
                    TimeCard(
                        time = time,
                        isSelected = selectedTime == time,
                        onClick = { onTimeSelected(time) }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Botones de navegación
        if (onBack != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Botón Atrás
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Atrás")
                }
                
                // Botón Continuar
                Button(
                    onClick = onNext,
                    modifier = Modifier.weight(1f),
                    enabled = selectedDate != null && selectedTime != null
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Continuar")
                }
            }
        } else {
            // Solo botón Continuar (para crear nueva clase)
            Button(
                onClick = onNext,
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedDate != null && selectedTime != null
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Continuar")
            }
        }
    }
}

private fun getAvailableDates(): List<AvailableDate> {
    val calendar = Calendar.getInstance()
    val spanishLocale = java.util.Locale("es", "ES")
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", spanishLocale)
    val dayFormat = SimpleDateFormat("EEE", spanishLocale)
    val monthFormat = SimpleDateFormat("MMM", spanishLocale)
    
    val dates = mutableListOf<AvailableDate>()
    
    // Generar próximos 14 días laborables (excluyendo domingo) - 2 semanas
    var addedDays = 0
    var dayOffset = 1
    
    while (addedDays < 14) {
        calendar.add(Calendar.DAY_OF_MONTH, dayOffset)
        dayOffset = 1
        
        // Excluir domingos
        if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            dates.add(
                AvailableDate(
                    dayName = dayFormat.format(calendar.time).uppercase(),
                    dayNumber = calendar.get(Calendar.DAY_OF_MONTH).toString(),
                    monthName = monthFormat.format(calendar.time).uppercase(),
                    fullDate = dateFormat.format(calendar.time)
                )
            )
            addedDays++
        }
    }
    
    return dates
}

private fun getAvailableTimes(): List<String> {
    return listOf(
        "08:00", "08:30", "09:00", "09:30", "10:00",
        "14:00", "14:30", "15:00", "15:30", "16:00"
    )
}