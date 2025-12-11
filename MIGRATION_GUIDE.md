# 📚 Migration Guide: Clean Architecture + DI

## 🎯 Что было сделано

Проект полностью реорганизован по принципам **Clean Architecture** с внедрением **Dependency
Injection (Koin)**.

### Основные изменения:

1. ✅ **Разделение на слои**
    - Domain Layer (бизнес-логика)
    - Data Layer (работа с данными)
    - Presentation Layer (UI)

2. ✅ **Dependency Injection с Koin**
    - Автоматическое внедрение зависимостей
    - Модульная структура DI

3. ✅ **Use Cases**
    - Изолированная бизнес-логика
    - Легкое тестирование

4. ✅ **DTO Pattern**
    - Разделение API моделей и Domain моделей
    - Mappers для преобразования

## 🗂 Новая структура файлов

### До:

```
data/
├── model/           # Модели с @Serializable
└── network/
    └── ApiService   # Прямые вызовы API

presentation/
└── home/
    └── HomeViewModel  # Напрямую использует ApiService
```

### После:

```
domain/              # ⭐ НОВОЕ
├── model/           # Чистые Kotlin модели (без @Serializable)
├── repository/      # Интерфейсы репозиториев
└── usecase/         # Бизнес-логика

data/
├── remote/
│   ├── api/         # ApiService
│   └── dto/         # ⭐ НОВОЕ: DTO модели с @Serializable
└── repository/      # ⭐ НОВОЕ: Реализации репозиториев

presentation/
└── home/
    └── HomeViewModel  # Теперь использует Use Cases

di/                  # ⭐ НОВОЕ: Модули Koin
├── NetworkModule.kt
├── RepositoryModule.kt
├── UseCaseModule.kt
└── ViewModelModule.kt
```

## 🔧 Как использовать новую архитектуру

### 1. Добавление нового экрана

#### Шаг 1: Создать ViewModel с Use Cases

```kotlin
class NewFeatureViewModel(
    private val getSomeDataUseCase: GetSomeDataUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(NewFeatureUiState())
    val uiState = _uiState.asStateFlow()
    
    init {
        loadData()
    }
    
    private fun loadData() {
        viewModelScope.launch {
            getSomeDataUseCase()
                .onSuccess { data ->
                    _uiState.value = _uiState.value.copy(data = data)
                }
        }
    }
}
```

#### Шаг 2: Зарегистрировать в DI

```kotlin
// di/ViewModelModule.kt
val viewModelModule = module {
    // ... существующие
    viewModelOf(::NewFeatureViewModel)  // ⭐ Добавить эту строку
}
```

#### Шаг 3: Использовать в Screen

```kotlin
@Composable
fun NewFeatureScreen(
    viewModel: NewFeatureViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    // ... UI код
}
```

### 2. Добавление нового API endpoint

#### Шаг 1: Добавить DTO

```kotlin
// data/remote/dto/NewDataDto.kt
@Serializable
data class NewDataDto(
    val id: String,
    val name: String
)

fun NewDataDto.toDomain() = NewData(
    id = id,
    name = name
)
```

#### Шаг 2: Добавить Domain Model

```kotlin
// domain/model/NewData.kt
data class NewData(
    val id: String,
    val name: String
)
```

#### Шаг 3: Добавить метод в ApiService

```kotlin
// data/remote/api/ApiService.kt
suspend fun getNewData(): Result<List<NewDataDto>> {
    return try {
        val response = httpClient.get("/new-data")
        Result.success(response.body<List<NewDataDto>>())
    } catch (e: Exception) {
        Result.failure(e)
    }
}
```

#### Шаг 4: Создать Repository

```kotlin
// domain/repository/NewDataRepository.kt
interface NewDataRepository {
    suspend fun getNewData(): Result<List<NewData>>
}

// data/repository/NewDataRepositoryImpl.kt
class NewDataRepositoryImpl(
    private val apiService: ApiService
) : NewDataRepository {
    override suspend fun getNewData(): Result<List<NewData>> {
        return apiService.getNewData()
            .map { list -> list.map { it.toDomain() } }
    }
}
```

#### Шаг 5: Создать Use Case

```kotlin
// domain/usecase/GetNewDataUseCase.kt
class GetNewDataUseCase(
    private val repository: NewDataRepository
) {
    suspend operator fun invoke(): Result<List<NewData>> {
        return repository.getNewData()
    }
}
```

#### Шаг 6: Зарегистрировать в DI

```kotlin
// di/RepositoryModule.kt
singleOf(::NewDataRepositoryImpl) bind NewDataRepository::class

// di/UseCaseModule.kt
factoryOf(::GetNewDataUseCase)
```

### 3. Работа с существующими ViewModels

Все ViewModels обновлены для работы с DI. Примеры:

```kotlin
// ❌ СТАРЫЙ КОД
class HomeViewModel : ViewModel() {
    private val apiService = ApiService()  // Ручное создание
}

// ✅ НОВЫЙ КОД
class HomeViewModel(
    private val getNewsUseCase: GetNewsUseCase,
    private val getBirthdaysUseCase: GetBirthdaysUseCase
) : ViewModel() {
    // Зависимости внедряются автоматически через Koin
}
```

## 🔨 Сборка и запуск

### Android

```bash
./gradlew :composeApp:assembleDebug
```

### Web

```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

### iOS (после настройки)

```bash
./gradlew :composeApp:iosSimulatorArm64Test
```

## 🧪 Тестирование

### Пример теста Use Case

```kotlin
class LoginUseCaseTest {
    
