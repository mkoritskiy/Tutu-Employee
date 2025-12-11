package ru.tutu.tutuemployee.data.remote.datasource

import ru.tutu.tutuemployee.data.remote.api.ApiService
import ru.tutu.tutuemployee.data.remote.dto.MerchItemDto

/**
 * Remote data source для работы с магазином мерча
 */
interface MerchRemoteDataSource {
    suspend fun getMerchItems(): Result<List<MerchItemDto>>
    suspend fun purchaseMerchItem(itemId: String): Result<Unit>
}

class MerchRemoteDataSourceImpl(
    private val apiService: ApiService
) : MerchRemoteDataSource {

    override suspend fun getMerchItems(): Result<List<MerchItemDto>> {
        return apiService.getMerchItems()
    }

    override suspend fun purchaseMerchItem(itemId: String): Result<Unit> {
        return apiService.purchaseMerchItem(itemId)
    }
}
