package ru.tutu.tutuemployee.data.network

import kotlinx.coroutines.delay
import ru.tutu.tutuemployee.data.model.*

/**
 * Mock API Service для разработки без реального backend
 *
 * Использование:
 * В ApiService.kt замените:
 * - private val client = ApiClient.httpClient
 * на:
 * - private val mockService = MockApiService()
 *
 * И измените методы чтобы использовать mockService вместо client
 */
class MockApiService {

    // Симуляция задержки сети
    private suspend fun simulateNetworkDelay() {
        delay(500) // 500ms
    }

    suspend fun login(username: String, password: String): Result<AuthResponse> {
        simulateNetworkDelay()

        // Простая проверка
        return if (username.isNotEmpty() && password.isNotEmpty()) {
            Result.success(
                AuthResponse(
                    token = "mock_token_${username}_${password.hashCode()}",
                    user = mockUser
                )
            )
        } else {
            Result.failure(Exception("Неверный логин или пароль"))
        }
    }

    suspend fun getCurrentUser(): Result<User> {
        simulateNetworkDelay()
        return Result.success(mockUser)
    }

    suspend fun getNews(): Result<List<News>> {
        simulateNetworkDelay()
        return Result.success(mockNews)
    }

    suspend fun getBirthdays(): Result<List<Birthday>> {
        simulateNetworkDelay()
        return Result.success(mockBirthdays)
    }

    suspend fun searchEmployees(query: String): Result<List<User>> {
        simulateNetworkDelay()
        val results = mockEmployees.filter {
            it.firstName.contains(query, ignoreCase = true) ||
                    it.lastName.contains(query, ignoreCase = true) ||
                    it.department.contains(query, ignoreCase = true)
        }
        return Result.success(results)
    }

    suspend fun getAchievements(): Result<List<Achievement>> {
        simulateNetworkDelay()
        return Result.success(mockAchievements)
    }

    suspend fun getTasks(): Result<List<Task>> {
        simulateNetworkDelay()
        return Result.success(mockTasks)
    }

    suspend fun getVacations(): Result<List<Vacation>> {
        simulateNetworkDelay()
        return Result.success(mockVacations)
    }

    suspend fun getCourses(): Result<List<Course>> {
        simulateNetworkDelay()
        return Result.success(mockCourses)
    }

    suspend fun getWorkspaceBookings(date: String): Result<List<WorkspaceBooking>> {
        simulateNetworkDelay()
        return Result.success(mockWorkspaces)
    }

    suspend fun getOfficeNews(): Result<List<News>> {
        simulateNetworkDelay()
        return Result.success(mockOfficeNews)
    }

    suspend fun getMerchItems(): Result<List<MerchItem>> {
        simulateNetworkDelay()
        return Result.success(mockMerchItems)
    }

    suspend fun purchaseMerchItem(itemId: String): Result<Unit> {
        simulateNetworkDelay()
        // Симуляция успешной покупки
        return Result.success(Unit)
    }

    suspend fun getFavorites(): Result<List<FavoriteCard>> {
        simulateNetworkDelay()
        return Result.success(mockFavorites)
    }

    suspend fun addFavorite(title: String, url: String): Result<FavoriteCard> {
        simulateNetworkDelay()
        val newFavorite = FavoriteCard(
            id = "fav_${title.hashCode()}_${url.hashCode()}",
            title = title,
            url = url
        )
        return Result.success(newFavorite)
    }

    suspend fun deleteFavorite(id: String): Result<Unit> {
        simulateNetworkDelay()
        return Result.success(Unit)
    }

