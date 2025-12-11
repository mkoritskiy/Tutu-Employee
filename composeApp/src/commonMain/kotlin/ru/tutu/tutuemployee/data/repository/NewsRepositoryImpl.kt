package ru.tutu.tutuemployee.data.repository

import ru.tutu.tutuemployee.data.remote.datasource.NewsRemoteDataSource
import ru.tutu.tutuemployee.data.remote.dto.toDomain
import ru.tutu.tutuemployee.domain.model.News
import ru.tutu.tutuemployee.domain.repository.NewsRepository

/**
 * Реализация NewsRepository
 */
class NewsRepositoryImpl(
    private val remoteDataSource: NewsRemoteDataSource
) : NewsRepository {

    override suspend fun getNews(): Result<List<News>> {
        return remoteDataSource.getNews()
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getOfficeNews(): Result<List<News>> {
        return remoteDataSource.getOfficeNews()
            .map { list -> list.map { it.toDomain() } }
    }
}
