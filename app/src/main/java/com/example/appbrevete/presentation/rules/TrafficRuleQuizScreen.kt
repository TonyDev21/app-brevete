package com.example.appbrevete.presentation.rules

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.appbrevete.domain.model.TrafficRuleQuestion
import com.example.appbrevete.domain.model.CarLicenseCategory
import com.example.appbrevete.domain.model.VehicleCategory

// Datos de muestra para el quiz (posteriormente se cargarán de la base de datos)
private val sampleQuestions = listOf(
    TrafficRuleQuestion(
        id = "1",
        category = CarLicenseCategory.A_IIA,
        vehicleType = VehicleCategory.CAR,
        question = "¿Cuál es la velocidad máxima permitida en zonas urbanas?",
        options = listOf("40 km/h", "50 km/h", "60 km/h", "70 km/h"),
        correctAnswerIndex = 1,
        explanation = "En zonas urbanas la velocidad máxima es de 50 km/h según el Reglamento Nacional de Tránsito."
    ),
    TrafficRuleQuestion(
        id = "2",
        category = CarLicenseCategory.A_IIA,
        vehicleType = VehicleCategory.CAR,
        question = "¿Qué significa una luz amarilla en el semáforo?",
        options = listOf("Acelerar", "Detenerse si es posible", "Seguir", "Tocar bocina"),
        correctAnswerIndex = 1,
        explanation = "La luz amarilla indica precaución y que debe detenerse si es posible hacerlo de manera segura."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrafficRuleQuizScreen(
    vehicleType: VehicleCategory,
    category: CarLicenseCategory? = null,
    onNavigateBack: () -> Unit = {},
    onQuizCompleted: (score: Float) -> Unit = {}
) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedAnswers by remember { mutableStateOf(mutableMapOf<Int, Int>()) }
    var showResult by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(1800) } // 30 minutos en segundos
    
    val questions = remember {
        // Filtrar preguntas por categoría
        sampleQuestions.filter { 
            it.vehicleType == vehicleType && 
            (category == null || it.category == category) 
        }
    }
    
    // Timer effect
    LaunchedEffect(timeLeft) {
        if (timeLeft > 0 && !showResult) {
            kotlinx.coroutines.delay(1000L)
            timeLeft--
        } else if (timeLeft == 0) {
            showResult = true
        }
    }
    
    if (showResult) {
        QuizResultScreen(
            questions = questions,
            answers = selectedAnswers,
            timeSpent = 1800 - timeLeft,
            onNavigateBack = onNavigateBack,
            onRetakeQuiz = {
                currentQuestionIndex = 0
                selectedAnswers.clear()
                showResult = false
                timeLeft = 1800
            }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header con progreso y timer
            QuizHeader(
                currentQuestion = currentQuestionIndex + 1,
                totalQuestions = questions.size,
                timeLeft = timeLeft,
                onNavigateBack = onNavigateBack
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            if (questions.isNotEmpty()) {
                val currentQuestion = questions[currentQuestionIndex]
                
                QuestionCard(
                    question = currentQuestion,
                    selectedAnswer = selectedAnswers[currentQuestionIndex],
                    onAnswerSelected = { answerIndex ->
                        selectedAnswers[currentQuestionIndex] = answerIndex
                    }
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Botones de navegación
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (currentQuestionIndex > 0) {
                        OutlinedButton(
                            onClick = { currentQuestionIndex-- },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Anterior")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                    
                    Button(
                        onClick = {
                            if (currentQuestionIndex < questions.size - 1) {
                                currentQuestionIndex++
                            } else {
                                showResult = true
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = selectedAnswers.containsKey(currentQuestionIndex)
                    ) {
                        Text(
                            if (currentQuestionIndex == questions.size - 1) "Terminar" 
                            else "Siguiente"
                        )
                    }
                }
            } else {
                // No hay preguntas disponibles
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Quiz no disponible",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Text(
                            text = "Las preguntas para esta categoría estarán disponibles próximamente.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizHeader(
    currentQuestion: Int,
    totalQuestions: Int,
    timeLeft: Int,
    onNavigateBack: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onNavigateBack) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Regresar"
            )
        }
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Pregunta $currentQuestion de $totalQuestions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            LinearProgressIndicator(
                progress = currentQuestion.toFloat() / totalQuestions,
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // Timer
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (timeLeft < 300) MaterialTheme.colorScheme.errorContainer
                              else MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = if (timeLeft < 300) MaterialTheme.colorScheme.onErrorContainer
                          else MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${timeLeft / 60}:${(timeLeft % 60).toString().padStart(2, '0')}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = if (timeLeft < 300) MaterialTheme.colorScheme.onErrorContainer
                           else MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
fun QuestionCard(
    question: TrafficRuleQuestion,
    selectedAnswer: Int?,
    onAnswerSelected: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = question.question,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            question.options.forEachIndexed { index, option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = selectedAnswer == index,
                            onClick = { onAnswerSelected(index) },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedAnswer == index,
                        onClick = { onAnswerSelected(index) }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun QuizResultScreen(
    questions: List<TrafficRuleQuestion>,
    answers: Map<Int, Int>,
    timeSpent: Int,
    onNavigateBack: () -> Unit,
    onRetakeQuiz: () -> Unit
) {
    val correctAnswers = questions.mapIndexed { index, question ->
        answers[index] == question.correctAnswerIndex
    }.count { it }
    
    val score = (correctAnswers.toFloat() / questions.size) * 100
    val passed = score >= 70
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Regresar"
                )
            }
            Text(
                text = "Resultado del Examen",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Resultado principal
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (passed) MaterialTheme.colorScheme.primaryContainer
                               else MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = if (passed) Icons.Default.CheckCircle else Icons.Default.Cancel,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = if (passed) MaterialTheme.colorScheme.onPrimaryContainer
                          else MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (passed) "¡Aprobado!" else "No Aprobado",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (passed) MaterialTheme.colorScheme.onPrimaryContainer
                           else MaterialTheme.colorScheme.onErrorContainer
                )
                Text(
                    text = "Puntaje: ${score.toInt()}%",
                    style = MaterialTheme.typography.titleLarge,
                    color = if (passed) MaterialTheme.colorScheme.onPrimaryContainer
                           else MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Estadísticas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatCard(
                title = "Correctas",
                value = correctAnswers.toString(),
                icon = Icons.Default.CheckCircle
            )
            StatCard(
                title = "Incorrectas",
                value = (questions.size - correctAnswers).toString(),
                icon = Icons.Default.Cancel
            )
            StatCard(
                title = "Tiempo",
                value = "${timeSpent / 60}:${(timeSpent % 60).toString().padStart(2, '0')}",
                icon = Icons.Default.Timer
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Botones de acción
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onNavigateBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("Salir")
            }
            Button(
                onClick = onRetakeQuiz,
                modifier = Modifier.weight(1f)
            ) {
                Text("Reintentar")
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        modifier = Modifier.width(100.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}