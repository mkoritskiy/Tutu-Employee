# 🔐 Keycloak Integration для TutuEmployee

Полноценная интеграция OAuth2/OIDC авторизации через Keycloak для Kotlin Multiplatform приложения
TutuEmployee.

## 📦 Что включено

### ✅ Реализованные компоненты

- **KeycloakConfig** - Конфигурация подключения к Keycloak серверу
- **KeycloakTokens** - Модели для access, refresh и id токенов
- **KeycloakTokenStorage** - Интерфейс для безопасного хранения токенов
- **KeycloakClient** - HTTP клиент для всех Keycloak API endpoints
- **KeycloakOAuthHandler** - Обработчик OAuth Authorization Code Flow
- **PKCEHelper** - Генератор PKCE для защиты OAuth flow
- **Use Cases** - Domain layer для авторизации
- **Repository** - Data layer с поддержкой Keycloak
- **ViewModel** - Presentation layer с Keycloak методами
- **UI** - Экран авторизации с кнопками Keycloak

### ✨ Возможности

- 🔐 **Authorization Code Flow с PKCE** (рекомендуется)
- 🔑 **Resource Owner Password Credentials** (для тестирования)
- 🔄 **Автоматическое обновление токенов**
- 👤 **UserInfo endpoint** для получения данных пользователя
- 🚪 **Logout** с отзывом токенов
- 🌍 **Multiplatform** (Android, iOS, Web, Desktop)
- 🏗️ **Clean Architecture** с разделением слоев
- 🧪 **Тестируемый код** с использованием интерфейсов

## 🚀 Быстрый старт

### 1. Запустите Keycloak

```bash
docker run -d -p 8080:8080 \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  quay.io/keycloak/keycloak:latest start-dev
```

### 2. Настройте Keycloak

1. Создайте realm: `tutu`
2. Создайте client: `tutu-employee-app`
3. Создайте пользователя: `test@tutu.ru` / `test123`

Подробнее: [KEYCLOAK_QUICK_START.md](KEYCLOAK_QUICK_START.md)

### 3. Обновите конфигурацию

```kotlin
// composeApp/src/commonMain/kotlin/ru/tutu/tutuemployee/data/auth/KeycloakConfig.kt
KeycloakConfig(
    serverUrl = "http://localhost:8080",
    realm = "tutu",
    clientId = "tutu-employee-app"
)
```

### 4. Используйте в коде

```kotlin
// ViewModel
class MyViewModel(
    private val loginWithKeycloakUseCase: LoginWithKeycloakUseCase
) : ViewModel() {
    
    fun login(username: String, password: String) {
        viewModelScope.launch {
            loginWithKeycloakUseCase(username, password)
                .onSuccess { (token, user) ->
                    // Успешная авторизация
                }
        }
    }
}
```

## 📚 Документация

- **[KEYCLOAK_QUICK_START.md](KEYCLOAK_QUICK_START.md)** - Быстрый старт за 15 минут
- **[KEYCLOAK_INTEGRATION.md](KEYCLOAK_INTEGRATION.md)** - Полная документация
- **[keycloak-config-example.env](keycloak-config-example.env)** - Пример конфигурации

## 🏗️ Архитектура

```
Presentation Layer (UI)
    ↓
Domain Layer (Use Cases)
    ↓
Data Layer (Repository + Keycloak Client)
    ↓
Keycloak Server (OAuth2/OIDC)
```

### Файловая структура

```
composeApp/src/commonMain/kotlin/ru/tutu/tutuemployee/
│
├── data/auth/                          # Data Layer
│   ├── KeycloakConfig.kt              # Конфигурация
│   ├── KeycloakTokens.kt              # Модели токенов
│   ├── KeycloakTokenStorage.kt        # Хранилище
│   ├── KeycloakClient.kt              # HTTP клиент
│   ├── KeycloakOAuthHandler.kt        # OAuth flow
│   └── PKCEHelper.kt                  # PKCE генератор
│
├── domain/                             # Domain Layer
│   ├── repository/
│   │   └── AuthRepository.kt          # Interface (+ Keycloak методы)
│   └── usecase/auth/
│       ├── LoginWithKeycloakUseCase.kt
│       ├── GetKeycloakAuthUrlUseCase.kt
│       └── HandleKeycloakCallbackUseCase.kt
│
├── presentation/auth/                  # Presentation Layer
│   ├── AuthViewModel.kt               # ViewModel (+ Keycloak методы)
│   └── AuthScreen.kt                  # UI (+ Keycloak кнопки)
│
└── di/                                 # Dependency Injection
    ├── NetworkModule.kt               # Keycloak DI
    ├── RepositoryModule.kt            # Repository DI
    └── UseCaseModule.kt               # Use Case DI
```

## 🔑 Методы авторизации

### 1. Authorization Code Flow с PKCE (рекомендуется)

**Самый безопасный метод для production.**

