package ru.tutu.tutuemployee.data.repository

import ru.tutu.tutuemployee.data.remote.datasource.MerchRemoteDataSource
import ru.tutu.tutuemployee.data.remote.dto.toDomain
import ru.tutu.tutuemployee.domain.model.MerchItem
import ru.tutu.tutuemployee.domain.repository.MerchRepository

/**
 * Реализация MerchRepository
 */
class MerchRepositoryImpl(
    private val remoteDataSource: MerchRemoteDataSource
) : MerchRepository {

    override suspend fun getMerchItems(): Result<List<MerchItem>> {
        return remoteDataSource.getMerchItems()
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun purchaseMerchItem(itemId: String): Result<Unit> {
        return remoteDataSource.purchaseMerchItem(itemId)
    }
}
