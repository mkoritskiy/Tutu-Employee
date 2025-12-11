package ru.tutu.tutuemployee.domain.repository

import ru.tutu.tutuemployee.domain.model.News

/**
 * Repository interface для новостей
 */
interface NewsRepository {
    suspend fun getNews(): Result<List<News>>
    suspend fun getOfficeNews(): Result<List<News>>
}
