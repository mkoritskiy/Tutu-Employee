package ru.tutu.tutuemployee.data.repository

import ru.tutu.tutuemployee.data.remote.datasource.FavoritesRemoteDataSource
import ru.tutu.tutuemployee.data.remote.dto.toDomain
import ru.tutu.tutuemployee.domain.model.FavoriteCard
import ru.tutu.tutuemployee.domain.repository.FavoritesRepository

/**
 * Реализация FavoritesRepository
 */
class FavoritesRepositoryImpl(
    private val remoteDataSource: FavoritesRemoteDataSource
) : FavoritesRepository {

    override suspend fun getFavorites(): Result<List<FavoriteCard>> {
        return remoteDataSource.getFavorites()
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun addFavorite(title: String, url: String): Result<FavoriteCard> {
        return remoteDataSource.addFavorite(title, url)
            .map { it.toDomain() }
    }

    override suspend fun deleteFavorite(id: String): Result<Unit> {
        return remoteDataSource.deleteFavorite(id)
    }
}
