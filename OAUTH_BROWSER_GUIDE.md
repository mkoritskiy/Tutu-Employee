# OAuth авторизация через браузер - Руководство

## Обзор

Реализована авторизация через Keycloak с использованием **Authorization Code Flow с PKCE** через
системный браузер. Это безопасный и рекомендуемый способ авторизации для нативных и десктопных
приложений.

## Преимущества OAuth через браузер

### ✅ Безопасность

- **Пароль не попадает в приложение** - пользователь вводит учетные данные только в доверенном
  браузере Keycloak
- **PKCE защита** - защита от перехвата authorization code
- **State parameter** - защита от CSRF атак
- **Система не хранит пароли** - работа только с временными токенами

### ✅ Удобство

- **Single Sign-On (SSO)** - если пользователь уже авторизован в браузере, вход происходит
  автоматически
- **Unified experience** - пользователь использует знакомый интерфейс Keycloak
- **Multi-factor authentication** - автоматическая поддержка MFA, если настроено в Keycloak

### ✅ Соответствие стандартам

- **OAuth 2.0 / OpenID Connect** - индустриальные стандарты
- **Best practices** - рекомендации от IETF и OAuth Security BCP

## Архитектура

```
┌──────────────┐          ┌──────────────┐          ┌──────────────┐
│              │  1. Auth │              │  Opens   │              │
│     App      │─────────→│   Browser    │─────────→│   Keycloak   │
│              │   URL    │              │          │              │
└──────────────┘          └──────────────┘          └──────────────┘
       ↑                                                     │
       │                                                     │
       │  3. Deep Link                           2. User    │
       │  (callback)                             Login      │
       │                                                     │
       └─────────────────────────────────────────────────────┘
```

## Компоненты

### 1. BrowserHelper (expect/actual)

Platform-specific функции для работы с браузером:

```kotlin
// Common
expect fun openUrlInBrowser(url: String)
expect fun canHandleDeepLinks(): Boolean

@Composable
expect fun SetupOAuthCallbackHandler(onCallback: (String) -> Unit)
```

#### Android реализация

```kotlin
// Использует Chrome Custom Tabs для лучшего UX
actual fun openUrlInBrowser(url: String) {
    val customTabsIntent = CustomTabsIntent.Builder()
        .setShowTitle(true)
        .build()
    customTabsIntent.launchUrl(context, Uri.parse(url))
}

// Регистрирует callback в MainActivity для Deep Links
@Composable
actual fun SetupOAuthCallbackHandler(onCallback: (String) -> Unit) {
    val activity = LocalContext.current as? MainActivity
    activity?.setOAuthCallback(onCallback)
}
```

#### iOS реализация

```kotlin
// Открывает Safari или ASWebAuthenticationSession
actual fun openUrlInBrowser(url: String) {
    UIApplication.sharedApplication.openURL(NSURL.URLWithString(url))
}
```

### 2. KeycloakOAuthHandler

Управляет OAuth flow:

```kotlin
class KeycloakOAuthHandler(
    private val config: KeycloakConfig,
    private val keycloakClient: KeycloakClient
) {
    // Создание URL для авторизации с PKCE
    suspend fun createAuthorizationUrl(
        scopes: List<String> = listOf("openid", "profile", "email"),
        usePKCE: Boolean = true
    ): String
    
    // Обработка callback URL с кодом
    suspend fun handleAuthorizationCallback(
        callbackUrl: String
    ): Result<KeycloakTokens>
}
```

### 3. AuthViewModel

```kotlin
// Инициация OAuth flow
fun startKeycloakOAuth() {
    viewModelScope.launch {
        getKeycloakAuthUrlUseCase()
            .onSuccess { url ->
                _uiState.value = _uiState.value.copy(keycloakAuthUrl = url)
            }
    }
}

// Обработка callback
fun handleKeycloakCallback(callbackUrl: String) {
    viewModelScope.launch {
        handleKeycloakCallbackUseCase(callbackUrl)
            .onSuccess { (token, user) ->
                _uiState.value = _uiState.value.copy(
                    isAuthenticated = true,
                    user = user
                )
            }
    }
}
```

### 4. AuthScreen

