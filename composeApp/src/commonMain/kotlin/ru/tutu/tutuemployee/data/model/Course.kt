package ru.tutu.tutuemployee.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val id: String,
    val title: String,
    val description: String,
    val duration: String,
    val imageUrl: String? = null,
    val progress: Int = 0,
    val isCompleted: Boolean = false
)
