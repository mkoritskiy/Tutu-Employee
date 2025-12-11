# Замена Эмодзи на Material Icons

Этот документ описывает замену эмодзи на Material Icons в проекте TutuEmployee.

## Изменения в зависимостях

### build.gradle.kts

Добавлена зависимость:

```kotlin
implementation(compose.materialIconsExtended)
```

## Замененные иконки

### Навигация (BottomNavigationBar)

| Эмодзи | Material Icon | Использование |
|--------|---------------|---------------|
| 🏠 | `Icons.Default.Home` | Главная страница |
| 👤 | `Icons.Default.Person` | Профиль |
| 🏢 | `Icons.Default.Business` | Офис |
| 🛒 | `Icons.Default.ShoppingCart` | Магазин мерча |
| ⭐ | `Icons.Default.Star` | Избранное |

### HomeScreen

| Эмодзи | Material Icon | Использование |
|--------|---------------|---------------|
| 🔍 | `Icons.Default.Search` | Поиск сотрудников |
| ✕ | `Icons.Default.Close` | Очистить поиск |
| 🎂 | `Icons.Default.Cake` | День рождения |
| 📰 | `Icons.Default.Article` | Новости |
| 🕒 | `Icons.Default.Schedule` | Время публикации |

### ProfileScreen

| Эмодзи | Material Icon | Использование |
|--------|---------------|---------------|
| 📅 | `Icons.Default.CalendarToday` | Дни отпуска |
| ⭐ | `Icons.Default.Star` | Бонусные баллы |
| 🏆 | `Icons.Default.EmojiEvents` | Достижения |
| ⭕ | `Icons.Default.RadioButtonUnchecked` | Задача TODO |
| ⏳ | `Icons.Default.HourglassEmpty` | Задача в процессе |
| ✅ | `Icons.Default.CheckCircle` | Задача выполнена |

### OfficeScreen

| Эмодзи | Material Icon | Использование |
|--------|---------------|---------------|
| 📅 | `Icons.Default.CalendarToday` | Выбор даты |
| ✓ | `Icons.Default.Check` | Место свободно |
| ✕ | `Icons.Default.Close` | Место занято |

### MerchScreen

| Эмодзи | Material Icon | Использование |
|--------|---------------|---------------|
| ⭐ | `Icons.Default.Star` | Баллы/цена товара |
| 🎁 | `Icons.Default.CardGiftcard` | Изображение товара |

### FavoritesScreen

| Эмодзи | Material Icon | Использование |
|--------|---------------|---------------|
| 🗑️ | `Icons.Default.Delete` | Удалить из избранного |

### AuthScreen

| Эмодзи | Material Icon | Использование |
|--------|---------------|---------------|
| 🚂 | `Icons.Default.Train` | Логотип приложения |

### WebViewScreen

| Эмодзи | Material Icon | Использование |
|--------|---------------|---------------|
| ← | `Icons.Default.ArrowBack` | Кнопка назад |
| ℹ️ | `Icons.Default.Info` | Информация |

## Обновления в Mock Data

В файле `MockApiService.kt` иконки для достижений обновлены на текстовые идентификаторы:

- 🎉 → "celebration"
- 👨‍💻 → "code"
- 🎓 → "school"

## Обновления в ApiServiceExample.kt

Заменены эмодзи в логах на текстовые маркеры:

- ✅ → [SUCCESS]
- ❌ → [ERROR]
- 🏆 → [TROPHY]
- 📋 → [LIST]
- 🏖️ → [VACATION]
- 📚 → [COURSE]
- 🎂 → [BIRTHDAY]

## Преимущества замены

1. **Кросс-платформенность**: Material Icons одинаково выглядят на всех платформах
2. **Консистентность**: Единый стиль иконок согласно Material Design
3. **Размер**: Vector-иконки масштабируются без потери качества
4. **Темная тема**: Иконки автоматически адаптируются под цветовую схему
5. **Доступность**: Поддержка `contentDescription` для screen readers
6. **Производительность**: Нет проблем с отрисовкой эмодзи на разных устройствах

## Использование

### Базовое использование:

```kotlin
Icon(
    Icons.Default.Home,
    contentDescription = "Главная"
)
```

### С кастомным размером:

```kotlin
Icon(
    Icons.Default.Star,
    contentDescription = null,
    modifier = Modifier.size(24.dp)
)
```

### С кастомным цветом:

```kotlin
Icon(
    Icons.Default.CheckCircle,
    contentDescription = "Завершено",
    tint = MaterialTheme.colorScheme.primary
)
```

## Дополнительные ресурсы

- [Material Icons Gallery](https://fonts.google.com/icons)
- [Compose Material Icons Documentation](https://developer.android.com/reference/kotlin/androidx/compose/material/icons/package-summary)
- [Material Design Guidelines](https://m3.material.io/styles/icons/overview)
