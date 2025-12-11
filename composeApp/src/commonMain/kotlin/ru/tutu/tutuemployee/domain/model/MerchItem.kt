package ru.tutu.tutuemployee.domain.model

/**
 * Domain model для товаров в магазине мерча
 */
data class MerchItem(
    val id: String,
    val name: String,
    val description: String,
    val price: Int,
    val imageUrl: String?,
    val category: MerchCategory,
    val inStock: Boolean
)

enum class MerchCategory {
    CLOTHING,
    ACCESSORIES,
    STATIONERY,
    ELECTRONICS
}
