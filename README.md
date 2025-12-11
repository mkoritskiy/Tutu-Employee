# 🚀 Tutu Employee - Мультиплатформенное приложение для сотрудников

Современное корпоративное приложение, разработанное с использованием **Compose Multiplatform** и *
*Clean Architecture** для платформ Android, iOS и Web.

## ✨ Основные возможности

### 📱 Экраны приложения

- **Авторизация** 🔐 - OAuth вход через браузер (Keycloak) ⭐ *Новое!*
- **Главная страница** 🏠 - Новости компании, дни рождения, поиск сотрудников
- **Профиль** 👤 - Личная информация, достижения, задачи, отпуска, курсы
- **Офис** 🏢 - Бронирование рабочих мест, новости офиса
- **Магазин мерча** 🛒 - Покупка товаров за внутренние баллы
- **Избранное** ⭐ - Персональные ссылки и закладки

### 🛠 Технологии

- **Kotlin** 2.2.21
- **Compose Multiplatform** 1.9.3
- **Ktor** 3.1.1 - HTTP клиент
- **Koin** 4.0.0 - Dependency Injection
- **Material 3** Design 🎨 *Новый стильный UI!*
- **Clean Architecture** + **MVVM**

## 🏛 Архитектура

Проект построен по принципам **Clean Architecture** с четким разделением на слои:

```
📦 Domain Layer        → Бизнес-логика (Use Cases, Repository Interfaces)
📦 Data Layer          → Работа с данными (API, DTO, Repository Implementations)
📦 Presentation Layer  → UI (ViewModels, Screens)
📦 DI Layer            → Dependency Injection (Koin Modules)
```

### 📚 Документация по архитектуре

- **[🏛 Clean Architecture Guide](CLEAN_ARCHITECTURE.md)** - Подробное описание архитектуры
- **[📚 Migration Guide](MIGRATION_GUIDE.md)** - Как работать с новой архитектурой
- **[🚀 DI Quick Reference](DI_QUICK_REFERENCE.md)** - Быстрый справочник по DI

## 📦 Платформы

- ✅ **Android** (API 24+)
- ✅ **Web** (JavaScript & WebAssembly)
- 🔄 **iOS** (в разработке)

## 🆕 Последние обновления (11.12.2024)

### ✅ Унифицированное форматирование дат

- Все даты теперь в формате **dd.mm.yyyy**
- Поддержка ISO дат, дат с временем и коротких дат
- См. [Date Formatting Guide](docs/DATE_FORMATTING_GUIDE.md)

### ✅ WebView для Android

- Полноценный WebView с JavaScript
- Поддержка iOS (WKWebView)
- См. [WebView Guide](docs/WEBVIEW_GUIDE.md)

Подробнее в [CHANGELOG.md](CHANGELOG.md)

---

## 🚀 Быстрый старт

> **⭐ Новое!** Приложение работает с **мок-данными** по умолчанию - backend не нужен!
>
> Подробнее: [API Mocks README](API_MOCKS_README.md)

### Требования

- JDK 17+
- Android SDK (для Android)
- Gradle 8.x

### Сборка и запуск

#### Android

```bash
./gradlew :composeApp:assembleDebug
```

APK будет в: `composeApp/build/outputs/apk/debug/`

#### Web (JavaScript)

```bash
./gradlew :composeApp:jsBrowserDevelopmentRun
```

Откроется в браузере: `http://localhost:8080`

#### Web (WebAssembly - быстрее)

