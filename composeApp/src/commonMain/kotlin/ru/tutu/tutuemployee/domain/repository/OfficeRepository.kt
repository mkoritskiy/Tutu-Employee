package ru.tutu.tutuemployee.domain.repository

import ru.tutu.tutuemployee.domain.model.WorkspaceBooking

/**
 * Repository interface для работы с офисом
 */
interface OfficeRepository {
    suspend fun getWorkspaceBookings(date: String): Result<List<WorkspaceBooking>>
}
