# Руководство по UI компонентам Tutu Employee 🎨

## Оглавление

1. [Цвета](#цвета)
2. [Типографика](#типографика)
3. [Карточки](#карточки)
4. [Кнопки](#кнопки)
5. [Формы ввода](#формы-ввода)
6. [Аватары](#аватары)
7. [Статистика](#статистика)
8. [Пустые состояния](#пустые-состояния)
9. [Диалоги](#диалоги)
10. [Анимации](#анимации)

---

## Цвета

### Основная палитра

```kotlin
// Primary - для основных действий и акцентов
MaterialTheme.colorScheme.primary
MaterialTheme.colorScheme.onPrimary
MaterialTheme.colorScheme.primaryContainer
MaterialTheme.colorScheme.onPrimaryContainer

// Secondary - для второстепенных акцентов
MaterialTheme.colorScheme.secondary
MaterialTheme.colorScheme.onSecondary
MaterialTheme.colorScheme.secondaryContainer
MaterialTheme.colorScheme.onSecondaryContainer

// Tertiary - для дополнительных акцентов
MaterialTheme.colorScheme.tertiary
MaterialTheme.colorScheme.onTertiary
MaterialTheme.colorScheme.tertiaryContainer
MaterialTheme.colorScheme.onTertiaryContainer
```

### Пример использования

```kotlin
Card(
    colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    )
) {
    Text(
        text = "Главная информация",
        color = MaterialTheme.colorScheme.onPrimaryContainer
    )
}
```

---

## Типографика

### Заголовки

```kotlin
// Очень крупный заголовок (57sp)
Text(
    text = "Display Large",
    style = MaterialTheme.typography.displayLarge
)

// Крупный заголовок (45sp)
Text(
    text = "Display Medium",
    style = MaterialTheme.typography.displayMedium
)

// Средний заголовок (36sp)
Text(
    text = "Display Small",
    style = MaterialTheme.typography.displaySmall
)

// Заголовок раздела (32sp, Bold)
Text(
    text = "Headline Large",
    style = MaterialTheme.typography.headlineLarge
)

// Средний заголовок раздела (28sp, Bold)
Text(
    text = "Headline Medium",
    style = MaterialTheme.typography.headlineMedium
)

// Малый заголовок раздела (24sp, SemiBold)
Text(
    text = "Headline Small",
    style = MaterialTheme.typography.headlineSmall
)
```

### Заголовки карточек

```kotlin
// Заголовок карточки (22sp, SemiBold)
Text(
    text = "Title Large",
    style = MaterialTheme.typography.titleLarge
)

// Средний заголовок (16sp, SemiBold)
Text(
    text = "Title Medium",
    style = MaterialTheme.typography.titleMedium
)

// Малый заголовок (14sp, Medium)
Text(
    text = "Title Small",
    style = MaterialTheme.typography.titleSmall
)
```

### Основной текст

```kotlin
// Крупный текст (16sp)
Text(
    text = "Body Large",
    style = MaterialTheme.typography.bodyLarge
)

// Средний текст (14sp)
Text(
    text = "Body Medium",
    style = MaterialTheme.typography.bodyMedium
)

// Малый текст (12sp)
Text(
    text = "Body Small",
    style = MaterialTheme.typography.bodySmall
)
```

### Метки

```kotlin
// Метка кнопки (14sp, Medium)
Text(
    text = "Label Large",
    style = MaterialTheme.typography.labelLarge
)

// Средняя метка (12sp, Medium)
Text(
    text = "Label Medium",
    style = MaterialTheme.typography.labelMedium
)

// Малая метка (11sp, Medium)
Text(
    text = "Label Small",
    style = MaterialTheme.typography.labelSmall
)
```

---

## Карточки

### Простая карточка

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
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Описание карточки",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
```

### Цветная карточка

```kotlin
Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
) {
    Text(
        text = "Цветная карточка",
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = Modifier.padding(20.dp)
    )
}
```

### Кликабельная карточка

```kotlin
Card(
    onClick = { /* action */ },
    modifier = Modifier.fillMaxWidth(),
    elevation = CardDefaults.cardElevation(
        defaultElevation = 2.dp,
        pressedElevation = 8.dp  // При нажатии
    )
) {
    Text(
        text = "Нажми меня",
        modifier = Modifier.padding(16.dp)
    )
}
```

### IconCard (готовый компонент)

```kotlin
IconCard(
    icon = "🎂",
    title = "День рождения",
    subtitle = "Иван Иванов",
    onClick = { /* action */ }
)
```

---

## Кнопки

### Основная кнопка

```kotlin
Button(
    onClick = { /* action */ },
    modifier = Modifier
        .fillMaxWidth()
        .height(56.dp),
    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
) {
    Text(
        text = "Войти",
        style = MaterialTheme.typography.titleMedium
    )
}
```

### Кнопка с иконкой

```kotlin
Button(onClick = { /* action */ }) {
    Text("⭐")
    Spacer(modifier = Modifier.width(8.dp))
    Text("Добавить в избранное")
}
```

### Outlined Button

```kotlin
OutlinedButton(onClick = { /* action */ }) {
    Text("Отмена")
}
```

### Text Button

```kotlin
TextButton(onClick = { /* action */ }) {
    Text("Подробнее")
}
```

### FloatingActionButton

```kotlin
FloatingActionButton(
    onClick = { /* action */ },
    containerColor = MaterialTheme.colorScheme.primaryContainer,
    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
) {
    Text("+", style = MaterialTheme.typography.headlineMedium)
}
```

### Chip (фильтры)

```kotlin
FilterChip(
    selected = isSelected,
    onClick = { /* action */ },
    label = { Text("Категория") }
)

AssistChip(
    onClick = { /* action */ },
    label = { Text("Помощь") }
)
```

---

## Формы ввода

### TextField

```kotlin
OutlinedTextField(
    value = text,
    onValueChange = { text = it },
    label = { Text("Логин") },
    modifier = Modifier.fillMaxWidth(),
    singleLine = true
)
```

### Password TextField

```kotlin
var passwordVisible by remember { mutableStateOf(false) }

OutlinedTextField(
    value = password,
    onValueChange = { password = it },
    label = { Text("Пароль") },
    visualTransformation = if (passwordVisible) 
        VisualTransformation.None 
    else 
        PasswordVisualTransformation(),
    trailingIcon = {
        TextButton(onClick = { passwordVisible = !passwordVisible }) {
            Text(if (passwordVisible) "Скрыть" else "Показать")
        }
    },
    modifier = Modifier.fillMaxWidth(),
    singleLine = true
)
```

### Search Bar

```kotlin
OutlinedTextField(
    value = query,
    onValueChange = { query = it },
    placeholder = { Text("Поиск сотрудников") },
    leadingIcon = { Text("🔍") },
    trailingIcon = {
        if (query.isNotEmpty()) {
            IconButton(onClick = { query = "" }) {
                Text("✕")
            }
        }
    },
    modifier = Modifier.fillMaxWidth(),
    singleLine = true
)
```

---

## Аватары

### InitialsAvatar (готовый компонент)

```kotlin
// Маленький аватар
InitialsAvatar(
    initials = "ИИ",
    size = 40
)

// Средний аватар
InitialsAvatar(
    initials = "ИИ",
    size = 64,
    backgroundColor = MaterialTheme.colorScheme.primary,
    textColor = MaterialTheme.colorScheme.onPrimary
)

// Большой аватар
InitialsAvatar(
    initials = "ИИ",
    size = 88
)
```

### Ручное создание аватара

```kotlin
Surface(
    modifier = Modifier.size(64.dp),
    shape = CircleShape,
    color = MaterialTheme.colorScheme.primary,
    shadowElevation = 4.dp
) {
    Box(contentAlignment = Alignment.Center) {
        Text(
            text = "ИИ",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}
```

---

## Статистика

### StatCard (готовый компонент)

```kotlin
Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(16.dp)
) {
    StatCard(
        icon = "📅",
        value = "14",
        label = "дней отпуска",
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        modifier = Modifier.weight(1f)
    )
    
    StatCard(
        icon = "⭐",
        value = "150",
        label = "бонусов",
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        modifier = Modifier.weight(1f)
    )
}
```

### Ручное создание статистики

```kotlin
Card(
    modifier = Modifier.weight(1f),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.secondaryContainer
    )
) {
    Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "📅",
            style = MaterialTheme.typography.displayMedium
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "14",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Text(
            text = "дней отпуска",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}
```

---

## Пустые состояния

### EmptyState (готовый компонент)

```kotlin
EmptyState(
    icon = "📭",
    title = "Список пуст",
    subtitle = "Добавьте первый элемент",
    actionButton = {
        Button(onClick = { /* action */ }) {
            Text("Добавить")
        }
    }
)
```

### Ручное создание

```kotlin
Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "📭",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
        Text(
            text = "Список пуст",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Добавьте первый элемент",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
```

---

## Диалоги

### AlertDialog

```kotlin
AlertDialog(
    onDismissRequest = { /* dismiss */ },
    title = { Text("Подтверждение") },
    text = { Text("Вы уверены?") },
    confirmButton = {
        Button(onClick = { /* confirm */ }) {
            Text("Да")
        }
    },
    dismissButton = {
        TextButton(onClick = { /* dismiss */ }) {
            Text("Отмена")
        }
    }
)
```

### Dialog с формой

```kotlin
AlertDialog(
    onDismissRequest = { /* dismiss */ },
    title = { Text("Добавить элемент") },
    text = {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Название") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Описание") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )
        }
    },
    confirmButton = {
        Button(
            onClick = { /* save */ },
            enabled = name.isNotEmpty()
        ) {
            Text("Сохранить")
        }
    },
    dismissButton = {
        TextButton(onClick = { /* dismiss */ }) {
            Text("Отмена")
        }
    }
)
```

---

## Анимации

### Использование стандартных анимаций

```kotlin
import ru.tutu.tutuemployee.ui.theme.TutuAnimations

// В navigation transitions
composable(
    route = "screen",
    enterTransition = { TutuAnimations.slideInFromRight() },
    exitTransition = { TutuAnimations.slideOutToLeft() }
) {
    Screen()
}

// AnimatedVisibility
AnimatedVisibility(
    visible = isVisible,
    enter = TutuAnimations.scaleIn(),
    exit = TutuAnimations.scaleOut()
) {
    Content()
}
```

### Длительности

```kotlin
// Быстрая анимация
TutuAnimations.FAST_DURATION    // 150ms

// Средняя анимация
TutuAnimations.MEDIUM_DURATION  // 300ms

// Медленная анимация
TutuAnimations.SLOW_DURATION    // 500ms
```

---

## Дополнительные компоненты

### SectionHeader

```kotlin
SectionHeader(
    title = "Новости компании",
    icon = "📰"
)
```

### ColoredBadge

```kotlin
ColoredBadge(
    text = "Новое",
    containerColor = MaterialTheme.colorScheme.error,
    contentColor = MaterialTheme.colorScheme.onError
)
```

### TextDivider

```kotlin
TextDivider(text = "или")
```

---

## Лучшие практики

### 1. Elevation (поднятие)

- Карточки: 2dp
- FAB: 6dp
- Диалоги: 8dp

### 2. Отступы

- Малые: 8dp
- Средние: 16dp
- Большие: 24dp
- XL: 32dp

### 3. Скругления

- Малые элементы: 8dp
- Карточки: 12dp
- Диалоги: 16dp
- Модальные окна: 28dp

### 4. Размеры текста

- Минимальный: 12sp
- Оптимальный для чтения: 14-16sp
- Заголовки: 22-28sp

### 5. Кликабельные элементы

- Минимальный размер: 48dp
- Рекомендуемый: 56dp

---

## Заключение

Используйте эти компоненты для создания консистентного и красивого UI! 🎨

Для более подробной информации см:

- [Material3 Theme README](composeApp/src/commonMain/kotlin/ru/tutu/tutuemployee/ui/theme/README.md)
- [Material3 Upgrade Guide](MATERIAL3_UPGRADE.md)
