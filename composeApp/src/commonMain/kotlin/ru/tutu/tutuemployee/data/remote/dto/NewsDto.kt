package ru.tutu.tutuemployee.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.tutu.tutuemployee.domain.model.News
import ru.tutu.tutuemployee.domain.model.NewsCategory

/**
 * DTO для списка новостей (согласно OpenAPI)
 */
@Serializable
data class NewsListResponse(
    val total: Int,
    val items: List<NewsPreviewDto>
)

/**
 * Preview новости для списка
 */
@Serializable
data class NewsPreviewDto(
    val id: String,
    val title: String,
    @SerialName("preview_text")
    val previewText: String,
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("published_at")
    val publishedAt: String
)

/**
 * Полная новость
 */
@Serializable
data class NewsItemDto(
    val id: String,
    val title: String,
    @SerialName("full_text")
    val fullText: String,
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("published_at")
    val publishedAt: String,
    val tags: List<String> = emptyList(),
    val author: AuthorDto? = null
)

@Serializable
data class AuthorDto(
    val id: String,
    val name: String
)

/**
 * DTO для новостей из API (для обратной совместимости)
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

fun NewsPreviewDto.toDomain(): News {
    return News(
        id = id,
        title = title,
        content = previewText,
        imageUrl = imageUrl,
        publishedAt = publishedAt,
        category = NewsCategory.COMPANY
    )
}

fun NewsItemDto.toDomain(): News {
    return News(
        id = id,
        title = title,
        content = fullText,
        imageUrl = imageUrl,
        publishedAt = publishedAt,
        category = NewsCategory.COMPANY
    )
}

fun NewsCategoryDto.toDomain(): NewsCategory {
    return when (this) {
        NewsCategoryDto.COMPANY -> NewsCategory.COMPANY
        NewsCategoryDto.OFFICE -> NewsCategory.OFFICE
        NewsCategoryDto.EVENTS -> NewsCategory.EVENTS
    }
}