```kotlin
// Получить URL авторизации
viewModel.startKeycloakOAuth()

// Открыть браузер
val authUrl = uiState.keycloakAuthUrl
openBrowser(authUrl)

// Обработать callback
viewModel.handleKeycloakCallback(callbackUrl)
```

### 2. Password Flow (только для тестирования)

**⚠️ Не рекомендуется для production!**

```kotlin
viewModel.loginWithKeycloak()
```

## 🔒 Безопасность

### ⚠️ Текущая конфигурация (Development)

- ✅ `InMemoryKeycloakTokenStorage` - Токены в памяти
- ⚠️ HTTP localhost - Только для development
- ⚠️ Password flow включен - Только для тестирования

### ✅ Рекомендации для Production

1. **Используйте Authorization Code Flow с PKCE**
   ```kotlin
   usePKCE = true  // Включено по умолчанию
   ```

2. **Безопасное хранилище токенов**
    - Android: `EncryptedSharedPreferences`
    - iOS: `Keychain`
    - Web: `sessionStorage` (не localStorage!)

3. **HTTPS обязателен**
   ```kotlin
   serverUrl = "https://auth.tutu.ru"  // Только HTTPS!
   ```

4. **Короткие сроки жизни токенов**
   ```yaml
   Access Token Lifespan: 5 minutes
   Refresh Token Lifespan: 30 minutes
   ```

## 🧪 Тестирование

### Запуск тестов

```bash
./gradlew test
```

### Тестовые данные

```
Username: test@tutu.ru
Password: test123
Realm: tutu
Client ID: tutu-employee-app
```

### Проверка интеграции

```bash
# 1. Запустите Keycloak
docker start keycloak

# 2. Запустите приложение
./gradlew :composeApp:run

# 3. Войдите
# Username: test@tutu.ru
# Password: test123
```

## 🔧 Конфигурация

### Environment Variables (опционально)

```bash
export KEYCLOAK_URL="http://localhost:8080"
export KEYCLOAK_REALM="tutu"
export KEYCLOAK_CLIENT_ID="tutu-employee-app"
```

### В коде

```kotlin
KeycloakConfig(
    serverUrl = System.getenv("KEYCLOAK_URL") ?: "http://localhost:8080",
    realm = System.getenv("KEYCLOAK_REALM") ?: "tutu",
    clientId = System.getenv("KEYCLOAK_CLIENT_ID") ?: "tutu-employee-app"
)
```

## 📱 Platform Support

### ✅ Android

- Deep linking: `tutuemployee://oauth/callback`
- EncryptedSharedPreferences для токенов
- Chrome Custom Tabs для OAuth

### ✅ iOS

- URL Scheme: `tutuemployee://oauth/callback`
- Keychain для токенов
- ASWebAuthenticationSession для OAuth

### ✅ Web

- Redirect: `https://your-app.com/auth/callback`
- SessionStorage для токенов
- Window redirect для OAuth

### ✅ Desktop

- HTTP redirect: `http://localhost:*/callback`
- Encrypted file для токенов
- Desktop browser для OAuth

## 🐛 Troubleshooting

| Проблема | Решение |
|----------|---------|
| Connection refused | Проверьте, запущен ли Keycloak: `docker start keycloak` |
| Invalid redirect_uri | Добавьте URI в настройках Keycloak client |
| Client authentication failed | Установите `clientSecret = null` для public clients |
| CORS errors | Настройте Web Origins в Keycloak |
| Token not saved | Проверьте реализацию `KeycloakTokenStorage` |

Подробнее: [KEYCLOAK_INTEGRATION.md - Troubleshooting](KEYCLOAK_INTEGRATION.md#-troubleshooting)

## 🎯 Roadmap

- [x] Authorization Code Flow с PKCE
- [x] Password Flow (development only)
- [x] Token refresh
- [x] UserInfo endpoint
- [x] Logout с token revocation
- [ ] Social Login (Google, Facebook)
- [ ] Multi-factor Authentication (MFA)
- [ ] Biometric authentication
- [ ] Certificate pinning
- [ ] Token encryption at rest

## 🤝 Contributing

При добавлении новых фич:

1. Следуйте Clean Architecture
2. Добавляйте тесты
3. Обновляйте документацию
4. Проверяйте безопасность

## 📄 License

Copyright © 2025 TutuEmployee Development Team

## 🆘 Поддержка

- **Быстрый старт**: [KEYCLOAK_QUICK_START.md](KEYCLOAK_QUICK_START.md)
- **Полная документация**: [KEYCLOAK_INTEGRATION.md](KEYCLOAK_INTEGRATION.md)
- **Конфигурация**: [keycloak-config-example.env](keycloak-config-example.env)

---

**Создано с ❤️ для TutuEmployee**

**Версия:** 1.0.0  
**Дата:** 2025  
**Keycloak Version:** 23.0+
