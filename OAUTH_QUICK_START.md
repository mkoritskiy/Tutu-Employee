# OAuth через браузер - Быстрый старт

## Что это?

Безопасная авторизация через Keycloak с открытием браузера вместо ввода пароля в приложении.

## Как пользователь видит это?

1. **Нажимает кнопку "Войти через браузер"**
2. **Открывается браузер с Keycloak** (Chrome/Safari)
3. **Вводит логин/пароль** в знакомом интерфейсе Keycloak
4. **Автоматически возвращается в приложение** - готово!

## Зачем это нужно?

✅ **Безопасность** - пароль не попадает в приложение  
✅ **SSO** - если уже авторизован в браузере, вход мгновенный  
✅ **MFA** - автоматическая поддержка двухфакторной аутентификации  
✅ **Стандарты** - OAuth 2.0 / OpenID Connect

## Для разработчика

### 1. Использование в коде

```kotlin
// В AuthScreen уже всё готово!
@Composable
fun AuthScreen(onLoginSuccess: () -> Unit) {
    val viewModel: AuthViewModel = koinViewModel()
    
    Button(onClick = { viewModel.startKeycloakOAuth() }) {
        Icon(Icons.Default.Language)
        Text("Войти через браузер")
    }
}
```

### 2. Что происходит внутри?

```kotlin
// 1. Генерируем URL с PKCE защитой
viewModel.startKeycloakOAuth()
// → openUrlInBrowser("https://keycloak.tutu.ru/realms/tutu/...")

// 2. Браузер открывается автоматически
// → Пользователь логинится

// 3. Keycloak редиректит обратно
// → tutuemployee://oauth/callback?code=...

// 4. Приложение пе��ехватывает Deep Link
// → viewModel.handleKeycloakCallback(url)

// 5. Получаем токены и пользователя
// → isAuthenticated = true
```

### 3. Конфигурация (уже сделано)

#### Android ✅

**AndroidManifest.xml** - Deep Link для callback

```xml
<data
    android:scheme="tutuemployee"
    android:host="oauth"
    android:pathPrefix="/callback" />
```

**MainActivity.kt** - обработка входящих URL

```kotlin
private fun handleIntent(intent: Intent?) {
    val url = intent?.data?.toString()
    if (url?.startsWith("tutuemployee://oauth/callback") == true) {
        oauthCallback?.invoke(url)
    }
}
```

#### Keycloak ✅

**Valid Redirect URIs:**

```
tutuemployee://oauth/callback
```

**KeycloakConfig.kt:**

```kotlin
KeycloakConfig(
    serverUrl = "https://keycloak.tutu.ru",
    realm = "tutu",
    clientId = "dom-confluence",
    redirectUri = "tutuemployee://oauth/callback"
)
```

## Тестирование

### Android

```bash
# 1. Запуск приложения
./gradlew :composeApp:installDebug

# 2. Нажмите "Войти через браузер"
# → Должен открыться Chrome Custom Tab с Keycloak

# 3. Войдите в Keycloak
# → После входа автоматически вернётесь в приложение

# 4. Проверьте логи (если не работает)
adb logcat | grep -E "tutuemployee|OAuth|Keycloak"
```

### Тест Deep Link вручную

```bash
# Симуляция callback от Keycloak
adb shell am start -W -a android.intent.action.VIEW \
  -d "tutuemployee://oauth/callback?code=test123&state=abc456"
```

## Отладка

### Браузер не открывается?

**Проверьте:**

```kotlin
// AndroidContextProvider должен быть инициализирован в TutuEmployeeApp
AndroidContextProvider.applicationContext = this
```

### Deep Link не работает?

```bash
# Проверьте, что Intent Filter зарегистрирован
adb shell dumpsys package r | grep tutuemployee

# Должно быть:
# tutuemployee://oauth/callback filter
```

### Keycloak возвращает ошибку?

**Invalid redirect_uri:**

- Проверьте в Keycloak Admin Console
- Client Settings → Valid Redirect URIs
- Должен быть **точно**: `tutuemployee://oauth/callback`

**Invalid client:**

- Проверьте `clientId` в `KeycloakConfig.kt`
- Должен совпадать с Keycloak

## Сравнение методов авторизации

| Метод | Безопасность | SSO | UX | Рекомендация |
|-------|-------------|-----|----|--------------| 
| **OAuth через браузер** 🏆 | ✅✅✅ | ✅ | ⭐⭐⭐⭐⭐ | **Используйте это!** |
| OAuth с паролем | ⚠️ | ❌ | ⭐⭐⭐ | Только для тестов |
| Legacy API | ⚠️ | ❌ | ⭐⭐ | Для обратной совместимости |

## Архитектура

```
User clicks button
        ↓
    ViewModel.startKeycloakOAuth()
        ↓
    openUrlInBrowser(url)
        ↓
    Chrome Custom Tab opens
        ↓
    User logs in Keycloak
        ↓
    Redirect: tutuemployee://oauth/callback?code=...
        ↓
    MainActivity catches Deep Link
        ↓
    ViewModel.handleKeycloakCallback(url)
        ↓
    Exchange code → tokens
        ↓
    Get user info
        ↓
    ✅ Authenticated!
```

## Файлы проекта

### Основные

- `BrowserHelper.kt` - expect/actual для открытия браузера
- `KeycloakOAuthHandler.kt` - OAuth flow с PKCE
- `AuthViewModel.kt` - управление состоянием авторизации
- `AuthScreen.kt` - UI с кнопкой входа

### Android-специфичные

- `BrowserHelper.android.kt` - Chrome Custom Tabs
- `AndroidOAuthHelper.kt` - Deep Link handling
- `MainActivity.kt` - перехват Deep Links
- `AndroidManifest.xml` - Intent Filter

### Конфигурация

- `KeycloakConfig.kt` - настройки Keycloak
- `NetworkModule.kt` - Koin DI setup

## Полная документация

📚 См. [OAUTH_BROWSER_GUIDE.md](OAUTH_BROWSER_GUIDE.md) для деталей

## Что дальше?

### iOS

- [ ] Настроить URL Scheme в Info.plist
- [ ] Реализовать обработку в AppDelegate

### Web

- [ ] postMessage callback от popup окна
- [ ] Redirect back на callback URL

---

**Готово! Можно пользоваться на Android прямо сейчас! 🎉**
