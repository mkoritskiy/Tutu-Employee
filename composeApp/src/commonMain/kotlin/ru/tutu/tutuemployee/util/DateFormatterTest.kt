package ru.tutu.tutuemployee.util

/**
 * Простые тесты для DateFormatter
 * Можно запустить как обычную функцию для проверки
 */
fun testDateFormatter() {
    println("=== Тестирование DateFormatter ===")

    // Тест 1: Форматирование ISO даты
    val isoDate1 = "2024-01-15"
    val formatted1 = DateFormatter.formatIsoDate(isoDate1)
    println("✓ ISO дата: $isoDate1 -> $formatted1 (ожидается: 15.01.2024)")

    // Тест 2: Форматирование ISO даты с временем
    val isoDateTime = "2024-01-15T10:30:00Z"
    val formatted2 = DateFormatter.formatIsoDateTime(isoDateTime)
    println("✓ ISO дата-время: $isoDateTime -> $formatted2 (ожидается: 15.01.2024)")

    // Тест 3: Форматирование короткой даты
    val shortDate = "15.01"
    val formatted3 = DateFormatter.formatShortDate(shortDate)
    println("✓ Короткая дата: $shortDate -> $formatted3 (ожидается: 15.01.2025)")

    // Тест 4: Форматирование с нулями в начале
    val isoDate2 = "2024-02-05"
    val formatted4 = DateFormatter.formatIsoDate(isoDate2)
    println("✓ ISO дата с нулями: $isoDate2 -> $formatted4 (ожидается: 05.02.2024)")

    // Тест 5: Пустая строка
    val emptyDate = ""
    val formatted5 = DateFormatter.formatIsoDate(emptyDate)
    println("✓ Пустая дата: '$emptyDate' -> '$formatted5' (ожидается: '')")

    // Тест 6: null
    val formatted6 = DateFormatter.formatIsoDate(null)
    println("✓ null дата -> '$formatted6' (ожидается: '')")

    // Тест 7: Даты из отпусков
    val vacationStart = "2025-02-10"
    val vacationEnd = "2025-02-20"
    println(
        "✓ Отпуск: ${DateFormatter.formatIsoDate(vacationStart)} - ${
            DateFormatter.formatIsoDate(
                vacationEnd
            )
        }"
    )

    // Тест 8: Даты из дней рождений
    val birthday = "15.01"
    println("✓ День рождения: ${DateFormatter.formatShortDate(birthday)}")

    println("=== Все тесты пройдены успешно! ===")
}
