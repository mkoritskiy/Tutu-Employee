package ru.tutu.tutuemployee.data.remote.api

import kotlinx.coroutines.delay
import ru.tutu.tutuemployee.data.remote.dto.*

/**
 * Mock API Service для тестирования и разработки UI (адаптирован под OpenAPI спецификацию)
 * Возвращает тестовые данные без реальных сетевых запросов
 */
class MockApiService : ApiService {

    // Симуляция задержки сети (можно настроить)
    private val networkDelay = 500L

    // Хранилище для избранного
    private val favoritesStorage = mutableListOf<FavoriteItemDto>()

    // Хранилище для мерча
    private var userBonusPoints = 500

    // Счетчик для генерации ID
    private var idCounter = 0

    // Mock user ID
    private val mockUserId = "user-123"

    private fun generateId(prefix: String): String {
        idCounter++
        return "${prefix}_$idCounter"
    }

    override suspend fun login(username: String, password: String): Result<AuthResponse> {
        delay(networkDelay)

        return if (username.isNotEmpty() && password.isNotEmpty()) {
            Result.success(
                AuthResponse(
                    token = generateId("mock_token"),
                    user = getMockUserDto()
                )
            )
        } else {
            Result.failure(Exception("Неверный логин или пароль"))
        }
    }

    // GET /v1/user - получение информации о пользователе
    override suspend fun getUser(id: String): Result<UserDto> {
        delay(networkDelay)
        return Result.success(getMockUserDto())
    }

    // GET /v1/news - получение списка новостей
    override suspend fun getNewsList(
        limit: Int?,
        offset: Int?,
        category: String?,
        search: String?
    ): Result<NewsListResponse> {
        delay(networkDelay)
        val allNews = getMockNewsPreviewList()

        var filteredNews = allNews
        if (!search.isNullOrEmpty()) {
            filteredNews = filteredNews.filter {
                it.title.contains(search, ignoreCase = true) ||
                        it.previewText.contains(search, ignoreCase = true)
            }
        }

        val actualOffset = offset ?: 0
        val actualLimit = limit ?: filteredNews.size
        val paginatedNews = filteredNews.drop(actualOffset).take(actualLimit)

        return Result.success(
            NewsListResponse(
                total = filteredNews.size,
                items = paginatedNews
            )
        )
    }

    // GET /v1/news/{id} - получение полной новости
    override suspend fun getNewsItem(id: String): Result<NewsItemDto> {
        delay(networkDelay)
        return Result.success(getMockNewsItem(id))
    }

    // GET /v1/employees/search-by-last-name - поиск сотрудников по фамилии
    override suspend fun searchEmployeesByLastName(
        lastName: String,
        limit: Int?,
        offset: Int?
    ): Result<EmployeeSearchResponse> {
        delay(networkDelay)
        val allEmployees = getMockEmployeesPreviews()
        val filtered = allEmployees.filter {
            it.lastName.contains(lastName, ignoreCase = true)
        }

        val actualOffset = offset ?: 0
        val actualLimit = limit ?: 20
        val paginated = filtered.drop(actualOffset).take(actualLimit)

        return Result.success(
            EmployeeSearchResponse(
                total = filtered.size,
                items = paginated
            )
        )
    }

    // GET /v1/vacations - получение отпусков сотрудника
    override suspend fun getVacations(
        employeeId: String,
        year: Int?
    ): Result<VacationListResponse> {
        delay(networkDelay)
        val allVacations = getMockVacationPeriods()
        val filtered = if (year != null) {
            allVacations.filter { it.startDate.startsWith(year.toString()) }
        } else {
            allVacations
        }

        return Result.success(
            VacationListResponse(
                employeeId = employeeId,
                items = filtered
            )
        )
    }

    // GET /v1/favorites - получение избранных ссылок
    override suspend fun getFavorites(userId: String): Result<FavoriteListResponse> {
        delay(networkDelay)
        if (favoritesStorage.isEmpty()) {
            favoritesStorage.addAll(getDefaultFavoritesNew())
        }
        return Result.success(
            FavoriteListResponse(
                userId = userId,
                items = favoritesStorage.toList()
            )
        )
    }

