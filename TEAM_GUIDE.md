# 👥 Руководство для команды разработчиков

## 🎯 Добро пожаловать в TutuEmployee!

Проект построен на **Clean Architecture** с использованием **Kotlin Multiplatform** и **Compose
Multiplatform**.

## 📚 С чего начать?

### 1. Прочитайте документацию (в порядке важности)

1. **[ARCHITECTURE_README.md](ARCHITECTURE_README.md)** ← Начните здесь!
    - Обзор проекта
    - Быстрый старт
    - Примеры кода

2. **[CLEAN_ARCHITECTURE_STRUCTURE.md](CLEAN_ARCHITECTURE_STRUCTURE.md)**
    - Детальное описание архитектуры
    - Структура слоев
    - Лучшие практики

3. **[ARCHITECTURE_DIAGRAM.md](ARCHITECTURE_DIAGRAM.md)**
    - Визуальные диаграммы
    - Поток данных
    - Схема зависимостей

4. **[KOIN_MIGRATION_GUIDE.md](KOIN_MIGRATION_GUIDE.md)**
    - Работа с Dependency Injection
    - Как добавлять новые компоненты в DI

### 2. Изучите структуру проекта

```
composeApp/src/commonMain/kotlin/ru/tutu/tutuemployee/
│
├── presentation/   ← UI компоненты (Composable) и ViewModels
├── domain/         ← Бизнес-логика (Use Cases, Models, Interfaces)
├── data/           ← Работа с данными (API, DTO, Repositories)
└── di/             ← Dependency Injection (Koin modules)
```

### 3. Запустите проект

```bash
# Android
./gradlew :composeApp:assembleDebug

# iOS
cd iosApp && pod install && open iosApp.xcworkspace

# Web
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

## 🛠️ Как добавить новую фичу?

### Пример: Добавить функцию "Календарь событий"

#### Шаг 1: Domain Layer

**1.1. Создайте Domain модель:**

```kotlin
// domain/model/CalendarEvent.kt
data class CalendarEvent(
    val id: String,
    val title: String,
    val description: String,
    val startDate: String,
    val endDate: String,
    val type: EventType
)

enum class EventType {
    MEETING,
    DEADLINE,
    HOLIDAY
}
```

**1.2. Создайте Repository интерфейс:**

```kotlin
// domain/repository/CalendarRepository.kt
interface CalendarRepository {
    suspend fun getEvents(date: String): Result<List<CalendarEvent>>
    suspend fun addEvent(event: CalendarEvent): Result<Unit>
    suspend fun deleteEvent(eventId: String): Result<Unit>
}
```

**1.3. Создайте Use Cases:**

```kotlin
// domain/usecase/calendar/GetCalendarEventsUseCase.kt
class GetCalendarEventsUseCase(
    private val calendarRepository: CalendarRepository
) {
    suspend operator fun invoke(date: String): Result<List<CalendarEvent>> {
        return calendarRepository.getEvents(date)
    }
}

// domain/usecase/calendar/AddCalendarEventUseCase.kt
class AddCalendarEventUseCase(
    private val calendarRepository: CalendarRepository
) {
    suspend operator fun invoke(event: CalendarEvent): Result<Unit> {
        // Валидация
        if (event.title.isBlank()) {
            return Result.failure(
                DomainException.ValidationException("Title is required")
            )
        }
        
        return calendarRepository.addEvent(event)
    }
}
```

#### Шаг 2: Data Layer

**2.1. Создайте DTO:**

```kotlin
// data/remote/dto/CalendarEventDto.kt
@Serializable
data class CalendarEventDto(
    val id: String,
    val title: String,
    val description: String,
    val startDate: String,
    val endDate: String,
    val type: String
)

// Mapper
fun CalendarEventDto.toDomain() = CalendarEvent(
    id = id,
    title = title,
    description = description,
    startDate = startDate,
    endDate = endDate,
    type = EventType.valueOf(type)
)
```

**2.2. Создайте Remote Data Source:**

```kotlin
// data/remote/datasource/CalendarRemoteDataSource.kt
interface CalendarRemoteDataSource {
    suspend fun getEvents(date: String): Result<List<CalendarEventDto>>
    suspend fun addEvent(event: CalendarEventDto): Result<Unit>
    suspend fun deleteEvent(eventId: String): Result<Unit>
}

