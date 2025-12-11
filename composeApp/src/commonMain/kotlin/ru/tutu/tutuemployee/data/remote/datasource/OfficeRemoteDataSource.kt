package ru.tutu.tutuemployee.data.remote.datasource

import ru.tutu.tutuemployee.data.remote.api.ApiService
import ru.tutu.tutuemployee.data.remote.dto.WorkspaceBookingDto

/**
 * Remote data source для работы с офисом
 */
interface OfficeRemoteDataSource {
    suspend fun getWorkspaceBookings(date: String): Result<List<WorkspaceBookingDto>>
}

class OfficeRemoteDataSourceImpl(
    private val apiService: ApiService
) : OfficeRemoteDataSource {

    override suspend fun getWorkspaceBookings(date: String): Result<List<WorkspaceBookingDto>> {
        return apiService.getWorkspaceBookings(date)
    }
}