    // POST /v1/favorites - добавление избранной ссылки
    override suspend fun addFavorite(request: AddFavoriteRequest): Result<FavoriteItemDto> {
        delay(networkDelay)
        val newFavorite = FavoriteItemDto(
            id = generateId("fav"),
            link = request.link,
            description = request.description,
            createdAt = "2025-12-11T10:00:00Z"
        )
        favoritesStorage.add(newFavorite)
        return Result.success(newFavorite)
    }

    // DELETE /v1/favorites/{id} - удаление избранной ссылки
    override suspend fun deleteFavorite(id: String): Result<Unit> {
        delay(networkDelay)
        favoritesStorage.removeAll { it.id == id }
        return Result.success(Unit)
    }

    // GET /v1/achievements - получение ачивок пользователя
    override suspend fun getAchievements(userId: String): Result<AchievementListResponse> {
        delay(networkDelay)
        return Result.success(getMockAchievementsResponse())
    }

    // Deprecated - старые методы для обратной совместимости
    @Deprecated("Use getUser instead")
    override suspend fun getCurrentUser(): Result<UserDto> {
        delay(networkDelay)
        return Result.success(getMockUserDto())
    }

    @Deprecated("Use getNewsList instead")
    override suspend fun getNews(): Result<List<NewsDto>> {
        delay(networkDelay)
        return Result.success(getMockNewsList())
    }

    @Deprecated("Use searchEmployeesByLastName instead")
    override suspend fun searchEmployees(query: String): Result<List<UserDto>> {
        delay(networkDelay)
        val allEmployees = getMockEmployees()
        val filtered = if (query.isEmpty()) {
            allEmployees
        } else {
            allEmployees.filter {
                val firstName = it.personal?.firstName ?: ""
                val lastName = it.personal?.lastName ?: ""
                val position = it.employment?.position ?: ""
                val department = it.employment?.department ?: ""
                firstName.contains(query, ignoreCase = true) ||
                        lastName.contains(query, ignoreCase = true) ||
                        position.contains(query, ignoreCase = true) ||
                        department.contains(query, ignoreCase = true)
            }
        }
        return Result.success(filtered)
    }

    override suspend fun getBirthdays(): Result<List<BirthdayDto>> {
        delay(networkDelay)
        return Result.success(getMockBirthdays())
    }

    override suspend fun getTasks(): Result<List<TaskDto>> {
        delay(networkDelay)
        return Result.success(getMockTasks())
    }

    override suspend fun getCourses(): Result<List<CourseDto>> {
        delay(networkDelay)
        return Result.success(getMockCourses())
    }

    override suspend fun getWorkspaceBookings(date: String): Result<List<WorkspaceBookingDto>> {
        delay(networkDelay)
        return Result.success(getMockWorkspaceBookings())
    }

    override suspend fun getOfficeNews(): Result<List<NewsDto>> {
        delay(networkDelay)
        return Result.success(getMockOfficeNews())
    }

    override suspend fun getMerchItems(): Result<List<MerchItemDto>> {
        delay(networkDelay)
        return Result.success(getMockMerchItems())
    }

    override suspend fun purchaseMerchItem(itemId: String): Result<Unit> {
        delay(networkDelay)
        val item = getMockMerchItems().find { it.id == itemId }
        return if (item != null && userBonusPoints >= item.price) {
            userBonusPoints -= item.price
            Result.success(Unit)
        } else {
            Result.failure(Exception("Недостаточно баллов или товар не найден"))
        }
    }

    // Mock Data Generators for new API