```kotlin
@Composable
fun AuthScreen(onLoginSuccess: () -> Unit) {
    val viewModel: AuthViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    // Автоматически открываем браузер при получении URL
    LaunchedEffect(uiState.keycloakAuthUrl) {
        uiState.keycloakAuthUrl?.let { url ->
            openUrlInBrowser(url)
            viewModel.clearKeycloakAuthUrl()
        }
    }

    // Регистрируем обработчик callback
    SetupOAuthCallbackHandler { callbackUrl ->
        viewModel.handleKeycloakCallback(callbackUrl)
    }
    
    // UI с кнопкой "Войти через браузер"
    Button(onClick = { viewModel.startKeycloakOAuth() }) {
        Text("Войти через браузер (OAuth)")
    }
}
```

## Конфигурация

### Android

#### 1. AndroidManifest.xml

Добавлен Intent Filter для обработки Deep Links:

```xml
<activity
    android:name=".MainActivity"
    android:launchMode="singleTask">
    
    <!-- Deep Link для OAuth callback -->
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        
        <data
            android:scheme="tutuemployee"
            android:host="oauth"
            android:pathPrefix="/callback" />
    </intent-filter>
</activity>
```

#### 2. MainActivity

Обработка входящих Deep Links:

```kotlin
class MainActivity : ComponentActivity() {
    private var oauthCallback: ((String) -> Unit)? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)
    }
    
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }
    
    private fun handleIntent(intent: Intent?) {
        val data = intent?.data
        if (data != null && data.toString().startsWith("tutuemployee://oauth/callback")) {
            oauthCallback?.invoke(data.toString())
        }
    }
}
```

#### 3. build.gradle.kts

Добавлена зависимость для Chrome Custom Tabs:

```kotlin
androidMain.dependencies {
    implementation(libs.androidx.browser)
}
```

### Keycloak

#### 1. Настройка Client

В Keycloak Admin Console для клиента `dom-confluence`:

**Valid Redirect URIs:**

```
tutuemployee://oauth/callback
```

**Client Authentication:** OFF (Public client)

**Standard Flow Enabled:** ON

**Direct Access Grants:** ON (опционально, для fallback)

#### 2. KeycloakConfig.kt

```kotlin
data class KeycloakConfig(
    val serverUrl: String = "https://keycloak.tutu.ru",
    val realm: String = "tutu",
    val clientId: String = "dom-confluence",
    val redirectUri: String = "tutuemployee://oauth/callback"
)
```

## Workflow пользователя

### 1. Пользователь нажимает "Войти через браузер"

```kotlin
Button(onClick = { viewModel.startKeycloakOAuth() }) {
    Icon(Icons.Default.Language)
    Text("Войти через браузер (OAuth)")
}
```

### 2. Приложение генерирует URL с PKCE

```
https://keycloak.tutu.ru/realms/tutu/protocol/openid-connect/auth?
  client_id=dom-confluence&
  redirect_uri=tutuemployee://oauth/callback&
  response_type=code&
  scope=openid%20profile%20email&
  state=abc123...&
  code_challenge=xyz789...&
  code_challenge_method=S256
```

### 3. Открывается браузер с Keycloak

- Пользователь видит стандартную форму входа Keycloak
- Если уже авторизован → автоматический вход (SSO)
- Если нет → вводит логин/пароль

### 4. После успешной авторизации

Keycloak перенаправляет на:

```
tutuemployee://oauth/callback?code=AUTH_CODE&state=abc123...
```

### 5. Приложение перехватывает Deep Link

- Android: через Intent Filter
- iOS: через URL Scheme
- Web: через window.location или postMessage

### 6. Приложение обменивает code на токены

```http
POST /realms/tutu/protocol/openid-connect/token
Content-Type: application/x-www-form-urlencoded

grant_type=authorization_code&
code=AUTH_CODE&
redirect_uri=tutuemployee://oauth/callback&
client_id=dom-confluence&
code_verifier=PKCE_VERIFIER
```

### 7. Получение токенов и информации о пользователе

```json
{
  "access_token": "eyJhbG...",
  "refresh_token": "eyJhbG...",
  "id_token": "eyJhbG...",
  "token_type": "Bearer",
  "expires_in": 300
}
```

