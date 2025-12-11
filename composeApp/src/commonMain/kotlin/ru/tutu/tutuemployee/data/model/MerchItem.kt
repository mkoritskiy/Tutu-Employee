package ru.tutu.tutuemployee.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MerchItem(
    val id: String,
    val name: String,
    val description: String,
    val price: Int,
    val imageUrl: String? = null,
    val category: MerchCategory,
    val inStock: Boolean = true
)

@Serializable
enum class MerchCategory {
    CLOTHING,
    ACCESSORIES,
    STATIONERY,
    ELECTRONICS
}
