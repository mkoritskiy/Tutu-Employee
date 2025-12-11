package ru.tutu.tutuemployee.data.remote.datasource

import ru.tutu.tutuemployee.data.remote.api.ApiService
import ru.tutu.tutuemployee.data.remote.dto.FavoriteCardDto

/**
 * Remote data source для работы с избранным
 */
interface FavoritesRemoteDataSource {
    suspend fun getFavorites(): Result<List<FavoriteCardDto>>
    suspend fun addFavorite(title: String, url: String): Result<FavoriteCardDto>
    suspend fun deleteFavorite(id: String): Result<Unit>
}

class FavoritesRemoteDataSourceImpl(
    private val apiService: ApiService
) : FavoritesRemoteDataSource {

    override suspend fun getFavorites(): Result<List<FavoriteCardDto>> {
        return apiService.getFavorites()
    }

    override suspend fun addFavorite(title: String, url: String): Result<FavoriteCardDto> {
        return apiService.addFavorite(title, url)
    }

    override suspend fun deleteFavorite(id: String): Result<Unit> {
        return apiService.deleteFavorite(id)
    }
}
