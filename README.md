# App Brevete - Sistema de Gestión de Licencias de Conducir

Una aplicación móvil desarrollada en Kotlin con Jetpack Compose para la gestión integral de licencias de conducir en Perú.

## 📱 Características Principales

### 🔐 Sistema de Autenticación y Roles
- **Login y Registro** con validación de datos
- **Sistema de Roles** diferenciado:
  - 👨‍🎓 **Estudiante**: Obtener licencia de conducir
  - 👨‍🏫 **Instructor**: Enseñar manejo a estudiantes
  - 👨‍⚖️ **Examinador**: Evaluar exámenes de manejo
  - 👨‍⚕️ **Doctor Médico**: Realizar exámenes médicos
  - 👨‍💼 **Administrador**: Gestionar el sistema completo

### 🏠 Dashboard Principal
- **Pantalla de inicio** personalizada según el rol del usuario
- **Acciones rápidas** adaptadas a cada tipo de usuario
- **Progreso de licencia** con seguimiento de exámenes
- **Citas próximas** y notificaciones importantes

### 📅 Gestión de Citas
- **Programación de citas** para exámenes médicos, teóricos y prácticos
- **Filtros** por tipo de cita y estado
- **Reprogramación** y cancelación de citas
- **Notificaciones** de recordatorios

### 🚗 Clases de Manejo
- **Seguimiento de progreso** por sesión
- **Asignación de instructores**
- **Registro de habilidades** desarrolladas
- **Evaluación continua** del estudiante

### 📝 Simulador de Examen
- **Preguntas categorizadas** por tipo de licencia
- **Exámenes de práctica** con tiempo límite
- **Retroalimentación inmediata** de respuestas
- **Estadísticas de rendimiento**

### 🆔 Tipos de Licencia
- **Catálogo completo** de licencias disponibles en Perú
- **Requisitos específicos** por tipo de licencia
- **Precios y vigencia** actualizados
- **Proceso de solicitud** guiado

### 👤 Perfil de Usuario
- **Información personal** completa
- **Progreso de licencia** detallado
- **Edición de datos** personales
- **Cambio de contraseña**

### ⚙️ Panel Administrativo
- **Gestión de usuarios** del sistema
- **Estadísticas generales** de la aplicación
- **Configuración** de parámetros
- **Reportes** y análisis

## 🏗️ Arquitectura del Proyecto

### 📁 Estructura de Carpetas
```
app/src/main/java/com/example/appbrevete/
├── data/
│   ├── local/           # Base de datos Room
│   ├── remote/          # Servicios de red
│   └── repository/      # Repositorios de datos
├── domain/
│   ├── model/          # Modelos de datos
│   └── usecase/        # Casos de uso
├── presentation/
│   ├── auth/           # Autenticación
│   ├── home/           # Pantalla principal
│   ├── appointments/   # Gestión de citas
│   ├── classes/        # Clases de manejo
│   ├── exam/           # Simulador de examen
│   ├── license/        # Tipos de licencia
│   ├── profile/        # Perfil de usuario
│   └── admin/          # Panel administrativo
├── navigation/         # Sistema de navegación
└── di/                 # Inyección de dependencias
```

### 🛠️ Tecnologías Utilizadas

- **Kotlin** - Lenguaje de programación
- **Jetpack Compose** - UI moderna y declarativa
- **Room** - Base de datos local
- **Hilt** - Inyección de dependencias
- **Navigation Compose** - Navegación entre pantallas
- **ViewModel** - Gestión de estado
- **Coroutines** - Programación asíncrona
- **Material Design 3** - Sistema de diseño

## 🚀 Instalación y Configuración

### Prerrequisitos
- Android Studio Arctic Fox o superior
- SDK de Android API 24 o superior
- Kotlin 2.0.21

### Pasos de Instalación

1. **Clonar el repositorio**
   ```bash
   git clone [url-del-repositorio]
   cd Proyectos-Movil
   ```

2. **Abrir en Android Studio**
   - Abrir Android Studio
   - Seleccionar "Open an existing project"
   - Navegar a la carpeta del proyecto

3. **Sincronizar dependencias**
   - Android Studio sincronizará automáticamente las dependencias
   - Si hay errores, ejecutar "Sync Project with Gradle Files"

4. **Ejecutar la aplicación**
   - Conectar dispositivo Android o iniciar emulador
   - Presionar el botón "Run" o Shift+F10

## 📊 Base de Datos

### Entidades Principales

#### 👤 Usuario (User)
- Información personal completa
- Credenciales de acceso
- Rol y permisos
- Estado activo/inactivo

#### 🆔 Tipo de Licencia (LicenseType)
- Categorías A1, A2, A3, B1, B2, C1, C2, C3, D1, D2, E1, E2
- Requisitos de edad
- Precios y vigencia
- Exámenes requeridos

#### 📅 Cita (Appointment)
- Tipos: Médico, Teoría, Práctica, Clase, Consulta
- Estados: Programada, Confirmada, En Progreso, Completada, Cancelada
- Asignación de examinadores/instructores

