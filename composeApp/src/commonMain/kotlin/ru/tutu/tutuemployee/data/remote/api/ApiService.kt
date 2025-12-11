package ru.tutu.tutuemployee.data.remote.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.tutu.tutuemployee.data.remote.dto.*

/**
 * Интерфейс API Service для работы с backend (согласно OpenAPI спецификации v1)
 */
interface ApiService {
    // Auth (для обратной совместимости)
    suspend fun login(username: String, password: String): Result<AuthResponse>

    // GET /v1/user - получение информации о пользователе
    suspend fun getUser(id: String): Result<UserDto>

    // GET /v1/news - получение списка новостей
    suspend fun getNewsList(
        limit: Int? = null,
        offset: Int? = null,
        category: String? = null,
        search: String? = null
    ): Result<NewsListResponse>

    // GET /v1/news/{id} - получение полной новости
    suspend fun getNewsItem(id: String): Result<NewsItemDto>

    // GET /v1/employees/search-by-last-name - поиск сотрудников по фамилии
    suspend fun searchEmployeesByLastName(
        lastName: String,
        limit: Int? = null,
        offset: Int? = null
    ): Result<EmployeeSearchResponse>

    // GET /v1/vacations - получение отпусков сотрудника
    suspend fun getVacations(
        employeeId: String,
        year: Int? = null
    ): Result<VacationListResponse>

    // GET /v1/favorites - получение избранных ссылок
    suspend fun getFavorites(userId: String): Result<FavoriteListResponse>

    // POST /v1/favorites - добавление избранной ссылки
    suspend fun addFavorite(request: AddFavoriteRequest): Result<FavoriteItemDto>

    // DELETE /v1/favorites/{id} - удаление избранной ссылки
    suspend fun deleteFavorite(id: String): Result<Unit>

    // GET /v1/achievements - получение ачивок пользователя
    suspend fun getAchievements(userId: String): Result<AchievementListResponse>

    // Deprecated - старые методы для обратной совместимости
    @Deprecated("Use getUser instead")
    suspend fun getCurrentUser(): Result<UserDto>

    @Deprecated("Use getNewsList instead")
    suspend fun getNews(): Result<List<NewsDto>>

    @Deprecated("Use searchEmployeesByLastName instead")
    suspend fun searchEmployees(query: String): Result<List<UserDto>>

    suspend fun getBirthdays(): Result<List<BirthdayDto>>
    suspend fun getTasks(): Result<List<TaskDto>>
    suspend fun getCourses(): Result<List<CourseDto>>
    suspend fun getWorkspaceBookings(date: String): Result<List<WorkspaceBookingDto>>
    suspend fun getOfficeNews(): Result<List<NewsDto>>
    suspend fun getMerchItems(): Result<List<MerchItemDto>>
    suspend fun purchaseMerchItem(itemId: String): Result<Unit>
}

/**
 * Реальная реализация API Service с HTTP запросами (согласно OpenAPI спецификации)
 */
