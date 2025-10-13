# App Brevete - Configuración de Desarrollo

## 🚀 Guía de Inicio Rápido

### 1. Configuración Inicial
```bash
# Clonar el repositorio
git clone [url-del-repositorio]
cd Proyectos-Movil

# Abrir en Android Studio
# File -> Open -> Seleccionar carpeta del proyecto
```

### 2. Configuración de Android Studio
- **SDK**: Android API 24+ (Android 7.0)
- **Build Tools**: 34.0.0+
- **Gradle**: 8.0+
- **Kotlin**: 2.0.21

### 3. Dependencias Principales
```kotlin
// Navigation
implementation("androidx.navigation:navigation-compose:2.7.7")

// Room Database
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
kapt("androidx.room:room-compiler:2.6.1")

// Hilt Dependency Injection
implementation("com.google.dagger:hilt-android:2.48")
kapt("com.google.dagger:hilt-compiler:2.48")

// ViewModel
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
```

## 📱 Estructura de la Aplicación

### Módulos Implementados ✅

#### 🔐 Autenticación
- **LoginScreen**: Pantalla de inicio de sesión
- **RegisterScreen**: Registro de nuevos usuarios
- **AuthViewModel**: Lógica de autenticación
- **AuthRepository**: Gestión de datos de usuario

#### 🏠 Navegación
- **AppNavigation**: Sistema de navegación principal
- **NavigationDrawer**: Menú lateral contextual
- **Screen**: Definición de rutas

#### 🗄️ Base de Datos
- **AppDatabase**: Configuración de Room
- **UserDao**: Acceso a datos de usuarios
- **Converters**: Conversión de tipos complejos

#### 🏡 Pantalla Principal
- **HomeScreen**: Dashboard personalizado por rol
- **WelcomeCard**: Tarjeta de bienvenida
- **QuickActionsCard**: Acciones rápidas
- **ProgressCard**: Seguimiento de progreso

### Módulos en Desarrollo 🔄

#### 📅 Citas
- **AppointmentsScreen**: Gestión de citas
- **NewAppointmentDialog**: Crear nueva cita
- **AppointmentCard**: Tarjeta de cita

#### 🚗 Clases
- **ClassesScreen**: Lista de clases
- **ClassCard**: Tarjeta de clase

#### 📝 Examen
- **ExamSimulatorScreen**: Simulador de examen
- **ExamCategoryCard**: Categorías de preguntas

#### 🆔 Licencias
- **LicenseTypesScreen**: Tipos de licencia
- **LicenseTypeCard**: Información de licencia

#### 👤 Perfil
- **ProfileScreen**: Información del usuario
- **ProfileField**: Campo de perfil

#### ⚙️ Administración
- **AdminScreen**: Panel administrativo
- **AdminFunctionCard**: Funciones administrativas

## 🔧 Configuración de Desarrollo

### Variables de Entorno
```properties
# En local.properties
API_BASE_URL=https://api.appbrevete.com
DATABASE_NAME=appbrevete_database
DEBUG_MODE=true
```

### Build Configuration
```kotlin
android {
    compileSdk = 36
    defaultConfig {
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }
}
```

## 📊 Base de Datos - Esquema

### Tablas Principales
```sql
-- Usuarios
CREATE TABLE users (
    id TEXT PRIMARY KEY,
    email TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    firstName TEXT NOT NULL,
    lastName TEXT NOT NULL,
    dni TEXT UNIQUE NOT NULL,
    phoneNumber TEXT NOT NULL,
    address TEXT NOT NULL,
    birthDate TEXT NOT NULL,
    role TEXT NOT NULL,
    isActive INTEGER NOT NULL DEFAULT 1,
    createdAt INTEGER NOT NULL,
    updatedAt INTEGER NOT NULL
);

-- Tipos de Licencia
CREATE TABLE license_types (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    category TEXT NOT NULL,
    ageRequirement INTEGER NOT NULL,
    medicalExamRequired INTEGER NOT NULL DEFAULT 1,
    theoryExamRequired INTEGER NOT NULL DEFAULT 1,
    practicalExamRequired INTEGER NOT NULL DEFAULT 1,
    validityYears INTEGER NOT NULL,
    price REAL NOT NULL,
    isActive INTEGER NOT NULL DEFAULT 1
);

-- Citas
CREATE TABLE appointments (
    id TEXT PRIMARY KEY,
    userId TEXT NOT NULL,
    type TEXT NOT NULL,
    licenseTypeId TEXT,
    scheduledDate INTEGER NOT NULL,
    scheduledTime TEXT NOT NULL,
    location TEXT NOT NULL,
    status TEXT NOT NULL,
    notes TEXT,
    examinerId TEXT,
    instructorId TEXT,
    createdAt INTEGER NOT NULL,
    updatedAt INTEGER NOT NULL
);
```

## 🎨 Guía de Diseño

### Colores del Tema
```kotlin
// Colores principales
Primary: #1976D2 (Azul)
Primary Container: #E3F2FD
On Primary: #FFFFFF
Secondary: #FF9800 (Naranja)
Error: #F44336 (Rojo)
Success: #4CAF50 (Verde)
```

### Tipografía
```kotlin
// Títulos principales
fontSize = 24.sp
fontWeight = FontWeight.Bold

// Subtítulos
fontSize = 18.sp
fontWeight = FontWeight.Medium

// Texto normal
fontSize = 14.sp
fontWeight = FontWeight.Normal

// Texto pequeño
fontSize = 12.sp
fontWeight = FontWeight.Normal
```

### Espaciado
```kotlin
// Espaciado estándar
padding = 16.dp
spacing = 8.dp
margin = 24.dp
```

## 🧪 Testing

### Estructura de Tests
```
app/src/test/java/
├── AuthViewModelTest.kt
├── UserRepositoryTest.kt
└── UtilsTest.kt

app/src/androidTest/java/
├── DatabaseTest.kt
├── NavigationTest.kt
└── UITest.kt
```

### Comandos de Testing
```bash
# Ejecutar tests unitarios
./gradlew test

# Ejecutar tests de integración
./gradlew connectedAndroidTest

# Generar reporte de cobertura
./gradlew jacocoTestReport
```

## 📦 Build y Deploy

### Generar APK
```bash
# Debug APK
./gradlew assembleDebug

# Release APK
./gradlew assembleRelease

# Bundle para Play Store
./gradlew bundleRelease
```

### Configuración de Release
```kotlin
buildTypes {
    release {
        isMinifyEnabled = true
        isShrinkResources = true
        proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
        )
    }
}
```

## 🔍 Debugging

### Logs de Desarrollo
```kotlin
// Habilitar logs detallados
if (BuildConfig.DEBUG) {
    Log.d("AppBrevete", "Debug message")
}
```

### Herramientas de Debug
- **Layout Inspector**: Inspeccionar UI
- **Database Inspector**: Ver base de datos
- **Network Inspector**: Monitorear red
- **Memory Profiler**: Analizar memoria

## 📚 Recursos Adicionales

### Documentación Oficial
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [Hilt](https://dagger.dev/hilt/)
- [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)

### Tutoriales Recomendados
- [Compose Tutorial](https://developer.android.com/codelabs/jetpack-compose)
- [Room Tutorial](https://developer.android.com/codelabs/android-room-with-a-view)
- [Hilt Tutorial](https://developer.android.com/codelabs/android-hilt)

---

**¡Desarrollo exitoso!** 🚀