```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

#### iOS (из Xcode)

Откройте папку `/iosApp` в Xcode и запустите проект.

## 📚 Документация

### 🔐 OAuth Авторизация через браузер ⭐ *Новое!*

- **[📚 OAuth Index](OAUTH_INDEX.md)** ⭐ *Главная страница OAuth документации - начните здесь!*
- **[🚀 OAuth Quick Start](OAUTH_QUICK_START.md)** - Быстрый старт за 5 минут
- **[📖 OAuth Browser Guide](OAUTH_BROWSER_GUIDE.md)** - Полное техническое руководство
- **[💡 OAuth Examples](OAUTH_EXAMPLE.md)** - Примеры использования
- **[🧪 OAuth Test Guide](OAUTH_TEST_GUIDE.md)** - Руководство по тестированию
- **[👥 OAuth Team Guide](OAUTH_TEAM_GUIDE.md)** - Инструкция для всей команды
- **[📊 OAuth Summary](OAUTH_BROWSER_SUMMARY.md)** - Итоговая сводка

### 🎨 Material3 UI Design

- **[📚 Material3 Index](MATERIAL3_INDEX.md)** ⭐ *Главная страница UI документации*
- **[🚀 Quick Start Material3](QUICK_START_MATERIAL3.md)** - Быстрый старт за 5 минут
- **[🎨 UI Components Guide](UI_COMPONENTS_GUIDE.md)** - Гид по всем компонентам
- **[🌈 Color Palette Reference](COLOR_PALETTE_REFERENCE.md)** - Цветовая палитра
- **[📊 Material3 Summary](MATERIAL3_SUMMARY.md)** - Итоговая сводка обновления

### Архитектура и разработка

- **[🏛 Clean Architecture](CLEAN_ARCHITECTURE.md)** - Полное описание архитектуры проекта
- **[📚 Migration Guide](MIGRATION_GUIDE.md)** - Руководство по работе с DI и Clean Architecture
- **[🚀 DI Quick Reference](DI_QUICK_REFERENCE.md)** - Быстрый справочник по Dependency Injection
- **[🏗 Architecture Overview](ARCHITECTURE.md)** - Оригинальная документация архитектуры

### API и Моки ⭐ *Новое!*

- **[🗂️ Mock API Index](MOCK_API_INDEX.md)** ⭐ *Навигация по всей документации моков*
- **[🎭 API Mocks - Быстрый старт](API_MOCKS_README.md)** - Работа без backend!
- **[🇷🇺 Моки API](MOКИ_API.md)** - Краткое руководство на русском
- **[📋 Quick Reference](MOCK_API_QUICK_REFERENCE.md)** - Быстрая справка
- **[📚 Mock API Guide](MOCK_API_GUIDE.md)** - Полная документация по мокам
- **[✅ Setup Checklist](MOCK_SETUP_CHECKLIST.md)** - Чеклист настройки
- **[📊 Mock API Summary](MOCK_API_SUMMARY.md)** - Итоговая сводка
- **[📊 Модели данных API](API_MODELS.md)** - Описание всех моделей данных

### Настройка и документация

- **[📖 Полная документация приложения](EMPLOYEE_APP_README.md)** - Детальное описание функционала
- **[🔧 Руководство по настройке](SETUP_GUIDE.md)** - Setup guide для разработчиков
- **[📝 Сводка проекта](PROJECT_SUMMARY.md)** - Краткая сводка проекта

### Дополнительные руководства ⭐ *Новое!*

- **[📅 Date Formatting Guide](docs/DATE_FORMATTING_GUIDE.md)** - Форматирование дат
- **[🌐 WebView Guide](docs/WEBVIEW_GUIDE.md)** - Реализация WebView
- **[📋 Testing Checklist](TESTING_CHECKLIST.md)** - Чеклист для тестирования
- **[📊 Summary](SUMMARY.md)** - Резюме последних изменений

## 🏗 Структура проекта (Clean Architecture)

```
TutuEmployee/
└── composeApp/src/commonMain/kotlin/ru/tutu/tutuemployee/
    ├── domain/              # 🎯 Domain Layer (бизнес-логика)
    │   ├── model/           # Domain entities
    │   ├── repository/      # Repository interfaces
    │   └── usecase/         # Business use cases
    │
    ├── data/                # 💾 Data Layer (работа с данными)
    │   ├── remote/
    │   │   ├── api/         # API Service (Ktor)
    │   │   └── dto/         # Data Transfer Objects
    │   └── repository/      # Repository implementations
    │
    ├── presentation/        # 🎨 Presentation Layer (UI)
    │   ├── auth/
    │   ├── home/
    │   ├── profile/
    │   ├── office/
    │   ├── merch/
    │   └── favorites/
    │
    ├── di/                  # 🔌 DI Layer (Koin)
    │   ├── NetworkModule.kt
    │   ├── RepositoryModule.kt
    │   ├── UseCaseModule.kt
    │   ├── ViewModelModule.kt
    │   └── AppModule.kt
    │
    └── navigation/          # 🧭 Navigation
```

## 🎯 Ключевые преимущества архитектуры

### ✅ Чистая архитектура

- **Разделение ответственности**: каждый слой имеет четкую задачу
- **Тестируемость**: легко писать unit-тесты для Use Cases
- **Независимость от фреймворков**: бизнес-логика не зависит от UI или API

### ✅ Dependency Injection (Koin)

- **Автоматическое внедрение зависимостей**
- **Модульная структура**
- **Легкое тестирование**

### ✅ Use Cases Pattern

- **Изолированная бизнес-логика**
- **Переиспользуемые компоненты**
- **Валидация на уровне бизнес-логики**

### ✅ DTO Pattern

- **Разделение API моделей и Domain моделей**
- **Mappers для преобразования**
- **Гибкость при изменении API**

## 🔌 Пример работы с архитектурой

### Добавление новой фичи

```kotlin
// 1. Domain Layer - Use Case
class GetProductsUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(): Result<List<Product>> {
        return repository.getProducts()
    }
}

// 2. Presentation Layer - ViewModel
class ProductViewModel(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState = _uiState.asStateFlow()
    
    init {
        loadProducts()
    }
    
    private fun loadProducts() {
        viewModelScope.launch {
            getProductsUseCase()
                .onSuccess { products ->
                    _uiState.value = _uiState.value.copy(products = products)
                }
        }
    }
}

// 3. DI - Регистрация
val useCaseModule = module {
    factoryOf(::GetProductsUseCase)
}

