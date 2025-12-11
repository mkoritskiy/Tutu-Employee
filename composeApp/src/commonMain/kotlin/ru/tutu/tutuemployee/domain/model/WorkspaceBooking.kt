package ru.tutu.tutuemployee.domain.model

/**
 * Domain model для бронирования рабочих мест
 */
data class WorkspaceBooking(
    val id: String,
    val workspaceNumber: String,
    val date: String,
    val isBooked: Boolean,
    val bookedBy: String?,
    val floor: Int
)
