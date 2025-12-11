package ru.tutu.tutuemployee.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteCard(
    val id: String,
    val title: String,
    val url: String,
    val iconUrl: String? = null
)
