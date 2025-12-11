# ✅ Чеклист настройки Mock API

## Первый запуск

### 1. Проверьте настройку

Откройте `composeApp/src/commonMain/kotlin/ru/tutu/tutuemployee/di/NetworkModule.kt`:

```kotlin
const val USE_MOCK_API = true  // ✅ Должно быть true
```

### 2. Запустите приложение

```bash
# Android
./gradlew :composeApp:assembleDebug

# Web
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

### 3. Проверьте работу

- [ ] Авторизация работает с любым логином/паролем (непустые значения)
- [ ] На главной отображаются 4 новости
- [ ] В профиле есть достижения, задачи, отпуска, курсы
- [ ] В офисе видны рабочие места
- [ ] В магазине 8 товаров мерча
- [ ] В избранном 4 карточки по умолчанию

## Разработка новой фичи

### 1. Добавьте метод в интерфейс

```kotlin
// ApiService.kt
interface ApiService {
    suspend fun getNewFeature(): Result<List<FeatureDto>>
}
```

### 2. Реализуйте в Mock

```kotlin
// MockApiService.kt
override suspend fun getNewFeature(): Result<List<FeatureDto>> {
    delay(networkDelay)
    return Result.success(getMockFeatures())
}

private fun getMockFeatures(): List<FeatureDto> {
    return listOf(
        FeatureDto(id = "1", name = "Feature 1"),
        FeatureDto(id = "2", name = "Feature 2")
    )
}
```

### 3. Реализуйте в Real API

```kotlin
// ApiServiceImpl.kt
override suspend fun getNewFeature(): Result<List<FeatureDto>> {
    return try {
        val response = httpClient.get("/features")
        Result.success(response.body<List<FeatureDto>>())
    } catch (e: Exception) {
        Result.failure(e)
    }
}
```

### 4. Используйте в DataSource

```kotlin
class FeatureRemoteDataSource(
    private val apiService: ApiService
) {
    suspend fun getFeatures() = apiService.getNewFeature()
}
```

## Настройка данных

### Изменить текущего пользователя

```kotlin
// MockApiService.kt - метод getMockUser()
private fun getMockUser(): UserDto {
    return UserDto(
        firstName = "Ваше",      // ← Измените
        lastName = "Имя",         // ← Измените
        position = "Должность",   // ← Измените
        bonusPoints = 1000,       // ← Измените
        // ...
    )
}
```

### Добавить больше новостей

```kotlin
// MockApiService.kt - метод getMockNewsList()
private fun getMockNewsList(): List<NewsDto> {
    return listOf(
        // ... существующие новости
        NewsDto(
            id = "news_5",                    // ← Новая новость
            title = "Ваша новость",
            content = "Описание",
            publishedAt = "2024-01-16T10:00:00Z",
            category = NewsCategoryDto.COMPANY
        )
    )
}
```

### Добавить больше сотрудников

```kotlin
// MockApiService.kt - метод getMockEmployees()
private fun getMockEmployees(): List<UserDto> {
    return listOf(
        // ... существующие сотрудники
        UserDto(
            id = "emp_6",                     // ← Новый сотрудник
            firstName = "Новый",
            lastName = "Сотрудник",
            position = "Должность",
            department = "Отдел",
            // ...
        )
    )
}
```

### Изменить задержку сети

```kotlin
// MockApiService.kt - в начале класса
private val networkDelay = 100L  // Было: 500L (быстрее для тестирования)
// или
private val networkDelay = 0L    // Без задержки (мгновенно)
```

## Тестирование разных сценариев

### Ошибка при авторизации

```kotlin
// MockApiService.kt - метод login()
override suspend fun login(username: String, password: String): Result<AuthResponse> {
    delay(networkDelay)
    
    // Вариант 1: всегда успех (текущее поведение)
    return if (username.isNotEmpty() && password.isNotEmpty()) {
        Result.success(...)
    } else {
        Result.failure(Exception("Неверный логин или пароль"))
    }
    
    // Вариант 2: всегда ошибка (для тестирования ошибки)
    // return Result.failure(Exception("Сервер недоступен"))
    
    // Вариант 3: ошибка для конкретного пользователя
    // if (username == "error") {
    //     return Result.failure(Exception("Пользователь заблокирован"))
    // }
}
```

### Недостаточно баллов для покупки

```kotlin
// MockApiService.kt - в начале класса
private var userBonusPoints = 50  // Было: 500 (для тестирования нехватки баллов)
```

### Нет доступных рабочих мест

```kotlin
// MockApiService.kt - метод getMockWorkspaceBookings()
private fun getMockWorkspaceBookings(): List<WorkspaceBookingDto> {
    return listOf(
        WorkspaceBookingDto(
            id = "ws_1",
            workspaceNumber = "A-15",
            date = "2024-01-16",
            isBooked = true,              // ← Все заняты
            bookedBy = "Иван Иванов",
            floor = 1
        ),
        // ... все с isBooked = true
    )
}
```

### Пустые списки (для тестирования Empty State)

```kotlin
// Новости
private fun getMockNewsList(): List<NewsDto> = emptyList()

// Достижения
private fun getMockAchievements(): List<AchievementDto> = emptyList()

// И т.д.
```

## Переход на реальный API

### 1. Убедитесь, что backend готов

- [ ] Backend доступен по URL
- [ ] API endpoints совпадают с мок API
- [ ] DTO модели совпадают с backend

### 2. Отключите моки

```kotlin
// NetworkModule.kt
const val USE_MOCK_API = false  // ← Измените на false
```

### 3. Настройте URL

```kotlin
// NetworkModule.kt
install(DefaultRequest) {
    url("https://api.tutu.ru/employee")  // ← Реальный URL
}
```

### 4. Протестируйте

- [ ] Авторизация работает с реальными credentials
- [ ] Все endpoints отвечают
- [ ] Данные корректно маппятся из DTO в Domain
- [ ] Ошибки обрабатываются правильно

## Troubleshooting

### Проблема: Compilation error в MockApiService

**Решение**: Проверьте, что все методы интерфейса `ApiService` реализованы в `MockApiService`

### Проблема: Приложение не запускается

**Решение**:

1. Проверьте `USE_MOCK_API = true` в `NetworkModule.kt`
2. Выполните `./gradlew clean`
3. Пересоберите проект

### Проблема: Данные не отображаются

**Решение**:

1. Проверьте логи (должна быть задержка ~500ms)
2. Убедитесь, что методы в MockApiService возвращают данные
3. Проверьте маппинг DTO → Domain

### Проблема: "Unresolved reference" ошибки

**Решение**: Проверьте импорты:

```kotlin
import ru.tutu.tutuemployee.data.remote.dto.*
```

## Полезные команды

```bash
# Очистить кеш
./gradlew clean

# Собрать проект
./gradlew build

# Запустить только компиляцию
./gradlew :composeApp:compileDebugKotlinAndroid

# Запустить Web версию
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

## Дополнительная документация

- 📚 [MOCK_API_GUIDE.md](./MOCK_API_GUIDE.md) - Полная документация
- 🇷🇺 [MOКИ_API.md](./MOКИ_API.md) - Краткое руководство
- 🚀 [API_MOCKS_README.md](./API_MOCKS_README.md) - Быстрый старт
- 📊 [API_MODELS.md](./API_MODELS.md) - Модели данных

## Примеры кода

См. [ApiServiceExample.kt](./composeApp/src/commonMain/kotlin/ru/tutu/tutuemployee/data/remote/api/ApiServiceExample.kt)

---

✅ **Чеклист завершен!** Теперь вы готовы работать с Mock API
