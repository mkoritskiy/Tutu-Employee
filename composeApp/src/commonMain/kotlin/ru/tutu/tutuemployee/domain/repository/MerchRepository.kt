package ru.tutu.tutuemployee.domain.repository

import ru.tutu.tutuemployee.domain.model.MerchItem

/**
 * Repository interface для магазина мерча
 */
interface MerchRepository {
    suspend fun getMerchItems(): Result<List<MerchItem>>
    suspend fun purchaseMerchItem(itemId: String): Result<Unit>
}
