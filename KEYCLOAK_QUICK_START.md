# Keycloak Quick Start Guide

Быстрый старт для интеграции Keycloak в TutuEmployee за 15 минут.

## 🚀 Быстрый старт (15 минут)

### Шаг 1: Запустите Keycloak (2 минуты)

```bash
docker run -d -p 8080:8080 \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  --name keycloak \
  quay.io/keycloak/keycloak:latest start-dev
```

Откройте: http://localhost:8080  
Войдите: `admin` / `admin`

### Шаг 2: Создайте Realm (3 минуты)

1. В Admin Console нажмите **"Create realm"**
2. Name: `tutu`
3. Enabled: **ON**
4. Нажмите **"Create"**

### Шаг 3: Создайте Client (3 минуты)

1. Перейдите в **Clients** → **Create client**
2. Заполните:
   ```
   Client ID: tutu-employee-app
   Client type: OpenID Connect
   ```
3. Нажмите **"Next"**
4. Включите:
   ```
   ✅ Standard flow
   ✅ Direct access grants
   ```
5. Нажмите **"Next"**
6. В Valid redirect URIs добавьте:
   ```
   http://localhost:*
   tutuemployee://oauth/callback
   ```
7. Нажмите **"Save"**

### Шаг 4: Создайте пользователя (2 минуты)

1. Перейдите в **Users** → **Add user**
2. Заполните:
   ```
   Username: test@tutu.ru
   Email: test@tutu.ru
   First name: Тест
   Last name: Пользователь
   Email verified: ✅
   ```
3. Нажмите **"Create"**
4. Перейдите на вкладку **"Credentials"**
5. Нажмите **"Set password"**:
   ```
   Password: test123
   Temporary: OFF
   ```
6. Нажмите **"Save"**

### Шаг 5: Настройте приложение (5 минут)

#### 5.1 Обновите конфигурацию

Откройте `composeApp/src/commonMain/kotlin/ru/tutu/tutuemployee/data/auth/KeycloakConfig.kt`:

```kotlin
fun getDefault() = KeycloakConfig(
    serverUrl = "http://localhost:8080",  // ← Ваш Keycloak
    realm = "tutu",                       // ← Имя realm
    clientId = "tutu-employee-app",       // ← Client ID
    clientSecret = null,                  // Public client
    redirectUri = "tutuemployee://oauth/callback"
)
```

#### 5.2 Включите Keycloak

Откройте `composeApp/src/commonMain/kotlin/ru/tutu/tutuemployee/di/NetworkModule.kt`:

```kotlin
const val USE_KEYCLOAK = true  // ← Включить
```

#### 5.3 Запустите приложение

```bash
# Android
./gradlew :composeApp:installDebug

# Desktop
./gradlew :composeApp:run

# iOS
open iosApp/iosApp.xcodeproj
```

### Шаг 6: Войдите в приложение ✨

1. Откройте приложение
2. Введите:
   ```
   Логин: test@tutu.ru
   Пароль: test123
   ```
3. Нажмите **"Войти через Keycloak"**
4. Готово! 🎉

## 🎯 Что дальше?

### Метод 1: Username/Password (текущий)

✅ Работает сейчас  
⚠️ Не рекомендуется для production

```kotlin
viewModel.loginWithKeycloak()
```

### Метод 2: OAuth через браузер (рекомендуется)

Для production используйте Authorization Code Flow:

```kotlin
// 1. Получить URL
viewModel.startKeycloakOAuth()

// 2. Открыть браузер
val authUrl = uiState.keycloakAuthUrl
openBrowser(authUrl)

// 3. Обработать callback
viewModel.handleKeycloakCallback(callbackUrl)
```

## 🔧 Частые проблемы

### Ошибка: "Connection refused"

**Решение:** Keycloak не запущен.

```bash
docker start keycloak
```

### Ошибка: "Invalid user credentials"

**Решение:** Проверьте username/password пользователя в Keycloak.

### Ошибка: "Client not found"

**Решение:** Проверьте `clientId` в `KeycloakConfig`.

### Ошибка: "Invalid redirect_uri"

**Решение:** Добавьте redirect URI в настройках client.

## 📱 Тестирование разных flow

### Password Flow (простой)

```kotlin
// В AuthScreen нажмите "Войти через Keycloak"
viewModel.loginWithKeycloak()

// Данные:
// Username: test@tutu.ru
// Password: test123
```

### OAuth Flow (безопасный)

```kotlin
// 1. Нажмите "Войти через браузер"
viewModel.startKeycloakOAuth()

// 2. Вы будете перенаправлены в браузер
// 3. Войдите через Keycloak
// 4. Вернетесь в приложение с токеном
```

## 🔒 Безопасность

### Для тестирования (текущая конфигурация)

```kotlin
✅ InMemoryTokenStorage - OK для development
✅ HTTP localhost - OK для development
✅ Password flow - OK для development
```

### Для production

```kotlin
❌ InMemoryTokenStorage → Замените на EncryptedSharedPreferences/Keychain
❌ HTTP → Используйте только HTTPS
❌ Password flow → Используйте Authorization Code Flow
```

## 📚 Подробная документация

См. [KEYCLOAK_INTEGRATION.md](KEYCLOAK_INTEGRATION.md) для:

- Детальной архитектуры
- Production конфигурации
- Безопасности
- Troubleshooting

## ✅ Чеклист готовности к production

- [ ] Используете HTTPS
- [ ] Настроили безопасное хранилище токенов
- [ ] Используете Authorization Code Flow с PKCE
- [ ] Отключили Password Flow
- [ ] Настроили валидные Redirect URIs
- [ ] Настроили Web Origins для CORS
- [ ] Настроили роли и permissions
- [ ] Добавили логирование и мониторинг
- [ ] Настроили короткие сроки жизни токенов
- [ ] Протестировали на всех платформах

## 🎉 Готово!

Теперь у вас работает авторизация через Keycloak!

**Следующие шаги:**

1. Настройте production Keycloak сервер
2. Переключитесь на Authorization Code Flow
3. Настройте безопасное хранилище токенов
4. Добавьте роли и permissions
5. Настройте мониторинг

---

**Нужна помощь?** См. [KEYCLOAK_INTEGRATION.md](KEYCLOAK_INTEGRATION.md)
