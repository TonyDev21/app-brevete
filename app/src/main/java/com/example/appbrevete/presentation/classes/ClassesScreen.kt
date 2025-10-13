package com.example.appbrevete.presentation.classes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ClassesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Clases de Manejo",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(getSampleClasses()) { classItem ->
                ClassCard(classItem = classItem)
            }
        }
    }
}

@Composable
fun ClassCard(classItem: ClassData) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Clase ${classItem.sessionNumber}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Event, contentDescription = "Fecha")
                Spacer(modifier = Modifier.width(8.dp))
                Text(classItem.date)
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.AccessTime, contentDescription = "Hora")
                Spacer(modifier = Modifier.width(8.dp))
                Text(classItem.time)
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Person, contentDescription = "Instructor")
                Spacer(modifier = Modifier.width(8.dp))
                Text(classItem.instructor)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LinearProgressIndicator(
                progress = classItem.progress / 100f,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "Progreso: ${classItem.progress}%",
                fontSize = 12.sp
            )
        }
    }
}

data class ClassData(
    val id: String,
    val sessionNumber: Int,
    val date: String,
    val time: String,
    val instructor: String,
    val progress: Int
)

fun getSampleClasses(): List<ClassData> {
    return listOf(
        ClassData("1", 1, "10 de Enero, 2025", "9:00 AM", "Carlos Mendoza", 25),
        ClassData("2", 2, "12 de Enero, 2025", "9:00 AM", "Carlos Mendoza", 50),
        ClassData("3", 3, "15 de Enero, 2025", "9:00 AM", "Carlos Mendoza", 75)
    )
}