class CalendarRemoteDataSourceImpl(
    private val apiService: ApiService
) : CalendarRemoteDataSource {
    override suspend fun getEvents(date: String): Result<List<CalendarEventDto>> {
        return apiService.getCalendarEvents(date)
    }
    
    override suspend fun addEvent(event: CalendarEventDto): Result<Unit> {
        return apiService.addCalendarEvent(event)
    }
    
    override suspend fun deleteEvent(eventId: String): Result<Unit> {
        return apiService.deleteCalendarEvent(eventId)
    }
}
```

**2.3. Реализуйте Repository:**

```kotlin
// data/repository/CalendarRepositoryImpl.kt
class CalendarRepositoryImpl(
    private val remoteDataSource: CalendarRemoteDataSource
) : CalendarRepository {
    
    override suspend fun getEvents(date: String): Result<List<CalendarEvent>> {
        return remoteDataSource.getEvents(date)
            .map { list -> list.map { it.toDomain() } }
    }
    
    override suspend fun addEvent(event: CalendarEvent): Result<Unit> {
        val dto = event.toDto()
        return remoteDataSource.addEvent(dto)
    }
    
    override suspend fun deleteEvent(eventId: String): Result<Unit> {
        return remoteDataSource.deleteEvent(eventId)
    }
}
```

#### Шаг 3: Presentation Layer

**3.1. Создайте UiState:**

```kotlin
// presentation/calendar/CalendarViewModel.kt
data class CalendarUiState(
    val events: List<CalendarEvent> = emptyList(),
    val selectedDate: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
```

**3.2. Создайте ViewModel:**

```kotlin
class CalendarViewModel(
    private val getCalendarEventsUseCase: GetCalendarEventsUseCase,
    private val addCalendarEventUseCase: AddCalendarEventUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()
    
    init {
        loadEvents()
    }
    
    private fun loadEvents() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            getCalendarEventsUseCase(date = "2024-12-11")
                .onSuccess { events ->
                    _uiState.value = _uiState.value.copy(
                        events = events,
                        isLoading = false
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        error = error.message,
                        isLoading = false
                    )
                }
        }
    }
    
    fun addEvent(event: CalendarEvent) {
        viewModelScope.launch {
            addCalendarEventUseCase(event)
                .onSuccess { loadEvents() }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(error = error.message)
                }
        }
    }
}
```

**3.3. Создайте Screen:**

```kotlin
// presentation/calendar/CalendarScreen.kt
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Календарь") })
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(uiState.events) { event ->
                    CalendarEventCard(event = event)
                }
            }
        }
    }
}

@Composable
fun CalendarEventCard(event: CalendarEvent) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(event.title, style = MaterialTheme.typography.titleMedium)
            Text(event.description, style = MaterialTheme.typography.bodyMedium)
            Text("${event.startDate} - ${event.endDate}")
        }
    }
}
```

#### Шаг 4: DI Configuration

**4.1. Добавьте в DataSourceModule:**

```kotlin
// di/DataSourceModule.kt
val dataSourceModule = module {
    // ... existing
    singleOf(::CalendarRemoteDataSourceImpl) bind CalendarRemoteDataSource::class
}
```

**4.2. Добавьте в RepositoryModule:**

```kotlin
// di/RepositoryModule.kt
val repositoryModule = module {
    // ... existing
    singleOf(::CalendarRepositoryImpl) bind CalendarRepository::class
}
```

**4.3. Добавьте в UseCaseModule:**

```kotlin
// di/UseCaseModule.kt
val useCaseModule = module {
    // ... existing
    factoryOf(::GetCalendarEventsUseCase)
    factoryOf(::AddCalendarEventUseCase)
}
```

**4.4. Добавьте в ViewModelModule:**

```kotlin
// di/ViewModelModule.kt
val viewModelModule = module {
    // ... existing
    viewModelOf(::CalendarViewModel)
}
```

#### Шаг 5: API Integration

**5.1. Добавьте методы в ApiService:**

```kotlin
// data/remote/api/ApiService.kt
class ApiService(private val httpClient: HttpClient) {
    
    // ... existing methods
    