    private fun getMockUserDto(): UserDto {
        return UserDto(
            id = mockUserId,
            status = "active",
            personal = PersonalInfo(
                firstName = "Иван",
                lastName = "Иванов",
                middleName = "Петрович",
                email = "ivan.ivanov@tutu.ru",
                phone = "+7 (999) 123-45-67"
            ),
            employment = EmploymentInfo(
                position = "Senior Android Developer",
                department = "Альфа",
                location = "Глобус Медиа",
                startDate = "2020-01-15"
            ),
            roles = listOf("developer", "mentor"),
            manager = ManagerInfo(
                id = "mgr-1",
                name = "Петр Сидоров",
                email = "petr.sidorov@tutu.ru"
            ),
            externalIds = mapOf("jira" to "JIRA-123", "gitlab" to "12345")
        )
    }

    private fun getMockNewsPreviewList(): List<NewsPreviewDto> {
        return listOf(
            NewsPreviewDto(
                id = "news_1",
                title = "Новый офис в Санкт-Петербурге",
                previewText = "Мы рады сообщить об открытии нашего нового офиса...",
                imageUrl = "https://picsum.photos/400/300?random=1",
                publishedAt = "2024-01-15T10:30:00Z"
            ),
            NewsPreviewDto(
                id = "news_2",
                title = "Tutu Tech Day 2025",
                previewText = "Приглашаем всех на ежегодную техническую конференцию...",
                imageUrl = "https://picsum.photos/400/300?random=2",
                publishedAt = "2024-01-14T14:00:00Z"
            ),
            NewsPreviewDto(
                id = "news_3",
                title = "Обновление корпоративного портала",
                previewText = "Представляем новую версию корпоративного портала...",
                imageUrl = "https://picsum.photos/400/300?random=3",
                publishedAt = "2024-01-13T09:00:00Z"
            ),
            NewsPreviewDto(
                id = "news_4",
                title = "Yoga в офисе",
                previewText = "Каждый четверг в 18:00 - занятия йогой в офисе...",
                imageUrl = "https://picsum.photos/400/300?random=4",
                publishedAt = "2024-01-12T12:00:00Z"
            )
        )
    }

    private fun getMockNewsItem(id: String): NewsItemDto {
        return NewsItemDto(
            id = id,
            title = "Новый офис в Санкт-Петербурге",
            fullText = """
                Мы рады сообщить об открытии нашего нового офиса в Санкт-Петербурге! 
                Современное пространство для работы и творчества.
                
                Офис оборудован всем необходимым для комфортной работы:
                - Современные рабочие места
                - Переговорные комнаты
                - Зоны отдыха
                - Кафетерий
                
                Добро пожаловать!
            """.trimIndent(),
            imageUrl = "https://picsum.photos/800/600?random=1",
            publishedAt = "2024-01-15T10:30:00Z",
            tags = listOf("офис", "новости", "санкт-петербург"),
            author = AuthorDto(
                id = "author-1",
                name = "HR Department"
            )
        )
    }

    private fun getMockEmployeesPreviews(): List<EmployeePreviewDto> {
        return listOf(
            EmployeePreviewDto(
                id = "emp-1",
                firstName = "Мария",
                lastName = "Петрова",
                department = "HR",
                position = "HR Manager",
                avatarUrl = "https://i.pravatar.cc/150?img=5"
            ),
            EmployeePreviewDto(
                id = "emp-2",
                firstName = "Алексей",
                lastName = "Сидоров",
                department = "Backend",
                position = "Senior Backend Developer",
                avatarUrl = "https://i.pravatar.cc/150?img=8"
            ),
            EmployeePreviewDto(
                id = "emp-3",
                firstName = "Елена",
                lastName = "Смирнова",
                department = "Дизайн",
                position = "UI/UX Designer",
                avatarUrl = "https://i.pravatar.cc/150?img=9"
            ),
            EmployeePreviewDto(
                id = "emp-4",
                firstName = "Дмитрий",
                lastName = "Козлов",
                department = "Продукт",
                position = "Product Manager",
                avatarUrl = "https://i.pravatar.cc/150?img=12"
            ),
            EmployeePreviewDto(
                id = "emp-5",
                firstName = "Анна",
                lastName = "Волкова",
                department = "Quality Assurance",
                position = "QA Engineer",
                avatarUrl = "https://i.pravatar.cc/150?img=10"
            )
        )
    }

