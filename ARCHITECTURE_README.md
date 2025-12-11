# TutuEmployee - Clean Architecture

> Корпоративное мобильное приложение для сотрудников Tutu.ru

## 🎯 О проекте

TutuEmployee - это Kotlin Multiplatform приложение, построенное на принципах **Clean Architecture**
с использованием **Jetpack Compose Multiplatform**.

### Поддерживаемые платформы

- ✅ Android
- ✅ iOS
- ✅ Web (JS/WASM)

## 🏗️ Архитектура

Проект следует принципам **Clean Architecture** с четким разделением на слои:

```
┌─────────────────────┐
│   Presentation      │  ← UI, ViewModels
├─────────────────────┤
│      Domain         │  ← Business Logic
├─────────────────────┤
│       Data          │  ← Data Sources, API
└─────────────────────┘
```

### Подробная структура

См. полную документацию:

- 📖 [CLEAN_ARCHITECTURE_STRUCTURE.md](CLEAN_ARCHITECTURE_STRUCTURE.md) - Подробное описание
  архитектуры
- 📊 [ARCHITECTURE_DIAGRAM.md](ARCHITECTURE_DIAGRAM.md) - Визуальные диаграммы
- 📝 [REFACTORING_SUMMARY.md](REFACTORING_SUMMARY.md) - Итоги рефакторинга

## 🛠️ Технологии

### Core

- **Kotlin Multiplatform** - Общий код для всех платформ
- **Compose Multiplatform** - Декларативный UI
- **Coroutines** - Асинхронность
- **Flow** - Reactive streams

### Networking

- **Ktor Client** - HTTP клиент
- **Kotlinx Serialization** - JSON сериализация

### Dependency Injection

- **Koin** - DI фреймворк

### Architecture Components

- **ViewModel** - Управление состоянием UI
- **StateFlow** - Reactive state management
- **Navigation Compose** - Навигация

## 📦 Модули

### Presentation Layer (`presentation/`)

UI компоненты и ViewModels:

```
presentation/
├── auth/           - Авторизация
├── home/           - Главный экран
├── profile/        - Профиль пользователя
├── office/         - Офис и бронирование
├── merch/          - Магазин мерча
├── favorites/      - Избранное
├── components/     - Переиспользуемые компоненты
└── navigation/     - Навигация
```

**Особенности:**

- ✅ Использует только Domain модели
- ✅ ViewModels инжектируются через Koin
- ✅ Reactive UI через StateFlow
- ✅ Никакой бизнес-логики в UI

### Domain Layer (`domain/`)

Бизнес-логика приложения:

```
domain/
├── model/          - Бизнес-модели
├── repository/     - Интерфейсы репозиториев
├── usecase/        - Use Cases
│   ├── auth/
│   ├── profile/
│   ├── news/
│   └── employee/
└── common/         - Общие классы
    ├── DomainException.kt
    └── Result.kt
```

**Особенности:**

- ✅ Не зависит от других слоев
- ✅ Чистый Kotlin код
- ✅ Легко тестируется
- ✅ Определяет контракты для Data слоя

### Data Layer (`data/`)

Работа с данными:

```
data/
├── repository/     - Реализации репозиториев
└── remote/
    ├── api/        - HTTP клиент
    ├── dto/        - Data Transfer Objects
    └── datasource/ - Абстракция над API
```

**Особенности:**

- ✅ Реализует интерфейсы Domain слоя
- ✅ Преобразует DTO в Domain модели
- ✅ Абстрагирует источники данных
- ✅ Обрабатывает сетевые запросы

### DI Module (`di/`)

Dependency Injection через Koin:

```
di/
├── AppModule.kt           - Главный модуль
├── NetworkModule.kt       - HTTP клиент
├── DataSourceModule.kt    - Data sources
├── RepositoryModule.kt    - Репозитории
├── UseCaseModule.kt       - Use cases
└── ViewModelModule.kt     - ViewModels
```

## 🚀 Быстрый старт

### Требования

- JDK 11 или выше
- Android Studio (для Android)
- Xcode (для iOS)

### Клонирование

```bash
git clone https://github.com/your-repo/TutuEmployee.git
cd TutuEmployee
```

### Запуск Android

```bash
./gradlew :composeApp:assembleDebug
```

Или откройте проект в Android Studio и запустите `composeApp` конфигурацию.

### Запуск iOS

```bash
cd iosApp
pod install
open iosApp.xcworkspace
```

### Запуск Web

```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

## 📝 Структура кода

### Пример: Feature Authentication

```kotlin
// 1. Domain Model (domain/model/)
data class User(
    val id: String,
    val username: String,
    val firstName: String,
    val lastName: String
)

// 2. Repository Interface (domain/repository/)
interface AuthRepository {
    suspend fun login(username: String, password: String): Result<User>
}

