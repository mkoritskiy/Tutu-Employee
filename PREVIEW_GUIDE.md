# Руководство по Preview в Compose Multiplatform 🎨

## Обзор

Preview функции позволяют просматривать UI компоненты прямо в IDE без запуска приложения.

---

## ✅ Добавлено Preview для HomeScreen

### Созданные Preview функции

1. **BirthdayCardPreview** - Карточка дня рождения
2. **NewsCardPreview** - Карточка новости
3. **BirthdaysSectionPreview** - Секция с днями рождений
4. **SearchBarPreview** - Пустой поисковый бар
5. **SearchBarWithResultsPreview** - Поиск с результатами
6. **HomeScreenContentPreview** - Полный контент главного экрана

---

## 📱 Как использовать Preview

### В Android Studio

1. Откройте файл `HomeScreen.kt`
2. Найдите функцию с аннотацией `@Preview`
3. Нажмите на иконку 🔍 рядом с функцией
4. Или откройте панель Preview (View → Tool Windows → Preview)

### В IntelliJ IDEA

Preview доступен только при использовании плагина Compose Multiplatform.

---

## 🎨 Примеры Preview

### 1. BirthdayCardPreview

Показывает одну карточку дня рождения с тестовыми данными.

```kotlin
@Preview
@Composable
fun BirthdayCardPreview() {
    TutuEmployeeTheme {
        BirthdayCard(
            birthday = Birthday(
                employeeId = "1",
                employeeName = "Иван Иванов",
                date = "15 декабря",
                department = "IT",
                avatarUrl = null
            )
        )
    }
}
```

**Результат**: Карточка с инициалами, именем и датой дня рождения.

---

### 2. NewsCardPreview

Показывает карточку новости.

```kotlin
@Preview
@Composable
fun NewsCardPreview() {
    TutuEmployeeTheme {
        NewsCard(
            news = News(
                id = "1",
                title = "Новая версия приложения",
                content = "Рады представить обновленную версию...",
                imageUrl = null,
                publishedAt = "10 декабря 2025",
                category = NewsCategory.COMPANY
            ),
            onClick = {}
        )
    }
}
```

**Результат**: Полноценная карточка новости с заголовком, текстом и датой.

---

### 3. BirthdaysSectionPreview

Показывает горизонтальную прокрутку дней рождений.

```kotlin
@Preview
@Composable
fun BirthdaysSectionPreview() {
    TutuEmployeeTheme {
        Surface {
            BirthdaysSection(
                birthdays = listOf(
                    Birthday("1", "Иван Иванов", "15 декабря", "IT", null),
                    Birthday("2", "Мария Петрова", "16 декабря", "HR", null),
                    Birthday("3", "Алексей Сидоров", "17 декабря", "Sales", null)
                )
            )
        }
    }
}
```

**Результат**: LazyRow с 3 карточками дней рождений.

---

### 4. SearchBarPreview

Показывает пустой поисковый бар.

```kotlin
@Preview
@Composable
fun SearchBarPreview() {
    TutuEmployeeTheme {
        Surface {
            SearchBar(
                query = "",
                onQueryChange = {},
                searchResults = emptyList()
            )
        }
    }
}
```

**Результат**: OutlinedTextField с иконкой поиска.

---

### 5. SearchBarWithResultsPreview

Показывает поисковый бар с результатами поиска.

```kotlin
@Preview
@Composable
fun SearchBarWithResultsPreview() {
    TutuEmployeeTheme {
        Surface {
            SearchBar(
                query = "Иван",
                onQueryChange = {},
                searchResults = listOf(
                    User(...), // 2 пользователя
                )
            )
        }
    }
}
```

**Результат**: Поисковый бар с выпадающим списком из 2 результатов.

---

### 6. HomeScreenContentPreview

Показывает полный контент главного экрана (без Scaffold).

```kotlin
@Preview
@Composable
fun HomeScreenContentPreview() {
    TutuEmployeeTheme {
        Surface {
            LazyColumn(...) {
                // Секция дней рождений
                // Секция новостей
            }
        }
    }
}
```

