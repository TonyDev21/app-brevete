package com.example.appbrevete.presentation.rules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.appbrevete.domain.model.*

@HiltViewModel
class TrafficRulesViewModel @Inject constructor(
    // TODO: Inject repositories when they're created
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(TrafficRulesUiState())
    val uiState: StateFlow<TrafficRulesUiState> = _uiState.asStateFlow()
    
    private val _quizState = MutableStateFlow(QuizUiState())
    val quizState: StateFlow<QuizUiState> = _quizState.asStateFlow()
    
    fun loadTrafficRules(vehicleType: VehicleCategory, category: CarLicenseCategory? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                // TODO: Cargar reglas desde el repositorio
                // val rules = trafficRulesRepository.getTrafficRules(vehicleType, category)
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    vehicleType = vehicleType,
                    selectedCategory = category
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
    
    fun loadQuizQuestions(vehicleType: VehicleCategory, category: CarLicenseCategory? = null) {
        viewModelScope.launch {
            _quizState.value = _quizState.value.copy(isLoading = true)
            
            try {
                // TODO: Cargar preguntas desde el repositorio
                // val questions = trafficRulesRepository.getQuizQuestions(vehicleType, category)
                val sampleQuestions = getSampleQuestions(vehicleType, category)
                
                _quizState.value = _quizState.value.copy(
                    isLoading = false,
                    questions = sampleQuestions,
                    currentQuestionIndex = 0,
                    selectedAnswers = mutableMapOf(),
                    timeLeft = 1800, // 30 minutos
                    showResult = false
                )
            } catch (e: Exception) {
                _quizState.value = _quizState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
    
    fun selectAnswer(questionIndex: Int, answerIndex: Int) {
        val currentAnswers = _quizState.value.selectedAnswers.toMutableMap()
        currentAnswers[questionIndex] = answerIndex
        
        _quizState.value = _quizState.value.copy(
            selectedAnswers = currentAnswers
        )
    }
    
    fun nextQuestion() {
        val currentIndex = _quizState.value.currentQuestionIndex
        val totalQuestions = _quizState.value.questions.size
        
        if (currentIndex < totalQuestions - 1) {
            _quizState.value = _quizState.value.copy(
                currentQuestionIndex = currentIndex + 1
            )
        }
    }
    
    fun previousQuestion() {
        val currentIndex = _quizState.value.currentQuestionIndex
        
        if (currentIndex > 0) {
            _quizState.value = _quizState.value.copy(
                currentQuestionIndex = currentIndex - 1
            )
        }
    }
    
    fun finishQuiz() {
        viewModelScope.launch {
            val state = _quizState.value
            val timeSpent = (1800 - state.timeLeft).toLong()
            val correctAnswers = calculateCorrectAnswers(state.questions, state.selectedAnswers)
            val score = (correctAnswers.toFloat() / state.questions.size) * 100
            
            val result = QuizResult(
                id = "",
                vehicleType = _uiState.value.vehicleType ?: VehicleCategory.CAR,
                category = _uiState.value.selectedCategory ?: CarLicenseCategory.A_IIA,
                score = score,
                correctAnswers = correctAnswers,
                totalQuestions = state.questions.size,
                timeSpent = timeSpent,
                passed = score >= 70,
                completedAt = System.currentTimeMillis()
            )
            
            // TODO: Guardar resultado en el repositorio
            // trafficRulesRepository.saveQuizResult(result)
            
            _quizState.value = _quizState.value.copy(
                showResult = true,
                quizResult = result
            )
        }
    }
    
    fun resetQuiz() {
        _quizState.value = _quizState.value.copy(
            currentQuestionIndex = 0,
            selectedAnswers = mutableMapOf(),
            timeLeft = 1800,
            showResult = false,
            quizResult = null
        )
    }
    
    fun updateTimer() {
        val currentTime = _quizState.value.timeLeft
        if (currentTime > 0 && !_quizState.value.showResult) {
            _quizState.value = _quizState.value.copy(timeLeft = currentTime - 1)
            
            if (currentTime - 1 <= 0) {
                finishQuiz()
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
        _quizState.value = _quizState.value.copy(error = null)
    }
    
    private fun calculateCorrectAnswers(questions: List<TrafficRuleQuestion>, answers: Map<Int, Int>): Int {
        return questions.mapIndexed { index, question ->
            answers[index] == question.correctAnswerIndex
        }.count { it }
    }
    
    // Datos de muestra - TODO: Reemplazar con datos reales del repositorio
    private fun getSampleQuestions(vehicleType: VehicleCategory, category: CarLicenseCategory?): List<TrafficRuleQuestion> {
        return when (vehicleType) {
            VehicleCategory.CAR -> getCarQuestions(category)
            VehicleCategory.MOTORCYCLE -> getMotorcycleQuestions()
        }
    }
    
    private fun getCarQuestions(category: CarLicenseCategory?): List<TrafficRuleQuestion> {
        val baseQuestions = listOf(
            TrafficRuleQuestion(
                id = "car_1",
                category = category ?: CarLicenseCategory.A_IIA,
                vehicleType = VehicleCategory.CAR,
                question = "¿Cuál es la velocidad máxima permitida en zonas urbanas?",
                options = listOf("40 km/h", "50 km/h", "60 km/h", "70 km/h"),
                correctAnswerIndex = 1,
                explanation = "En zonas urbanas la velocidad máxima es de 50 km/h según el Reglamento Nacional de Tránsito."
            ),
            TrafficRuleQuestion(
                id = "car_2",
                category = category ?: CarLicenseCategory.A_IIA,
                vehicleType = VehicleCategory.CAR,
                question = "¿Qué significa una luz amarilla en el semáforo?",
                options = listOf("Acelerar", "Detenerse si es posible", "Seguir sin detenerse", "Tocar bocina"),
                correctAnswerIndex = 1,
                explanation = "La luz amarilla indica precaución y que debe detenerse si es posible hacerlo de manera segura."
            ),
            TrafficRuleQuestion(
                id = "car_3",
                category = category ?: CarLicenseCategory.A_IIA,
                vehicleType = VehicleCategory.CAR,
                question = "¿Cuál es la distancia mínima que se debe mantener con el vehículo de adelante?",
                options = listOf("1 metro", "2 metros", "3 segundos de distancia", "5 metros"),
                correctAnswerIndex = 2,
                explanation = "Se debe mantener una distancia equivalente a 3 segundos de tiempo de reacción."
            ),
            TrafficRuleQuestion(
                id = "car_4",
                category = category ?: CarLicenseCategory.A_IIA,
                vehicleType = VehicleCategory.CAR,
                question = "¿En qué casos es obligatorio el uso del cinturón de seguridad?",
                options = listOf("Solo en carretera", "Solo en la ciudad", "Siempre", "Solo para el conductor"),
                correctAnswerIndex = 2,
                explanation = "El uso del cinturón de seguridad es obligatorio para todos los ocupantes del vehículo."
            ),
            TrafficRuleQuestion(
                id = "car_5",
                category = category ?: CarLicenseCategory.A_IIA,
                vehicleType = VehicleCategory.CAR,
                question = "¿Cuándo se debe utilizar las luces altas?",
                options = listOf("Siempre de noche", "En carretera sin tráfico contrario", "En la ciudad", "Con lluvia"),
                correctAnswerIndex = 1,
                explanation = "Las luces altas se usan en carretera cuando no hay tráfico contrario para evitar encandilamiento."
            )
        )
        
        // Agregar preguntas específicas según la categoría
        return when (category) {
            CarLicenseCategory.A_IIB, CarLicenseCategory.A_IIC -> {
                baseQuestions + listOf(
                    TrafficRuleQuestion(
                        id = "heavy_1",
                        category = category,
                        vehicleType = VehicleCategory.CAR,
                        question = "¿Cuál es el peso máximo permitido para vehículos categoría ${category?.name ?: "A-IIA"}?",
                        options = listOf("3500 kg", "7000 kg", "12000 kg", "Sin límite"),
                        correctAnswerIndex = if (category == CarLicenseCategory.A_IIB) 1 else 2,
                        explanation = "Los vehículos categoría ${category?.name ?: "A-IIA"} tienen límites específicos de peso bruto."
                    )
                )
            }
            else -> baseQuestions
        }
    }
    
    private fun getMotorcycleQuestions(): List<TrafficRuleQuestion> {
        return listOf(
            TrafficRuleQuestion(
                id = "moto_1",
                category = null,
                vehicleType = VehicleCategory.MOTORCYCLE,
                question = "¿Es obligatorio el uso de casco para motociclistas?",
                options = listOf("Solo en carretera", "Solo en la ciudad", "Siempre", "Solo para el conductor"),
                correctAnswerIndex = 2,
                explanation = "El uso de casco es obligatorio para conductor y acompañante en todo momento."
            ),
            TrafficRuleQuestion(
                id = "moto_2",
                category = null,
                vehicleType = VehicleCategory.MOTORCYCLE,
                question = "¿Puede una motocicleta transitar entre carriles?",
                options = listOf("Sí, siempre", "No, nunca", "Solo en tráfico lento", "Solo de día"),
                correctAnswerIndex = 1,
                explanation = "Las motocicletas no pueden transitar entre carriles por seguridad vial."
            ),
            TrafficRuleQuestion(
                id = "moto_3",
                category = null,
                vehicleType = VehicleCategory.MOTORCYCLE,
                question = "¿Cuántos acompañantes puede llevar una motocicleta?",
                options = listOf("Ninguno", "Uno", "Dos", "Tres"),
                correctAnswerIndex = 1,
                explanation = "Las motocicletas pueden llevar máximo un acompañante además del conductor."
            )
        )
    }
}

data class TrafficRulesUiState(
    val isLoading: Boolean = false,
    val vehicleType: VehicleCategory? = null,
    val selectedCategory: CarLicenseCategory? = null,
    val error: String? = null
)

data class QuizUiState(
    val isLoading: Boolean = false,
    val questions: List<TrafficRuleQuestion> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedAnswers: Map<Int, Int> = emptyMap(),
    val timeLeft: Int = 1800, // 30 minutos en segundos
    val showResult: Boolean = false,
    val quizResult: QuizResult? = null,
    val error: String? = null
)