    // Mock данные
    companion object {
        private val mockUser = User(
            id = "1",
            username = "ivan.ivanov",
            firstName = "Иван",
            lastName = "Иванов",
            position = "Senior Developer",
            department = "Разработка",
            legalEntity = "ООО \"Туту.ру\"",
            email = "ivan.ivanov@tutu.ru",
            avatarUrl = null,
            availableVacationDays = 28,
            bonusPoints = 1250
        )

        private val mockNews = listOf(
            News(
                id = "1",
                title = "Новый офис в Москве",
                content = "Мы открываем новый офис в центре Москвы с современными опенспейсами и зонами отдыха. Приглашаем всех на экскурсию!",
                publishedAt = "2024-01-15T10:00:00Z",
                category = NewsCategory.COMPANY
            ),
            News(
                id = "2",
                title = "Запуск нового продукта",
                content = "Наша команда разработала инновационное решение для бронирования билетов. Встреча состоится в четверг.",
                publishedAt = "2024-01-14T15:30:00Z",
                category = NewsCategory.COMPANY
            ),
            News(
                id = "3",
                title = "Корпоратив в пятницу",
                content = "Приглашаем всех на корпоративное мероприятие. Будет интересная программа и призы!",
                publishedAt = "2024-01-13T09:00:00Z",
                category = NewsCategory.EVENTS
            )
        )

        private val mockBirthdays = listOf(
            Birthday(
                employeeId = "2",
                employeeName = "Анна Петрова",
                date = "15.01",
                department = "HR"
            ),
            Birthday(
                employeeId = "3",
                employeeName = "Сергей Сидоров",
                date = "16.01",
                department = "Разработка"
            ),
            Birthday(
                employeeId = "4",
                employeeName = "Мария Кузнецова",
                date = "18.01",
                department = "Маркетинг"
            )
        )

        private val mockEmployees = listOf(
            mockUser,
            User(
                "2",
                "anna.petrova",
                "Анна",
                "Петрова",
                "HR Manager",
                "HR",
                "ООО \"Туту.ру\"",
                "anna.petrova@tutu.ru",
                null,
                28,
                800
            ),
            User(
                "3",
                "sergey.sidorov",
                "Сергей",
                "Сидоров",
                "Backend Developer",
                "Разработка",
                "ООО \"Туту.ру\"",
                "sergey.sidorov@tutu.ru",
                null,
                28,
                950
            ),
            User(
                "4",
                "maria.kuznetsova",
                "Мария",
                "Кузнецова",
                "Marketing Manager",
                "Маркетинг",
                "ООО \"Туту.ру\"",
                "maria.kuznetsova@tutu.ru",
                null,
                28,
                700
            )
        )

        private val mockAchievements = listOf(
            Achievement(
                "1",
                "Первый проект",
                "Завершен первый проект",
                null,
                "2023-12-01T00:00:00Z"
            ),
            Achievement("2", "100 коммитов", "Сделано 100 коммитов", null, "2023-11-15T00:00:00Z"),
            Achievement(
                "3",
                "Лучший сотрудник месяца",
                "Награда за ноябрь 2023",
                null,
                "2023-11-30T00:00:00Z"
            )
        )

        private val mockTasks = listOf(
            Task(
                "1",
                "Разработка новой фичи",
                "Реализовать новый функционал бронирования",
                TaskStatus.IN_PROGRESS,
                "2024-01-20"
            ),
            Task("2", "Code Review", "Провести ревью кода коллег", TaskStatus.TODO, "2024-01-16"),
            Task("3", "Документация API", "Обновить документацию REST API", TaskStatus.DONE, null)
        )

        private val mockVacations = listOf(
            Vacation("1", "2024-02-10", "2024-02-24", 14, VacationStatus.APPROVED, "Зимний отпуск"),
            Vacation("2", "2024-07-01", "2024-07-14", 14, VacationStatus.PLANNED, "Летний отпуск")
        )

        private val mockCourses = listOf(
            Course(
                "1",
                "Kotlin Advanced",
                "Продвинутый курс по Kotlin",
                "20 hours",
                null,
                75,
                false
            ),
            Course(
                "2",
                "Compose Multiplatform",
                "Разработка кроссплатформенных приложений",
                "15 hours",
                null,
                100,
                true
            ),
            Course(
                "3",
                "Soft Skills",
                "Развитие коммуникативных навыков",
                "10 hours",
                null,
                30,
                false
            )
        )

        private val mockWorkspaces = (1..16).map { num ->
            WorkspaceBooking(
                id = "ws_$num",
                workspaceNumber = "A-$num",
                date = "2024-01-15",
                isBooked = num % 3 == 0, // Каждое 3-е место занято
                bookedBy = if (num % 3 == 0) "Иван Иванов" else null,
                floor = 2
            )
        }

        private val mockOfficeNews = listOf(
            News(
                "101",
                "Ремонт в опенспейсе",
                "На следующей неделе будет небольшой ремонт",
                null,
                "2024-01-14T12:00:00Z",
                NewsCategory.OFFICE
            ),
            News(
                "102",
                "Новые кофемашины",
                "Установлены новые кофемашины на каждом этаже",
                null,
                "2024-01-13T10:00:00Z",
                NewsCategory.OFFICE
            )
        )

        private val mockMerchItems = listOf(
            MerchItem(
                "1",
                "Футболка с логотипом",
                "Хлопковая футболка с логотипом компании",
                500,
                null,
                MerchCategory.CLOTHING,
                true
            ),
            MerchItem(
                "2",
                "Кружка",
                "Керамическая кружка с логотипом",
                200,
                null,
                MerchCategory.ACCESSORIES,
                true
            ),
            MerchItem(
                "3",
                "Блокнот",
                "Фирменный блокнот в линейку",
                150,
                null,
                MerchCategory.STATIONERY,
                true
            ),
            MerchItem(
                "4",
                "Худи",
                "Теплое худи с вышивкой",
                1200,
                null,
                MerchCategory.CLOTHING,
                false
            ),
            MerchItem(
                "5",
                "Рюкзак",
                "Городской рюкзак с отделением для ноутбука",
                2000,
                null,
                MerchCategory.ACCESSORIES,
                true
            ),
            MerchItem(
                "6",
                "Power Bank",
                "Портативное зарядное устройство 10000mAh",
                800,
                null,
                MerchCategory.ELECTRONICS,
                true
            )
        )

        private val mockFavorites = listOf(
            FavoriteCard("1", "Jira", "https://jira.tutu.ru"),
            FavoriteCard("2", "Confluence", "https://confluence.tutu.ru"),
            FavoriteCard("3", "GitLab", "https://gitlab.tutu.ru")
        )
    }
}