class ApiServiceImpl(
    private val httpClient: HttpClient
) : ApiService {

    // Auth (для обратной совместимости)
    override suspend fun login(username: String, password: String): Result<AuthResponse> {
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

    // GET /v1/user - получение информации о пользователе
    override suspend fun getUser(id: String): Result<UserDto> {
        return try {
            val response = httpClient.get("/v1/user") {
                parameter("id", id)
            }
            Result.success(response.body<UserDto>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // GET /v1/news - получение списка новостей
    override suspend fun getNewsList(
        limit: Int?,
        offset: Int?,
        category: String?,
        search: String?
    ): Result<NewsListResponse> {
        return try {
            val response = httpClient.get("/v1/news") {
                limit?.let { parameter("limit", it) }
                offset?.let { parameter("offset", it) }
                category?.let { parameter("category", it) }
                search?.let { parameter("search", it) }
            }
            Result.success(response.body<NewsListResponse>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // GET /v1/news/{id} - получение полной новости
    override suspend fun getNewsItem(id: String): Result<NewsItemDto> {
        return try {
            val response = httpClient.get("/v1/news/$id")
            Result.success(response.body<NewsItemDto>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // GET /v1/employees/search-by-last-name - поиск сотрудников по фамилии
    override suspend fun searchEmployeesByLastName(
        lastName: String,
        limit: Int?,
        offset: Int?
    ): Result<EmployeeSearchResponse> {
        return try {
            val response = httpClient.get("/v1/employees/search-by-last-name") {
                parameter("last_name", lastName)
                limit?.let { parameter("limit", it) }
                offset?.let { parameter("offset", it) }
            }
            Result.success(response.body<EmployeeSearchResponse>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // GET /v1/vacations - получение отпусков сотрудника
    override suspend fun getVacations(
        employeeId: String,
        year: Int?
    ): Result<VacationListResponse> {
        return try {
            val response = httpClient.get("/v1/vacations") {
                parameter("employee_id", employeeId)
                year?.let { parameter("year", it) }
            }
            Result.success(response.body<VacationListResponse>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // GET /v1/favorites - получение избранных ссылок
    override suspend fun getFavorites(userId: String): Result<FavoriteListResponse> {
        return try {
            val response = httpClient.get("/v1/favorites") {
                parameter("user_id", userId)
            }
            Result.success(response.body<FavoriteListResponse>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // POST /v1/favorites - добавление избранной ссылки
    override suspend fun addFavorite(request: AddFavoriteRequest): Result<FavoriteItemDto> {
        return try {
            val response = httpClient.post("/v1/favorites") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            Result.success(response.body<FavoriteItemDto>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // DELETE /v1/favorites/{id} - удаление избранной ссылки
    override suspend fun deleteFavorite(id: String): Result<Unit> {
        return try {
            httpClient.delete("/v1/favorites/$id")
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // GET /v1/achievements - получение ачивок пользователя
    override suspend fun getAchievements(userId: String): Result<AchievementListResponse> {
        return try {
            val response = httpClient.get("/v1/achievements") {
                parameter("user_id", userId)
            }
            Result.success(response.body<AchievementListResponse>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Deprecated - старые методы для обратной совместимости
    @Deprecated("Use getUser instead")
    override suspend fun getCurrentUser(): Result<UserDto> {
        return try {
            val response = httpClient.get("/user/me")
            Result.success(response.body<UserDto>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Deprecated("Use getNewsList instead")
    override suspend fun getNews(): Result<List<NewsDto>> {
        return try {
            val response = httpClient.get("/news")
            Result.success(response.body<List<NewsDto>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Deprecated("Use searchEmployeesByLastName instead")
    override suspend fun searchEmployees(query: String): Result<List<UserDto>> {
        return try {
            val response = httpClient.get("/search/employees") {
                parameter("q", query)
            }
            Result.success(response.body<List<UserDto>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getBirthdays(): Result<List<BirthdayDto>> {
        return try {
            val response = httpClient.get("/birthdays")
            Result.success(response.body<List<BirthdayDto>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTasks(): Result<List<TaskDto>> {
        return try {
            val response = httpClient.get("/profile/tasks")
            Result.success(response.body<List<TaskDto>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCourses(): Result<List<CourseDto>> {
        return try {
            val response = httpClient.get("/profile/courses")
            Result.success(response.body<List<CourseDto>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWorkspaceBookings(date: String): Result<List<WorkspaceBookingDto>> {
        return try {
            val response = httpClient.get("/office/workspaces") {
                parameter("date", date)
            }
            Result.success(response.body<List<WorkspaceBookingDto>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getOfficeNews(): Result<List<NewsDto>> {
        return try {
            val response = httpClient.get("/office/news")
            Result.success(response.body<List<NewsDto>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMerchItems(): Result<List<MerchItemDto>> {
        return try {
            val response = httpClient.get("/merch")
            Result.success(response.body<List<MerchItemDto>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun purchaseMerchItem(itemId: String): Result<Unit> {
        return try {
            httpClient.post("/merch/$itemId/purchase")
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
