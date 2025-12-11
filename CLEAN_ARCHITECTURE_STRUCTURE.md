# Clean Architecture в TutuEmployee

## 📋 Оглавление

1. [Обзор архитектуры](#обзор-архитектуры)
2. [Структура проекта](#структура-проекта)
3. [Слои приложения](#слои-приложения)
4. [Dependency Injection](#dependency-injection)
5. [Потоки данных](#потоки-данных)
6. [Лучшие практики](#лучшие-практики)

## 🏗️ Обзор архитектуры

Проект TutuEmployee построен по принципам **Clean Architecture** с четким разделением на слои:

```
┌─────────────────────────────────────────┐
│         Presentation Layer              │
│  (UI, ViewModels, Compose Screens)      │
└────────────┬────────────────────────────┘
             │
             ↓
┌─────────────────────────────────────────┐
│          Domain Layer                   │
│  (Use Cases, Domain Models, Repository  │
│   Interfaces, Business Logic)           │
└────────────┬────────────────────────────┘
             │
             ↓
┌─────────────────────────────────────────┐
│           Data Layer                    │
│  (Repository Implementations, Data      │
│   Sources, DTO, Mappers)                │
└─────────────────────────────────────────┘
```

### Ключевые принципы

1. **Dependency Rule**: Зависимости направлены только внутрь (к Domain слою)
2. **Separation of Concerns**: Каждый слой отвечает за свою функциональность
3. **Testability**: Легкое тестирование благодаря инверсии зависимостей
4. **Scalability**: Простое добавление новых функций

## 📁 Структура проекта

```
composeApp/src/commonMain/kotlin/ru/tutu/tutuemployee/
│
├── presentation/               # Presentation Layer
│   ├── auth/                  # Экран авторизации
│   │   ├── AuthScreen.kt      # UI компонент
│   │   └── AuthViewModel.kt   # ViewModel
│   ├── home/                  # Главный экран
│   ├── profile/               # Профиль пользователя
│   ├── office/                # Офис
│   ├── merch/                 # Магазин мерча
│   ├── favorites/             # Избранное
│   ├── components/            # Переиспользуемые UI компоненты
│   └── navigation/            # Навигация
│
├── domain/                    # Domain Layer
│   ├── model/                 # Domain модели (бизнес-сущности)
│   │   ├── User.kt
│   │   ├── News.kt
│   │   ├── Task.kt
│   │   └── ...
│   ├── repository/            # Интерфейсы репозиториев
│   │   ├── AuthRepository.kt
│   │   ├── NewsRepository.kt
│   │   └── ...
│   ├── usecase/               # Use Cases (бизнес-логика)
│   │   ├── auth/
│   │   │   ├── LoginUseCase.kt
│   │   │   └── GetCurrentUserUseCase.kt
│   │   ├── profile/
│   │   ├── news/
│   │   └── employee/
│   └── common/                # Общие domain классы
│       ├── DomainException.kt # Обработка ошибок
│       └── Result.kt          # Wrapper для результатов
│
├── data/                      # Data Layer
│   ├── repository/            # Реализации репозиториев
│   │   ├── AuthRepositoryImpl.kt
│   │   ├── NewsRepositoryImpl.kt
│   │   ├── TokenStorage.kt    # Хранилище токенов
│   │   └── ...
│   └── remote/                # Удаленные источники данных
│       ├── api/               # API клиенты
│       │   └── ApiService.kt  # HTTP клиент
│       ├── dto/               # Data Transfer Objects
│       │   ├── UserDto.kt
│       │   ├── NewsDto.kt
│       │   └── ...
│       └── datasource/        # Data Sources (абстракция API)
│           ├── AuthRemoteDataSource.kt
│           ├── NewsRemoteDataSource.kt
│           └── ...
│
└── di/                        # Dependency Injection
    ├── AppModule.kt           # Главный модуль
    ├── NetworkModule.kt       # Сетевые зависимости
    ├── DataSourceModule.kt    # Data sources
    ├── RepositoryModule.kt    # Репозитории
    ├── UseCaseModule.kt       # Use cases
    └── ViewModelModule.kt     # ViewModels
```

## 🎯 Слои приложения

### 1. Presentation Layer (UI + ViewModel)

**Ответственность:**

- Отображение данных пользователю
- Обработка пользовательского ввода
- Управление состоянием UI

**Компоненты:**

- **Screen**: Composable функции для UI
- **ViewModel**: Хранение и управление состоянием UI
- **UiState**: Data классы для состояния экрана

**Пример:**

```kotlin
// AuthViewModel.kt
class AuthViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    fun login() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            loginUseCase(username, password)
                .onSuccess { /* ... */ }
                .onFailure { /* ... */ }
        }
    }
}
```

### 2. Domain Layer (Бизнес-логика)

**Ответственность:**

- Определение бизнес-правил
- Определение сущностей (моделей)
- Определение интерфейсов для репозиториев

**Компоненты:**

- **Model**: Бизнес-сущности (User, News, Task...)
- **Repository Interface**: Контракты для получения данных
- **Use Case**: Отдельные бизнес-операции

**Пример:**

```kotlin
// LoginUseCase.kt
class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        username: String, 
        password: String
    ): Result<Pair<String, User>> {
        // Валидация
        if (username.isBlank()) {
            return Result.failure(
                IllegalArgumentException("Username cannot be empty")
            )
        }
        
        // Делегирование репозиторию
        return authRepository.login(username, password)
    }
}
```

### 3. Data Layer (Источники данных)

**Ответственность:**

- Получение данных из различных источников
- Кэширование данных
- Преобразование DTO в Domain модели

**Компоненты:**

- **Repository Implementation**: Реализация репозиториев
- **Data Source**: Абстракция над источниками данных
- **DTO**: Модели для сериализации данных
- **Mapper**: Функции преобразования DTO → Domain

**Пример:**

```kotlin
// AuthRepositoryImpl.kt
class AuthRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource,
    private val tokenStorage: TokenStorage
) : AuthRepository {
    
    override suspend fun login(
        username: String, 
        password: String
    ): Result<Pair<String, User>> {
        return remoteDataSource.login(username, password)
            .mapCatching { authResponse ->
                tokenStorage.saveToken(authResponse.token)
                Pair(authResponse.token, authResponse.user.toDomain())
            }
    }
}
```

## 🔌 Dependency Injection

Проект использует **Koin** для DI. Модули организованы по слоям:

### NetworkModule

Конфигурация HTTP клиента, сериализации, авторизации:

```kotlin
val networkModule = module {
    single<TokenStorage> { InMemoryTokenStorage() }
    
    single {
        HttpClient {
            install(ContentNegotiation) { json() }
            install(Auth) { bearer { /* ... */ } }
            // ...
        }
    }
    
    single { ApiService(get()) }
}
```

### DataSourceModule

Data sources для работы с API:

```kotlin
val dataSourceModule = module {
    singleOf(::AuthRemoteDataSourceImpl) bind AuthRemoteDataSource::class
    singleOf(::NewsRemoteDataSourceImpl) bind NewsRemoteDataSource::class
    // ...
}
```

### RepositoryModule

Реализации репозиториев:

```kotlin
val repositoryModule = module {
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
    singleOf(::NewsRepositoryImpl) bind NewsRepository::class
    // ...
}
```

### UseCaseModule

Бизнес-логика приложения:

```kotlin
val useCaseModule = module {
    factoryOf(::LoginUseCase)
    factoryOf(::GetCurrentUserUseCase)
    // ...
}
```

### ViewModelModule

ViewModels для экранов:

```kotlin
val viewModelModule = module {
    viewModelOf(::AuthViewModel)
    viewModelOf(::HomeViewModel)
    // ...
}
```

### Инициализация Koin

**Android:**

```kotlin
// TutuEmployeeApp.kt
class TutuEmployeeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger()
            androidContext(this@TutuEmployeeApp)
            modules(appModules)
        }
    }
}
```

**iOS, Web, Desktop:**

```kotlin
// В точке входа приложения
initKoin()

fun initKoin() {
    startKoin {
        modules(appModules)
    }
}
```

## 🔄 Потоки данных

### Поток данных при загрузке

```
User Action (Click)
    ↓
Screen → ViewModel
    ↓
ViewModel → Use Case
    ↓
Use Case → Repository Interface
    ↓
Repository Impl → Data Source
    ↓
Data Source → API Service
    ↓
HTTP Request → Backend
    ↓
Response (DTO) ← Backend
    ↓
Mapper: DTO → Domain Model
    ↓
Result → Repository → Use Case
    ↓
ViewModel updates State
    ↓
Screen recomposes with new data
```

### Пример полного потока

```kotlin
// 1. User нажимает кнопку Login
AuthScreen { viewModel.login() }

// 2. ViewModel вызывает Use Case
class AuthViewModel(private val loginUseCase: LoginUseCase) {
    fun login() {
        loginUseCase(username, password)
            .onSuccess { /* update UI */ }
    }
}

// 3. Use Case валидирует и вызывает Repository
class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(...): Result<...> {
        // Validation
        return authRepository.login(username, password)
    }
}

// 4. Repository вызывает Data Source
class AuthRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource
) : AuthRepository {
    override suspend fun login(...): Result<...> {
        return remoteDataSource.login(...)
            .map { it.toDomain() }
    }
}

// 5. Data Source делает HTTP запрос
class AuthRemoteDataSourceImpl(
    private val apiService: ApiService
) : AuthRemoteDataSource {
    override suspend fun login(...): Result<AuthResponse> {
        return apiService.login(username, password)
    }
}

// 6. API Service выполняет запрос
class ApiService(private val httpClient: HttpClient) {
    suspend fun login(...): Result<AuthResponse> {
        return try {
            val response = httpClient.post("/auth/login") {
                setBody(AuthRequest(username, password))
            }
            Result.success(response.body<AuthResponse>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

## ✅ Лучшие практики

### 1. Именование

- **ViewModel**: `{Feature}ViewModel` (например, `AuthViewModel`)
- **Screen**: `{Feature}Screen` (например, `AuthScreen`)
- **Use Case**: `{Verb}{Entity}UseCase` (например, `GetCurrentUserUseCase`)
- **Repository**: `{Entity}Repository` (например, `AuthRepository`)
- **DTO**: `{Entity}Dto` (например, `UserDto`)

### 2. Обработка ошибок

Используйте `DomainException` для типизированных ошибок:

```kotlin
sealed class DomainException : Exception() {
    data class NetworkException(message: String) : DomainException()
    data class AuthenticationException(message: String) : DomainException()
    data class ValidationException(message: String) : DomainException()
    // ...
}
```

### 3. Маппинг данных

Всегда преобразуйте DTO в Domain модели:

```kotlin
// DTO (Data Layer)
@Serializable
data class UserDto(
    val id: String,
    val username: String,
    // ...
)

// Domain Model
data class User(
    val id: String,
    val username: String,
    // ...
)

// Mapper
fun UserDto.toDomain(): User = User(
    id = id,
    username = username,
    // ...
)
```

### 4. Использование Result

Оборачивайте результаты операций в `Result<T>`:

```kotlin
suspend fun login(...): Result<Pair<String, User>>

// Использование
loginUseCase(username, password)
    .onSuccess { (token, user) -> /* ... */ }
    .onFailure { exception -> /* ... */ }
```

### 5. StateFlow для состояния

```kotlin
private val _uiState = MutableStateFlow(AuthUiState())
val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
```

### 6. Dependency Injection

- Используйте интерфейсы для зависимостей
- Регистрируйте реализации в DI модулях
- Инжектируйте зависимости через конструктор

```kotlin
class AuthViewModel(
    private val loginUseCase: LoginUseCase  // ← Injection
) : ViewModel()
```

### 7. Разделение ответственности

- **Screen**: Только UI
- **ViewModel**: Управление состоянием и обработка событий
- **Use Case**: Бизнес-логика для одной операции
- **Repository**: Управление источниками данных
- **Data Source**: Работа с конкретным API

### 8. Тестирование

Каждый слой легко тестируется независимо:

```kotlin
// Тестирование Use Case с mock Repository
class LoginUseCaseTest {
    private val mockRepository = mockk<AuthRepository>()
    private val useCase = LoginUseCase(mockRepository)
    
    @Test
    fun `login with empty username returns failure`() = runTest {
        val result = useCase("", "password")
        assertTrue(result.isFailure)
    }
}
```

## 🎨 Преимущества архитектуры

1. **Модульность**: Каждый модуль независим
2. **Тестируемость**: Легкое unit-тестирование
3. **Масштабируемость**: Простое добавление функций
4. **Поддерживаемость**: Четкое разделение ответственности
5. **Переиспользование**: Общая логика в Use Cases
6. **Независимость от UI**: Бизнес-логика не зависит от UI
7. **Независимость от фреймворков**: Domain слой чистый Kotlin

## 📚 Дополнительные ресурсы

- [Clean Architecture by Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Koin Documentation](https://insert-koin.io/)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