    private fun getMockVacationPeriods(): List<VacationPeriodDto> {
        return listOf(
            VacationPeriodDto(
                vacationId = "vac-1",
                startDate = "2025-02-10",
                endDate = "2025-02-20",
                type = "paid",
                approvedBy = "mgr-1",
                status = "approved"
            ),
            VacationPeriodDto(
                vacationId = "vac-2",
                startDate = "2024-12-25",
                endDate = "2025-01-08",
                type = "paid",
                approvedBy = "mgr-1",
                status = "approved"
            ),
            VacationPeriodDto(
                vacationId = "vac-3",
                startDate = "2024-08-01",
                endDate = "2024-08-14",
                type = "paid",
                approvedBy = "mgr-1",
                status = "approved"
            )
        )
    }

    private fun getMockAchievementsResponse(): AchievementListResponse {
        return AchievementListResponse(
            userId = mockUserId,
            totalPoints = 150,
            items = listOf(
                AchievementItemDto(
                    id = "ach-1",
                    title = "Первый год в компании",
                    description = "Поздравляем с первым годом работы в Tutu!",
                    points = 50,
                    achievedAt = "2023-06-01T00:00:00Z"
                ),
                AchievementItemDto(
                    id = "ach-2",
                    title = "Code Review Master",
                    description = "Провел 100+ code review",
                    points = 50,
                    achievedAt = "2023-09-15T00:00:00Z"
                ),
                AchievementItemDto(
                    id = "ach-3",
                    title = "Наставник",
                    description = "Помог адаптироваться 3 новым сотрудникам",
                    points = 50,
                    achievedAt = "2023-12-01T00:00:00Z"
                )
            )
        )
    }

    private fun getDefaultFavoritesNew(): List<FavoriteItemDto> {
        return listOf(
            FavoriteItemDto(
                id = "fav-1",
                link = "https://hq.tutu.ru",
                description = "Jira",
                createdAt = "2025-01-01T10:00:00Z"
            ),
            FavoriteItemDto(
                id = "fav-2",
                link = "https://dom.tutu.ru",
                description = "Confluence",
                createdAt = "2025-01-01T10:00:00Z"
            )
        )
    }

    // Old mock data generators for backward compatibility

    private fun getMockNewsList(): List<NewsDto> {
        return listOf(
            NewsDto(
                id = "news_1",
                title = "Новый офис в Санкт-Петербурге",
                content = "Мы рады сообщить об открытии нашего нового офиса в Санкт-Петербурге! Современное пространство для работы и творчества.",
                imageUrl = "https://picsum.photos/400/300?random=1",
                publishedAt = "2024-01-15T10:30:00Z",
                category = NewsCategoryDto.COMPANY
            ),
            NewsDto(
                id = "news_2",
                title = "Tutu Tech Day 2025",
                content = "Приглашаем всех на ежегодную техническую конференцию! Доклады, workshops и networking.",
                imageUrl = "https://picsum.photos/400/300?random=2",
                publishedAt = "2024-01-14T14:00:00Z",
                category = NewsCategoryDto.EVENTS
            ),
            NewsDto(
                id = "news_3",
                title = "Обновление корпоративного портала",
                content = "Представляем новую версию корпоративного портала с улучшенным интерфейсом и новыми функциями.",
                imageUrl = "https://picsum.photos/400/300?random=3",
                publishedAt = "2024-01-13T09:00:00Z",
                category = NewsCategoryDto.COMPANY
            ),
            NewsDto(
                id = "news_4",
                title = "Yoga в офисе",
                content = "Каждый четверг в 18:00 - занятия йогой в офисе. Присоединяйтесь!",
                imageUrl = "https://picsum.photos/400/300?random=4",
                publishedAt = "2024-01-12T12:00:00Z",
                category = NewsCategoryDto.OFFICE
            )
        )
    }