    suspend fun getCalendarEvents(date: String): Result<List<CalendarEventDto>> {
        return try {
            val response = httpClient.get("/calendar/events") {
                parameter("date", date)
            }
            Result.success(response.body<List<CalendarEventDto>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun addCalendarEvent(event: CalendarEventDto): Result<Unit> {
        return try {
            httpClient.post("/calendar/events") {
                contentType(ContentType.Application.Json)
                setBody(event)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

#### Шаг 6: Добавьте в навигацию

```kotlin
// navigation/Screen.kt
sealed class Screen(val route: String) {
    // ... existing
    data object Calendar : Screen("calendar")
}

// navigation/NavigationHost.kt
@Composable
fun NavigationHost(...) {
    NavHost(...) {
        // ... existing
        composable(Screen.Calendar.route) {
            CalendarScreen()
        }
    }
}
```

**Готово!** 🎉

## ⚠️ Важные правила

### ❌ НЕ делайте так:

```kotlin
// ❌ Прямое использование DTO в UI
@Composable
fun MyScreen() {
    val dto: UserDto = api.getUser() // ПЛОХО!
}

// ❌ Бизнес-логика в ViewModel
class MyViewModel {
    fun saveUser(user: User) {
        if (user.email.contains("@")) { // ПЛОХО! Должно быть в Use Case
            repository.save(user)
        }
    }
}

// ❌ Зависимость от конкретной реализации
class MyUseCase(
    private val repositoryImpl: AuthRepositoryImpl // ПЛОХО!
) { }

// ❌ Domain модели знают про DTO
data class User(
    val id: String,
    fun toDto(): UserDto { ... } // ПЛОХО! Domain не должен знать про Data слой
)
```

### ✅ Делайте так:

```kotlin
// ✅ Используйте Domain модели в UI
@Composable
fun MyScreen(viewModel: MyViewModel = koinViewModel()) {
    val user: User = viewModel.uiState.collectAsState().value.user
}

// ✅ Бизнес-логика в Use Case
class SaveUserUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(user: User): Result<Unit> {
        if (!user.email.contains("@")) {
            return Result.failure(ValidationException("Invalid email"))
        }
        return repository.save(user)
    }
}

// ✅ Зависимость от интерфейса
class MyUseCase(
    private val repository: AuthRepository // ХОРОШО!
) { }

// ✅ Маппинг в Data слое
fun UserDto.toDomain(): User { ... }
```

## 🔍 Code Review Checklist

При ревью кода проверяйте:

- [ ] Зависимости идут только внутрь (к Domain слою)
- [ ] DTO не просачиваются в Domain/Presentation
- [ ] Бизнес-логика в Use Cases, не в ViewModels
- [ ] ViewModels получают зависимости через Koin
- [ ] Используются Domain модели, не DTO
- [ ] Repository реализации зависят от Data Sources
- [ ] Новые компоненты зарегистрированы в DI
- [ ] Есть обработка ошибок через Result/DomainException
- [ ] Код документирован (хотя бы KDoc для публичных API)

## 🧪 Тестирование

### Unit тесты

```kotlin
// Use Case
class LoginUseCaseTest {
    private val mockRepository = mockk<AuthRepository>()
    private val useCase = LoginUseCase(mockRepository)
    
    @Test
    fun `login with valid credentials returns success`() = runTest {
        // Given
        val username = "test@tutu.ru"
        val password = "password123"
        coEvery { mockRepository.login(any(), any()) } returns 
            Result.success(mockUser)
        
        // When
        val result = useCase(username, password)
        
        // Then
        assertTrue(result.isSuccess)
        coVerify { mockRepository.login(username, password) }
    }
}
```

## 📞 Помощь

### Где спросить?

- **Slack**: #tutu-employee-dev
- **Email**: dev-team@tutu.ru
- **Wiki**: confluence.tutu.ru/employee

### Полезные ссылки

- [Clean Architecture (книга)](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Koin Documentation](https://insert-koin.io/)
- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)

## 🎯 Советы

1. **Начните с Domain слоя** - определите модели и интерфейсы
2. **Используйте Use Cases** - даже для простых операций
3. **Не бойтесь создавать файлы** - лучше много маленьких, чем один большой
4. **Тестируйте бизнес-логику** - Use Cases должны быть покрыты тестами
5. **Следите за зависимостями** - Domain не должен зависеть ни от кого

---

**Удачного кодирования!** 🚀
