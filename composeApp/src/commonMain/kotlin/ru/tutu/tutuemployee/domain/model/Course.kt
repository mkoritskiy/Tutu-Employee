package ru.tutu.tutuemployee.domain.model

/**
 * Domain model для курсов
 */
data class Course(
    val id: String,
    val title: String,
    val description: String,
    val duration: String,
    val imageUrl: String?,
    val progress: Int,
    val isCompleted: Boolean
)
