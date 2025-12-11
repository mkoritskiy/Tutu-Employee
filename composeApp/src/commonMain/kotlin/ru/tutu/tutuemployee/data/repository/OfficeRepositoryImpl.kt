package ru.tutu.tutuemployee.data.repository

import ru.tutu.tutuemployee.data.remote.datasource.OfficeRemoteDataSource
import ru.tutu.tutuemployee.data.remote.dto.toDomain
import ru.tutu.tutuemployee.domain.model.WorkspaceBooking
import ru.tutu.tutuemployee.domain.repository.OfficeRepository

/**
 * Реализация OfficeRepository
 */
class OfficeRepositoryImpl(
    private val remoteDataSource: OfficeRemoteDataSource
) : OfficeRepository {

    override suspend fun getWorkspaceBookings(date: String): Result<List<WorkspaceBooking>> {
        return remoteDataSource.getWorkspaceBookings(date)
            .map { list -> list.map { it.toDomain() } }
    }
}
