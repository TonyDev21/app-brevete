# 🗑️ Limpiar Base de Datos - AppBrevete

## Método 1: Desinstalar y Reinstalar (Recomendado)

La forma más rápida y segura de limpiar todos los datos de la aplicación:

1. **Desinstalar la app del dispositivo/emulador**
   - En Android: Configuración → Apps → AppBrevete → Desinstalar
   - O mantener presionado el ícono de la app → Desinstalar

2. **Instalar nuevamente**
   ```bash
   cd c:\Users\Tony\Desktop\Proyectos-Movil
   .\gradlew installDebug
   ```

## Método 2: Borrar Datos de la App (Sin Desinstalar)

Si quieres mantener la app instalada pero limpiar los datos:

1. **En el dispositivo Android:**
   - Configuración → Apps → AppBrevete
   - Almacenamiento → Borrar datos
   - Confirmar

2. **Reiniciar la app**

## Método 3: Comando ADB (Para Desarrolladores)

Si tienes el dispositivo conectado por USB:

```bash
# Borrar datos de la app
adb shell pm clear com.example.appbrevete

# O reinstalar desde cero
adb uninstall com.example.appbrevete
.\gradlew installDebug
```

## ✅ Resultado

Después de cualquiera de estos métodos:
- ✅ Todas las citas médicas serán eliminadas
- ✅ Todas las evaluaciones médicas serán eliminadas
- ✅ Todas las clases de manejo serán eliminadas
- ⚠️ Los usuarios registrados también serán eliminados (deberás crear usuarios nuevamente)

## 📝 Nota

Después de limpiar, la app volverá a su estado inicial:
- Deberás volver a registrar usuarios
- Los tipos de licencia predefinidos se cargarán automáticamente
- Podrás crear citas médicas nuevas desde cero

## 🔄 Para Crear Citas Nuevas

1. Inicia sesión como **Estudiante**
2. Ve a **Mis Citas** → **Agendar Cita**
3. Selecciona:
   - Tipo: **Examen Médico**
   - Categoría de licencia (A-I, BII-A, etc.)
   - Fecha y hora
4. Confirma la cita

Luego el admin podrá ver y evaluar estas citas nuevas.
