package ru.tutu.tutuemployee.data.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class News(
    val id: String,
    val title: String,
    val content: String,
    val imageUrl: String? = null,
    val publishedAt: String,
    val category: NewsCategory = NewsCategory.COMPANY
)

@Serializable
enum class NewsCategory {
    COMPANY,
    OFFICE,
    EVENTS
}
