# 🚀 Quick Reference: Dependency Injection

## 📦 Добавление новых компонентов

### 1. Добавить новый ViewModel

```kotlin
// 1. Создайте ViewModel
class MyViewModel(
    private val myUseCase: MyUseCase
) : ViewModel() {
    // ...
}

// 2. Зарегистрируйте в di/ViewModelModule.kt
val viewModelModule = module {
    // ... existing
    viewModelOf(::MyViewModel)  // ⭐ Add this line
}

// 3. Используйте в Screen
@Composable
fun MyScreen(
    viewModel: MyViewModel = koinViewModel()
) {
    // ...
}
```

### 2. Добавить новый Use Case

```kotlin
// 1. Создайте Use Case
class MyUseCase(
    private val repository: MyRepository
) {
    suspend operator fun invoke(): Result<Data> {
        return repository.getData()
    }
}

// 2. Зарегистрируйте в di/UseCaseModule.kt
val useCaseModule = module {
    // ... existing
    factoryOf(::MyUseCase)  // ⭐ Add this line
}
```

### 3. Добавить новый Repository

```kotlin
// 1. Создайте интерфейс в domain/repository/
interface MyRepository {
    suspend fun getData(): Result<Data>
}

// 2. Создайте реализацию в data/repository/
class MyRepositoryImpl(
    private val apiService: ApiService
) : MyRepository {
    override suspend fun getData(): Result<Data> {
        return apiService.getData()
            .map { it.toDomain() }
    }
}

// 3. Зарегистрируйте в di/RepositoryModule.kt
val repositoryModule = module {
    // ... existing
    singleOf(::MyRepositoryImpl) bind MyRepository::class  // ⭐ Add this line
}
```

### 4. Добавить новый API endpoint

```kotlin
// 1. Добавьте метод в data/remote/api/ApiService.kt
suspend fun getData(): Result<DataDto> {
    return try {
        val response = httpClient.get("/data")
        Result.success(response.body<DataDto>())
    } catch (e: Exception) {
        Result.failure(e)
    }
}

// ApiService уже зарегистрирован в NetworkModule
```

## 🎯 Типы зависимостей в Koin

### `single` - Синглтон

Один экземпляр на всё приложение:

```kotlin
single { ApiService(get()) }
single<TokenStorage> { InMemoryTokenStorage() }
```

### `factory` - Новый экземпляр каждый раз

```kotlin
factory { MyUseCase(get()) }
factoryOf(::MyUseCase)  // Короткая форма
```

### `viewModel` - ViewModel с lifecycle

```kotlin
viewModel { MyViewModel(get()) }
viewModelOf(::MyViewModel)  // Короткая форма
```

### `scoped` - Экземпляр на scope

```kotlin
scope<MyActivity> {
    scoped { MyService() }
}
```

## 📝 Шаблоны кода

### Полный пример добавления новой фичи

