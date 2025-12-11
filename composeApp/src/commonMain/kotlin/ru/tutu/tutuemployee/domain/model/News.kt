package ru.tutu.tutuemployee.domain.model

/**
 * Domain model для новостей
 */
data class News(
    val id: String,
    val title: String,
    val content: String,
    val imageUrl: String?,
    val publishedAt: String,
    val category: NewsCategory
)

enum class NewsCategory {
    COMPANY,
    OFFICE,
    EVENTS
}
