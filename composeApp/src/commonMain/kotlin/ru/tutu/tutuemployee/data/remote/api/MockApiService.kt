package ru.tutu.tutuemployee.data.remote.api

import kotlinx.coroutines.delay
import ru.tutu.tutuemployee.data.remote.dto.*

/**
 * Mock API Service для тестирования и разработки UI
 * Возвращает тестовые данные без реальных сетевых запросов
 */
class MockApiService : ApiService {

    // Симуляция задержки сети (можно настроить)
    private val networkDelay = 500L

    // Хранилище для избранного
    private val favoritesStorage = mutableListOf<FavoriteCardDto>()

    // Хранилище для мерча
    private var userBonusPoints = 500

    // Счетчик для генерации ID
    private var idCounter = 0

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
                    user = getMockUser()
                )
            )
        } else {
            Result.failure(Exception("Неверный логин или пароль"))
        }
    }

    override suspend fun getCurrentUser(): Result<UserDto> {
        delay(networkDelay)
        return Result.success(getMockUser())
    }

    override suspend fun getNews(): Result<List<NewsDto>> {
        delay(networkDelay)
        return Result.success(getMockNewsList())
    }

    override suspend fun getBirthdays(): Result<List<BirthdayDto>> {
        delay(networkDelay)
        return Result.success(getMockBirthdays())
    }

    override suspend fun searchEmployees(query: String): Result<List<UserDto>> {
        delay(networkDelay)
        val allEmployees = getMockEmployees()
        val filtered = if (query.isEmpty()) {
            allEmployees
        } else {
            allEmployees.filter {
                it.firstName.contains(query, ignoreCase = true) ||
                        it.lastName.contains(query, ignoreCase = true) ||
                        it.position.contains(query, ignoreCase = true) ||
                        it.department.contains(query, ignoreCase = true)
            }
        }
        return Result.success(filtered)
    }

    override suspend fun getAchievements(): Result<List<AchievementDto>> {
        delay(networkDelay)
        return Result.success(getMockAchievements())
    }

    override suspend fun getTasks(): Result<List<TaskDto>> {
        delay(networkDelay)
        return Result.success(getMockTasks())
    }

    override suspend fun getVacations(): Result<List<VacationDto>> {
        delay(networkDelay)
        return Result.success(getMockVacations())
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

    override suspend fun getFavorites(): Result<List<FavoriteCardDto>> {
        delay(networkDelay)
        if (favoritesStorage.isEmpty()) {
            favoritesStorage.addAll(getDefaultFavorites())
        }
        return Result.success(favoritesStorage.toList())
    }

    override suspend fun addFavorite(title: String, url: String): Result<FavoriteCardDto> {
        delay(networkDelay)
        val newFavorite = FavoriteCardDto(
            id = generateId("fav"),
            title = title,
            url = url,
            iconUrl = null
        )
        favoritesStorage.add(newFavorite)
        return Result.success(newFavorite)
    }

    override suspend fun deleteFavorite(id: String): Result<Unit> {
        delay(networkDelay)
        favoritesStorage.removeAll { it.id == id }
        return Result.success(Unit)
    }

    // Mock Data Generators

    private fun getMockUser(): UserDto {
        return UserDto(
            id = "user_1",
            username = "ivan.ivanov",
            firstName = "Иван",
            lastName = "Иванов",
            position = "Senior Android Developer",
            department = "Мобильная разработка",
            legalEntity = "ООО Туту",
            email = "ivan.ivanov@tutu.ru",
            avatarUrl = "https://i.pravatar.cc/300?img=1",
            availableVacationDays = 14,
            bonusPoints = userBonusPoints
        )
    }

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
                title = "Tutu Tech Day 2024",
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
                username = "maria.petrova",
                firstName = "Мария",
                lastName = "Петрова",
                position = "HR Manager",
                department = "HR",
                legalEntity = "ООО Туту",
                email = "maria.petrova@tutu.ru",
                avatarUrl = "https://i.pravatar.cc/150?img=5",
                availableVacationDays = 20,
                bonusPoints = 300
            ),
            UserDto(
                id = "emp_2",
                username = "alexey.sidorov",
                firstName = "Алексей",
                lastName = "Сидоров",
                position = "Senior Backend Developer",
                department = "Backend",
                legalEntity = "ООО Туту",
                email = "alexey.sidorov@tutu.ru",
                avatarUrl = "https://i.pravatar.cc/150?img=8",
                availableVacationDays = 15,
                bonusPoints = 450
            ),
            UserDto(
                id = "emp_3",
                username = "elena.smirnova",
                firstName = "Елена",
                lastName = "Смирнова",
                position = "UI/UX Designer",
                department = "Дизайн",
                legalEntity = "ООО Туту",
                email = "elena.smirnova@tutu.ru",
                avatarUrl = "https://i.pravatar.cc/150?img=9",
                availableVacationDays = 18,
                bonusPoints = 200
            ),
            UserDto(
                id = "emp_4",
                username = "dmitry.kozlov",
                firstName = "Дмитрий",
                lastName = "Козлов",
                position = "Product Manager",
                department = "Продукт",
                legalEntity = "ООО Туту",
                email = "dmitry.kozlov@tutu.ru",
                avatarUrl = "https://i.pravatar.cc/150?img=12",
                availableVacationDays = 10,
                bonusPoints = 600
            ),
            UserDto(
                id = "emp_5",
                username = "anna.volkova",
                firstName = "Анна",
                lastName = "Волкова",
                position = "QA Engineer",
                department = "Quality Assurance",
                legalEntity = "ООО Туту",
                email = "anna.volkova@tutu.ru",
                avatarUrl = "https://i.pravatar.cc/150?img=10",
                availableVacationDays = 22,
                bonusPoints = 350
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
