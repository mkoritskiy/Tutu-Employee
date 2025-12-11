package ru.tutu.tutuemployee.domain.usecase.news

import ru.tutu.tutuemployee.domain.model.News
import ru.tutu.tutuemployee.domain.repository.NewsRepository

/**
 * Use case для получения новостей
 */
class GetNewsUseCase(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(): Result<List<News>> {
        return newsRepository.getNews()
    }
}
