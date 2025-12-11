# 🚀 Keycloak Cheat Sheet

Быстрая справка по Keycloak интеграции в TutuEmployee.

## 📦 Быстрый запуск

### 1. Запустить Keycloak

```bash
docker run -d -p 8080:8080 \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  --name keycloak \
  quay.io/keycloak/keycloak:latest start-dev
```

### 2. Доступ

- URL: http://localhost:8080
- Username: `admin`
- Password: `admin`

### 3. Настройка (Quick)

```yaml
Realm: tutu
Client ID: tutu-employee-app
Client Type: Public
Redirect URI: tutuemployee://oauth/callback
User: test@tutu.ru / test123
```

## 🔧 Конфигурация в коде

### KeycloakConfig

```kotlin
// composeApp/src/commonMain/kotlin/ru/tutu/tutuemployee/data/auth/KeycloakConfig.kt

KeycloakConfig(
    serverUrl = "http://localhost:8080",
    realm = "tutu",
    clientId = "tutu-employee-app",
    clientSecret = null,  // Public client
    redirectUri = "tutuemployee://oauth/callback"
)
```

### Включить Keycloak

```kotlin
// composeApp/src/commonMain/kotlin/ru/tutu/tutuemployee/di/NetworkModule.kt

const val USE_KEYCLOAK = true
```

## 💻 Использование в коде

### ViewModel - Password Flow

```kotlin
class MyViewModel(
    private val loginWithKeycloakUseCase: LoginWithKeycloakUseCase
) : ViewModel() {
    
    fun login(username: String, password: String) {
        viewModelScope.launch {
            loginWithKeycloakUseCase(username, password)
                .onSuccess { (token, user) ->
                    // Успех
                }
                .onFailure { error ->
                    // Ошибка
                }
        }
    }
}
```

### ViewModel - OAuth Flow

```kotlin
class MyViewModel(
    private val getKeycloakAuthUrlUseCase: GetKeycloakAuthUrlUseCase,
    private val handleKeycloakCallbackUseCase: HandleKeycloakCallbackUseCase
) : ViewModel() {
    
    // Шаг 1: Получить URL
    suspend fun startOAuth() {
        getKeycloakAuthUrlUseCase()
            .onSuccess { url ->
                openBrowser(url)
            }
    }
    
    // Шаг 2: Обработать callback
    suspend fun handleCallback(callbackUrl: String) {
        handleKeycloakCallbackUseCase(callbackUrl)
            .onSuccess { (token, user) ->
                // Успех
            }
    }
}
```

### Repository

```kotlin
class MyRepository(
    private val keycloakClient: KeycloakClient
) {
    // Получить валидный токен
    suspend fun getToken() = 
        keycloakClient.getValidAccessToken().getOrNull()
    
    // Проверить авторизацию
    fun isAuth() = 
        keycloakClient.isAuthenticated()
    
    // Выйти
    suspend fun logout() = 
        keycloakClient.logout()
}
```

## 🎨 UI Components

### Compose

```kotlin
@Composable
fun LoginScreen(viewModel: AuthViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsState()
    
    // Password flow
    Button(onClick = { viewModel.loginWithKeycloak() }) {
        Text("Войти через Keycloak")
    }
    
    // OAuth flow
    LaunchedEffect(state.keycloakAuthUrl) {
        state.keycloakAuthUrl?.let { url ->
            openBrowser(url)
            viewModel.clearKeycloakAuthUrl()
        }
    }
    
    OutlinedButton(onClick = { viewModel.startKeycloakOAuth() }) {
        Text("Войти через браузер")
    }
}
```

## 🔒 Важные endpoints

```kotlin
// Token endpoint
POST ${serverUrl}/realms/${realm}/protocol/openid-connect/token

// Authorization endpoint
GET ${serverUrl}/realms/${realm}/protocol/openid-connect/auth

// UserInfo endpoint
GET ${serverUrl}/realms/${realm}/protocol/openid-connect/userinfo

// Logout endpoint
POST ${serverUrl}/realms/${realm}/protocol/openid-connect/logout

// Token revocation
POST ${serverUrl}/realms/${realm}/protocol/openid-connect/revoke
```

## 📝 OAuth Parameters

### Authorization Code Flow

```
GET /auth?
  client_id=tutu-employee-app
  &redirect_uri=tutuemployee://oauth/callback
  &response_type=code
  &scope=openid profile email
  &state=random_state
  &code_challenge=sha256_hash
  &code_challenge_method=S256
```

### Token Exchange

```
POST /token
Content-Type: application/x-www-form-urlencoded

grant_type=authorization_code
&client_id=tutu-employee-app
&code=authorization_code
&redirect_uri=tutuemployee://oauth/callback
&code_verifier=original_verifier
```

### Refresh Token

```
POST /token
Content-Type: application/x-www-form-urlencoded

grant_type=refresh_token
&client_id=tutu-employee-app
&refresh_token=your_refresh_token
```

