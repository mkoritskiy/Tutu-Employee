# 🏛 Clean Architecture Implementation

## 📐 Структура проекта

Проект теперь организован по принципам Clean Architecture с четким разделением на слои:

```
composeApp/src/commonMain/kotlin/ru/tutu/tutuemployee/
├── domain/                          # Бизнес-логика (не зависит от других слоев)
│   ├── model/                       # Domain entities
│   │   ├── User.kt
│   │   ├── News.kt
│   │   ├── Birthday.kt
│   │   ├── Achievement.kt
│   │   ├── Task.kt
│   │   ├── Vacation.kt
│   │   ├── Course.kt
│   │   ├── WorkspaceBooking.kt
│   │   ├── MerchItem.kt
│   │   └── FavoriteCard.kt
│   ├── repository/                  # Repository interfaces
│   │   ├── AuthRepository.kt
│   │   ├── NewsRepository.kt
│   │   ├── EmployeeRepository.kt
│   │   ├── ProfileRepository.kt
│   │   ├── OfficeRepository.kt
│   │   ├── MerchRepository.kt
│   │   └── FavoritesRepository.kt
│   └── usecase/                     # Business use cases
│       ├── auth/
│       │   ├── LoginUseCase.kt
│       │   └── GetCurrentUserUseCase.kt
│       ├── news/
│       │   └── GetNewsUseCase.kt
│       ├── employee/
│       │   ├── GetBirthdaysUseCase.kt
│       │   └── SearchEmployeesUseCase.kt
│       └── profile/
│           └── GetProfileDataUseCase.kt
│
├── data/                            # Реализация работы с данными
│   ├── remote/
│   │   ├── api/
│   │   │   └── ApiService.kt        # Ktor HTTP client service
│   │   └── dto/                     # Data Transfer Objects
│   │       ├── UserDto.kt
│   │       ├── NewsDto.kt
│   │       └── CommonDto.kt
│   └── repository/                  # Repository implementations
│       ├── AuthRepositoryImpl.kt
│       ├── NewsRepositoryImpl.kt
│       ├── EmployeeRepositoryImpl.kt
│       ├── ProfileRepositoryImpl.kt
│       ├── OfficeRepositoryImpl.kt
│       ├── MerchRepositoryImpl.kt
│       └── FavoritesRepositoryImpl.kt
│
├── presentation/                    # UI Layer
│   ├── auth/
│   │   ├── AuthScreen.kt
│   │   └── AuthViewModel.kt
│   ├── home/
│   │   ├── HomeScreen.kt
│   │   └── HomeViewModel.kt
│   ├── profile/
│   │   ├── ProfileScreen.kt
│   │   └── ProfileViewModel.kt
│   ├── office/
│   │   ├── OfficeScreen.kt
│   │   └── OfficeViewModel.kt
│   ├── merch/
│   │   ├── MerchScreen.kt
│   │   └── MerchViewModel.kt
│   ├── favorites/
│   │   ├── FavoritesScreen.kt
│   │   └── FavoritesViewModel.kt
│   └── components/
│       └── BottomNavigationBar.kt
│
├── di/                              # Dependency Injection
│   ├── NetworkModule.kt             # HTTP client, API service
│   ├── RepositoryModule.kt          # Repository implementations
│   ├── UseCaseModule.kt             # Use cases
│   ├── ViewModelModule.kt           # ViewModels
│   └── AppModule.kt                 # Main DI module
│
└── navigation/
    ├── Screen.kt
    └── NavigationHost.kt
```

## 🎯 Принципы Clean Architecture

### 1. Domain Layer (Внутренний слой)

**Не зависит ни от чего!** Содержит только бизнес-логику.

#### Domain Models

Чистые Kotlin data classes без аннотаций сериализации:

```kotlin
// domain/model/User.kt
data class User(
    val id: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    // ... другие поля
) {
    val fullName: String
        get() = "$firstName $lastName"  // Бизнес-логика
}
```

#### Repository Interfaces

Определяют контракты для работы с данными:

