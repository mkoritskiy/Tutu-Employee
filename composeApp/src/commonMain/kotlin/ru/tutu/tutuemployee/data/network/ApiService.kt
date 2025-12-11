package ru.tutu.tutuemployee.data.network

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.tutu.tutuemployee.data.model.*

class ApiService {
    private val client = ApiClient.httpClient

    // Авторизация
    suspend fun login(username: String, password: String): Result<AuthResponse> {
        return try {
            val response = client.post("/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(AuthRequest(username, password))
            }
            Result.success(response.body<AuthResponse>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Получение текущего пользователя
    suspend fun getCurrentUser(): Result<User> {
        return try {
            val response = client.get("/user/me")
            Result.success(response.body<User>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Главная страница
    suspend fun getNews(): Result<List<News>> {
        return try {
            val response = client.get("/news")
            Result.success(response.body<List<News>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBirthdays(): Result<List<Birthday>> {
        return try {
            val response = client.get("/birthdays")
            Result.success(response.body<List<Birthday>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchEmployees(query: String): Result<List<User>> {
        return try {
            val response = client.get("/search/employees") {
                parameter("q", query)
            }
            Result.success(response.body<List<User>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Профиль
    suspend fun getAchievements(): Result<List<Achievement>> {
        return try {
            val response = client.get("/profile/achievements")
            Result.success(response.body<List<Achievement>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTasks(): Result<List<Task>> {
        return try {
            val response = client.get("/profile/tasks")
            Result.success(response.body<List<Task>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getVacations(): Result<List<Vacation>> {
        return try {
            val response = client.get("/profile/vacations")
            Result.success(response.body<List<Vacation>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCourses(): Result<List<Course>> {
        return try {
            val response = client.get("/profile/courses")
            Result.success(response.body<List<Course>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Офис
    suspend fun getWorkspaceBookings(date: String): Result<List<WorkspaceBooking>> {
        return try {
            val response = client.get("/office/workspaces") {
                parameter("date", date)
            }
            Result.success(response.body<List<WorkspaceBooking>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getOfficeNews(): Result<List<News>> {
        return try {
            val response = client.get("/office/news")
            Result.success(response.body<List<News>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Магазин мерча
    suspend fun getMerchItems(): Result<List<MerchItem>> {
        return try {
            val response = client.get("/merch")
            Result.success(response.body<List<MerchItem>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun purchaseMerchItem(itemId: String): Result<Unit> {
        return try {
            client.post("/merch/$itemId/purchase")
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Избранное
    suspend fun getFavorites(): Result<List<FavoriteCard>> {
        return try {
            val response = client.get("/favorites")
            Result.success(response.body<List<FavoriteCard>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addFavorite(title: String, url: String): Result<FavoriteCard> {
        return try {
            val response = client.post("/favorites") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("title" to title, "url" to url))
            }
            Result.success(response.body<FavoriteCard>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteFavorite(id: String): Result<Unit> {
        return try {
            client.delete("/favorites/$id")
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
