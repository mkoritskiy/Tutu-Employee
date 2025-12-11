package ru.tutu.tutuemployee.data.remote.dto

import kotlinx.serialization.Serializable
import ru.tutu.tutuemployee.domain.model.News
import ru.tutu.tutuemployee.domain.model.NewsCategory

/**
 * DTO для новостей из API
 */
@Serializable
data class NewsDto(
    val id: String,
    val title: String,
    val content: String,
    val imageUrl: String? = null,
    val publishedAt: String,
    val category: NewsCategoryDto = NewsCategoryDto.COMPANY
)

@Serializable
enum class NewsCategoryDto {
    COMPANY,
    OFFICE,
    EVENTS
}

/**
 * Маппер DTO -> Domain
 */
fun NewsDto.toDomain(): News {
    return News(
        id = id,
        title = title,
        content = content,
        imageUrl = imageUrl,
        publishedAt = publishedAt,
        category = category.toDomain()
    )
}

fun NewsCategoryDto.toDomain(): NewsCategory {
    return when (this) {
        NewsCategoryDto.COMPANY -> NewsCategory.COMPANY
        NewsCategoryDto.OFFICE -> NewsCategory.OFFICE
        NewsCategoryDto.EVENTS -> NewsCategory.EVENTS
    }
}
