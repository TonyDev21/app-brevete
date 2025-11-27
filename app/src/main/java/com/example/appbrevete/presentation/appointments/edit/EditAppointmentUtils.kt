package com.example.appbrevete.presentation.appointments.edit

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Funciones de utilidad para EditAppointmentScreen
 */

@RequiresApi(Build.VERSION_CODES.O)
internal fun formatTimestamp(timestamp: Long): String {
    val instant = java.time.Instant.ofEpochMilli(timestamp)
    val localDateTime = java.time.LocalDateTime.ofInstant(instant, java.time.ZoneId.systemDefault())
    return localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
}

@RequiresApi(Build.VERSION_CODES.O)
internal fun generateAvailableDates(): List<LocalDate> {
    val today = LocalDate.now()
    return (1..30).map { dayOffset ->
        today.plusDays(dayOffset.toLong()).let { date ->
            // Saltar fines de semana (sábado = 6, domingo = 7)
            if (date.dayOfWeek.value in 6..7) {
                date.plusDays((8 - date.dayOfWeek.value).toLong())
            } else {
                date
            }
        }
    }.distinct().take(20)
}

internal fun generateAvailableTimes(): List<String> {
    return listOf(
        "08:00", "08:30", "09:00", "09:30", "10:00", "10:30",
        "11:00", "11:30", "14:00", "14:30", "15:00", "15:30",
        "16:00", "16:30", "17:00", "17:30"
    )
}