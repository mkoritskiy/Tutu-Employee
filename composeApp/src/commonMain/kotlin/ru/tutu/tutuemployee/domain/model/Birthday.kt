package ru.tutu.tutuemployee.domain.model

/**
 * Domain model для дня рождения сотрудника
 */
data class Birthday(
    val employeeId: String,
    val employeeName: String,
    val date: String,
    val department: String,
    val avatarUrl: String?
)
