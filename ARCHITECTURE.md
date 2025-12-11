# 🏗 Architecture Documentation

Документация архитектуры Tutu Employee App

## 📐 Общая архитектура

### Уровни приложения

```
┌─────────────────────────────────────────────────────────┐
│                    PRESENTATION                          │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │   Screens    │  │  ViewModels  │  │  Components  │  │
│  │ (Composable) │←→│ (StateFlow)  │  │   (Reusable) │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
└─────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────┐
│                     NAVIGATION                           │
│  ┌──────────────────────────────────────────────────┐   │
│  │    NavHost + Screen Sealed Classes                │   │
│  └──────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────┐
│                       DOMAIN                             │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │    Models    │  │   UseCase    │  │  Repository  │  │
│  │  (Data Class)│  │  (Business)  │  │ (Future)     │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
└─────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────┐
│                        DATA                              │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │  ApiService  │  │  ApiClient   │  │ MockService  │  │
│  │  (Endpoints) │  │   (Ktor)     │  │ (Test Data)  │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
└─────────────────────────────────────────────────────────┘
                           ↓
                    Backend API / Mock
```

## 🎯 MVVM Pattern

### Поток данных

```
User Action → Screen → ViewModel → ApiService → Backend
                  ↑        ↓
               StateFlow   Update State
                  ↑
             UI Recompose
```

### Пример: HomeScreen

```kotlin
// 1. User вводит текст в поиск
SearchTextField(
    value = query,
    onValueChange = { viewModel.onSearchQueryChange(it) }
)

// 2. ViewModel обрабатывает
class HomeViewModel : ViewModel() {
    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        searchEmployees(query)  // API запрос
    }
}

// 3. State обновляется
data class HomeUiState(
    val searchQuery: String = "",
    val searchResults: List<User> = emptyList()
)

// 4. UI реагирует
val uiState by viewModel.uiState.collectAsState()
Text(uiState.searchQuery)  // Автоматически обновляется
```

## 📦 Модульная структура

### commonMain

```
commonMain/
├── App.kt                      # Entry point, MaterialTheme
│
├── data/
│   ├── model/                  # Data models
│   │   ├── User.kt
│   │   ├── News.kt
│   │   └── ... (10 models)
│   │
│   └── network/                # API integration
│       ├── ApiClient.kt       # Ktor configuration
│       ├── ApiService.kt      # API endpoints
│       └── MockApiService.kt  # Test data
│
├── navigation/
│   ├── Screen.kt              # Sealed class with all screens
│   └── NavigationHost.kt      # NavHost setup
│
└── presentation/
    ├── auth/
    │   ├── AuthScreen.kt      # Login UI
    │   └── AuthViewModel.kt   # Auth logic
    │
    ├── home/
    │   ├── HomeScreen.kt      # News, birthdays, search
    │   └── HomeViewModel.kt   # Home state management
    │
    ├── profile/
    │   ├── ProfileScreen.kt   # User info, achievements
    │   └── ProfileViewModel.kt
    │
    ├── office/
    │   ├── OfficeScreen.kt    # Workspace booking
    │   └── OfficeViewModel.kt
    │
    ├── merch/
    │   ├── MerchScreen.kt     # Shop
    │   └── MerchViewModel.kt
    │
    ├── favorites/
    │   ├── FavoritesScreen.kt # Bookmarks
    │   └── FavoritesViewModel.kt
    │
    ├── webview/
    │   └── WebViewScreen.kt   # WebView placeholder
    │
    └── components/
        └── BottomNavigationBar.kt  # Shared navigation
```

### Platform-specific

```
androidMain/
├── AndroidManifest.xml
├── MainActivity.kt             # Android entry point
└── res/                        # Android resources

jsMain/
└── main.kt                     # Web JS entry point

wasmJsMain/
└── main.kt                     # Web Wasm entry point

iosMain/
└── Main.kt                     # iOS entry point (future)
```

## 🔄 State Management

### Унифицированный UiState паттерн

Каждый экран имеет свой UiState:

```kotlin
data class ScreenUiState(
    val data: List<Item> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val additionalState: Type = defaultValue
)
```

### ViewModel Template

```kotlin
class ScreenViewModel : ViewModel() {
    private val apiService = ApiService()
    
    private val _uiState = MutableStateFlow(ScreenUiState())
    val uiState: StateFlow<ScreenUiState> = _uiState.asStateFlow()
    
    init {
        loadData()
    }
    
    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            apiService.getData()
                .onSuccess { data ->
                    _uiState.value = _uiState.value.copy(
                        data = data,
                        isLoading = false
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
        }
    }
}
```

## 🌐 Network Layer

### API Client (Ktor)

```kotlin
ApiClient (Ktor HttpClient)
    ↓
    ├── ContentNegotiation (JSON)
    ├── Logging (Requests/Responses)
    ├── Auth (Bearer Token)
    ├── DefaultRequest (Base URL)
    └── HttpTimeout (30s)
```

### API Service Architecture

```kotlin
class ApiService {
    suspend fun getNews(): Result<List<News>> {
        return try {
            val response = client.get("/news")
            Result.success(response.body<List<News>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

### Request Flow

```
ViewModel
    ↓ viewModelScope.launch
ApiService
    ↓ suspend function
ApiClient (Ktor)
    ↓ HTTP Request
Backend API
    ↓ JSON Response
Kotlinx Serialization
    ↓ Deserialization
Data Model
    ↓ Result<T>
ViewModel
    ↓ StateFlow update
UI Recompose
```

## 🎨 UI Architecture

### Compose Hierarchy

```
Screen (Composable)
    ↓
Scaffold
    ├── TopAppBar
    ├── Content (LazyColumn/Grid)
    │   ├── Cards
    │   ├── Lists
    │   └── Custom Components
    └── BottomNavigationBar