    private fun getMockBirthdays(): List<BirthdayDto> {
        return listOf(
            BirthdayDto(
                employeeId = "emp_1",
                employeeName = "Мария Петрова",
                date = "15.01",
                department = "HR",
                avatarUrl = "https://i.pravatar.cc/150?img=5"
            ),
            BirthdayDto(
                employeeId = "emp_2",
                employeeName = "Алексей Сидоров",
                date = "16.01",
                department = "Backend",
                avatarUrl = "https://i.pravatar.cc/150?img=8"
            ),
            BirthdayDto(
                employeeId = "emp_3",
                employeeName = "Елена Смирнова",
                date = "18.01",
                department = "Дизайн",
                avatarUrl = "https://i.pravatar.cc/150?img=9"
            )
        )
    }

    private fun getMockEmployees(): List<UserDto> {
        return listOf(
            UserDto(
                id = "emp_1",
                status = "active",
                personal = PersonalInfo(
                    firstName = "Мария",
                    lastName = "Петрова",
                    email = "maria.petrova@tutu.ru"
                ),
                employment = EmploymentInfo(
                    position = "HR Manager",
                    department = "HR"
                )
            ),
            UserDto(
                id = "emp_2",
                status = "active",
                personal = PersonalInfo(
                    firstName = "Алексей",
                    lastName = "Сидоров",
                    email = "alexey.sidorov@tutu.ru"
                ),
                employment = EmploymentInfo(
                    position = "Senior Backend Developer",
                    department = "Backend"
                )
            ),
            UserDto(
                id = "emp_3",
                status = "active",
                personal = PersonalInfo(
                    firstName = "Елена",
                    lastName = "Смирнова",
                    email = "elena.smirnova@tutu.ru"
                ),
                employment = EmploymentInfo(
                    position = "UI/UX Designer",
                    department = "Дизайн"
                )
            ),
            UserDto(
                id = "emp_4",
                status = "active",
                personal = PersonalInfo(
                    firstName = "Дмитрий",
                    lastName = "Козлов",
                    email = "dmitry.kozlov@tutu.ru"
                ),
                employment = EmploymentInfo(
                    position = "Product Manager",
                    department = "Продукт"
                )
            ),
            UserDto(
                id = "emp_5",
                status = "active",
                personal = PersonalInfo(
                    firstName = "Анна",
                    lastName = "Волкова",
                    email = "anna.volkova@tutu.ru"
                ),
                employment = EmploymentInfo(
                    position = "QA Engineer",
                    department = "Quality Assurance"
                )
            )
        )
    }

    private fun getMockAchievements(): List<AchievementDto> {
        return listOf(
            AchievementDto(
                id = "ach_1",
                title = "Первый год в компании",
                description = "Поздравляем с первым годом работы в Tutu!",
                iconUrl = "celebration",
                earnedAt = "2023-06-01T00:00:00Z"
            ),
            AchievementDto(
                id = "ach_2",
                title = "Code Review Master",
                description = "Провел 100+ code review",
                iconUrl = "code",
                earnedAt = "2023-09-15T00:00:00Z"
            ),
            AchievementDto(
                id = "ach_3",
                title = "Наставник",
                description = "Помог адаптироваться 3 новым сотрудникам",
                iconUrl = "school",
                earnedAt = "2023-12-01T00:00:00Z"
            )
        )
    }

    private fun getMockTasks(): List<TaskDto> {
        return listOf(
            TaskDto(
                id = "task_1",
                title = "Провести 1-on-1 с менеджером",
                description = "Обсудить цели на квартал и текущие проекты",
                status = TaskStatusDto.TODO,
                dueDate = "2024-01-20T00:00:00Z"
            ),
            TaskDto(
                id = "task_2",
                title = "Заполнить отчет по проекту",
                description = "Подвести итоги спринта и описать результаты",
                status = TaskStatusDto.IN_PROGRESS,
                dueDate = "2024-01-18T00:00:00Z"
            ),
            TaskDto(
                id = "task_3",
                title = "Пройти курс по Kotlin Coroutines",
                description = "Завершить все модули и получить сертификат",
                status = TaskStatusDto.IN_PROGRESS,
                dueDate = "2024-01-25T00:00:00Z"
            ),
            TaskDto(
                id = "task_4",
                title = "Обновить профиль в корпоративной системе",
                description = "Добавить информацию о навыках и проектах",
                status = TaskStatusDto.DONE,
                dueDate = "2024-01-10T00:00:00Z"
            )
        )
    }

