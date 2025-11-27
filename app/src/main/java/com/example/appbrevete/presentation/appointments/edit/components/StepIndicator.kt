package com.example.appbrevete.presentation.appointments.edit.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appbrevete.presentation.appointments.edit.EditStep

/**
 * Componentes de UI para el indicador de pasos
 */

@Composable
fun StepIndicator(
    currentStep: EditStep,
    modifier: Modifier = Modifier
) {
    val steps = listOf(
        EditStep.LICENSE_SELECTION to "Licencia",
        EditStep.DATE_TIME_SELECTION to "Fecha y Hora", 
        EditStep.CONFIRMATION to "Confirmación"
    )
    
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(steps) { (step, title) ->
            StepItem(
                title = title,
                isActive = step == currentStep,
                isCompleted = step.ordinal < currentStep.ordinal
            )
        }
    }
}

@Composable
private fun StepItem(
    title: String,
    isActive: Boolean,
    isCompleted: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        // Círculo del paso
        Surface(
            shape = MaterialTheme.shapes.small,
            color = when {
                isCompleted -> MaterialTheme.colorScheme.primary
                isActive -> MaterialTheme.colorScheme.primaryContainer
                else -> MaterialTheme.colorScheme.surfaceVariant
            },
            modifier = Modifier.size(32.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = if (isCompleted) "✓" else "",
                    color = if (isCompleted) Color.White else MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Título del paso
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = when {
                isActive -> MaterialTheme.colorScheme.primary
                isCompleted -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            },
            fontWeight = if (isActive || isCompleted) FontWeight.Bold else FontWeight.Normal
        )
    }
}