## 🐛 Частые проблемы

| Ошибка | Решение |
|--------|---------|
| Connection refused | `docker start keycloak` |
| Invalid redirect_uri | Добавьте URI в Keycloak Client |
| Invalid credentials | Проверьте username/password |
| Client not found | Проверьте clientId в config |
| CORS error | Настройте Web Origins в Keycloak |

## 🔧 Docker команды

```bash
# Запустить
docker start keycloak

# Остановить
docker stop keycloak

# Логи
docker logs -f keycloak

# Удалить
docker rm -f keycloak

# Restart
docker restart keycloak
```

## 🧪 Тестовые данные

```yaml
Server: http://localhost:8080
Realm: tutu
Client: tutu-employee-app
User: test@tutu.ru
Password: test123
```

## 📱 Platform Deep Links

```yaml
Android: tutuemployee://oauth/callback
iOS: tutuemployee://oauth/callback
Web: https://your-app.com/auth/callback
Desktop: http://localhost:*/callback
```

## 🔑 Token Structure

### Access Token (JWT)

```json
{
  "sub": "user-uuid",
  "preferred_username": "test@tutu.ru",
  "email": "test@tutu.ru",
  "name": "Тест Пользователь",
  "given_name": "Тест",
  "family_name": "Пользователь",
  "exp": 1234567890,
  "iat": 1234567890,
  "roles": ["employee"]
}
```

## 🔄 Token Lifecycle

```
Login → Get Tokens
  ↓
Use Access Token
  ↓
Token Expired? → Yes → Refresh Token → New Tokens
  ↓ No                      ↓ Failed
Use Token              Re-login
```

## ⚙️ Keycloak Client Settings

```yaml
# Admin Console → Clients → tutu-employee-app

Client Authentication: OFF
Authorization: OFF
Standard Flow: ON
Direct Access Grants: ON (dev only)
Implicit Flow: OFF

Valid Redirect URIs:
  - http://localhost:*
  - tutuemployee://oauth/callback
  - https://your-app.com/*

Web Origins:
  - * (dev)
  - https://your-app.com (prod)
```

## 📊 Модели данных

### KeycloakTokens

```kotlin
data class KeycloakTokens(
    val accessToken: String,
    val refreshToken: String?,
    val idToken: String?,
    val tokenType: String,
    val expiresIn: Long,
    val refreshExpiresIn: Long?
)
```

### KeycloakUserInfo

```kotlin
data class KeycloakUserInfo(
    val subject: String,
    val preferredUsername: String?,
    val name: String?,
    val givenName: String?,
    val familyName: String?,
    val email: String?,
    val emailVerified: Boolean?,
    val roles: List<String>?
)
```

## 🎯 DI Setup

```kotlin
// NetworkModule.kt
single<KeycloakConfig> { KeycloakConfig.getDefault() }
single<KeycloakTokenStorage> { InMemoryKeycloakTokenStorage() }
single<KeycloakClient> { KeycloakClient(get(), get(), get()) }
single<KeycloakOAuthHandler> { KeycloakOAuthHandler(get(), get()) }

// UseCaseModule.kt
factoryOf(::LoginWithKeycloakUseCase)
factoryOf(::GetKeycloakAuthUrlUseCase)
factoryOf(::HandleKeycloakCallbackUseCase)

// RepositoryModule.kt
single<AuthRepository> {
    AuthRepositoryImpl(
        remoteDataSource = get(),
        tokenStorage = get(),
        keycloakClient = get(),
        keycloakOAuthHandler = get()
    )
}
```

## 🚀 Build & Run

```bash
# Build
./gradlew :composeApp:build

# Android
./gradlew :composeApp:installDebug
adb shell am start -a android.intent.action.VIEW \
  -d "tutuemployee://oauth/callback?code=abc&state=xyz"

# Desktop
./gradlew :composeApp:run

# iOS
open iosApp/iosApp.xcodeproj
```

## 📚 Документация

- [KEYCLOAK_README.md](KEYCLOAK_README.md) - Обзор
- [KEYCLOAK_QUICK_START.md](KEYCLOAK_QUICK_START.md) - Быстрый старт
- [KEYCLOAK_INTEGRATION.md](KEYCLOAK_INTEGRATION.md) - Полная документация
- [KEYCLOAK_TODO.md](KEYCLOAK_TODO.md) - Production checklist

## 💡 Best Practices

✅ **DO:**

- Используйте Authorization Code Flow с PKCE
- Храните токены в защищенном хранилище
- Используйте HTTPS в production
- Настройте короткие сроки жизни токенов
- Отзывайте токены при logout

❌ **DON'T:**

- Не используйте Password flow в production
- Не храните токены в localStorage (web)
- Не логируйте токены
- Не используйте HTTP в production
- Не забывайте про token expiration

---

**Quick Reference для Keycloak интеграции в TutuEmployee**

**Version:** 1.0.0 | **Date:** 2025