```kotlin
// domain/repository/AuthRepository.kt
interface AuthRepository {
    suspend fun login(username: String, password: String): Result<Pair<String, User>>
    suspend fun getCurrentUser(): Result<User>
    suspend fun logout()
}
```

#### Use Cases

Инкапсулируют бизнес-логику конкретного сценария:

```kotlin
// domain/usecase/auth/LoginUseCase.kt
class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): Result<Pair<String, User>> {
        // Валидация на уровне бизнес-логики
        if (username.isBlank()) {
            return Result.failure(IllegalArgumentException("Username cannot be empty"))
        }
        if (password.isBlank()) {
            return Result.failure(IllegalArgumentException("Password cannot be empty"))
        }
        
        return authRepository.login(username, password)
    }
}
```

### 2. Data Layer (Внешний слой)

**Зависит только от Domain!** Реализует работу с данными.

#### DTO (Data Transfer Objects)

Используются для сериализации/десериализации данных из API:

```kotlin
// data/remote/dto/UserDto.kt
@Serializable
data class UserDto(
    val id: String,
    val username: String,
    // ... поля как в API
)

// Mapper: DTO -> Domain
fun UserDto.toDomain(): User {
    return User(
        id = id,
        username = username,
        // ... маппинг полей
    )
}
```

#### API Service

Работает с HTTP клиентом и возвращает DTO:

```kotlin
// data/remote/api/ApiService.kt
class ApiService(
    private val httpClient: HttpClient
) {
    suspend fun getCurrentUser(): Result<UserDto> {
        return try {
            val response = httpClient.get("/user/me")
            Result.success(response.body<UserDto>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

#### Repository Implementation

Реализует интерфейс из Domain, маппит DTO в Domain модели:

```kotlin
// data/repository/AuthRepositoryImpl.kt
class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val tokenStorage: TokenStorage
) : AuthRepository {
    
    override suspend fun login(username: String, password: String): Result<Pair<String, User>> {
        return apiService.login(username, password)
            .mapCatching { authResponse ->
                tokenStorage.saveToken(authResponse.token)
                Pair(authResponse.token, authResponse.user.toDomain())
            }
    }
    
    override suspend fun getCurrentUser(): Result<User> {
        return apiService.getCurrentUser()
            .map { it.toDomain() }  // DTO -> Domain
    }
}
```

### 3. Presentation Layer

**Зависит только от Domain!** Содержит UI и ViewModels.

#### ViewModel

Использует Use Cases из Domain:

```kotlin
// presentation/home/HomeViewModel.kt
class HomeViewModel(
    private val getNewsUseCase: GetNewsUseCase,
    private val getBirthdaysUseCase: GetBirthdaysUseCase,
    private val searchEmployeesUseCase: SearchEmployeesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val newsResult = getNewsUseCase()
            val birthdaysResult = getBirthdaysUseCase()

            _uiState.value = _uiState.value.copy(
                news = newsResult.getOrDefault(emptyList()),
                birthdays = birthdaysResult.getOrDefault(emptyList()),
                isLoading = false
            )
        }
    }
}
```

#### Screen

Получает ViewModel через DI:

```kotlin
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // UI код
}
```

## 🔌 Dependency Injection (Koin)

### Модули

#### 1. NetworkModule

```kotlin
val networkModule = module {
    single<TokenStorage> { InMemoryTokenStorage() }
    single { 
        HttpClient {
            // Конфигурация Ktor
        }
    }
    single { ApiService(get()) }
}
```

#### 2. RepositoryModule

```kotlin
val repositoryModule = module {
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
    singleOf(::NewsRepositoryImpl) bind NewsRepository::class
    // ... другие репозитории
}
```

#### 3. UseCaseModule

```kotlin
val useCaseModule = module {
    factoryOf(::LoginUseCase)
    factoryOf(::GetNewsUseCase)
    // ... другие use cases
}
```

#### 4. ViewModelModule

```kotlin
val viewModelModule = module {
    viewModelOf(::AuthViewModel)
    viewModelOf(::HomeViewModel)
    // ... другие ViewModels
}
```

### Инициализация Koin

#### Android

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

Не забудьте добавить в `AndroidManifest.xml`:

```xml
<application
    android:name=".TutuEmployeeApp"
    ...>
