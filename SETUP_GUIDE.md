# Setup Guide - Руководство по настройке

## Требования

### Общие требования

- JDK 17 или выше
- Gradle 8.x (включен в проект)
- Git

### Для Android разработки

- Android Studio Hedgehog (2023.1.1) или новее
- Android SDK API 24+
- Android SDK Build Tools 36

### Для iOS разработки (опционально)

- macOS
- Xcode 15+
- CocoaPods

### Для Web разработки

- Современный браузер (Chrome, Firefox, Safari)

## Быстрый старт

### 1. Клонирование репозитория

```bash
git clone <repository-url>
cd TutuEmployee
```

### 2. Настройка backend URL

Откройте `composeApp/src/commonMain/kotlin/ru/tutu/tutuemployee/data/network/ApiClient.kt`:

```kotlin
object ApiClient {
    private const val BASE_URL = "https://your-api-url.com/employee"
    // ...
}
```

### 3. Сборка проекта

#### Проверка зависимостей

```bash
./gradlew clean build
```

#### Android

```bash
./gradlew :composeApp:assembleDebug
```

APK будет в: `composeApp/build/outputs/apk/debug/composeApp-debug.apk`

#### Web (JavaScript)

```bash
./gradlew :composeApp:jsBrowserDevelopmentRun
```

Откроется в браузере: `http://localhost:8080`

#### Web (WebAssembly)

```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

## Настройка IDE

### Android Studio / IntelliJ IDEA

1. Откройте проект через `File > Open`
2. Выберите директорию `TutuEmployee`
3. Дождитесь синхронизации Gradle
4. Выберите конфигурацию запуска:
    - `composeApp` для Android
    - `jsBrowserDevelopmentRun` для Web

### VS Code (опционально)

1. Установите расширения:
    - Kotlin Language
    - Gradle for Java
2. Откройте папку проекта
3. Используйте терминал для сборки

## Конфигурация приложения

### Изменение namespace и applicationId

`composeApp/build.gradle.kts`:

```kotlin
android {
    namespace = "ru.tutu.tutuemployee"
    
    defaultConfig {
        applicationId = "ru.tutu.tutuemployee"
        // ...
    }
}
```

### Изменение версии приложения

```kotlin
defaultConfig {
    versionCode = 1
    versionName = "1.0"
}
```

### Настройка ключей подписи (для релизной сборки)

Создайте `keystore.properties` в корне проекта:

```properties
storeFile=/path/to/keystore.jks
storePassword=yourStorePassword
keyAlias=yourKeyAlias
keyPassword=yourKeyPassword
```

Добавьте в `.gitignore`:

```
keystore.properties
*.jks
*.keystore
```

## Mock Backend (для разработки)

Если у вас нет готового backend, можно использовать mock данные.

Создайте `MockApiService.kt`:

```kotlin
class MockApiService {
    suspend fun getNews(): Result<List<News>> {
        return Result.success(listOf(
            News(
                id = "1",
                title = "Новость 1",
                content = "Содержание новости...",
                publishedAt = "2024-01-15T10:00:00Z",
                category = NewsCategory.COMPANY
            ),
            // ... больше новостей
        ))
    }
    
    // ... остальные методы
}
```

Замените в `ApiService.kt`:

```kotlin
// private val client = ApiClient.httpClient  // Закомментировать
private val mockService = MockApiService()    // Добавить

suspend fun getNews(): Result<List<News>> {
    return mockService.getNews()  // Использовать mock
}
```

## Отладка

### Android

1. Подключите устройство или запустите эмулятор
2. В Android Studio: `Run > Debug 'composeApp'`
3. Используйте Logcat для просмотра логов

### Web

1. Запустите dev сервер
2. Откройте Developer Tools в браузере (F12)
3. Используйте вкладку Console для отладки

### Логирование Ktor

В `ApiClient.kt` измените уровень логирования:

```kotlin
install(Logging) {
    level = LogLevel.ALL  // ALL, HEADERS, BODY, INFO, NONE
}
```

## Типичные проблемы и решения

### Проблема: Gradle sync failed

**Решение:**

```bash
./gradlew clean
rm -rf .gradle
./gradlew build
```

### Проблема: Navigation не компилируется

**Решение:**
Используется упрощенная версия навигации. Проверьте версию в `gradle/libs.versions.toml`:

```toml
navigation-compose = "2.8.0-alpha08"
```

### Проблема: OutOfMemory при сборке

**Решение:**
Увеличьте heap в `gradle.properties`:

```properties
org.gradle.jvmargs=-Xmx4096m -XX:MaxMetaspaceSize=512m
```

### Проблема: Медленная сборка

**Решение:**
Включите параллельную сборку в `gradle.properties`:

```properties
org.gradle.parallel=true
org.gradle.caching=true
```

## Тестирование

### Unit тесты

```bash
./gradlew :composeApp:testDebugUnitTest
```

### UI тесты (Android)

```bash
./gradlew :composeApp:connectedAndroidTest
```

## Deployment

### Android APK

```bash
./gradlew :composeApp:assembleRelease
```

### Android App Bundle (для Google Play)

```bash
./gradlew :composeApp:bundleRelease
```

### Web Production

```bash
./gradlew :composeApp:jsBrowserProductionWebpack
```

Результат в: `composeApp/build/dist/js/productionExecutable/`

## CI/CD

### GitHub Actions пример

`.github/workflows/build.yml`:

```yaml
name: Build

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
        
    - name: Grant execute permission for gradlew
      run: chmod +x gradlew
      
    - name: Build with Gradle
      run: ./gradlew build
      
    - name: Upload APK
      uses: actions/upload-artifact@v3
      with:
        name: app-debug
        path: composeApp/build/outputs/apk/debug/*.apk
```

## Полезные команды

### Просмотр всех задач Gradle

```bash
./gradlew tasks
```

### Очистка проекта

```bash
./gradlew clean
```

### Проверка зависимостей

```bash
./gradlew :composeApp:dependencies
```

### Обновление Gradle Wrapper

```bash
./gradlew wrapper --gradle-version=8.5
```

## Структура каталогов

```
TutuEmployee/
├── composeApp/              # Основное приложение
│   ├── src/
│   │   ├── commonMain/      # Общий код для всех платформ
│   │   ├── androidMain/     # Android-специфичный код
│   │   ├── jsMain/          # Web JS код
│   │   ├── wasmJsMain/      # Web Wasm код
│   │   └── iosMain/         # iOS-специфичный код
│   └── build.gradle.kts     # Конфигурация модуля
├── gradle/                  # Gradle wrapper
├── build.gradle.kts         # Корневая конфигурация
├── settings.gradle.kts      # Настройки проекта
└── gradle.properties        # Gradle настройки
```

## Следующие шаги

1. Настройте подключение к реальному API
2. Добавьте тесты
3. Настройте CI/CD
4. Реализуйте offline режим
5. Добавьте push уведомления
6. Настройте аналитику

## Полезные ссылки

- [Compose Multiplatform Documentation](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Ktor Documentation](https://ktor.io/docs/)
- [Material 3 Design](https://m3.material.io/)

## Поддержка

При возникновении проблем:

1. Проверьте этот гайд
2. Посмотрите логи Gradle/Logcat
3. Создайте issue в репозитории
4. Свяжитесь с командой разработки