    private fun getMockVacations(): List<VacationDto> {
        return listOf(
            VacationDto(
                id = "vac_1",
                startDate = "2024-02-10",
                endDate = "2024-02-20",
                daysCount = 10,
                status = VacationStatusDto.PLANNED,
                reason = "Отпуск"
            ),
            VacationDto(
                id = "vac_2",
                startDate = "2023-12-25",
                endDate = "2024-01-08",
                daysCount = 14,
                status = VacationStatusDto.APPROVED,
                reason = "Новогодние праздники"
            ),
            VacationDto(
                id = "vac_3",
                startDate = "2023-08-01",
                endDate = "2023-08-14",
                daysCount = 14,
                status = VacationStatusDto.APPROVED,
                reason = "Летний отпуск"
            )
        )
    }

    private fun getMockCourses(): List<CourseDto> {
        return listOf(
            CourseDto(
                id = "course_1",
                title = "Kotlin Coroutines и Flow",
                description = "Глубокое погружение в асинхронное программирование на Kotlin",
                duration = "8 часов",
                imageUrl = "https://picsum.photos/200/150?random=10",
                progress = 65,
                isCompleted = false
            ),
            CourseDto(
                id = "course_2",
                title = "Clean Architecture в Android",
                description = "Принципы построения масштабируемых приложений",
                duration = "12 часов",
                imageUrl = "https://picsum.photos/200/150?random=11",
                progress = 100,
                isCompleted = true
            ),
            CourseDto(
                id = "course_3",
                title = "Jetpack Compose Advanced",
                description = "Продвинутые техники работы с Compose",
                duration = "10 часов",
                imageUrl = "https://picsum.photos/200/150?random=12",
                progress = 30,
                isCompleted = false
            ),
            CourseDto(
                id = "course_4",
                title = "Soft Skills для разработчиков",
                description = "Коммуникация, презентация идей, работа в команде",
                duration = "6 часов",
                imageUrl = "https://picsum.photos/200/150?random=13",
                progress = 0,
                isCompleted = false
            )
        )
    }

    private fun getMockWorkspaceBookings(): List<WorkspaceBookingDto> {
        return listOf(
            WorkspaceBookingDto(
                id = "ws_1",
                workspaceNumber = "A-15",
                date = "2024-01-16",
                isBooked = true,
                bookedBy = "Иван Иванов",
                floor = 1
            ),
            WorkspaceBookingDto(
                id = "ws_2",
                workspaceNumber = "A-16",
                date = "2024-01-16",
                isBooked = false,
                bookedBy = null,
                floor = 1
            ),
            WorkspaceBookingDto(
                id = "ws_3",
                workspaceNumber = "B-23",
                date = "2024-01-16",
                isBooked = true,
                bookedBy = "Мария Петрова",
                floor = 2
            ),
            WorkspaceBookingDto(
                id = "ws_4",
                workspaceNumber = "B-24",
                date = "2024-01-16",
                isBooked = false,
                bookedBy = null,
                floor = 2
            ),
            WorkspaceBookingDto(
                id = "ws_5",
                workspaceNumber = "C-10",
                date = "2024-01-16",
                isBooked = false,
                bookedBy = null,
                floor = 3
            )
        )
    }

    private fun getMockOfficeNews(): List<NewsDto> {
        return listOf(
            NewsDto(
                id = "office_1",
                title = "Обновление кофейных точек",
                content = "На первом этаже установлена новая кофемашина с большим выбором напитков!",
                imageUrl = "https://picsum.photos/400/300?random=20",
                publishedAt = "2024-01-15T08:00:00Z",
                category = NewsCategoryDto.OFFICE
            ),
            NewsDto(
                id = "office_2",
                title = "Настольный теннис по средам",
                content = "Каждую среду в 17:00 - турнир по настольному теннису. Регистрация в Slack.",
                imageUrl = "https://picsum.photos/400/300?random=21",
                publishedAt = "2024-01-14T10:00:00Z",
                category = NewsCategoryDto.OFFICE
            ),
            NewsDto(
                id = "office_3",
                title = "Книжный клуб",
                content = "Присоединяйтесь к книжному клубу! Следующая встреча - обсуждение 'Clean Code'.",
                imageUrl = "https://picsum.photos/400/300?random=22",
                publishedAt = "2024-01-13T15:00:00Z",
                category = NewsCategoryDto.OFFICE
            )
        )
    }

