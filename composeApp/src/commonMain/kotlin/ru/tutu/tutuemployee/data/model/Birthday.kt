package ru.tutu.tutuemployee.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Birthday(
    val employeeId: String,
    val employeeName: String,
    val date: String,
    val department: String,
    val avatarUrl: String? = null
)