val viewModelModule = module {
    viewModelOf(::ProductViewModel)
}

// 4. UI - Screen
@Composable
fun ProductScreen(
    viewModel: ProductViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    // ... UI код
}
```

## 🔧 Настройка backend URL

Откройте `composeApp/src/commonMain/kotlin/ru/tutu/tutuemployee/di/NetworkModule.kt`:

```kotlin
single {
    HttpClient {
        install(DefaultRequest) {
            url("https://your-api-url.com/employee")  // ← Измените здесь
        }
    }
}
```

## 🧪 Тестирование

### Unit тесты для Use Cases

```kotlin
class LoginUseCaseTest {
    @Test
    fun `login with valid credentials returns success`() = runTest {
        val mockRepository = mockk<AuthRepository>()
        coEvery { mockRepository.login("user", "pass") } returns Result.success(...)
        
        val useCase = LoginUseCase(mockRepository)
        val result = useCase("user", "pass")
        
        assertTrue(result.isSuccess)
    }
}
```

### Unit тесты для ViewModels

```kotlin
class HomeViewModelTest {
    @Test
    fun `loadData updates state with news`() = runTest {
        val mockUseCase = mockk<GetNewsUseCase>()
        coEvery { mockUseCase() } returns Result.success(listOf(mockNews))
        
        val viewModel = HomeViewModel(mockUseCase, mockk(), mockk())
        advanceUntilIdle()
        
        assertEquals(1, viewModel.uiState.value.news.size)
    }
}
```

## 🚢 Deployment

### Production сборка для Android

```bash
./gradlew :composeApp:assembleRelease
```

### Production сборка для Web

```bash
./gradlew :composeApp:jsBrowserProductionWebpack
```

Результат в: `composeApp/build/dist/js/productionExecutable/`

## 📊 Статистика проекта

- **Файлов Kotlin**: 60+
- **Экранов**: 7
- **Domain Models**: 10
- **Use Cases**: 10+
- **Repositories**: 7
- **ViewModels**: 6
- **DI Modules**: 4
- **Lines of Code**: ~4000+

## 🔮 Roadmap

### ✅ Завершено

- ✅ Clean Architecture
- ✅ Dependency Injection (Koin)
- ✅ Domain Layer с Use Cases
- ✅ Repository Pattern
- ✅ DTO Pattern с mappers
- ✅ MVVM с StateFlow
- ✅ Material3 UI Design 🎨
- ✅ Стильные компоненты
- ✅ Темная тема
- ✅ Полная UI документация
- ✅ Mock API для разработки без backend
- ✅ OAuth авторизация через браузер (Keycloak)
- ✅ Authorization Code Flow с PKCE
- ✅ Deep Links для OAuth callback (Android)
- ✅ Chrome Custom Tabs integration
- ✅ Унифицированное форматирование дат (dd.mm.yyyy) ⭐ *Новое!*
- ✅ WebView для Android с полной функциональностью ⭐ *Новое!*
- ✅ Кроссплатформенный WebView (Android/iOS/Web) ⭐ *Новое!*

### В разработке

- [ ] Unit тесты для Use Cases
- [ ] UI тесты для Screens
- [ ] Локальное хранилище (Room/SQLDelight)
- [ ] Offline-first подход

### Планируется

- [ ] Push уведомления
- [ ] Локализация (EN/RU)
- [ ] Биометрическая авторизация
- [ ] iOS версия
- [ ] Анимации переходов между экранами

## 🤝 Contributing

### Добавление новой фичи

1. Создайте Domain модели в `domain/model/`
2. Создайте Repository interface в `domain/repository/`
3. Создайте Use Cases в `domain/usecase/`
4. Реализуйте Repository в `data/repository/`
5. Создайте ViewModel в `presentation/`
6. Зарегистрируйте все в соответствующих DI модулях
7. Создайте Screen

См. подробнее в [Migration Guide](MIGRATION_GUIDE.md)

## 📄 Лицензия

Проект для внутреннего использования © Tutu.ru 2025

## 🆘 Поддержка

При возникновении проблем:

1. Проверьте [Clean Architecture Guide](CLEAN_ARCHITECTURE.md)
2. Посмотрите [Migration Guide](MIGRATION_GUIDE.md)
3. Используйте [DI Quick Reference](DI_QUICK_REFERENCE.md)
4. Создайте Issue в репозитории

---

**Версия**: 2.2.0 (Clean Architecture + Material3 UI + WebView + Date Formatting)  
**Статус**: ✅ Production Ready  
**Последнее обновление**: 11 Декабря 2024

## 🔗 Полезные ссылки

- [Kotlin Multiplatform](https://www.jetbrains.com/kotlin-multiplatform/)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Koin Documentation](https://insert-koin.io/)
- [Ktor Documentation](https://ktor.io/)
- [Material 3 Design](https://m3.material.io/)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

---

Made with ❤️ by Tutu.ru Team using Clean Architecture + Material3 Design 🎨