    private fun getMockMerchItems(): List<MerchItemDto> {
        return listOf(
            MerchItemDto(
                id = "merch_1",
                name = "Футболка Tutu",
                description = "Хлопковая футболка с логотипом компании. Доступны размеры S-XXL.",
                price = 100,
                imageUrl = "https://picsum.photos/300/300?random=30",
                category = MerchCategoryDto.CLOTHING,
                inStock = true
            ),
            MerchItemDto(
                id = "merch_2",
                name = "Худи Tutu Tech",
                description = "Теплое худи с капюшоном для прохладной погоды.",
                price = 250,
                imageUrl = "https://picsum.photos/300/300?random=31",
                category = MerchCategoryDto.CLOTHING,
                inStock = true
            ),
            MerchItemDto(
                id = "merch_3",
                name = "Рюкзак для ноутбука",
                description = "Вместительный рюкзак с отделением для ноутбука 15''.",
                price = 300,
                imageUrl = "https://picsum.photos/300/300?random=32",
                category = MerchCategoryDto.ACCESSORIES,
                inStock = true
            ),
            MerchItemDto(
                id = "merch_4",
                name = "Термокружка",
                description = "Стильная термокружка объемом 350мл, сохраняет температуру 6 часов.",
                price = 150,
                imageUrl = "https://picsum.photos/300/300?random=33",
                category = MerchCategoryDto.ACCESSORIES,
                inStock = true
            ),
            MerchItemDto(
                id = "merch_5",
                name = "Блокнот А5",
                description = "Качественный блокнот в линейку, 120 листов.",
                price = 50,
                imageUrl = "https://picsum.photos/300/300?random=34",
                category = MerchCategoryDto.STATIONERY,
                inStock = true
            ),
            MerchItemDto(
                id = "merch_6",
                name = "Набор стикеров",
                description = "Яркие стикеры с символикой компании, 50 штук.",
                price = 30,
                imageUrl = "https://picsum.photos/300/300?random=35",
                category = MerchCategoryDto.STATIONERY,
                inStock = true
            ),
            MerchItemDto(
                id = "merch_7",
                name = "Беспроводная мышь",
                description = "Эргономичная беспроводная мышь с бесшумными кнопками.",
                price = 200,
                imageUrl = "https://picsum.photos/300/300?random=36",
                category = MerchCategoryDto.ELECTRONICS,
                inStock = false
            ),
            MerchItemDto(
                id = "merch_8",
                name = "USB флешка 64GB",
                description = "Скоростная USB 3.0 флешка с логотипом Tutu.",
                price = 120,
                imageUrl = "https://picsum.photos/300/300?random=37",
                category = MerchCategoryDto.ELECTRONICS,
                inStock = true
            )
        )
    }

    private fun getDefaultFavorites(): List<FavoriteCardDto> {
        return listOf(
            FavoriteCardDto(
                id = "fav_1",
                title = "Jira",
                url = "https://jira.tutu.ru",
                iconUrl = null
            ),
            FavoriteCardDto(
                id = "fav_2",
                title = "Confluence",
                url = "https://confluence.tutu.ru",
                iconUrl = null
            ),
            FavoriteCardDto(
                id = "fav_3",
                title = "GitLab",
                url = "https://gitlab.tutu.ru",
                iconUrl = null
            ),
            FavoriteCardDto(
                id = "fav_4",
                title = "Корпоративный портал",
                url = "https://portal.tutu.ru",
                iconUrl = null
            )
        )
    }
}