```

#### iOS (будущая реализация)

```kotlin
fun initKoin() {
    startKoin {
        modules(appModules)
    }
}
```

#### Web

```kotlin
fun main() {
    startKoin {
        modules(appModules)
    }
    
    // Инициализация Compose
}
```

## 🔄 Поток данных

```
User Action (Screen)
    ↓
ViewModel
    ↓
Use Case (бизнес-логика)
    ↓
Repository Interface (Domain)
    ↓
Repository Implementation (Data)
    ↓
API Service
    ↓
HTTP Client (Ktor)
    ↓
Backend API
    ↓
DTO Response
    ↓
Mapper (DTO → Domain)
    ↓
Domain Model
    ↓
Use Case
    ↓
ViewModel (обновление State)
    ↓
Screen (UI recompose)
```

## ✅ Преимущества этой архитектуры

### 1. **Separation of Concerns**

Каждый слой отвечает за свою задачу:

- **Domain**: Бизнес-логика
- **Data**: Работа с данными
- **Presentation**: UI

### 2. **Testability**

```kotlin
class LoginUseCaseTest {
    @Test
    fun `login with empty username returns error`() = runTest {
        val mockRepository = mockk<AuthRepository>()
        val useCase = LoginUseCase(mockRepository)
        
        val result = useCase("", "password")
        
        assertTrue(result.isFailure)
    }
}
```

### 3. **Dependency Rule**

Зависимости всегда направлены внутрь:

```
Presentation → Domain ← Data
```

Domain не зависит ни от чего!

### 4. **Scalability**

- Легко добавлять новые фичи
- Легко менять источники данных
- Легко тестировать

### 5. **Reusability**

- Use Cases можно использовать в разных ViewModels
- Repositories можно использовать в разных Use Cases
- Domain модели общие для всего приложения

## 🎨 Паттерны использования

### Repository Pattern

```kotlin
interface UserRepository {
    suspend fun getUser(id: String): Result<User>
}

class UserRepositoryImpl(
    private val apiService: ApiService
) : UserRepository {
    override suspend fun getUser(id: String): Result<User> {
        return apiService.getUser(id)
            .map { it.toDomain() }
    }
}
```

### Use Case Pattern

```kotlin
class GetUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String): Result<User> {
        // Дополнительная бизнес-логика
        return userRepository.getUser(userId)
    }
}
```

### MVVM Pattern

```kotlin
class UserViewModel(
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(UserUiState())
    val uiState = _uiState.asStateFlow()
    
    fun loadUser(userId: String) {
        viewModelScope.launch {
            getUserUseCase(userId)
                .onSuccess { user ->
                    _uiState.value = _uiState.value.copy(user = user)
                }
        }
    }
}
```

## 🚀 Следующие шаги

### 1. Локальное хранилище

```kotlin
// data/local/
├── database/
│   └── AppDatabase.kt
└── dao/
    └── UserDao.kt

// Обновить репозитории для работы с кешем
class NewsRepositoryImpl(
    private val apiService: ApiService,
    private val newsDao: NewsDao
) : NewsRepository {
    override suspend fun getNews(): Result<List<News>> {
        // 1. Загрузить из кеша
        // 2. Обновить из сети
        // 3. Сохранить в кеш
    }
}
```

### 2. Error Handling

```kotlin
sealed class DomainError {
    data class NetworkError(val message: String) : DomainError()
    data class ValidationError(val field: String) : DomainError()
    data class UnknownError(val throwable: Throwable) : DomainError()
}
```

### 3. Mappers для всех моделей

Создать отдельные mapper функции для сложной логики преобразования

### 4. Platform-specific Storage

Реализовать TokenStorage для каждой платформы:

- Android: SharedPreferences
- iOS: UserDefaults
- Web: localStorage

---

**Версия**: 2.0.0 (Clean Architecture)  
**Дата обновления**: Декабрь 2024
