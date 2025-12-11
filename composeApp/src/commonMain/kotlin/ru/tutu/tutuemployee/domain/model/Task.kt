package ru.tutu.tutuemployee.domain.model

/**
 * Domain model для задач
 */
data class Task(
    val id: String,
    val title: String,
    val description: String,
    val status: TaskStatus,
    val dueDate: String?
)

enum class TaskStatus {
    TODO,
    IN_PROGRESS,
    DONE
}