#### 📝 Examen (Exam)
- Resultados de exámenes
- Preguntas categorizadas
- Respuestas del usuario
- Calificaciones y retroalimentación

#### 🚗 Clase de Manejo (DrivingClass)
- Sesiones programadas
- Progreso por sesión
- Instructores asignados
- Evaluaciones continuas

#### 🆔 Licencia (License)
- Licencias emitidas
- Estado de aprobación
- Intentos de examen práctico
- Fechas de emisión y vencimiento

## 🔄 Flujo del Proceso de Licencia

### Para Estudiantes

1. **Registro** en la aplicación
2. **Examen Médico** - Certificación de aptitud física
3. **Examen de Reglas** - Conocimiento de normas de tránsito
4. **Clases de Manejo** - Práctica supervisada (opcional)
5. **Examen Práctico** - Evaluación de manejo
   - Máximo 3 intentos
   - Resultado inmediato
6. **Emisión de Licencia** - Si aprueba todos los exámenes

### Para Instructores
- Gestión de clases asignadas
- Evaluación de estudiantes
- Registro de progreso
- Programación de horarios

### Para Examinadores
- Evaluación de exámenes prácticos
- Calificación inmediata
- Registro de resultados
- Gestión de agenda

### Para Administradores
- Supervisión completa del sistema
- Gestión de usuarios
- Configuración de parámetros
- Generación de reportes

## 🎨 Diseño de UI/UX

### Principios de Diseño
- **Material Design 3** como base
- **Accesibilidad** en todos los componentes
- **Responsive** para diferentes tamaños de pantalla
- **Consistencia** en colores y tipografía

### Componentes Principales
- **Navigation Drawer** con menú lateral
- **Cards** para información agrupada
- **Floating Action Buttons** para acciones principales
- **Progress Indicators** para seguimiento
- **Status Chips** para estados

## 🔧 Configuración de Desarrollo

### Variables de Entorno
```kotlin
// En local.properties
API_BASE_URL=https://api.appbrevete.com
DATABASE_NAME=appbrevete_database
```

### Build Variants
- **Debug**: Para desarrollo con logs detallados
- **Release**: Para producción optimizada

## 📱 Funcionalidades por Módulo

### Módulo de Autenticación ✅
- [x] Login con email y contraseña
- [x] Registro con validación de datos
- [x] Sistema de roles y permisos
- [x] Gestión de sesiones

### Módulo de Navegación ✅
- [x] Drawer lateral con menú contextual
- [x] Navegación entre pantallas
- [x] Gestión de estado de navegación

### Módulo de Base de Datos ✅
- [x] Configuración de Room
- [x] Entidades y DAOs
- [x] Converters para tipos complejos
- [x] Migraciones de base de datos

### Módulo de Pantalla Principal ✅
- [x] Dashboard personalizado por rol
- [x] Acciones rápidas
- [x] Progreso de licencia
- [x] Notificaciones importantes

### Módulo de Citas 🔄
- [x] Pantalla básica de citas
- [x] Filtros por tipo y estado
- [ ] Integración con calendario
- [ ] Notificaciones push

### Módulo de Clases 🔄
- [x] Pantalla básica de clases
- [x] Seguimiento de progreso
- [ ] Asignación de instructores
- [ ] Evaluaciones detalladas

### Módulo de Simulador 🔄
- [x] Pantalla básica del simulador
- [x] Categorías de preguntas
- [ ] Lógica de examen completa
- [ ] Estadísticas de rendimiento

### Módulo de Licencias 🔄
- [x] Catálogo de tipos de licencia
- [x] Información detallada
- [ ] Proceso de solicitud
- [ ] Integración con pagos

### Módulo de Perfil 🔄
- [x] Información personal
- [x] Progreso de licencia
- [ ] Edición de datos
- [ ] Cambio de contraseña

### Módulo Administrativo 🔄
- [x] Panel básico de administración
- [x] Estadísticas generales
- [ ] Gestión completa de usuarios
- [ ] Reportes avanzados

## 🚧 Próximos Pasos

### Fase 2 - Funcionalidades Avanzadas
1. **Integración con API** para sincronización
2. **Notificaciones push** para recordatorios
3. **Sistema de pagos** integrado
4. **Reportes avanzados** con gráficos
5. **Modo offline** para uso sin conexión

### Fase 3 - Optimizaciones
1. **Performance** y optimización de memoria
2. **Testing** automatizado
3. **CI/CD** pipeline
4. **Analytics** de uso
5. **Backup** automático de datos

## 🤝 Contribución

### Cómo Contribuir
1. Fork del repositorio
2. Crear rama para nueva funcionalidad
3. Commit de cambios con mensajes descriptivos
4. Push a la rama
5. Crear Pull Request

### Estándares de Código
- **Kotlin Coding Conventions**
- **Compose Guidelines**
- **Material Design Principles**
- **Clean Architecture**

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 📞 Contacto

Para preguntas o soporte técnico:
- **Email**: soporte@appbrevete.com
- **Teléfono**: +51 1 234 5678
- **Dirección**: Av. Principal 123, Lima, Perú

---

**App Brevete** - Simplificando el proceso de obtención de licencias de conducir en Perú 🇵🇪
