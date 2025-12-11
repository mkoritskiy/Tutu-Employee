package ru.tutu.tutuemployee.data.remote.datasource

import ru.tutu.tutuemployee.data.remote.api.ApiService
import ru.tutu.tutuemployee.data.remote.dto.*

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

    // TODO: получать userId из AuthManager когда он будет реализован
    private fun getCurrentUserId(): String = "user-123"

    override suspend fun getFavorites(): Result<List<FavoriteCardDto>> {
        val userId = getCurrentUserId()
        return apiService.getFavorites(userId).map { response ->
            response.items.map { item ->
                FavoriteCardDto(
                    id = item.id,
                    title = item.description ?: item.link,
                    url = item.link,
                    iconUrl = null
                )
            }
        }
    }

    override suspend fun addFavorite(title: String, url: String): Result<FavoriteCardDto> {
        val userId = getCurrentUserId()
        val request = AddFavoriteRequest(
            userId = userId,
            link = url,
            description = title
        )
        return apiService.addFavorite(request).map { item ->
            FavoriteCardDto(
                id = item.id,
                title = item.description ?: item.link,
                url = item.link,
                iconUrl = null
            )
        }
    }

    override suspend fun deleteFavorite(id: String): Result<Unit> {
        return apiService.deleteFavorite(id)
    }
}