```

### Component Reusability

```
Common Components:
├── BottomNavigationBar    # Навигация (все экраны)
├── SearchBar              # Поиск (Home)
├── Cards                  # Новости, товары, и т.д.
└── Dialogs                # Подтверждения, формы
```

## 🔐 Authentication Flow

```
┌──────────────┐
│ App Start    │
└──────┬───────┘
       ↓
┌──────────────┐
│ Check Token  │
└──────┬───────┘
       ↓
  ┌────┴────┐
  │ Exists? │
  └────┬────┘
       ↓
   Yes ↓    No
       ↓    ↓
┌──────┴────┴──────┐
│ Navigate to:     │
│ • Home (token)   │
│ • Auth (no token)│
└──────────────────┘
```

### Login Process

```
1. User enters credentials
   ↓
2. AuthViewModel.login()
   ↓
3. ApiService.login()
   ↓
4. Backend validates
   ↓
5. Return AuthResponse(token, user)
   ↓
6. ApiClient.setAuthToken(token)
   ↓
7. Navigate to Home
   ↓
8. All subsequent requests use Bearer token
```

## 📱 Navigation Architecture

### Screen Definition

```kotlin
sealed interface Screen {
    @Serializable
    data object Auth : Screen
    
    @Serializable
    data object Home : Screen
    
    @Serializable
    data class WebView(val url: String, val title: String) : Screen
}
```

### Navigation Graph

```
Auth Screen (Start)
    ↓ Login success
    ↓
Home Screen
    ├─→ Profile Screen
    ├─→ Office Screen
    ├─→ Merch Screen
    ├─→ Favorites Screen
    └─→ WebView Screen (modal)
```

## 🎯 Design Patterns

### 1. Repository Pattern (Future)

```kotlin
interface NewsRepository {
    suspend fun getNews(): Result<List<News>>
    suspend fun refreshNews(): Result<List<News>>
    fun observeNews(): Flow<List<News>>
}

class NewsRepositoryImpl(
    private val apiService: ApiService,
    private val database: Database
) : NewsRepository {
    // Combine remote and local data
}
```

### 2. UseCase Pattern (Future)

```kotlin
class GetNewsUseCase(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(): Result<List<News>> {
        return repository.getNews()
            .map { news -> news.filter { it.isRelevant() } }
    }
}
```

### 3. Dependency Injection (Manual)

Текущая реализация использует ручное внедрение зависимостей:

```kotlin
class ViewModel {
    private val apiService = ApiService()  // Manual DI
}
```

Для production рекомендуется использовать Koin или Kodein.

## 🔄 Data Flow Patterns

### Pull-to-Refresh

```kotlin
@Composable
fun Screen(viewModel: ViewModel) {
    val state = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = { viewModel.refresh() }
    )
    
    Box(Modifier.pullRefresh(state)) {
        // Content
    }
}
```

### Pagination (Future)

```kotlin
data class PaginatedUiState<T>(
    val items: List<T> = emptyList(),
    val page: Int = 0,
    val hasMore: Boolean = true,
    val isLoadingMore: Boolean = false
)
```

## 🧪 Testing Strategy

### Unit Tests (ViewModels)

```kotlin
class HomeViewModelTest {
    @Test
    fun `search updates state correctly`() = runTest {
        val viewModel = HomeViewModel()
        viewModel.onSearchQueryChange("test")
        
        assertEquals("test", viewModel.uiState.value.searchQuery)
    }
}
```

### UI Tests (Compose)

```kotlin
class HomeScreenTest {
    @Test
    fun searchBar_displaysQuery() {
        composeTestRule.setContent {
            HomeScreen(...)
        }
        
        composeTestRule
            .onNodeWithTag("searchField")
            .performTextInput("test")
            .assertTextEquals("test")
    }
}
```

## 🚀 Performance Considerations

### 1. Compose Performance

```kotlin
// ✅ Good: Stable parameters
@Composable
fun Card(item: Item) { /* ... */ }

// ❌ Bad: Unstable lambda
@Composable
fun Card(item: Item, onClick: () -> Unit) { /* Recomposes */ }

// ✅ Better: Remember lambda
onClick = remember(item) { { /* ... */ } }
```

### 2. StateFlow vs State

```kotlin
// ✅ StateFlow для ViewModel → Screen
class ViewModel {
    val uiState: StateFlow<UiState>
}

// ✅ remember для локального состояния
@Composable
fun Screen() {
    var text by remember { mutableStateOf("") }
}
```

### 3. LazyColumn Performance

```kotlin
LazyColumn {
    items(
        items = list,
        key = { it.id }  // ✅ Stable keys для performance
    ) { item ->
        ItemCard(item)
    }
}
```

## 🔮 Future Improvements

### 1. Add Repository Layer

```
ViewModel → UseCase → Repository → (Remote + Local)
```

### 2. Dependency Injection

```kotlin
// Koin
val appModule = module {
    single { ApiService() }
    viewModel { HomeViewModel(get()) }
}
```

### 3. Offline Support

```kotlin
class NewsRepository {
    suspend fun getNews(): Flow<List<News>> {
        return combine(
            remoteDataSource.getNews(),
            localDataSource.getNews()
        ) { remote, local ->
            remote.ifEmpty { local }
        }
    }
}
```

### 4. Error Handling

```kotlin
sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error<T>(val error: AppError) : Result<T>()
    data class Loading<T> : Result<T>()
}
```

## 📊 Metrics

- **Screens**: 7
- **ViewModels**: 6
- **Data Models**: 10
- **API Endpoints**: 14
- **Reusable Components**: 10+
- **Lines of Code**: ~2500+

---

**Версия**: 1.0.0  
**Последнее обновление**: Декабрь 2024
