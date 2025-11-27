package com.example.appbrevete.presentation.appointments.edit.steps

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appbrevete.presentation.appointments.edit.components.DateCard
import com.example.appbrevete.presentation.appointments.edit.components.TimeCard
import com.example.appbrevete.presentation.appointments.edit.generateAvailableDates
import com.example.appbrevete.presentation.appointments.edit.generateAvailableTimes
import java.time.LocalDate

/**
 * Paso 2: Selección de fecha y hora
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateTimeSelectionStep(
    selectedDate: LocalDate?,
    selectedTime: String?,
    onDateSelected: (LocalDate) -> Unit,
    onTimeSelected: (String) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val availableDates = remember { generateAvailableDates() }
    val availableTimes = remember { generateAvailableTimes() }
    
    Column(modifier = modifier) {
        Text(
            text = "Selecciona nueva fecha y hora",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Selección de fecha
        Text(
            text = "Fecha:",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(availableDates) { date ->
                DateCard(
                    date = date,
                    isSelected = selectedDate == date,
                    onClick = { onDateSelected(date) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Selección de hora
        Text(
            text = "Hora:",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(availableTimes.chunked(3)) { timeChunk ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    timeChunk.forEach { time ->
                        Box(modifier = Modifier.weight(1f)) {
                            TimeCard(
                                time = time,
                                isSelected = selectedTime == time,
                                onClick = { onTimeSelected(time) }
                            )
                        }
                    }
                    // Llenar espacios vacíos
                    repeat(3 - timeChunk.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("Atrás")
            }
            
            Button(
                onClick = onNext,
                modifier = Modifier.weight(1f),
                enabled = selectedDate != null && selectedTime != null
            ) {
                Text("Continuar")
            }
        }
    }
}