package ru.tutu.tutuemployee.domain.model

/**
 * Domain model для избранных карточек
 */
data class FavoriteCard(
    val id: String,
    val title: String,
    val url: String,
    val iconUrl: String?
)