**Результат**: Полноценная прокручиваемая страница с днями рождений и новостями.

---

## 🎯 Рекомендации по Preview

### ✅ Хорошие практики

1. **Оборачивайте в Theme**
   ```kotlin
   TutuEmployeeTheme {
       YourComponent()
   }
   ```

2. **Используйте Surface для фона**
   ```kotlin
   Surface {
       YourComponent()
   }
   ```

3. **Создавайте тестовые данные**
    - Реалистичные имена и данные
    - Разные состояния (пустое, заполненное, ошибка)

4. **Несколько Preview для одного компонента**
    - Светлая тема
    - Темная тема
    - Разные размеры экрана

### ❌ Чего избегать

1. **Не использовать реальные данные**
    - Только mock/тестовые данные

2. **Не делать сетевые запросы**
    - Preview должен работать offline

3. **Не использовать Navigation**
    - Preview показывает только статичный UI

4. **Избегать зависимостей от ViewModels**
    - Передавайте данные напрямую

---

## 🔧 Расширенные Preview

### Dark Theme Preview

Создайте Preview для темной темы:

```kotlin
@Preview
@Composable
fun BirthdayCardDarkPreview() {
    TutuEmployeeTheme(darkTheme = true) {
        BirthdayCard(birthday = testBirthday)
    }
}
```

### Multiple Previews

Создайте несколько вариантов:

```kotlin
@Preview(name = "Empty State")
@Composable
fun SearchBarEmptyPreview() { ... }

@Preview(name = "With Results")
@Composable
fun SearchBarWithResultsPreview() { ... }

@Preview(name = "Long Query")
@Composable
fun SearchBarLongQueryPreview() { ... }
```

---

## 📊 Доступные Preview параметры

```kotlin
@Preview(
    name = "Light Theme",           // Название
    showBackground = true,          // Показать фон
    backgroundColor = 0xFFFFFFFF,   // Цвет фона
    widthDp = 360,                  // Ширина в dp
    heightDp = 640,                 // Высота в dp
    locale = "ru",                  // Локаль
    fontScale = 1.0f,              // Масштаб шрифта
    showSystemUi = false            // Показать системный UI
)
@Composable
fun MyPreview() { ... }
```

---

## 🚀 Следующие шаги

### Добавить Preview для других экранов

1. **ProfileScreen** - профиль пользователя
2. **AuthScreen** - форма входа
3. **OfficeScreen** - бронирование мест
4. **FavoritesScreen** - избранное
5. **MerchScreen** - магазин мерча

### Пример для нового экрана

```kotlin
// В конце файла экрана
@Preview
@Composable
fun ProfileScreenPreview() {
    TutuEmployeeTheme {
        Surface {
            // Ваш контент без Scaffold
            ProfileContent(
                user = User(...)
            )
        }
    }
}
```

---

## 📚 Дополнительная информация

### Документация

- [Compose Multiplatform Preview](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-preview.html)
- [Android Compose Preview](https://developer.android.com/jetpack/compose/tooling/previews)

### Горячие клавиши

- **Refresh Preview**: `Ctrl+Shift+F5` (Win/Linux) или `Cmd+Shift+F5` (Mac)
- **Run Preview**: Иконка ▶️ рядом с Preview

---

## ✅ Итого

### Создано Preview функций: 6

1. ✅ BirthdayCardPreview
2. ✅ NewsCardPreview
3. ✅ BirthdaysSectionPreview
4. ✅ SearchBarPreview
5. ✅ SearchBarWithResultsPreview
6. ✅ HomeScreenContentPreview

### Преимущества

- ✅ Быстрая разработка UI
- ✅ Не нужно запускать приложение
- ✅ Видно изменения моментально
- ✅ Легко тестировать разные состояния
- ✅ Удобно для дизайнеров

---

**Статус**: ✅ Preview добавлены для HomeScreen  
**Компиляция**: ✅ BUILD SUCCESSFUL  
**Готово к использованию**: Да 🎉

---

Made with ❤️ using Compose Multiplatform 🎨