```kotlin
// ============================================
// 1. DOMAIN LAYER
// ============================================

// domain/model/Product.kt
data class Product(
    val id: String,
    val name: String,
    val price: Int
)

// domain/repository/ProductRepository.kt
interface ProductRepository {
    suspend fun getProducts(): Result<List<Product>>
    suspend fun getProduct(id: String): Result<Product>
}

// domain/usecase/product/GetProductsUseCase.kt
class GetProductsUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(): Result<List<Product>> {
        return repository.getProducts()
    }
}

// domain/usecase/product/GetProductUseCase.kt
class GetProductUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(id: String): Result<Product> {
        if (id.isBlank()) {
            return Result.failure(IllegalArgumentException("ID cannot be empty"))
        }
        return repository.getProduct(id)
    }
}

// ============================================
// 2. DATA LAYER
// ============================================

// data/remote/dto/ProductDto.kt
@Serializable
data class ProductDto(
    val id: String,
    val name: String,
    val price: Int
)

fun ProductDto.toDomain() = Product(
    id = id,
    name = name,
    price = price
)

// data/remote/api/ApiService.kt (добавьте методы)
suspend fun getProducts(): Result<List<ProductDto>> {
    return try {
        val response = httpClient.get("/products")
        Result.success(response.body<List<ProductDto>>())
    } catch (e: Exception) {
        Result.failure(e)
    }
}

suspend fun getProduct(id: String): Result<ProductDto> {
    return try {
        val response = httpClient.get("/products/$id")
        Result.success(response.body<ProductDto>())
    } catch (e: Exception) {
        Result.failure(e)
    }
}

// data/repository/ProductRepositoryImpl.kt
class ProductRepositoryImpl(
    private val apiService: ApiService
) : ProductRepository {
    
    override suspend fun getProducts(): Result<List<Product>> {
        return apiService.getProducts()
            .map { list -> list.map { it.toDomain() } }
    }
    
    override suspend fun getProduct(id: String): Result<Product> {
        return apiService.getProduct(id)
            .map { it.toDomain() }
    }
}

// ============================================
// 3. PRESENTATION LAYER
// ============================================

// presentation/product/ProductUiState.kt
data class ProductListUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

// presentation/product/ProductViewModel.kt
class ProductViewModel(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ProductListUiState())
    val uiState = _uiState.asStateFlow()
    
    init {
        loadProducts()
    }
    
    private fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            getProductsUseCase()
                .onSuccess { products ->
                    _uiState.value = _uiState.value.copy(
                        products = products,
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
    
    fun refresh() {
        loadProducts()
    }
}

// presentation/product/ProductScreen.kt
@Composable
fun ProductScreen(
    viewModel: ProductViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Products") })
        }
    ) { padding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            uiState.error != null -> {
                ErrorView(
                    message = uiState.error!!,
                    onRetry = { viewModel.refresh() }
                )
            }
            
            else -> {
                LazyColumn(
                    modifier = Modifier.padding(padding)
                ) {
                    items(uiState.products) { product ->
                        ProductCard(product = product)
                    }
                }
            }
        }
    }
}

// ============================================
// 4. DI REGISTRATION
// ============================================

// di/RepositoryModule.kt
val repositoryModule = module {
    // ... existing
    singleOf(::ProductRepositoryImpl) bind ProductRepository::class
}

// di/UseCaseModule.kt
val useCaseModule = module {
    // ... existing
    factoryOf(::GetProductsUseCase)
    factoryOf(::GetProductUseCase)
}

// di/ViewModelModule.kt
val viewModelModule = module {
    // ... existing
    viewModelOf(::ProductViewModel)
}
```

## 🔍 Получение зависимостей

### В Composable функции

```kotlin
@Composable
fun MyScreen() {
    val viewModel: MyViewModel = koinViewModel()
    val repository: MyRepository = koinInject()
}
```

### В обычном классе

```kotlin
class MyClass : KoinComponent {
    private val repository: MyRepository by inject()
}
```

### Вручную (не рекомендуется)

```kotlin
val koin = GlobalContext.get()
val repository = koin.get<MyRepository>()
```

## 🎨 Advanced Patterns

### Qualifier (именованные зависимости)

```kotlin
// Регистрация
single(named("prod")) { ApiService("https://prod.api.com") }
single(named("dev")) { ApiService("https://dev.api.com") }

// Использование
class MyViewModel(
    @Named("prod") private val apiService: ApiService
) : ViewModel()
```

### Module включает другие модули

```kotlin
val featureModule = module {
    includes(
        networkModule,
        repositoryModule,
        useCaseModule
    )
}
```

### Условная регистрация

```kotlin
val platformModule = module {
    if (Platform.isAndroid) {
        single<Storage> { AndroidStorage() }
    } else {
        single<Storage> { IOSStorage() }
    }
}
```

## 🐛 Debug

### Проверка всех зависимостей при старте

```kotlin
startKoin {
    modules(appModules)
    checkModules()  // ⭐ Проверит все зависимости
}
```

### Логирование

```kotlin
startKoin {
    // Android
    androidLogger(Level.DEBUG)
    
    // Other platforms
    logger(Level.INFO)
    
    modules(appModules)
}
```

### Получить все определения модуля

```kotlin
val koin = GlobalContext.get()
koin.getAll<MyInterface>().forEach { 
    println(it) 
}
```

## 📊 Performance Tips

### ❌ Избегайте

```kotlin
// Создание в каждом экране
@Composable
fun MyScreen() {
    val heavyObject = remember { HeavyObject() }  // ❌
}
```

### ✅ Делайте правильно

```kotlin
// Зарегистрируйте в Koin
single { HeavyObject() }

// Переиспользуйте
@Composable
fun MyScreen() {
    val heavyObject: HeavyObject = koinInject()  // ✅
}
```

## 🧪 Testing с Koin

### Мок зависимостей

```kotlin
@Test
fun testViewModel() = runTest {
    // Создайте тестовый модуль
    val testModule = module {
        single<MyRepository> { mockk<MyRepository>() }
        viewModelOf(::MyViewModel)
    }
    
    startKoin {
        modules(testModule)
    }
    
    val viewModel = koin.get<MyViewModel>()
    // ... тест
    
    stopKoin()
}
```

---

**Quick Tip**: Всегда используйте `koinViewModel()` в Composable и внедряйте зависимости через
конструктор!