// 3. Use Case (domain/usecase/)
class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        username: String, 
        password: String
    ): Result<User> {
        // Validation
        if (username.isBlank()) {
            return Result.failure(ValidationException("Username is required"))
        }
        
        return authRepository.login(username, password)
    }
}

// 4. DTO (data/remote/dto/)
@Serializable
data class UserDto(
    val id: String,
    val username: String,
    val firstName: String,
    val lastName: String
)

fun UserDto.toDomain() = User(
    id = id,
    username = username,
    firstName = firstName,
    lastName = lastName
)

// 5. Data Source (data/remote/datasource/)
interface AuthRemoteDataSource {
    suspend fun login(username: String, password: String): Result<AuthResponse>
}

class AuthRemoteDataSourceImpl(
    private val apiService: ApiService
) : AuthRemoteDataSource {
    override suspend fun login(
        username: String, 
        password: String
    ): Result<AuthResponse> {
        return apiService.login(username, password)
    }
}

// 6. Repository Implementation (data/repository/)
class AuthRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource,
    private val tokenStorage: TokenStorage
) : AuthRepository {
    override suspend fun login(
        username: String, 
        password: String
    ): Result<User> {
        return remoteDataSource.login(username, password)
            .mapCatching { response ->
                tokenStorage.saveToken(response.token)
                response.user.toDomain()
            }
    }
}

// 7. ViewModel (presentation/auth/)
class AuthViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    fun login() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            loginUseCase(username, password)
                .onSuccess { user -> /* ... */ }
                .onFailure { error -> /* ... */ }
        }
    }
}

// 8. Screen (presentation/auth/)
@Composable
fun AuthScreen(
    viewModel: AuthViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // UI code...
}

// 9. DI Configuration (di/)
val useCaseModule = module {
    factoryOf(::LoginUseCase)
}

val viewModelModule = module {
    viewModelOf(::AuthViewModel)
}
```

## 🧪 Тестирование

### Unit Tests

```kotlin
// Domain Layer
class LoginUseCaseTest {
    @Test
    fun `login with empty username returns failure`() = runTest {
        val mockRepository = mockk<AuthRepository>()
        val useCase = LoginUseCase(mockRepository)
        
        val result = useCase("", "password")
        
        assertTrue(result.isFailure)
    }
}

// ViewModel
class AuthViewModelTest {
    @Test
    fun `login updates state correctly`() = runTest {
        val mockUseCase = mockk<LoginUseCase>()
        val viewModel = AuthViewModel(mockUseCase)
        
        // Test implementation
    }
}
```

## 📚 Документация

### Основная документация

- [CLEAN_ARCHITECTURE_STRUCTURE.md](CLEAN_ARCHITECTURE_STRUCTURE.md) - Полное описание архитектуры (
  700+ строк)
- [ARCHITECTURE_DIAGRAM.md](ARCHITECTURE_DIAGRAM.md) - Визуальные диаграммы и схемы
- [REFACTORING_SUMMARY.md](REFACTORING_SUMMARY.md) - История рефакторинга

### Дополнительная документация

- [KOIN_MIGRATION_GUIDE.md](KOIN_MIGRATION_GUIDE.md) - Руководство по Koin DI
- [API_MODELS.md](API_MODELS.md) - Описание моделей API
- [QUICK_START.md](QUICK_START.md) - Быстрый старт для разработчиков

## 🔧 Конфигурация

### Gradle Версии

См. `gradle/libs.versions.toml`:

```toml
[versions]
kotlin = "2.2.21"
compose = "1.9.3"
ktor = "3.1.1"
koin = "4.0.0"
```

### API Endpoints

Настройка в `NetworkModule.kt`:

```kotlin
install(DefaultRequest) {
    url("https://api.tutu.ru/employee")
}
```

## 🤝 Contribution

### Добавление новой функции

1. **Domain Layer**: Создайте модель, repository interface, use case
2. **Data Layer**: Создайте DTO, data source, repository impl
3. **Presentation Layer**: Создайте ViewModel, Screen
4. **DI**: Зарегистрируйте компоненты в модулях

### Code Style

- Используйте Kotlin conventions
- Документируйте публичные API
- Пишите тесты для бизнес-логики
- Следуйте SOLID принципам

## 📄 Лицензия

Copyright © 2024 Tutu.ru

## 👥 Команда

- Архитектура: Clean Architecture
- UI: Jetpack Compose Multiplatform
- Backend: REST API

## 🎯 Roadmap

- [ ] Unit тесты для всех слоев
- [ ] UI тесты
- [ ] Offline режим (Local Data Sources)
- [ ] Push уведомления
- [ ] Deep links
- [ ] Аналитика

## 📞 Контакты

- Slack: #tutu-employee
- Email: dev@tutu.ru

---

**Создано с ❤️ командой Tutu.ru**
