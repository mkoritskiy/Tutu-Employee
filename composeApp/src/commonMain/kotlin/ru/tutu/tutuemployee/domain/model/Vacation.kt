package ru.tutu.tutuemployee.domain.model

/**
 * Domain model для отпусков
 */
data class Vacation(
    val id: String,
    val startDate: String,
    val endDate: String,
    val daysCount: Int,
    val status: VacationStatus,
    val reason: String?
)

enum class VacationStatus {
    PLANNED,
    APPROVED,
    REJECTED
}
