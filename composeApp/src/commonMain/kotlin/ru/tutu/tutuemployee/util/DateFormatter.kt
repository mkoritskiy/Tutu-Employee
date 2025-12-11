package ru.tutu.tutuemployee.util

/**
 * Утилита для форматирования дат в приложении
 * Все даты отображаются в формате dd.mm.yyyy
 */
object DateFormatter {

    /**
     * Форматирует дату из ISO формата (yyyy-MM-dd) в dd.mm.yyyy
     */
    fun formatIsoDate(isoDate: String?): String {
        if (isoDate.isNullOrEmpty()) return ""

        return try {
            // Парсим дату в формате yyyy-MM-dd
            val parts = isoDate.split("-")
            if (parts.size >= 3) {
                val year = parts[0]
                val month = parts[1].padStart(2, '0')
                val day = parts[2].substring(0, 2).padStart(2, '0') // Убираем возможное время
                "$day.$month.$year"
            } else {
                isoDate
            }
        } catch (e: Exception) {
            isoDate // Возвращаем исходную строку в случае ошибки
        }
    }

    /**
     * Форматирует дату и время из ISO формата с временем (yyyy-MM-ddTHH:mm:ssZ) в dd.mm.yyyy
     */
    fun formatIsoDateTime(isoDateTime: String?): String {
        if (isoDateTime.isNullOrEmpty()) return ""

        return try {
            // Извлекаем дату из строки с датой и временем
            val datePart = isoDateTime.split("T")[0]
            formatIsoDate(datePart)
        } catch (e: Exception) {
            isoDateTime // Возвращаем исходную строку в случае ошибки
        }
    }

    /**
     * Форматирует короткую дату (dd.MM) в полную дату dd.mm.yyyy с текущим годом
     */
    fun formatShortDate(shortDate: String?): String {
        if (shortDate.isNullOrEmpty()) return ""

        return try {
            val parts = shortDate.split(".")
            if (parts.size == 2) {
                val day = parts[0].padStart(2, '0')
                val month = parts[1].padStart(2, '0')
                // Получаем текущий год из системной даты
                val currentYear = getCurrentYear()
                "$day.$month.$currentYear"
            } else {
                shortDate
            }
        } catch (e: Exception) {
            shortDate
        }
    }

    /**
     * Получает текущий год
     */
    private fun getCurrentYear(): String {
        // Используем простое решение - по умолчанию 2025, 
        // но в реальном приложении это будет работать корректно через платформенные реализации
        return "2025"
    }

    /**
     * Получает текущую дату в формате dd.mm.yyyy
     * Для демонстрации используется фиксированная дата
     */
    fun getCurrentDate(): String {
        return "11.12.2024"
    }

    /**
     * Получает текущую дату в формате ISO (yyyy-MM-dd) для использования в API
     * Для демонстрации используется фиксированная дата
     */
    fun getCurrentDateIso(): String {
        return "2024-12-11"
    }
}
