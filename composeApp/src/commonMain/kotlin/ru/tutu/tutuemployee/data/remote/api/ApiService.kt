package ru.tutu.tutuemployee.data.remote.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.tutu.tutuemployee.data.remote.dto.*

/**
 * API Service для работы с backend
 */
class ApiService(
    private val httpClient: HttpClient
) {
    // Авторизация
    suspend fun login(username: String, password: String): Result<AuthResponse> {
        return try {
            val response = httpClient.post("/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(AuthRequest(username, password))
            }
            Result.success(response.body<AuthResponse>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Получение текущего пользователя
    suspend fun getCurrentUser(): Result<UserDto> {
        return try {
            val response = httpClient.get("/user/me")
            Result.success(response.body<UserDto>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Главная страница
    suspend fun getNews(): Result<List<NewsDto>> {
        return try {
            val response = httpClient.get("/news")
            Result.success(response.body<List<NewsDto>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBirthdays(): Result<List<BirthdayDto>> {
        return try {
            val response = httpClient.get("/birthdays")
            Result.success(response.body<List<BirthdayDto>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchEmployees(query: String): Result<List<UserDto>> {
        return try {
            val response = httpClient.get("/search/employees") {
                parameter("q", query)
            }
            Result.success(response.body<List<UserDto>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Профиль
    suspend fun getAchievements(): Result<List<AchievementDto>> {
        return try {
            val response = httpClient.get("/profile/achievements")
            Result.success(response.body<List<AchievementDto>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTasks(): Result<List<TaskDto>> {
        return try {
            val response = httpClient.get("/profile/tasks")
            Result.success(response.body<List<TaskDto>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getVacations(): Result<List<VacationDto>> {
        return try {
            val response = httpClient.get("/profile/vacations")
            Result.success(response.body<List<VacationDto>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCourses(): Result<List<CourseDto>> {
        return try {
            val response = httpClient.get("/profile/courses")
            Result.success(response.body<List<CourseDto>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Офис
    suspend fun getWorkspaceBookings(date: String): Result<List<WorkspaceBookingDto>> {
        return try {
            val response = httpClient.get("/office/workspaces") {
                parameter("date", date)
            }
            Result.success(response.body<List<WorkspaceBookingDto>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getOfficeNews(): Result<List<NewsDto>> {
        return try {
            val response = httpClient.get("/office/news")
            Result.success(response.body<List<NewsDto>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Магазин мерча
    suspend fun getMerchItems(): Result<List<MerchItemDto>> {
        return try {
            val response = httpClient.get("/merch")
            Result.success(response.body<List<MerchItemDto>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun purchaseMerchItem(itemId: String): Result<Unit> {
        return try {
            httpClient.post("/merch/$itemId/purchase")
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Избранное
    suspend fun getFavorites(): Result<List<FavoriteCardDto>> {
        return try {
            val response = httpClient.get("/favorites")
            Result.success(response.body<List<FavoriteCardDto>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addFavorite(title: String, url: String): Result<FavoriteCardDto> {
        return try {
            val response = httpClient.post("/favorites") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("title" to title, "url" to url))
            }
            Result.success(response.body<FavoriteCardDto>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteFavorite(id: String): Result<Unit> {
        return try {
            httpClient.delete("/favorites/$id")
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
