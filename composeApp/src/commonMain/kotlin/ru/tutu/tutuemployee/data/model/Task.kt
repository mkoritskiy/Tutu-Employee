package ru.tutu.tutuemployee.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: String,
    val title: String,
    val description: String,
    val status: TaskStatus,
    val dueDate: String? = null
)

@Serializable
enum class TaskStatus {
    TODO,
    IN_PROGRESS,
    DONE
}
