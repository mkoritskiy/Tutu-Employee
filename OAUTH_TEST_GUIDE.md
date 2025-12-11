# OAuth через браузер - Руководство по тестированию

## Быстрый тест на Android

### Шаг 1: Установка приложения

```bash
# Сборка и установка
./gradlew :composeApp:installDebug

# Или через Android Studio:
# Run → Run 'composeApp'
```

### Шаг 2: Открытие приложения

1. Запустите приложение на устройстве/эмуляторе
2. Вы увидите экран авторизации

### Шаг 3: OAuth вход

1. Найдите кнопку **"Войти через браузер (OAuth)"** (синяя кнопка с иконкой глобуса)
2. Нажмите на неё
3. **Ожидаемое поведение:**
    - Открывается Chrome Custom Tab
    - Загружается страница Keycloak
    - URL: `https://keycloak.tutu.ru/realms/tutu/protocol/openid-connect/auth?...`

### Шаг 4: Авторизация в Keycloak

1. Введите логин и пароль от Keycloak
2. Нажмите "Sign In"
3. **Ожидаемое поведение:**
    - Браузер закрывается автоматически
    - Возврат в приложение
    - Переход на главный экран

### Шаг 5: Проверка авторизации

✅ **Успешная авторизация:**

- Отображается главный экран
- Видно имя пользователя в профиле
- Доступны все разделы приложения

## Тестовые сценарии

### TC-1: Первый вход (без SSO)

**Предусловия:** Пользователь не авторизован в Keycloak в браузере

**Шаги:**

1. Открыть приложение
2. Нажать "Войти через браузер"
3. Ввести валидные учетные данные
4. Нажать "Sign In"

**Ожидаемый результат:**

- ✅ Chrome Custom Tab открылся
- ✅ Форма логина Keycloak отображается
- ✅ После ввода данных браузер закрывается
- ✅ Приложение показывает главный экран
- ✅ Пользователь авторизован

**Время выполнения:** ~10-15 секунд

---

### TC-2: Повторный вход (SSO)

**Предусловия:** Пользователь уже авторизован в Keycloak в браузере

**Шаги:**

1. Открыть приложение
2. Нажать "Войти через браузер"

**Ожидаемый результат:**

- ✅ Chrome Custom Tab открывается
- ✅ Форма логина НЕ показывается (автоматический вход)
- ✅ Браузер сразу закрывается
- ✅ Приложение показывает главный экран

**Время выполнения:** ~2-3 секунды

---

### TC-3: Неверный пароль

**Шаги:**

1. Открыть приложение
2. Нажать "Войти через браузер"
3. Ввести **неверный** пароль
4. Нажать "Sign In"

**Ожидаемый результат:**

- ✅ Keycloak показывает ошибку
- ✅ Пользователь остается на форме логина
- ✅ Можно повторить попытку
- ❌ Приложение не получает callback

---

### TC-4: Отмена входа

**Шаги:**

1. Открыть приложение
2. Нажать "Войти через браузер"
3. Нажать "Back" или закрыть браузер

**Ожидаемый результат:**

- ✅ Возврат в приложение
- ✅ Экран авторизации остается
- ✅ Можно повторить попытку входа

---

### TC-5: Проверка Deep Link вручную

**Цель:** Убедиться, что Deep Link работает

```bash
# Отправить тестовый Deep Link
adb shell am start -W -a android.intent.action.VIEW \
  -d "tutuemployee://oauth/callback?code=test123&state=abc456"
```

**Ожидаемый результат:**

- ✅ Приложение открывается
- ✅ Появляется на экране авторизации
- ⚠️ Ошибка авторизации (код невалидный, это нормально)

---

## Логирование для отладки

### Включить логи

```bash
# Фильтр логов для OAuth
adb logcat | grep -E "Keycloak|OAuth|tutuemployee"

# Фильтр только ошибок
adb logcat *:E | grep -E "Keycloak|OAuth"
```

### Что искать в логах

**Успешный flow:**

```
D/OAuth: Creating authorization URL
D/OAuth: Opening browser with URL: https://keycloak.tutu.ru/...
D/MainActivity: Received deep link: tutuemployee://oauth/callback?code=...
D/OAuth: Handling authorization callback
D/Keycloak: Exchanging code for token
D/Keycloak: Token received successfully
D/Keycloak: Getting user info
D/Auth: User authenticated successfully
```

**Ошибка:**

```
E/OAuth: Failed to create auth URL: ...
E/Keycloak: Token exchange failed: ...
E/Auth: Authentication failed: ...
```

---

## Проверка Intent Filter

### Убедиться, что Deep Link зарегистрирован

```bash
# Проверить Intent Filters для приложения
adb shell dumpsys package ru.tutu.tutuemployee | grep -A 20 "intent-filter"

# Должно быть:
# Scheme: tutuemployee
# Host: oauth
# Path: /callback
```

**Ожидаемый вывод:**

```
Intent Filter:
  Action: android.intent.action.VIEW
  Category: android.intent.category.DEFAULT
  Category: android.intent.category.BROWSABLE
  Scheme: tutuemployee
  Host: oauth
  Path: /callback
```

---

