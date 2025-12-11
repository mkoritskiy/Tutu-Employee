# Руководство по миграции на Koin ViewModels

## Проблема

В текущей версии экраны создают ViewModels вручную:

```kotlin
@Composable
fun AuthScreen(
    viewModel: AuthViewModel = viewModel { AuthViewModel() }  // ❌ Неправильно
)
```

Это не позволяет Koin инжектировать зависимости в ViewModels.

## Решение

Использовать `koinViewModel()` из библиотеки Koin Compose:

```kotlin
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = koinViewModel()  // ✅ Правильно
)
```

## Необходимые изменения

### 1. Обновить импорты

**Было:**

```kotlin
import androidx.lifecycle.viewmodel.compose.viewModel
```

**Стало:**

```kotlin
import org.koin.compose.viewmodel.koinViewModel
```

### 2. Обновить вызов viewModel

**Было:**

```kotlin
viewModel: AuthViewModel = viewModel { AuthViewModel() }
```

**Стало:**

```kotlin
viewModel: AuthViewModel = koinViewModel()
```

## Список файлов для обновления

- [x] `presentation/auth/AuthScreen.kt`
- [x] `presentation/home/HomeScreen.kt`
- [x] `presentation/profile/ProfileScreen.kt`
- [x] `presentation/office/OfficeScreen.kt`
- [x] `presentation/merch/MerchScreen.kt`
- [x] `presentation/favorites/FavoritesScreen.kt`

## Преимущества

✅ Автоматическая инжекция зависимостей  
✅ Нет необходимости вручную создавать ViewModels  
✅ Упрощенное тестирование  
✅ Соответствие Clean Architecture принципам
