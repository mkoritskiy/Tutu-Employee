# Material3 Quick Start 🚀

## Быстрый старт за 5 минут

### 1. Использование темы (уже применено)

Тема автоматически применяется в `App.kt`:

```kotlin
@Composable
fun App() {
    KoinContext {
        TutuEmployeeTheme {  // 👈 Тема применена здесь
            NavigationHost(...)
        }
    }
}
```

✅ Больше ничего делать не нужно - тема работает!

---

## Основные сценарии

### 🎨 Создать карточку

```kotlin
Card(
    modifier = Modifier.fillMaxWidth(),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Заголовок",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Описание",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
```

### 🎨 Создать цветную карточку

```kotlin
Card(
    colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    )
) {
    Text(
        text = "Синяя карточка",
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = Modifier.padding(16.dp)
    )
}
```

### 🔘 Создать кнопку

```kotlin
Button(
    onClick = { /* action */ },
    modifier = Modifier.height(56.dp)
) {
    Text("Действие")
}
```

### 📝 Создать поле ввода

```kotlin
OutlinedTextField(
    value = text,
    onValueChange = { text = it },
    label = { Text("Название") },
    modifier = Modifier.fillMaxWidth()
)
```

### 👤 Создать аватар

```kotlin
import ru.tutu.tutuemployee.presentation.components.InitialsAvatar

InitialsAvatar(
    initials = "ИИ",
    size = 64
)
```

### 📊 Создать статистику

```kotlin
import ru.tutu.tutuemployee.presentation.components.StatCard

StatCard(
    icon = "⭐",
    value = "150",
    label = "бонусов"
)
```

---

## Готовые компоненты

Все импорты: `import ru.tutu.tutuemployee.presentation.components.*`

### 1. SectionHeader

```kotlin
SectionHeader(
    title = "Новости",
    icon = "📰"
)
```

### 2. IconCard

```kotlin
IconCard(
    icon = "🎂",
    title = "День рождения",
    subtitle = "Сегодня",
    onClick = { /* action */ }
)
```

### 3. StatCard

```kotlin
StatCard(
    icon = "📅",
    value = "14",
    label = "дней отпуска",
    containerColor = MaterialTheme.colorScheme.secondaryContainer,
    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
)
```

### 4. InitialsAvatar

```kotlin
InitialsAvatar(
    initials = "ИИ",
    size = 64,
    backgroundColor = MaterialTheme.colorScheme.primary,
    textColor = MaterialTheme.colorScheme.onPrimary
)
```

### 5. ColoredBadge

```kotlin
ColoredBadge(
    text = "Новое",
    containerColor = MaterialTheme.colorScheme.error,
    contentColor = MaterialTheme.colorScheme.onError
)
```

### 6. EmptyState

```kotlin
EmptyState(
    icon = "📭",
    title = "Список пуст",
    subtitle = "Добавьте первый элемент",
    actionButton = {
        Button(onClick = { /* add */ }) {
            Text("Добавить")
        }
    }
)
```

### 7. TextDivider

```kotlin
TextDivider(text = "или")
```

---

## Цвета - шпаргалка

### Primary (Синий)

```kotlin
MaterialTheme.colorScheme.primary               // Основной синий
MaterialTheme.colorScheme.onPrimary            // Белый текст
MaterialTheme.colorScheme.primaryContainer     // Светло-синий фон
MaterialTheme.colorScheme.onPrimaryContainer   // Темный текст
```

### Secondary (Оранжевый)

```kotlin
MaterialTheme.colorScheme.secondary
MaterialTheme.colorScheme.onSecondary
MaterialTheme.colorScheme.secondaryContainer
MaterialTheme.colorScheme.onSecondaryContainer
```

### Tertiary (Бирюзовый)

```kotlin
MaterialTheme.colorScheme.tertiary
MaterialTheme.colorScheme.onTertiary
MaterialTheme.colorScheme.tertiaryContainer
MaterialTheme.colorScheme.onTertiaryContainer
```

### Базовые

```kotlin
MaterialTheme.colorScheme.background           // Фон приложения
MaterialTheme.colorScheme.surface              // Фон карточек
MaterialTheme.colorScheme.error                // Ошибки
```

---

## Типографика - шпаргалка

### Заголовки

```kotlin
MaterialTheme.typography.displayLarge          // 57sp - очень крупно
MaterialTheme.typography.displayMedium         // 45sp
MaterialTheme.typography.displaySmall          // 36sp

MaterialTheme.typography.headlineLarge         // 32sp - крупный заголовок
MaterialTheme.typography.headlineMedium        // 28sp
MaterialTheme.typography.headlineSmall         // 24sp - заголовок раздела
```

### Заголовки карточек

```kotlin
MaterialTheme.typography.titleLarge            // 22sp - заголовок карточки
MaterialTheme.typography.titleMedium           // 16sp
MaterialTheme.typography.titleSmall            // 14sp
```

### Текст