## Проверка Keycloak конфигурации

### В Keycloak Admin Console

1. **Realm:** tutu
2. **Clients → dom-confluence → Settings:**

**Проверьте:**

- ✅ Client ID: `dom-confluence`
- ✅ Access Type: `public`
- ✅ Standard Flow Enabled: `ON`
- ✅ Valid Redirect URIs: `tutuemployee://oauth/callback`

### Тест Keycloak availability

```bash
# Проверить доступность
curl -I https://keycloak.tutu.ru/realms/tutu/protocol/openid-connect/auth

# Ожидаемый ответ:
# HTTP/2 200
```

---

## Типичные проблемы и решения

### ❌ Браузер не открывается

**Проблема:** Нажатие кнопки не открывает браузер

**Проверки:**

```bash
# Проверить логи
adb logcat | grep "openUrlInBrowser"

# Проверить, что Chrome установлен
adb shell pm list packages | grep chrome
```

**Решение:**

- Убедитесь, что Chrome установлен
- Проверьте, что `AndroidContextProvider.applicationContext` инициализирован
- Перезапустите приложение

---

### ❌ Deep Link не работает

**Проблема:** После входа браузер не возвращает в приложение

**Проверки:**

```bash
# Проверить Intent Filter
adb shell dumpsys package r | grep tutuemployee

# Тест Deep Link
adb shell am start -W -a android.intent.action.VIEW \
  -d "tutuemployee://oauth/callback?code=test"
```

**Решение:**

- Проверьте AndroidManifest.xml - Intent Filter
- Убедитесь, что scheme = "tutuemployee"
- Проверьте MainActivity.handleIntent()
- Переустановите приложение (uninstall → install)

---

### ❌ Invalid redirect_uri в Keycloak

**Проблема:** Keycloak возвращает ошибку "Invalid redirect_uri"

**Что проверить:**

1. В Keycloak Admin: Valid Redirect URIs
2. Должно быть **точно**: `tutuemployee://oauth/callback`
3. Case-sensitive!
4. Без пробелов и лишних символов

**Лог в браузере:**

```
error: invalid_redirect_uri
error_description: Invalid redirect_uri
```

**Решение:**

- Добавьте redirect URI в Keycloak
- Перезагрузите конфигурацию клиента

---

### ❌ Нет интернета

**Проблема:** Ошибка сети при открытии браузера

**Проверка:**

```bash
# Проверить интернет на устройстве
adb shell ping -c 4 keycloak.tutu.ru
```

**Решение:**

- Подключите устройство к Wi-Fi
- Проверьте доступность Keycloak сервера

---

## Performance тестирование

### Измерение времени OAuth flow

```kotlin
// Добавить в AuthViewModel
private var startTime: Long = 0

fun startKeycloakOAuth() {
    startTime = System.currentTimeMillis()
    // ... existing code
}

fun handleKeycloakCallback(url: String) {
    val duration = System.currentTimeMillis() - startTime
    Log.d("OAuth", "Total time: ${duration}ms")
    // ... existing code
}
```

**Целевые показатели:**

- Первый вход: < 15 секунд
- SSO вход: < 5 секунд
- Token exchange: < 2 секунд

---

## Регрессионное тестирование

### Чеклист перед релизом

- [ ] OAuth вход работает на Android
- [ ] SSO работает корректно
- [ ] Deep Link возвращает в приложение
- [ ] Ошибки отображаются понятно
- [ ] Loading indicator показывается
- [ ] Работает на разных версиях Android (24+)
- [ ] Работает на разных устройствах
- [ ] Работает при слабом интернете
- [ ] Работает после минимизации приложения
- [ ] Токены сохраняются корректно

---

## Автоматизированное тестирование (будущее)

### Пример UI теста

```kotlin
@Test
fun testOAuthLogin() {
    // 1. Открыть экран
    composeTestRule.onNodeWithText("Войти через браузер")
        .assertIsDisplayed()
        .performClick()
    
    // 2. Симулировать callback
    val testUrl = "tutuemployee://oauth/callback?code=test&state=abc"
    activityRule.scenario.onActivity { activity ->
        activity.handleIntent(Intent(Intent.ACTION_VIEW, Uri.parse(testUrl)))
    }
    
    // 3. Проверить результат
    composeTestRule.onNodeWithText("Главная")
        .assertIsDisplayed()
}
```

---

## Заключение

OAuth авторизация через браузер - **безопасный и удобный** метод входа. Следуйте этому руководству
для тестирования и убедитесь, что все тест-кейсы проходят успешно!

**Основные тесты:**

1. ✅ Первый вход с паролем
2. ✅ SSO вход без пароля
3. ✅ Отмена входа
4. ✅ Deep Link работает

**Минимальное время тестирования:** ~5 минут для всех сценариев

---

**Дополнительная документация:**

- [OAUTH_QUICK_START.md](OAUTH_QUICK_START.md) - быстрый старт
- [OAUTH_BROWSER_GUIDE.md](OAUTH_BROWSER_GUIDE.md) - полное руководство
- [OAUTH_TEAM_GUIDE.md](OAUTH_TEAM_GUIDE.md) - для всей команды
