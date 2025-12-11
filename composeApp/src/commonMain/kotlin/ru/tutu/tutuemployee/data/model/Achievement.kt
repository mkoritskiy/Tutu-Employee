package ru.tutu.tutuemployee.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val iconUrl: String? = null,
    val earnedAt: String
)
