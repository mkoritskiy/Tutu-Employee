package ru.tutu.tutuemployee.domain.repository

import ru.tutu.tutuemployee.domain.model.FavoriteCard

/**
 * Repository interface для избранного
 */
interface FavoritesRepository {
    suspend fun getFavorites(): Result<List<FavoriteCard>>
    suspend fun addFavorite(title: String, url: String): Result<FavoriteCard>
    suspend fun deleteFavorite(id: String): Result<Unit>
}
