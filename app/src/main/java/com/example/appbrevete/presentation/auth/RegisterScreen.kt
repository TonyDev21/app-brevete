package com.example.appbrevete.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appbrevete.domain.model.UserRole
import com.example.appbrevete.presentation.viewmodel.AuthViewModel
import com.example.appbrevete.presentation.viewmodel.AuthState
import android.app.DatePickerDialog
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    
    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    
    // Observar cambios en el estado de autenticación
    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) {
            onNavigateToHome()
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título
        Text(
            text = "Crear Cuenta",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Formulario de registro
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                // Información personal
                Text(
                    text = "Información Personal",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = { Text("Nombres") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    
                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = { Text("Apellidos") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = dni,
                    onValueChange = { dni = it },
                    label = { Text("DNI") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Teléfono") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Dirección") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Campo de fecha con DatePicker
                var showDatePicker by remember { mutableStateOf(false) }
                
                OutlinedTextField(
                    value = birthDate,
                    onValueChange = { }, // No editable directamente
                    label = { Text("Fecha de nacimiento") },
                    placeholder = { Text("DD/MM/AAAA") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true },
                    singleLine = true
                )
                
                // DatePicker Dialog
                if (showDatePicker) {
                    DatePickerDialog(
                        onDateSelected = { selectedDate ->
                            birthDate = selectedDate
                            showDatePicker = false
                        },
                        onDismiss = { showDatePicker = false }
                    )
                }
                
                if (birthDate.isNotEmpty() && !isValidDateFormat(birthDate)) {
                    Text(
                        text = "Formato inválido. Use DD/MM/AAAA",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Datos de cuenta
                Text(
                    text = "Datos de Cuenta",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Contraseña") },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirmar Contraseña") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Confirmar contraseña") },
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null
                            )
                        }
                    },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = confirmPassword.isNotEmpty() && password != confirmPassword
                )
                
                if (confirmPassword.isNotEmpty() && password != confirmPassword) {
                    Text(
                        text = "Las contraseñas no coinciden",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Botón de registro
                Button(
                    onClick = {
                        if (isFormValid(email, password, confirmPassword, firstName, lastName, dni, phoneNumber, address, birthDate)) {
                            authViewModel.register(
                                email = email.trim(),
                                password = password,
                                firstName = firstName.trim(),
                                lastName = lastName.trim(),
                                dni = dni.trim(),
                                phoneNumber = phoneNumber.trim(),
                                address = address.trim(),
                                birthDate = birthDate.trim(),
                                role = UserRole.STUDENT // Por defecto todos los usuarios se registran como estudiantes
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = authState !is AuthState.Loading
                ) {
                    if (authState is AuthState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Crear Cuenta")
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                TextButton(
                    onClick = onNavigateToLogin,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("¿Ya tienes cuenta? Inicia sesión")
                }
            }
        }
        
        // Mostrar errores
        val currentAuthState = authState
        if (currentAuthState is AuthState.Error) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = currentAuthState.message,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}

private fun isFormValid(
    email: String,
    password: String,
    confirmPassword: String,
    firstName: String,
    lastName: String,
    dni: String,
    phoneNumber: String,
    address: String,
    birthDate: String
): Boolean {
    return email.isNotBlank() &&
            password.isNotBlank() &&
            password == confirmPassword &&
            firstName.isNotBlank() &&
            lastName.isNotBlank() &&
            dni.isNotBlank() &&
            phoneNumber.isNotBlank() &&
            address.isNotBlank() &&
            birthDate.isNotBlank() // Solo verificar que no esté vacío
}

private fun isCompleteDateFormat(date: String): Boolean {
    if (date.length != 10) return false
    
    val regex = Regex("^\\d{2}/\\d{2}/\\d{4}$")
    if (!regex.matches(date)) return false
    
    val parts = date.split("/")
    if (parts.size != 3) return false
    
    val day = parts[0].toIntOrNull() ?: return false
    val month = parts[1].toIntOrNull() ?: return false
    val year = parts[2].toIntOrNull() ?: return false
    
    return day in 1..31 && month in 1..12 && year in 1900..2010
}

@Composable
private fun DatePickerDialog(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    
    // Configurar fecha máxima (para personas de al menos 15 años)
    val maxCalendar = Calendar.getInstance()
    maxCalendar.add(Calendar.YEAR, -15)
    
    // Configurar fecha mínima (para personas de máximo 100 años)
    val minCalendar = Calendar.getInstance()
    minCalendar.add(Calendar.YEAR, -100)
    
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val formattedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
            onDateSelected(formattedDate)
        },
        calendar.get(Calendar.YEAR) - 25, // Año por defecto (25 años atrás)
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    
    // Configurar límites de fecha
    datePickerDialog.datePicker.maxDate = maxCalendar.timeInMillis
    datePickerDialog.datePicker.minDate = minCalendar.timeInMillis
    
    datePickerDialog.setOnDismissListener { onDismiss() }
    
    DisposableEffect(Unit) {
        datePickerDialog.show()
        onDispose {
            datePickerDialog.dismiss()
        }
    }
}

private fun isValidDateFormat(date: String): Boolean {
    // Permitir formatos parciales mientras se escribe
    if (date.isEmpty()) return true
    
    // Formato completo: DD/MM/AAAA
    if (date.length == 10 && date.matches(Regex("^\\d{2}/\\d{2}/\\d{4}$"))) {
        val parts = date.split("/")
        val day = parts[0].toIntOrNull() ?: return false
        val month = parts[1].toIntOrNull() ?: return false
        val year = parts[2].toIntOrNull() ?: return false
        
        return day in 1..31 && month in 1..12 && year in 1900..2010
    }
    
    // Formatos parciales válidos mientras se escribe
    return when {
        date.matches(Regex("^\\d{1,2}$")) -> true // Solo día
        date.matches(Regex("^\\d{2}/\\d{1,2}$")) -> true // Día/Mes
        date.matches(Regex("^\\d{2}/\\d{2}/\\d{1,4}$")) -> true // Día/Mes/Año parcial
        else -> false
    }
}
