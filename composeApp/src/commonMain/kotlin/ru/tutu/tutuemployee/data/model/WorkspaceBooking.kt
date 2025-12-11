package ru.tutu.tutuemployee.data.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkspaceBooking(
    val id: String,
    val workspaceNumber: String,
    val date: String,
    val isBooked: Boolean,
    val bookedBy: String? = null,
    val floor: Int
)
