package ru.tutu.tutuemployee.data.remote.datasource

import ru.tutu.tutuemployee.data.remote.api.ApiService
import ru.tutu.tutuemployee.data.remote.dto.NewsDto

/**
 * Remote data source для работы с новостями
 */
interface NewsRemoteDataSource {
    suspend fun getNews(): Result<List<NewsDto>>
    suspend fun getOfficeNews(): Result<List<NewsDto>>
}

class NewsRemoteDataSourceImpl(
    private val apiService: ApiService
) : NewsRemoteDataSource {

    override suspend fun getNews(): Result<List<NewsDto>> {
        return apiService.getNews()
    }

    override suspend fun getOfficeNews(): Result<List<NewsDto>> {
        return apiService.getOfficeNews()
    }
}
