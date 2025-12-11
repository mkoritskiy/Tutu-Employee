package ru.tutu.tutuemployee.domain.model

/**
 * Domain model для достижений
 */
data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val iconUrl: String?,
    val earnedAt: String
)
