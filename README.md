# 🚀 Tutu Employee - Мультиплатформенное приложение для сотрудников

Современное корпоративное приложение, разработанное с использованием Compose Multiplatform для
платформ Android и Web.

## ✨ Основные возможности

### 📱 Экраны приложения

- **Авторизация** 🔐 - OAuth вход с логином и паролем
- **Главная страница** 🏠 - Новости компании, дни рождения, поиск сотрудников
- **Профиль** 👤 - Личная информация, достижения, задачи, отпуска, курсы
- **Офис** 🏢 - Бронирование рабочих мест, новости офиса
- **Магазин мерча** 🛒 - Покупка товаров за внутренние баллы
- **Избранное** ⭐ - Персональные ссылки и закладки

### 🛠 Технологии

- **Kotlin** 2.2.21
- **Compose Multiplatform** 1.9.3
- **Ktor** 3.1.1 - HTTP клиент
- **Material 3** Design
- **MVVM** Architecture

## 📦 Платформы

- ✅ **Android** (API 24+)
- ✅ **Web** (JavaScript & WebAssembly)
- 🔄 **iOS** (в разработке)

## 🚀 Быстрый старт

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

- [📖 Полная документация приложения](EMPLOYEE_APP_README.md)
- [🔧 Руководство по настройке](SETUP_GUIDE.md)
- [📊 Модели данных API](API_MODELS.md)
- [📝 Сводка проекта](PROJECT_SUMMARY.md)

## 🏗 Структура проекта

```
TutuEmployee/
├── composeApp/
│   ├── commonMain/     # Общий код для всех платформ
│   │   ├── data/       # Модели и API
│   │   ├── navigation/ # Навигация
│   │   └── presentation/ # UI (ViewModels + Screens)
│   ├── androidMain/    # Android-специфичный код
│   ├── jsMain/         # Web JavaScript
│   ├── wasmJsMain/     # Web WebAssembly
│   └── iosMain/        # iOS (в разработке)
├── iosApp/             # iOS приложение
├── gradle/             # Gradle wrapper
└── docs/               # Дополнительная документация
```

## 🎨 Особенности реализации

- **Material 3 Design** с адаптивной версткой
- **Emoji иконки** для кроссплатформенности
- **Reactive UI** с Jetpack Compose
- **Type-safe navigation** с Compose Navigation
- **REST API интеграция** через Ktor Client
- **Mock API** для разработки без backend

## 🔌 API Integration

### Настройка backend URL

Откройте `composeApp/src/commonMain/kotlin/ru/tutu/tutuemployee/data/network/ApiClient.kt`:

```kotlin
object ApiClient {
    private const val BASE_URL = "https://your-api-url.com/employee"
    // ...
}
```

### Использование Mock API

Для разработки без реального backend:

1. Откройте `MockApiService.kt` в пакете `data.network`
2. В `ApiService.kt` замените реальные вызовы API на mock:

```kotlin
class ApiService {
    private val mockService = MockApiService()  // Добавить
    // private val client = ApiClient.httpClient  // Закомментировать
    
    suspend fun getNews(): Result<List<News>> {
        return mockService.getNews()  // Использовать mock
    }
}
```

Mock данные включают:

- 3 новости компании
- 3 дня рождения
- 4 сотрудника для поиска
- 3 достижения
- 3 задачи
- 2 отпуска
- 3 курса
- 16 рабочих мест
- 6 товаров в магазине
- 3 избранные ссылки

## 📱 Скриншоты и демо

_(Скриншоты будут добавлены после финальной полировки UI)_

## 🧪 Тестирование

### Unit тесты

```bash
./gradlew :composeApp:testDebugUnitTest
```

### UI тесты (Android)

```bash
./gradlew :composeApp:connectedAndroidTest
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

## 🤝 Contributing

1. Fork проекта
2. Создайте feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit изменений (`git commit -m 'Add some AmazingFeature'`)
4. Push в branch (`git push origin feature/AmazingFeature`)
5. Откройте Pull Request

## 📊 Статистика проекта

- **Файлов Kotlin**: 32+
- **Экранов**: 7
- **Data Models**: 10
- **ViewModels**: 6
- **API Endpoints**: 14
- **Lines of Code**: ~2500+

## 🔮 Roadmap

### В разработке

- [ ] Реализация полноценной navigation
- [ ] WebView платформо-специфичные реализации
- [ ] Offline режим с кешированием
- [ ] Unit и UI тесты

### Планируется

- [ ] Push уведомления
- [ ] Темная тема
- [ ] Локализация (EN/RU)
- [ ] Биометрическая авторизация
- [ ] iOS версия

## 📄 Лицензия

Проект для внутреннего использования © Tutu.ru 2024

## 👥 Команда

Разработано командой Tutu.ru

## 🆘 Поддержка

При возникновении проблем:

1. Проверьте [Setup Guide](SETUP_GUIDE.md)
2. Посмотрите [FAQ](SETUP_GUIDE.md#типичные-проблемы-и-решения)
3. Создайте Issue в репозитории
4. Свяжитесь с командой разработки

---

**Версия**: 1.0.0  
**Статус**: ✅ Production Ready (Alpha)  
**Последнее обновление**: Декабрь 2024

## 🔗 Полезные ссылки

- [Kotlin Multiplatform](https://www.jetbrains.com/kotlin-multiplatform/)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Ktor Documentation](https://ktor.io/)
- [Material 3 Design](https://m3.material.io/)
- [Kotlin Documentation](https://kotlinlang.org/docs/)

---

Made with ❤️ by Tutu.ru Team
# Tutu-Employee