```kotlin
MaterialTheme.typography.bodyLarge             // 16sp - крупный текст
MaterialTheme.typography.bodyMedium            // 14sp - обычный текст
MaterialTheme.typography.bodySmall             // 12sp - мелкий текст
```

### Метки

```kotlin
MaterialTheme.typography.labelLarge            // 14sp - кнопки
MaterialTheme.typography.labelMedium           // 12sp - метки
MaterialTheme.typography.labelSmall            // 11sp - маленькие метки
```

---

## Типичные паттерны

### Экран с TopAppBar

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Заголовок",
                        style = MaterialTheme.typography.headlineSmall
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Content(modifier = Modifier.padding(paddingValues))
    }
}
```

### Список карточек

```kotlin
LazyColumn(
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
) {
    items(list) { item ->
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Text(
                text = item.title,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
```

### Форма

```kotlin
Column(
    modifier = Modifier.padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
) {
    OutlinedTextField(
        value = name,
        onValueChange = { name = it },
        label = { Text("Имя") },
        modifier = Modifier.fillMaxWidth()
    )
    
    OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        label = { Text("Email") },
        modifier = Modifier.fillMaxWidth()
    )
    
    Button(
        onClick = { /* save */ },
        modifier = Modifier.fillMaxWidth().height(56.dp)
    ) {
        Text("Сохранить")
    }
}
```

### Диалог

```kotlin
AlertDialog(
    onDismissRequest = { showDialog = false },
    title = { Text("Заголовок") },
    text = { Text("Текст диалога") },
    confirmButton = {
        Button(onClick = { /* confirm */ }) {
            Text("Ок")
        }
    },
    dismissButton = {
        TextButton(onClick = { showDialog = false }) {
            Text("Отмена")
        }
    }
)
```

---

## Частые ошибки ❌ и как их избежать ✅

### ❌ Не использовать hardcoded цвета

```kotlin
// Плохо
Text(text = "Hello", color = Color.Blue)

// Хорошо
Text(
    text = "Hello", 
    color = MaterialTheme.colorScheme.primary
)
```

### ❌ Не использовать hardcoded размеры текста

```kotlin
// Плохо
Text(text = "Hello", fontSize = 16.sp)

// Хорошо
Text(
    text = "Hello",
    style = MaterialTheme.typography.bodyLarge
)
```

### ❌ Забывать про elevation

```kotlin
// Плохо
Card { ... }

// Хорошо
Card(
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
) { ... }
```

### ❌ Не указывать onColor для кастомных фонов

```kotlin
// Плохо
Card(
    colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    )
) {
    Text("Текст")  // Может быть не виден!
}

// Хорошо
Card(
    colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    )
) {
    Text(
        "Текст",
        color = MaterialTheme.colorScheme.onPrimaryContainer
    )
}
```

---

## Отступы - стандарты

```kotlin
// Малые
Modifier.padding(8.dp)

// Средние (по умолчанию)
Modifier.padding(16.dp)

// Большие
Modifier.padding(24.dp)

// Экстра большие
Modifier.padding(32.dp)

// Между элементами
verticalArrangement = Arrangement.spacedBy(12.dp)
```

---

## Размеры элементов

```kotlin
// Высота кнопки
Modifier.height(56.dp)

// Высота поля ввода
Modifier.height(56.dp)

// Размер маленького аватара
Modifier.size(40.dp)

// Размер среднего аватара
Modifier.size(64.dp)

// Размер большого аватара
Modifier.size(88.dp)

// Минимальный размер кликабельного элемента
Modifier.size(48.dp)
```

---

## Полезные ссылки

-
📖 [Полная документация темы](composeApp/src/commonMain/kotlin/ru/tutu/tutuemployee/ui/theme/README.md)
- 🎨 [Гид по компонентам](UI_COMPONENTS_GUIDE.md)
- 📋 [Цветовая палитра](COLOR_PALETTE_REFERENCE.md)
- 🚀 [Руководство по обновлению](MATERIAL3_UPGRADE.md)
- 📊 [Итоговая сводка](MATERIAL3_SUMMARY.md)

---

## Поддержка

При возникновении вопросов:

1. Проверьте примеры в существующих экранах
2. Изучите [UI_COMPONENTS_GUIDE.md](UI_COMPONENTS_GUIDE.md)
3. Посмотрите документацию темы

---

## Чек-лист для нового экрана

- [ ] Использовать `TutuEmployeeTheme` (уже применена)
- [ ] TopAppBar с цветами из темы
- [ ] Карточки с elevation
- [ ] Цвета из `MaterialTheme.colorScheme`
- [ ] Типографика из `MaterialTheme.typography`
- [ ] Отступы: 8dp, 16dp, 24dp, 32dp
- [ ] Кнопки высотой 56dp
- [ ] Аватары размером 40dp, 64dp или 88dp
- [ ] Empty state для пустых списков
- [ ] Loading indicator при загрузке

---

**Готово!** Начинайте создавать красивые UI! 🎨✨