    @Test
    fun `login with valid credentials returns success`() = runTest {
        // Given
        val mockRepository = mockk<AuthRepository>()
        coEvery { 
            mockRepository.login("user", "pass") 
        } returns Result.success(Pair("token", mockUser))
        
        val useCase = LoginUseCase(mockRepository)
        
        // When
        val result = useCase("user", "pass")
        
        // Then
        assertTrue(result.isSuccess)
    }
    
    @Test
    fun `login with empty username returns error`() = runTest {
        val mockRepository = mockk<AuthRepository>()
        val useCase = LoginUseCase(mockRepository)
        
        val result = useCase("", "password")
        
        assertTrue(result.isFailure)
        assertEquals(
            "Username cannot be empty",
            result.exceptionOrNull()?.message
        )
    }
}
```

### Пример теста ViewModel

```kotlin
class HomeViewModelTest {
    
    @Test
    fun `loadData updates state with news and birthdays`() = runTest {
        // Given
        val mockGetNews = mockk<GetNewsUseCase>()
        val mockGetBirthdays = mockk<GetBirthdaysUseCase>()
        
        coEvery { mockGetNews() } returns Result.success(listOf(mockNews))
        coEvery { mockGetBirthdays() } returns Result.success(listOf(mockBirthday))
        
        // When
        val viewModel = HomeViewModel(mockGetNews, mockGetBirthdays, mockk())
        advanceUntilIdle()
        
        // Then
        assertEquals(1, viewModel.uiState.value.news.size)
        assertEquals(1, viewModel.uiState.value.birthdays.size)
        assertFalse(viewModel.uiState.value.isLoading)
    }
}
```

## 📱 Platform-specific инициализация

### Android

Уже настроено! Koin инициализируется в `TutuEmployeeApp`:

```kotlin
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

И зарегистрировано в `AndroidManifest.xml`:

```xml
<application
    android:name=".TutuEmployeeApp"
    ...>
```

### iOS (TODO)

Создайте инициализатор:

```kotlin
// iosMain/kotlin/.../KoinInitializer.kt
fun initKoin() {
    startKoin {
        modules(appModules)
    }
}
```

Вызовите в Swift:

```swift
// iosApp/ContentView.swift
init() {
    KoinInitializerKt.initKoin()
}
```

### Web (TODO)

```kotlin
// jsMain/kotlin/.../main.kt
fun main() {
    startKoin {
        modules(appModules)
    }
    
    CanvasBasedWindow("Tutu Employee") {
        App()
    }
}
```

## 🎓 Best Practices

### 1. Domain Models vs DTO

```kotlin
// ❌ НЕ ДЕЛАЙТЕ ТАК
@Serializable  // Domain модель НЕ должна зависеть от сериализации!
data class User(...)

// ✅ ПРАВИЛЬНО
// domain/model/User.kt
data class User(...)  // Чистая Kotlin модель

// data/remote/dto/UserDto.kt
@Serializable
data class UserDto(...)

fun UserDto.toDomain() = User(...)  // Mapper
```

### 2. Use Case для каждого сценария

```kotlin
// ✅ ХОРОШО: Один Use Case = одна задача
class LoginUseCase(...)
class LogoutUseCase(...)
class GetCurrentUserUseCase(...)

// ❌ ПЛОХО: "Божественный" Use Case
class AuthUseCase {
    fun login(...) 
    fun logout(...)
    fun getUser(...)
    fun updateProfile(...)
}
```

### 3. Repository как единая точка доступа к данным

```kotlin
// ✅ ПРАВИЛЬНО
class NewsRepositoryImpl(
    private val apiService: ApiService,
    private val newsDao: NewsDao  // Будущее: локальный кеш
) : NewsRepository {
    override suspend fun getNews(): Result<List<News>> {
        // 1. Попытка загрузить из кеша
        // 2. Обновление из сети
        // 3. Сохранение в кеш
    }
}
```

### 4. Dependency Injection всегда

```kotlin
// ❌ ПЛОХО
class HomeViewModel : ViewModel() {
    private val apiService = ApiService()  // Жесткая зависимость
}

// ✅ ХОРОШО
class HomeViewModel(
    private val getNewsUseCase: GetNewsUseCase  // Внедрение через конструктор
) : ViewModel()
```

## 🔍 Troubleshooting

### Проблема: "Unresolved reference: koin"

**Решение**: Синхронизируйте Gradle

```bash
./gradlew clean build --refresh-dependencies
```

### Проблема: "No definition found for..."

**Решение**: Проверьте, что класс зарегистрирован в соответствующем модуле:

```kotlin
// di/ViewModelModule.kt
viewModelOf(::YourViewModel)  // Добавьте эту строку
```

### Проблема: "Circular dependency detected"

**Решение**: Проверьте зависимости в Use Cases и Repositories. Возможно, есть циклическая
зависимость.

## 📚 Дополнительные ресурсы

- [Clean Architecture Guide](CLEAN_ARCHITECTURE.md) - Подробная документация
- [Koin Documentation](https://insert-koin.io/) - Официальная документация Koin
- [Architecture Patterns](ARCHITECTURE.md) - Оригинальная архитектура

## ✨ Что дальше?

### Краткосрочные задачи:

1. ✅ Обновить все Screens для использования `koinViewModel()`
2. ⬜ Добавить unit тесты для Use Cases
3. ⬜ Добавить UI тесты для Screens
4. ⬜ Реализовать локальное хранилище (Room/SQLDelight)

### Долгосрочные задачи:

1. ⬜ Добавить offline-first подход
2. ⬜ Реализовать кеширование данных
3. ⬜ Добавить background sync
4. ⬜ Улучшить error handling с sealed classes

---

**Версия**: 1.0.0  
**Дата**: Декабрь 2024

Если у вас есть вопросы - создайте issue в репозитории!