## Безопасность

### PKCE (Proof Key for Code Exchange)

1. **Генерация Code Verifier** - случайная строка 43-128 символов
2. **Генерация Code Challenge** - SHA256(code_verifier), base64url encoded
3. **Authorization Request** - отправляется code_challenge
4. **Token Request** - отправляется code_verifier
5. **Keycloak проверяет** - SHA256(code_verifier) == code_challenge

### State Parameter

- Случайная строка для защиты от CSRF
- Генерируется перед authorization request
- Проверяется при получении callback

### Custom URL Scheme

- **Android**: `tutuemployee://oauth/callback`
- **iOS**: `tutuemployee://oauth/callback`
- Только ваше приложение может перехватить этот URL

## Тестирование

### 1. Запуск приложения

```bash
./gradlew :composeApp:installDebug
```

### 2. Нажатие "Войти через браузер"

- Должен открыться браузер с Keycloak
- URL должен содержать параметры: client_id, redirect_uri, code_challenge

### 3. Вход в Keycloak

- Введите логин/пароль
- Или воспользуйтесь SSO, если уже авторизованы

### 4. Проверка redirect

```bash
# Android: просмотр логов
adb logcat | grep "tutuemployee://oauth"
```

### 5. Проверка токенов

После успешной авторизации проверьте:

- Access token сохранен
- User info получен
- UI показывает авторизованного пользователя

## Отладка

### Проблема: Браузер не открывается

**Android:**

```kotlin
// Проверьте, что контекст установлен
AndroidContextProvider.applicationContext
```

**iOS:**

```kotlin
// Проверьте, что URL scheme зарегистрирован в Info.plist
```

### Проблема: Deep Link не работает

**Android:**

```bash
# Проверка Intent Filter
adb shell dumpsys package r | grep tutuemployee

# Тест Deep Link
adb shell am start -W -a android.intent.action.VIEW \
  -d "tutuemployee://oauth/callback?code=test"
```

### Проблема: Invalid redirect_uri в Keycloak

Проверьте в Keycloak Admin:

1. Client Settings → Valid Redirect URIs
2. Должен быть: `tutuemployee://oauth/callback`
3. Точное совпадение (case-sensitive!)

### Проблема: PKCE validation failed

```kotlin
// Убедитесь, что code_verifier сохраняется между запросами
private var currentCodeVerifier: String? = null
```

## Альтернативные методы авторизации

### 1. OAuth через браузер (Рекомендуется) ✅

```kotlin
viewModel.startKeycloakOAuth()
```

**Плюсы:** Безопасно, SSO, MFA support
**Минусы:** Требует Deep Link настройку

### 2. Direct Password Flow (Fallback)

```kotlin
viewModel.loginWithKeycloak(username, password)
```

**Плюсы:** Простая реализация
**Минусы:** Пароль попадает в приложение, нет SSO

### 3. Legacy API

```kotlin
viewModel.login(username, password)
```

**Плюсы:** Работает со старым API
**Минусы:** Не использует Keycloak

## Roadmap

### Android

- ✅ Chrome Custom Tabs
- ✅ Deep Link handling
- ✅ PKCE support
- ✅ State validation

### iOS

- ✅ Safari / ASWebAuthenticationSession
- ⚠️ Deep Link handling (требует настройка Info.plist)
- ✅ PKCE support

### Web (JS/WASM)

- ✅ window.open
- ⚠️ postMessage callback (TODO)
- ✅ PKCE support

## Полезные ссылки

- [OAuth 2.0 для Native Apps (RFC 8252)](https://tools.ietf.org/html/rfc8252)
- [PKCE (RFC 7636)](https://tools.ietf.org/html/rfc7636)
- [OAuth 2.0 Security Best Practices](https://tools.ietf.org/html/draft-ietf-oauth-security-topics)
- [Keycloak Documentation](https://www.keycloak.org/docs/latest/securing_apps/)

## Заключение

OAuth авторизация через браузер - это **безопасный, удобный и правильный** способ авторизации для
мобильных и десктопных приложений. Реализация следует индустриальным стандартам и best practices.

**Используйте этот метод по умолчанию!** 🎉
