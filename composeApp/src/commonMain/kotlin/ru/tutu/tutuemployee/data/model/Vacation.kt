package ru.tutu.tutuemployee.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Vacation(
    val id: String,
    val startDate: String,
    val endDate: String,
    val daysCount: Int,
    val status: VacationStatus,
    val reason: String? = null
)

@Serializable
enum class VacationStatus {
    PLANNED,
    APPROVED,
    REJECTED
}
