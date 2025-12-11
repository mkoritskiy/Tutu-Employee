package ru.tutu.tutuemployee.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val position: String,
    val department: String,
    val legalEntity: String,
    val email: String,
    val avatarUrl: String? = null,
    val availableVacationDays: Int = 0,
    val bonusPoints: Int = 0
)
